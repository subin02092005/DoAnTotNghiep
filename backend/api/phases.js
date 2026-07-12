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
    // 1. Tìm các trận đấu cần xóa (scheduled hoặc cancelled)
    const [rows] = await connection.execute(
        "SELECT id FROM matches WHERE phase_id = ? AND group_id = ? AND status IN ('scheduled', 'cancelled')",
        [phaseId, groupId]
    );
    const matchIds = rows.map(r => r.id);

    if (matchIds.length > 0) {
        const placeholders = matchIds.map(() => '?').join(',');
        // 2. Xóa các dữ liệu phụ thuộc để tránh lỗi khóa ngoại
        await connection.execute(`DELETE FROM match_events WHERE match_id IN (${placeholders})`, matchIds);
        await connection.execute(`DELETE FROM match_results WHERE match_id IN (${placeholders})`, matchIds);
        // 3. Xóa chính các trận đấu đó
        await connection.execute(
            `DELETE FROM matches WHERE id IN (${placeholders})`,
            matchIds
        );
    }
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

// 🌟 API: TẠO VÒNG ĐẤU MỚI (PHASE)
router.post('/seasons/:seasonId/phases', async (req, res) => {
    const seasonIdStr = req.params.seasonId;
    const { name, type, format, order, groupCount, group_count } = req.body;
    let connection;

    const seasonId = parseInt(seasonIdStr);
    console.log("DEBUG: Nhận yêu cầu tạo Phase:", { seasonId, name, type, format, order });

    if (isNaN(seasonId)) {
        return res.status(400).json({ success: false, message: "ID mùa giải không hợp lệ" });
    }

    try {
        connection = await mysql.createConnection(dbConfig);
        await connection.beginTransaction();

        // 1. Chèn vào bảng phases
        const [phaseResult] = await connection.execute(
            `INSERT INTO phases (season_id, name, type, format, \`order\`, is_active, created_at, updated_at, status)
             VALUES (?, ?, ?, ?, ?, 1, NOW(3), NOW(3), 'draft')`,
            [seasonId, name, type, format, parseInt(order) || 1]
        );
        const phaseId = phaseResult.insertId;

        // 2. Khởi tạo cấu trúc dựa trên định dạng
        if (format === 'round_robin') {
            const count = parseInt(groupCount || group_count) || 1;
            for (let i = 0; i < count; i++) {
                await connection.execute(
                    'INSERT INTO `groups` (phase_id, name, is_active, created_at, updated_at, status) VALUES (?, ?, 1, NOW(3), NOW(3), \'DRAFT\')',
                    [phaseId, `Bảng ${String.fromCharCode(65 + i)}`]
                );
            }
        } else if (format === 'knockout') {
            // TẠO BẢNG ẢO ĐỂ HIỂN THỊ TRÊN APP (Cho phép kéo thả)
            let groupName = "NHÁNH ĐẤU";
            if (type === 'quarter_final') groupName = "NHÁNH ĐẤU TỨ KẾT (8 ĐỘI)";
            else if (type === 'semi_final') groupName = "NHÁNH ĐẤU BÁN KẾT (4 ĐỘI)";
            else if (type === 'final') groupName = "TRẬN CHUNG KẾT (2 ĐỘI)";
            else if (type === 'third_place') groupName = "TRANH HẠNG BA";

            await connection.execute(
                'INSERT INTO `groups` (phase_id, name, is_active, created_at, updated_at, status) VALUES (?, ?, 1, NOW(3), NOW(3), \'DRAFT\')',
                [phaseId, groupName]
            );

            try {
                // Xác định số đội dựa trên loại vòng đấu
                let teamCount = 8;
                if (type === 'round_of_16') teamCount = 16;
                else if (type === 'quarter_final') teamCount = 8;
                else if (type === 'semi_final') teamCount = 4;
                else if (type === 'final' || type === 'third_place') teamCount = 2;

                console.log(`DEBUG: Tạo ${teamCount} slots cho vòng knockout ${type}`);
                await createBracketSlots(connection, phaseId, Array(teamCount).fill(null));
            } catch (bracketErr) {
                console.error("CẢNH BÁO: Lỗi tạo bracket_slots:", bracketErr.message);
            }
        }

        await connection.commit();
        console.log("DEBUG: Tạo Phase thành công ID:", phaseId);
        return res.status(201).json({ success: true, message: "Tạo vòng đấu thành công!", phaseId });

    } catch (error) {
        if (connection) await connection.rollback();
        console.error("LỖI TẠO PHASE CHI TIẾT:", error);
        return res.status(500).json({
            success: false,
            message: "Lỗi Server: " + error.message,
            sqlMessage: error.sqlMessage
        });
    } finally {
        if (connection) await connection.end();
    }
});

// 🌟 API: XÓA VÒNG ĐẤU (PHASE) - RESET TOÀN BỘ
router.delete('/phases/:id', async (req, res) => {
    const phaseId = req.params.id;
    let connection;

    try {
        connection = await mysql.createConnection(dbConfig);
        await connection.beginTransaction();

        // 1. Lấy danh sách các trận đấu thuộc phase này
        const [matchRows] = await connection.execute(
            "SELECT id FROM matches WHERE phase_id = ?",
            [phaseId]
        );
        const matchIds = matchRows.map(m => m.id);

        if (matchIds.length > 0) {
            const placeholders = matchIds.map(() => '?').join(',');
            // Xóa sự kiện trận đấu
            await connection.execute(`DELETE FROM match_events WHERE match_id IN (${placeholders})`, matchIds);
            // Xóa kết quả trận đấu
            await connection.execute(`DELETE FROM match_results WHERE match_id IN (${placeholders})`, matchIds);
            // Xóa chính các trận đấu
            await connection.execute(`DELETE FROM matches WHERE phase_id = ?`, [phaseId]);
        }

        // 2. Reset trạng thái đội bóng trong mùa giải (Đưa về trạng thái chưa xếp bảng)
        // Chỉ reset cho những đội thuộc bảng của phase bị xóa
        await connection.execute(
            `UPDATE season_teams
             SET group_id = NULL
             WHERE group_id IN (SELECT id FROM \`groups\` WHERE phase_id = ?)`,
            [phaseId]
        );

        // 3. Xóa dữ liệu bảng xếp hạng và các bảng đấu
        await connection.execute("DELETE FROM team_standings WHERE group_id IN (SELECT id FROM `groups` WHERE phase_id = ?)", [phaseId]);
        await connection.execute("DELETE FROM `groups` WHERE phase_id = ?", [phaseId]);
        await connection.execute("DELETE FROM bracket_slots WHERE phase_id = ?", [phaseId]);

        // 4. Cuối cùng xóa chính vòng đấu
        const [result] = await connection.execute("DELETE FROM phases WHERE id = ?", [phaseId]);

        if (result.affectedRows === 0) {
            await connection.rollback();
            return res.status(404).json({ success: false, message: "Vòng đấu không tồn tại." });
        }

        await connection.commit();
        console.log(`DEBUG: Đã reset và xóa Phase ${phaseId} thành công.`);
        return res.status(200).json({ success: true, message: "Đã xóa và reset dữ liệu vòng đấu thành công!" });

    } catch (error) {
        if (connection) await connection.rollback();
        console.error("Lỗi Reset Phase:", error);
        return res.status(500).json({ success: false, message: "Lỗi Server: " + error.message });
    } finally {
        if (connection) await connection.end();
    }
});

// 🌟 API: LẤY CÂY NHÁNH ĐẤU (BRACKET) CHO MÙA GIẢI
router.get('/seasons/:seasonId/knockout-bracket', async (req, res) => {
    const { seasonId } = req.params;
    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);
        // Tìm các vòng knockout của mùa giải này
        const [phases] = await connection.execute(
            "SELECT id, name, type, format FROM phases WHERE season_id = ? AND format = 'knockout' ORDER BY `order` ASC",
            [seasonId]
        );

        const result = [];
        for (const phase of phases) {
            // Lấy thông tin trận đấu và đội bóng từ bracket_slots
            const [slots] = await connection.execute(
                `SELECT
                    bs.id, bs.round, bs.slot_number, bs.is_bye,
                    m.id as match_id, m.home_score, m.away_score, m.status as match_status,
                    t1.name as home_team_name, t2.name as away_team_name
                FROM bracket_slots bs
                LEFT JOIN matches m ON bs.match_id = m.id
                LEFT JOIN teams t1 ON (bs.seeded_home_team_id = t1.id OR (m.home_team_id = t1.id AND bs.match_id IS NOT NULL))
                LEFT JOIN teams t2 ON (bs.seeded_away_team_id = t2.id OR (m.away_team_id = t2.id AND bs.match_id IS NOT NULL))
                WHERE bs.phase_id = ?
                ORDER BY bs.round ASC, bs.slot_number ASC`,
                [phase.id]
            );

            result.push({
                phaseId: phase.id,
                phaseName: phase.name,
                phaseType: phase.type,
                slots: slots
            });
        }

        return res.status(200).json({ success: true, data: result });
    } catch (error) {
        console.error("Lỗi lấy bracket:", error);
        return res.status(500).json({ success: false, message: error.message });
    } finally {
        if (connection) await connection.end();
    }
});

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

        const [phaseInfo] = await connection.execute('SELECT format FROM phases WHERE id = ?', [phaseId]);
        if (phaseInfo.length === 0) throw new Error("Vòng đấu không tồn tại");
        const format = phaseInfo[0].format;

        // Tự động tìm groupId nếu là knockout và không gửi groupId lên
        let targetGroupId = groupId;
        if (!targetGroupId && format === 'knockout') {
            const [groups] = await connection.execute('SELECT id FROM `groups` WHERE phase_id = ? LIMIT 1', [phaseId]);
            if (groups.length > 0) targetGroupId = groups[0].id;
        }

        // Cập nhật season_teams
        await connection.execute(
            `UPDATE season_teams SET group_id = ? 
             WHERE team_id = ? AND season_id = (SELECT season_id FROM phases WHERE id = ?)`, 
            [targetGroupId || null, teamId, phaseId]
        );

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
                VALUES (?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 1, NOW(3))`,
                [teamId, groupId]
            );
        } else if (format === 'knockout') {
            // 0. Đảm bảo đội có trong team_standings của bảng ảo để hiện lên BXH nếu cần
            if (targetGroupId) {
                await connection.execute(
                    `DELETE FROM team_standings WHERE team_id = ? AND group_id IN (SELECT id FROM \`groups\` WHERE phase_id = ?)`,
                    [teamId, phaseId]
                );
                await connection.execute(
                    `INSERT INTO team_standings (team_id, group_id, position, matches_played, wins, draws, losses, goals_for, goals_against, points, is_active, created_at)
                     VALUES (?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 1, NOW(3))`,
                    [teamId, targetGroupId]
                );
            }

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
        console.log(`DEBUG: Bắt đầu xếp lịch cho Phase ID: ${phaseId}`);

        // 1. Lấy thông tin Phase
        const [phaseRows] = await connection.execute(
            'SELECT id, season_id, format, type FROM phases WHERE id = ?',
            [phaseId]
        );
        if (phaseRows.length === 0) throw new Error('Vòng đấu không tồn tại.');

        const phase = phaseRows[0];
        const seasonId = phase.season_id;

        // 2. Lấy tất cả Group thuộc Phase này
        const [groups] = await connection.execute(
            'SELECT id, name FROM `groups` WHERE phase_id = ?',
            [phaseId]
        );

        if (groups.length === 0) {
            return { matchesCreated: 0, reason: 'Vòng đấu này chưa có bảng đấu nào.' };
        }

        const groupIds = groups.map(g => g.id);
        const placeholders = groupIds.map(() => '?').join(',');

        // 3. Lấy danh sách đội ĐÃ ĐƯỢC PHÂN VÀO CÁC BẢNG của Phase này
        const [teamsInPhase] = await connection.execute(
            `SELECT team_id, group_id FROM season_teams
             WHERE group_id IN (${placeholders}) AND season_id = ? AND is_active = 1`,
            [...groupIds, seasonId]
        );

        if (teamsInPhase.length < 2) {
            console.log("DEBUG: Không đủ đội trong các bảng để xếp lịch. Số lượng:", teamsInPhase.length);
            return { matchesCreated: 0, reason: 'Cần ít nhất 2 đội đã được xếp vào bảng để tạo lịch.' };
        }

        let totalMatchesCreated = 0;
        let baseDate = start_date ? new Date(start_date) : new Date();
        if (!start_date) baseDate.setDate(baseDate.getDate() + 1);

        if (start_time) {
            const [h, m] = start_time.split(':');
            baseDate.setHours(parseInt(h) || 18, parseInt(m) || 0, 0, 0);
        } else {
            baseDate.setHours(18, 0, 0, 0);
        }

        const intervalMs = ((interval_hours || 2) * 60 + (interval_minutes || 0)) * 60 * 1000;

        // --- XỬ LÝ VÒNG TRÒN (ROUND ROBIN) ---
        if (phase.format === 'round_robin') {
            for (const group of groups) {
                console.log(`DEBUG: Đang xử lý bảng: ${group.name}`);
                await clearScheduledMatchesForGroup(connection, phaseId, group.id);

                const teamIdsInGroup = teamsInPhase
                    .filter(t => t.group_id === group.id)
                    .map(t => t.team_id);

                if (teamIdsInGroup.length < 2) continue;

                const schedule = buildRoundRobinMatches(teamIdsInGroup);
                const randomizedSchedule = shuffleArray(schedule);

                for (const matchInfo of randomizedSchedule) {
                    const matchDate = new Date(baseDate.getTime() + (totalMatchesCreated * intervalMs));
                    await connection.execute(
                        `INSERT INTO matches (phase_id, group_id, home_team_id, away_team_id, scheduled_at, status, season_id, created_at, updated_at)
                         VALUES (?, ?, ?, ?, ?, 'scheduled', ?, NOW(3), NOW(3))`,
                        [phaseId, group.id, matchInfo.home, matchInfo.away, matchDate, seasonId]
                    );
                    totalMatchesCreated++;
                }

                await connection.execute(
                    'UPDATE `groups` SET status = "SCHEDULED", scheduleGeneratedAt = NOW(3) WHERE id = ?',
                    [group.id]
                );
            }
        }
        // --- XỬ LÝ LOẠI TRỰC TIẾP (KNOCKOUT) ---
        else if (phase.format === 'knockout') {
            console.log("DEBUG: Đang xử lý xếp lịch Knockout...");

            // Xóa lịch cũ và slots cũ
            await connection.execute('DELETE FROM bracket_slots WHERE phase_id = ?', [phaseId]);
            await connection.execute('DELETE FROM matches WHERE phase_id = ? AND status IN ("scheduled", "cancelled")', [phaseId]);

            const allTeamIds = shuffleArray(teamsInPhase.map(t => t.team_id));

            // Tạo khung đấu mới
            await createBracketSlots(connection, phaseId, allTeamIds);

            // Lấy các slot vòng 1 để tạo trận đấu thực tế
            const [round1Slots] = await connection.execute(
                'SELECT * FROM bracket_slots WHERE phase_id = ? AND round = 1 AND is_bye = 0',
                [phaseId]
            );

            for (const slot of round1Slots) {
                const matchDate = new Date(baseDate.getTime() + (totalMatchesCreated * intervalMs));
                const [mResult] = await connection.execute(
                    `INSERT INTO matches (phase_id, group_id, home_team_id, away_team_id, scheduled_at, status, season_id, created_at, updated_at)
                     VALUES (?, ?, ?, ?, ?, 'scheduled', ?, NOW(3), NOW(3))`,
                    [phaseId, slot.group_id || groupIds[0], slot.seeded_home_team_id, slot.seeded_away_team_id, matchDate, seasonId]
                );

                await connection.execute('UPDATE bracket_slots SET match_id = ? WHERE id = ?', [mResult.insertId, slot.id]);
                totalMatchesCreated++;
            }
        }

        console.log(`DEBUG: Hoàn tất! Đã tạo ${totalMatchesCreated} trận đấu.`);
        return { matchesCreated: totalMatchesCreated };

    } catch (err) {
        console.error("LỖI XẾP LỊCH CHI TIẾT:", err);
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