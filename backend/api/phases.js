const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');


const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// 🌟 ĐĂNG KÝ ĐƯỜNG DẪN TRỰC TIẾP:
const allowedPhaseTypes = ['group_stage','round_of_16','quarter_final','semi_final','third_place','final'];
const allowedFormats = ['round_robin','knockout'];

function isPowerOfTwo(n) {
    return Number.isInteger(n) && n > 0 && (n & (n - 1)) === 0;
}

function defaultGroupName(index) {
    return `Bảng ${String.fromCharCode(65 + index)}`;
}

function shuffleArray(array) {
    const shuffled = [...array];
    for (let i = shuffled.length - 1; i > 0; i -= 1) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled;
}

function buildRoundRobinMatches(teamIds) {
    const teams = [...teamIds];
    if (teams.length % 2 !== 0) {
        teams.push(null);
    }

    const numTeams = teams.length;
    const rounds = numTeams - 1;
    const matchesPerRound = numTeams / 2;
    const schedule = [];

    for (let round = 0; round < rounds; round += 1) {
        for (let i = 0; i < matchesPerRound; i += 1) {
            const home = teams[i];
            const away = teams[numTeams - 1 - i];
            if (home !== null && away !== null) {
                schedule.push({ home, away });
            }
        }
        teams.splice(1, 0, teams.pop());
    }

    return schedule;
}

async function clearScheduledMatchesForGroup(connection, phaseId, groupId) {
    await connection.execute(
        `DELETE FROM matches
         WHERE phase_id = ? AND group_id = ? AND status = 'scheduled' AND deleted_at IS NULL`,
        [phaseId, groupId]
    );
}

async function createBracketSlots(connection, phaseId, teamIds) {
    const totalTeams = teamIds.length;
    const bracketSize = isPowerOfTwo(totalTeams) ? totalTeams : 2 ** Math.ceil(Math.log2(totalTeams));
    const paddedTeams = [...teamIds];
    while (paddedTeams.length < bracketSize) {
        paddedTeams.push(null);
    }

    let previousRoundSlotIds = [];

    for (let round = 1; round <= Math.log2(bracketSize); round += 1) {
        const matchCount = bracketSize / 2 ** round;
        const values = [];

        for (let i = 0; i < matchCount; i += 1) {
            let seededHome = null;
            let seededAway = null;
            let sourceA = null;
            let sourceB = null;
            let isBye = 0;

            if (round === 1) {
                seededHome = paddedTeams[i * 2] || null;
                seededAway = paddedTeams[i * 2 + 1] || null;
                if (!seededAway) {
                    isBye = 1;
                }
            } else {
                sourceA = previousRoundSlotIds[i * 2] || null;
                sourceB = previousRoundSlotIds[i * 2 + 1] || null;
            }

            values.push(
                phaseId,
                round,
                i + 1,
                null,
                sourceA,
                sourceB,
                seededHome,
                seededAway,
                isBye
            );
        }

        const placeholders = values
            .map(() => '(?, ?, ?, ?, ?, ?, ?, ?, ?)')
            .join(', ');

        const sql = 'INSERT INTO bracket_slots (phase_id, `round`, slot_number, match_id, source_a_slot_id, source_b_slot_id, seeded_home_team_id, seeded_away_team_id, is_bye) VALUES ' + placeholders;
        const [result] = await connection.execute(sql, values);

        const firstId = result.insertId;
        const roundSlotIds = [];
        for (let i = 0; i < matchCount; i += 1) {
            roundSlotIds.push(firstId + i);
        }

        previousRoundSlotIds = roundSlotIds;
    }
}

async function createGroupsAndAssignTeams(connection, seasonId, phaseId, groupCount, groupNames) {
    const groupIds = [];
    const names = groupNames && Array.isArray(groupNames) && groupNames.length >= groupCount
        ? groupNames
        : Array.from({ length: groupCount }, (_, idx) => defaultGroupName(idx));

    for (let i = 0; i < groupCount; i += 1) {
        const [result] = await connection.execute(
            'INSERT INTO `groups` (phase_id, name, is_active, created_at, updated_at, status) VALUES (?, ?, 1, NOW(), NOW(), \'DRAFT\')',
            [phaseId, names[i]]
        );
        groupIds.push(result.insertId);
    }

    const [teams] = await connection.execute(
        'SELECT id AS season_team_id, team_id FROM season_teams WHERE season_id = ? AND is_active = 1 AND status = \'active\' ORDER BY id ASC',
        [seasonId]
    );

    if (teams.length > 0) {
        for (let i = 0; i < teams.length; i += 1) {
            const groupId = groupIds[i % groupCount];
            await connection.execute(
                'UPDATE season_teams SET group_id = ? WHERE id = ?',
                [groupId, teams[i].season_team_id]
            );
        }
    }

    return { groupIds, teamIds: teams.map(team => team.team_id) };
}

router.get('/seasons/:seasonId/phases', async (req, res) => {
    const seasonId = req.params.seasonId;

    if (!seasonId) {
        return res.status(400).json({ 
            status: "error", 
            message: 'Thiếu thông tin mã mùa giải (seasonId)!' 
        });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // 1. Sửa tên bảng từ 'tournament_phases' thành 'phases'
        // 2. Chọn thêm cột 'type' và các trường cần thiết
        const [rows] = await connection.execute(
            'SELECT id, name, type, format FROM phases WHERE season_id = ? ORDER BY `order` ASC', 
            [seasonId]
        );
        await connection.end();

        // Trả về dữ liệu thật từ DB
        return res.status(200).json({
            status: "success",
            message: "Tải danh sách vòng đấu thành công từ Database!",
            data: {
                seasonId: parseInt(seasonId),
                phases: rows.map(item => ({
                    id: item.id,
                    name: item.name, 
                    type: item.type,
                    format: item.format  // Giá trị này sẽ khớp với ENUM trong DB
                }))
            }
        });

    } catch (error) {
        console.error("Lỗi API lấy vòng đấu:", error);
        return res.status(500).json({ status: "error", message: 'Lỗi kết nối Server hoặc Database!' });
    }
});

router.post('/seasons/:seasonId/phases', async (req, res) => {
    const seasonId = req.params.seasonId;
    const {
        name,
        type,
        format,
        order,
        start_date,
        end_date,
        group_count,
        groupCount,
        group_names,
        groupNames,
        team_ids,
        teamIds
    } = req.body;

    const effectiveGroupCount = group_count || groupCount;
    const effectiveGroupNames = group_names || groupNames;
    const effectiveTeamIds = team_ids || teamIds;

    if (!seasonId) {
        return res.status(400).json({ status: 'error', message: 'Thiếu seasonId' });
    }
    if (!name || !type || !format || order === undefined || order === null) {
        return res.status(400).json({ status: 'error', message: 'Thiếu tên, loại, định dạng hoặc thứ tự vòng đấu' });
    }
    if (!allowedPhaseTypes.includes(type) || !allowedFormats.includes(format)) {
        return res.status(400).json({ status: 'error', message: 'Kiểu vòng đấu hoặc định dạng không hợp lệ' });
    }
    if (format === 'round_robin' && (!effectiveGroupCount || effectiveGroupCount < 1)) {
        return res.status(400).json({ status: 'error', message: 'Cần số lượng bảng hợp lệ cho vòng tròn' });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);

        const [phaseResult] = await connection.execute(
            `INSERT INTO phases (season_id, name, type, format, \`order\`, start_date, end_date, is_active, created_at, updated_at, status)
             VALUES (?, ?, ?, ?, ?, ?, ?, 1, NOW(), NOW(), 'draft')`,
            [seasonId, name, type, format, order, start_date || null, end_date || null]
        );

        const phaseId = phaseResult.insertId;

        let createdGroups = [];
        if (format === 'round_robin') {
            const { groupIds, teamIds: assignedTeams } = await createGroupsAndAssignTeams(connection, seasonId, phaseId, effectiveGroupCount, effectiveGroupNames);
            createdGroups = groupIds;

            if (assignedTeams.length > 0) {
                const placeholders = assignedTeams
                    .map(() => '(?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 1, NOW(), NOW(), NULL)')
                    .join(', ');
                const values = assignedTeams.flatMap((teamId, idx) => [teamId, groupIds[idx % groupIds.length]]);

                await connection.execute(
                    `INSERT INTO team_standings (team_id, group_id, position, matches_played, wins, draws, losses, goals_for, goals_against, points, is_active, created_at, updated_at, deleted_at)
                     VALUES ${placeholders}`,
                    values
                );
            }
        }

        if (format === 'knockout') {
            if (!Array.isArray(effectiveTeamIds) || effectiveTeamIds.length < 2) {
                await connection.end();
                return res.status(400).json({ status: 'error', message: 'Cần danh sách teamIds để tạo bracket loại trực tiếp' });
            }
            await createBracketSlots(connection, phaseId, effectiveTeamIds);
        }

        await connection.end();

        return res.status(201).json({
            status: 'success',
            message: 'Tạo phase thành công',
            data: {
                phaseId,
                groups: createdGroups
            }
        });
    } catch (error) {
        console.error('Lỗi tạo phase:', error);
        return res.status(500).json({ status: 'error', message: 'Lỗi server khi tạo phase' });
    }
});
router.post('/phases/:phaseId/generate-schedule', async (req, res) => {
    try {
        const phaseId = req.params.phaseId;
        // Thêm || {} để tránh lỗi TypeError khi req.body là undefined
        const { start_date, start_time, interval_hours, interval_minutes } = req.body || {}; 

        const result = await generateScheduleForPhase(phaseId, { start_date, start_time, interval_hours, interval_minutes });
        return res.status(200).json({ 
            status: 'success', 
            message: `Đã tự động xếp thành công ${result.matchesCreated} trận đấu!`, 
            data: { matchesCreated: result.matchesCreated } 
        });
    } catch (error) {
        console.error('Lỗi tự động xếp lịch:', error);
        return res.status(500).json({ status: 'error', message: 'Lỗi server khi xếp lịch tự động' });
    }
});

// Reusable function to generate schedule for a phase (can be called from other modules)
async function generateScheduleForPhase(phaseId, options = {}) {
    const { start_date, start_time, interval_hours, interval_minutes } = options;
    const connection = await mysql.createConnection(dbConfig);

    try {
        const [groups] = await connection.execute(
            'SELECT id FROM `groups` WHERE phase_id = ?',
            [phaseId]
        );

        if (groups.length === 0) {
            await connection.end();
            return { matchesCreated: 0 };
        }

        const [phaseRows] = await connection.execute(
            'SELECT season_id FROM phases WHERE id = ?',
            [phaseId]
        );
        const seasonId = phaseRows.length > 0 ? phaseRows[0].season_id : null;

        let totalMatchesCreated = 0;
        let baseDate = start_date ? new Date(start_date) : new Date();

        if (!start_date) {
            baseDate.setDate(baseDate.getDate() + 7);
        }

        if (start_time) {
            const [hourString, minuteString] = start_time.split(':');
            const hour = parseInt(hourString, 10);
            const minute = parseInt(minuteString, 10);
            if (!Number.isNaN(hour) && !Number.isNaN(minute)) {
                baseDate.setHours(hour, minute, 0, 0);
            }
        } else {
            baseDate.setHours(18, 0, 0, 0);
        }

        const intervalMs = ((interval_hours || 2) * 60 + (interval_minutes || 0)) * 60 * 1000;

        for (const group of groups) {
            await clearScheduledMatchesForGroup(connection, phaseId, group.id);

            const [teams] = await connection.execute(
                'SELECT team_id FROM season_teams WHERE group_id = ? AND is_active = 1',
                [group.id]
            );

            const teamIds = teams.map(t => t.team_id);
            if (teamIds.length === 0) {
                await connection.execute(
                    'UPDATE `groups` SET status = "SCHEDULED", scheduleGeneratedAt = NOW() WHERE id = ?',
                    [group.id]
                );
                continue;
            }

            const randomizedTeamIds = shuffleArray(teamIds);
            const schedule = buildRoundRobinMatches(randomizedTeamIds);
            const randomizedSchedule = shuffleArray(schedule);

            for (const matchInfo of randomizedSchedule) {
                const matchDate = new Date(baseDate.getTime() + (totalMatchesCreated * intervalMs));

                await connection.execute(
                    `INSERT INTO matches (phase_id, group_id, home_team_id, away_team_id, scheduled_at, status, season_id, created_at, updated_at) 
                     VALUES (?, ?, ?, ?, ?, 'scheduled', ?, NOW(), NOW())`,
                    [phaseId, group.id, matchInfo.home, matchInfo.away, matchDate, seasonId]
                );
                totalMatchesCreated++;
            }

            await connection.execute(
                'UPDATE `groups` SET status = "SCHEDULED", scheduleGeneratedAt = NOW() WHERE id = ?',
                [group.id]
            );
        }

        await connection.end();
        return { matchesCreated: totalMatchesCreated };
    } catch (err) {
        await connection.end();
        throw err;
    }
}

module.exports = router;

// Export helper for external callers (e.g., auto-scheduling after season creation)
module.exports.generateScheduleForPhase = generateScheduleForPhase;
module.exports.createGroupsAndAssignTeams = createGroupsAndAssignTeams;
module.exports.createBracketSlots = createBracketSlots;