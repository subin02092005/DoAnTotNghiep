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

module.exports = router;

module.exports = router;