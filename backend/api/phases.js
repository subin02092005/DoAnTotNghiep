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

// --- HÀM TIỆN ÍCH ---
async function createBracketSlots(connection, phaseId, teamIds) {
    console.log("DEBUG: Đang tạo bracket cho phase:", phaseId, "với teams:", teamIds);
    const totalTeams = teamIds.length || 0;
    // Nếu teamIds rỗng, mặc định tạo khung cho 4 đội (2 trận vòng 1, 1 trận vòng 2)
    const bracketSize = totalTeams > 0 ? Math.pow(2, Math.ceil(Math.log2(totalTeams))) : 4;
    console.log("DEBUG: Bracket size là:", bracketSize);
    const paddedTeams = [...teamIds, ...Array(bracketSize - totalTeams).fill(null)];

    let previousRoundSlotIds = [];

    for (let round = 1; round <= Math.log2(bracketSize); round += 1) {
        const matchCount = bracketSize / Math.pow(2, round);
        const roundSlotIds = [];

        for (let i = 0; i < matchCount; i += 1) {
            let seededHome = null, seededAway = null, sourceA = null, sourceB = null, isBye = 0;

            if (round === 1) {
                seededHome = paddedTeams[i * 2] || null;
                seededAway = paddedTeams[i * 2 + 1] || null;
                isBye = (!seededAway) ? 1 : 0;
            } else {
                sourceA = previousRoundSlotIds[i * 2];
                sourceB = previousRoundSlotIds[i * 2 + 1];
            }

            const [result] = await connection.execute(
                `INSERT INTO bracket_slots 
                (phase_id, \`round\`, slot_number, source_a_slot_id, source_b_slot_id, seeded_home_team_id, seeded_away_team_id, is_bye) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)`,
                [phaseId, round, i + 1, sourceA, sourceB, seededHome, seededAway, isBye]
            );
            roundSlotIds.push(result.insertId);
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
        const randomizedTeams = shuffleArray(teams);
        for (let i = 0; i < randomizedTeams.length; i += 1) {
            const groupId = groupIds[i % groupCount];
            await connection.execute(
                'UPDATE season_teams SET group_id = ? WHERE id = ?',
                [groupId, randomizedTeams[i].season_team_id]
            );
        }
    }

    return { groupIds, teamIds: teams.map(team => team.team_id) };
}

async function assignUnassignedSeasonTeamsToGroups(connection, phaseId, seasonId, groupIds) {
    if (!Array.isArray(groupIds) || groupIds.length === 0) {
        return [];
    }

    const [teams] = await connection.execute(
        `SELECT id AS season_team_id, team_id
         FROM season_teams
         WHERE season_id = ? AND is_active = 1 AND status = 'active'
           AND (group_id IS NULL OR group_id NOT IN (SELECT id FROM \`groups\` WHERE phase_id = ?))
         ORDER BY id ASC`,
        [seasonId, phaseId]
    );

    if (teams.length === 0) {
        return [];
    }

    const randomizedTeams = shuffleArray(teams);
    for (let i = 0; i < randomizedTeams.length; i += 1) {
        const groupId = groupIds[i % groupIds.length];
        await connection.execute(
            'UPDATE season_teams SET group_id = ? WHERE id = ?',
            [groupId, randomizedTeams[i].season_team_id]
        );
    }

    return randomizedTeams;
}

async function importTeamsIntoSeasonAndAssignGroups(connection, seasonId, phaseId, teamIds = []) {
    if (!seasonId || !phaseId) {
        return { importedCount: 0 };
    }

    let importedCount = 0;
    const teamList = Array.isArray(teamIds) ? teamIds : [];

    for (const teamId of teamList) {
        const [existing] = await connection.execute(
            'SELECT id FROM season_teams WHERE season_id = ? AND team_id = ? AND deleted_at IS NULL',
            [seasonId, teamId]
        );
        if (existing.length === 0) {
            await connection.execute(
                "INSERT INTO season_teams (season_id, team_id, status, is_active, created_at, updated_at) VALUES (?, ?, 'active', 1, NOW(), NOW())",
                [seasonId, teamId]
            );
            importedCount += 1;
        }
    }

    const [groups] = await connection.execute('SELECT id FROM `groups` WHERE phase_id = ? ORDER BY id ASC', [phaseId]);
    if (groups.length > 0) {
        await assignUnassignedSeasonTeamsToGroups(connection, phaseId, seasonId, groups.map(group => group.id));
    }

    return { importedCount };
}

router.get('/seasons/:seasonId/phases', async (req, res) => {
    const seasonId = req.params.seasonId;
    try {
        const connection = await mysql.createConnection(dbConfig);
        const [rows] = await connection.execute(`
            SELECT 
                p.id as phase_id, p.name as phase_name, p.format, p.type,
                g.id as group_id, g.name as group_name,
                ts.team_id, t.name as team_name
            FROM phases p
            LEFT JOIN \`groups\` g ON p.id = g.phase_id
            LEFT JOIN team_standings ts ON g.id = ts.group_id
            LEFT JOIN teams t ON ts.team_id = t.id
            WHERE p.season_id = ?
            ORDER BY p.order ASC`, 
            [seasonId]
        );
        await connection.end();

        const phasesMap = {};
        rows.forEach(row => {
            if (!phasesMap[row.phase_id]) {
                phasesMap[row.phase_id] = {
                    id: row.phase_id, name: row.phase_name, type: row.type, format: row.format,
                    teams: [], groups: []
                };
            }
            if (row.team_id && !phasesMap[row.phase_id].teams.find(t => t.id === row.team_id)) {
                phasesMap[row.phase_id].teams.push({ id: row.team_id, name: row.team_name, groupId: row.group_id });
            }
            if (row.group_id && !phasesMap[row.phase_id].groups.find(g => g.id === row.group_id)) {
                phasesMap[row.phase_id].groups.push({ id: row.group_id, name: row.group_name });
            }
        });
        return res.status(200).json({ status: "success", data: { phases: Object.values(phasesMap) } });
    } catch (error) {
        return res.status(500).json({ status: "error", message: error.message });
    }
});

router.post('/phases/:phaseId/generate-schedule', async (req, res) => {
    try {
        const phaseId = req.params.phaseId;
        const { start_date, start_time, interval_hours, interval_minutes, teamIds } = req.body || {};

        const connection = await mysql.createConnection(dbConfig);
        try {
            const [phaseRows] = await connection.execute('SELECT season_id FROM phases WHERE id = ?', [phaseId]);
            const seasonId = phaseRows.length > 0 ? phaseRows[0].season_id : null;

            if (seasonId) {
                await importTeamsIntoSeasonAndAssignGroups(connection, seasonId, phaseId, teamIds);
            }
        } finally {
            try { await connection.end(); } catch (_) {}
        }

        const result = await generateScheduleForPhase(phaseId, { start_date, start_time, interval_hours, interval_minutes });
        return res.status(200).json({ 
            status: 'success', 
            message: `Đã tự động xếp thành công ${result.matchesCreated} trận đấu!`, 
            data: result
        });
    } catch (error) {
        console.error('Lỗi tự động xếp lịch:', error);
        return res.status(500).json({ status: 'error', message: error.message || 'Lỗi server khi xếp lịch tự động' });
    }
});

router.post('/seasons/:seasonId/auto-import-teams-and-schedule', async (req, res) => {
    const seasonId = req.params.seasonId;
    const { phaseId, teamIds, start_date, start_time, interval_hours, interval_minutes } = req.body || {};

    if (!seasonId) {
        return res.status(400).json({ status: 'error', message: 'Thiếu seasonId' });
    }

    try {
        let targetPhaseId = phaseId;
        const connection = await mysql.createConnection(dbConfig);
        try {
            if (!targetPhaseId) {
                const [phaseRows] = await connection.execute(
                    'SELECT id FROM phases WHERE season_id = ? ORDER BY `order` ASC LIMIT 1',
                    [seasonId]
                );
                targetPhaseId = phaseRows[0] ? phaseRows[0].id : null;
            }

            if (!targetPhaseId) {
                await connection.end();
                return res.status(404).json({ status: 'error', message: 'Không tìm thấy phase phù hợp cho mùa này.' });
            }

            await importTeamsIntoSeasonAndAssignGroups(connection, seasonId, targetPhaseId, teamIds);
        } finally {
            try { await connection.end(); } catch (_) {}
        }

        const result = await generateScheduleForPhase(targetPhaseId, { start_date, start_time, interval_hours, interval_minutes });

        return res.status(200).json({
            status: 'success',
            message: 'Đã tự động thêm đội vào mùa và xếp lịch thành công.',
            data: result
        });
    } catch (error) {
        console.error('Lỗi auto import teams and schedule:', error);
        return res.status(500).json({ status: 'error', message: error.message || 'Lỗi server khi tự động thêm đội và xếp lịch' });
    }
});
// 🌟 API: XÓA ĐỘI BÓNG KHỎI VÒNG ĐẤU (PHASE)
// Đường dẫn ứng với Retrofit: DELETE http://localhost:3000/api/phases/:phaseId/teams/:teamId
router.delete('/phases/:phaseId/teams/:teamId', async (req, res) => {
    const { phaseId, teamId } = req.params;
    let connection;

    try {
        connection = await mysql.createConnection(dbConfig);
        await connection.beginTransaction(); // Bắt đầu giao dịch hóa dữ liệu

        // 1. Kiểm tra vòng đấu (Phase) tồn tại và lấy định dạng giải
        const [phaseInfo] = await connection.execute('SELECT format, season_id FROM phases WHERE id = ?', [phaseId]);
        if (phaseInfo.length === 0) {
            await connection.rollback();
            return res.status(404).json({ success: false, message: "Vòng đấu không tồn tại." });
        }
        
        const { format, season_id } = phaseInfo[0];

        // 1.5 Kiểm tra nếu đã có trận đấu đã diễn ra hoặc có kết quả
        const [playedMatches] = await connection.execute(
            `SELECT id FROM matches
             WHERE phase_id = ? AND (home_team_id = ? OR away_team_id = ?) AND status != 'scheduled'`,
            [phaseId, teamId, teamId]
        );

        if (playedMatches.length > 0) {
            await connection.rollback();
            return res.status(200).json({
                success: false,
                message: "Hệ thống: Không thể xóa đội vì đã có trận đấu chính thức (đã diễn ra/kết thúc)!"
            });
        }

        // 2. Cập nhật trạng thái trong bảng season_teams (Bỏ gán nhóm/bảng đấu cho đội này)
        // Chỉ gỡ group_id nếu group đó thuộc về Phase này
        await connection.execute(
            `UPDATE season_teams 
             SET group_id = NULL 
             WHERE team_id = ? AND season_id = ?
             AND group_id IN (SELECT id FROM \`groups\` WHERE phase_id = ?)`,
            [teamId, season_id, phaseId]
        );

        // 3. Xử lý xóa cấu trúc dữ liệu theo định dạng giải
        if (format === 'round_robin') {
            // Lấy danh sách ID nhóm thuộc Phase hiện tại
            const [groups] = await connection.execute('SELECT id FROM `groups` WHERE phase_id = ?', [phaseId]);
            const groupIds = groups.map(g => g.id);

            if (groupIds.length > 0) {
                // Xóa đội khỏi bảng xếp hạng của các nhóm thuộc vòng đấu này
                const placeholders = groupIds.map(() => '?').join(',');
                await connection.execute(
                    `DELETE FROM team_standings
                     WHERE team_id = ? AND group_id IN (${placeholders})`,
                    [teamId, ...groupIds]
                );
            }
        } 
        else if (format === 'knockout') {
            // Tìm và xóa đội ra khỏi hạt giống của vòng 1 (đặt về NULL)
            await connection.execute(
                `UPDATE bracket_slots 
                 SET seeded_home_team_id = CASE WHEN seeded_home_team_id = ? THEN NULL ELSE seeded_home_team_id END,
                     seeded_away_team_id = CASE WHEN seeded_away_team_id = ? THEN NULL ELSE seeded_away_team_id END
                 WHERE phase_id = ? AND round = 1`,
                [teamId, teamId, phaseId]
            );

            // Cập nhật lại trạng thái is_bye cho các slot vòng 1 của phase này
            await connection.execute(
                `UPDATE bracket_slots
                 SET is_bye = CASE WHEN seeded_home_team_id IS NOT NULL AND seeded_away_team_id IS NULL THEN 1 ELSE 0 END
                 WHERE phase_id = ? AND round = 1`,
                [phaseId]
            );
        }

        // 4. Xóa các trận đấu chưa diễn ra của đội này trong Phase này (Dọn dẹp lịch thi đấu)
        await connection.execute(
            `DELETE FROM matches
             WHERE phase_id = ? AND (home_team_id = ? OR away_team_id = ?) AND status = 'scheduled'`,
            [phaseId, teamId, teamId]
        );

        await connection.commit(); // Hoàn tất giao dịch dữ liệu an toàn
        return res.status(200).json({ 
            success: true, 
            message: "Đã xóa đội bóng khỏi vòng đấu thành công!" 
        });

    } catch (error) {
        if (connection) await connection.rollback(); // Hoàn tác nếu gặp lỗi hệ thống
        console.error("Lỗi xóa đội khỏi Phase:", error);
        return res.status(500).json({ 
            success: false, 
            message: "Lỗi server khi xóa đội: " + error.message 
        });
    } finally {
        if (connection) await connection.end(); // Đóng kết nối DB
    }
});
// Trong file phases.js
router.post('/phases/:phaseId/add-team', async (req, res) => {
    const { phaseId } = req.params;
    const { teamId, groupId } = req.body; // teamId ở đây là Global Team ID
    let connection;

    try {
        connection = await mysql.createConnection(dbConfig);
        await connection.beginTransaction();

        // Cập nhật season_teams
        await connection.execute(
            `UPDATE season_teams SET group_id = ? 
             WHERE team_id = ? AND season_id = (SELECT season_id FROM phases WHERE id = ?)`, 
            [groupId || null, teamId, phaseId]
        );

        const [phaseInfo] = await connection.execute('SELECT format FROM phases WHERE id = ?', [phaseId]);
        const format = phaseInfo[0].format;

        if (format === 'round_robin') {
            // Xóa record cũ của đội này trong TOÀN BỘ các bảng thuộc Phase này (để dọn dẹp trước khi thêm/chuyển)
            await connection.execute(
                `DELETE FROM team_standings WHERE team_id = ? AND group_id IN (SELECT id FROM \`groups\` WHERE phase_id = ?)`,
                [teamId, phaseId]
            );

            // Thêm vào bảng mới
            await connection.execute(`
                INSERT INTO team_standings 
                (team_id, group_id, position, matches_played, wins, draws, losses, goals_for, goals_against, points, is_active, created_at) 
                VALUES (?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 1, NOW())`, 
                [teamId, groupId]
            );

    } else if (format === 'knockout') {
    // 1. Kiểm tra xem đội đã có trong nhánh này chưa (tránh trùng lặp)
    const [alreadyExists] = await connection.execute(
        'SELECT id FROM bracket_slots WHERE phase_id = ? AND (seeded_home_team_id = ? OR seeded_away_team_id = ?)',
        [phaseId, teamId, teamId]
    );

    if (alreadyExists.length > 0) {
        await connection.rollback();
        return res.status(200).json({ success: false, message: "Đội đã có trong nhánh đấu này rồi!" });
    }

    // 2. Tìm slot trống ở vòng 1 (round = 1)
    // Dùng COALESCE hoặc kiểm tra cả 0 và NULL để chắc chắn
    const [emptySlot] = await connection.execute(
        `SELECT id, seeded_home_team_id, seeded_away_team_id 
         FROM bracket_slots 
         WHERE phase_id = ? AND round = 1 
         AND (seeded_home_team_id IS NULL OR seeded_home_team_id = 0 OR seeded_away_team_id IS NULL OR seeded_away_team_id = 0) 
         LIMIT 1`,
        [phaseId]
    );

    if (emptySlot.length > 0) {
        const slot = emptySlot[0];
        
        // Nếu home chưa có (null hoặc 0), điền vào home, ngược lại điền vào away
        if (slot.seeded_home_team_id === null || slot.seeded_home_team_id === 0) {
            await connection.execute('UPDATE bracket_slots SET seeded_home_team_id = ? WHERE id = ?', [teamId, slot.id]);
        } else {
            await connection.execute('UPDATE bracket_slots SET seeded_away_team_id = ? WHERE id = ?', [teamId, slot.id]);
        }
    } else {
        await connection.rollback();
        return res.status(400).json({ success: false, message: "Không tìm thấy slot trống ở vòng 1!" });
    }
}
       await connection.commit(); // <--- CỰC KỲ QUAN TRỌNG: Phải có dòng này!
        return res.status(200).json({ success: true, message: "Cập nhật thành công!" });

    } catch (e) {
        if (connection) await connection.rollback();
        res.status(500).json({ success: false, message: e.message });
    } finally {
        if (connection) await connection.end();
    }
});
// Reusable function to generate schedule for a phase
async function generateScheduleForPhase(phaseId, options = {}) {
    const { start_date, start_time, interval_hours, interval_minutes } = options;
   
    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // 1. Lấy thông tin Phase và các bảng đấu của nó
        const [phaseRows] = await connection.execute(
            'SELECT id, season_id, format FROM phases WHERE id = ?',
            [phaseId]
        );
        if (phaseRows.length === 0) {
            throw new Error('Vòng đấu không tồn tại.');
        }
        const phase = phaseRows[0];
        const seasonId = phase.season_id;

        if (phase.format === 'round_robin') {
            const [groups] = await connection.execute(
                'SELECT id, name FROM `groups` WHERE phase_id = ?',
                [phaseId]
            );

            if (groups.length === 0) {
                return { matchesCreated: 0, reason: 'Không có bảng đấu nào trong vòng này.' };
            }

            // 2. Lấy toàn bộ các đội tham gia mùa giải này
            const [seasonTeams] = await connection.execute(
                'SELECT team_id FROM season_teams WHERE season_id = ? AND is_active = 1 AND status = "active"',
                [seasonId]
            );

            if (seasonTeams.length === 0) {
                return { matchesCreated: 0, reason: 'Không có đội bóng nào đã đăng ký mùa giải này.' };
            }

            // 3. XÁO TRỘN NGẪU NHIÊN TOÀN BỘ ĐỘI VÀ CHIA VÀO CÁC BẢNG (A, B, C...)
            const randomizedAllTeams = shuffleArray(seasonTeams.map(t => t.team_id));

            // Cập nhật group_id cho từng đội trong season_teams
            for (let i = 0; i < randomizedAllTeams.length; i++) {
                const teamId = randomizedAllTeams[i];
                const groupId = groups[i % groups.length].id;
                await connection.execute(
                    'UPDATE season_teams SET group_id = ? WHERE team_id = ? AND season_id = ?',
                    [groupId, teamId, seasonId]
                );

                // Đảm bảo đội có mặt trong bảng team_standings để tính điểm
                const [exists] = await connection.execute(
                    'SELECT id FROM team_standings WHERE team_id = ? AND group_id = ?',
                    [teamId, groupId]
                );
                if (exists.length === 0) {
                    await connection.execute(
                        `INSERT INTO team_standings (team_id, group_id, position, matches_played, wins, draws, losses, goals_for, goals_against, points, is_active, created_at)
                         VALUES (?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 1, NOW())`,
                        [teamId, groupId]
                    );
                }
            }

            // 4. TẠO LỊCH THI ĐẤU VÒNG TRÒN CHO TỪNG BẢNG
            let totalMatchesCreated = 0;
            let baseDate = start_date ? new Date(start_date) : new Date();
            if (!start_date) baseDate.setDate(baseDate.getDate() + 1); // Mặc định từ ngày mai

            if (start_time) {
                const [h, m] = start_time.split(':');
                baseDate.setHours(parseInt(h) || 18, parseInt(m) || 0, 0, 0);
            } else {
                baseDate.setHours(18, 0, 0, 0);
            }

            const intervalMs = ((interval_hours || 2) * 60 + (interval_minutes || 0)) * 60 * 1000;

            for (const group of groups) {
                // Xóa lịch cũ chưa đá của bảng này
                await clearScheduledMatchesForGroup(connection, phaseId, group.id);

                const [teamsInGroup] = await connection.execute(
                    'SELECT team_id FROM season_teams WHERE group_id = ? AND season_id = ? AND is_active = 1',
                    [group.id, seasonId]
                );

                const teamIdsInGroup = teamsInGroup.map(t => t.team_id);
                if (teamIdsInGroup.length < 2) continue;

                const schedule = buildRoundRobinMatches(teamIdsInGroup);
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

            return { matchesCreated: totalMatchesCreated };
        } else if (phase.format === 'knockout') {
            // LOGIC KNOCKOUT RANDOM & XỬ LÝ 3 ĐỘI
            // 1. Lấy danh sách đội đã được add vào vòng này (từ bracket_slots)
            const [slotsData] = await connection.execute(
                `SELECT DISTINCT team_id FROM (
                    SELECT seeded_home_team_id as team_id FROM bracket_slots WHERE phase_id = ? AND seeded_home_team_id IS NOT NULL
                    UNION
                    SELECT seeded_away_team_id as team_id FROM bracket_slots WHERE phase_id = ? AND seeded_away_team_id IS NOT NULL
                ) as teams`,
                [phaseId, phaseId]
            );

            let teamIds = slotsData.map(s => s.team_id).filter(id => id > 0);
            if (teamIds.length < 2) {
                return { matchesCreated: 0, reason: 'Cần ít nhất 2 đội để xếp lịch Knockout.' };
            }

            // 2. Random đội
            teamIds = shuffleArray(teamIds);

            // 3. Xóa dữ liệu cũ của vòng này (chỉ các trận chưa đá và bracket slots)
            await connection.execute('DELETE FROM bracket_slots WHERE phase_id = ?', [phaseId]);
            await connection.execute('DELETE FROM matches WHERE phase_id = ? AND status = "scheduled"', [phaseId]);

            // 4. Tạo nhánh đấu mới (Hàm createBracketSlots xử lý Bye cho số lẻ/3 đội)
            // 3 đội -> Power of 2 là 4. T1 vs T2 (Match), T3 vs Bye. Final: Winner vs T3.
            await createBracketSlots(connection, phaseId, teamIds);

            // 5. Tạo các trận đấu cho Vòng 1 (Round 1)
            const [round1Slots] = await connection.execute(
                'SELECT * FROM bracket_slots WHERE phase_id = ? AND round = 1 AND is_bye = 0',
                [phaseId]
            );

            let matchesCreatedCount = 0;
            let baseDate = start_date ? new Date(start_date) : new Date();
            if (!start_date) baseDate.setDate(baseDate.getDate() + 1);

            if (start_time) {
                const [h, m] = start_time.split(':');
                baseDate.setHours(parseInt(h) || 18, parseInt(m) || 0, 0, 0);
            } else {
                baseDate.setHours(18, 0, 0, 0);
            }

            const intervalMs = ((interval_hours || 2) * 60 + (interval_minutes || 0)) * 60 * 1000;

            for (const slot of round1Slots) {
                const matchDate = new Date(baseDate.getTime() + (matchesCreatedCount * intervalMs));
                const [mResult] = await connection.execute(
                    `INSERT INTO matches (phase_id, home_team_id, away_team_id, scheduled_at, status, season_id, created_at, updated_at)
                     VALUES (?, ?, ?, ?, 'scheduled', ?, NOW(), NOW())`,
                    [phaseId, slot.seeded_home_team_id, slot.seeded_away_team_id, matchDate, seasonId]
                );
                // Liên kết trận đấu vào slot
                await connection.execute('UPDATE bracket_slots SET match_id = ? WHERE id = ?', [mResult.insertId, slot.id]);
                matchesCreatedCount++;
            }

            return { matchesCreated: matchesCreatedCount };
        }
    } catch (err) {
        console.error("Lỗi khi xếp lịch:", err);
        throw err;
    } finally {
        if (connection) await connection.end();
    }
}

module.exports = router;

// Export helper for external callers (e.g., auto-scheduling after season creation)
module.exports.generateScheduleForPhase = generateScheduleForPhase;
module.exports.createGroupsAndAssignTeams = createGroupsAndAssignTeams;
module.exports.createBracketSlots = createBracketSlots;