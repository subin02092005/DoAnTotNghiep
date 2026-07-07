const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const { sendToAll } = require('../notification/notifications');
// Cấu hình kết nối Database
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

// Lấy danh sách giải đấu
router.get('/tournaments', async (req, res) => {
    try {
        const { name, is_active } = req.query;
        let query = `
            SELECT
                t.id,
                t.name,
                t.description,
                t.logo,
                tr.max_players_per_team AS max_players,
                tr.min_players_per_team AS min_players,
                t.is_active,
                t.created_at,
                t.updated_at
            FROM tournaments t
            LEFT JOIN tournament_rules tr
                ON tr.tournament_id = t.id
                AND tr.deleted_at IS NULL
                AND tr.is_active = 1
            WHERE t.is_active = 1
        `;
        const params = [];

        if (name) {
            query += ' AND t.name LIKE ?';
            params.push(`%${name}%`);
        }

        if (is_active !== undefined) {
            query += ' AND is_active = ?';
            params.push(Number(is_active));
        }

        query += ' ORDER BY created_at DESC';

        const [rows] = await pool.execute(query, params);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Xem chi tiết giải đấu và mùa giải liên quan
router.get('/tournaments/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const [tournaments] = await pool.execute(
            `SELECT
                t.id,
                t.name,
                t.description,
                t.logo,
                tr.max_players_per_team AS max_teams,
                t.is_active,
                t.created_at,
                t.updated_at
             FROM tournaments t
             LEFT JOIN tournament_rules tr
                ON tr.tournament_id = t.id
                AND tr.deleted_at IS NULL
                AND tr.is_active = 1
             WHERE t.id = ?`,
            [id]
        );

        if (tournaments.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy giải đấu.' });
        }

        const tournament = tournaments[0];

        const [seasons] = await pool.execute(
            `SELECT id, name, description, status, start_date, end_date, registration_deadline, is_registration_open, is_active
             FROM seasons
             WHERE tournament_id = ? AND deleted_at IS NULL
             ORDER BY start_date ASC`,
            [id]
        );

        res.json({ success: true, data: { tournament, seasons } });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Tạo giải đấu mới
router.post('/tournaments', async (req, res) => {
    // Thêm max_players và min_players vào đây
    const { name, description, logo,max_players, min_players ,  user_id} = req.body;

    const fields = [];
    const params = [];

    // Xây dựng danh sách các trường cần cập nhật động
    if (name !== undefined) {
        fields.push('name = ?');
        params.push(name);
    }
    if (description !== undefined) {
        fields.push('description = ?');
        params.push(description);
    }
    if (logo !== undefined) {
        fields.push('logo = ?');
        params.push(logo);
    }

    if (!name) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp tên giải đấu.' });
    }

    try {
        const [result] = await pool.execute(
            `INSERT INTO tournaments (name, description, logo, is_active, created_at, updated_at, user_id)
             VALUES (?, ?, ?, 1, NOW(), NOW(), ?)`,
            [name, description || null, logo || null, user_id || 1]
        );

        const tournamentId = result.insertId;

        // Giữ nguyên cấu trúc cũ, thay thế số 20 và 7 bằng biến (hoặc giá trị mặc định nếu không có)
        await pool.execute(
            `INSERT INTO tournament_rules (
                tournament_id, points_per_win, points_per_draw, points_per_loss,
                yellow_cards_suspension, max_players_per_team, min_players_per_team,
                teams_advance_per_group, tiebreaker_order, created_at, updated_at, deleted_at, user_id
             ) VALUES (?, 3, 1, 0, 3, ?, ?, 2, '["goal_difference","goals_scored","head_to_head"]', NOW(), NOW(), NULL, ?)`,
            [tournamentId, max_players || 20, min_players || 7, user_id || 1]
        );

        // 1. Lấy tên mùa giải từ DB
       let seasonName = "Mùa giải hiện tại";
        // Vì không còn season_id trong req.body, đoạn này sẽ luôn lấy giá trị mặc định
        // hoặc bạn có thể điều chỉnh logic tại đây nếu cần truy vấn theo cách khác.

        // 2. Gửi thông báo chi tiết
        const notifyTitle = `Giải đấu ${name} đã mở!`;
        const notifyBody = `Nằm trong khuôn khổ ${seasonName}. ${description || ''}`;
        await sendToAll(notifyTitle, notifyBody);

        res.status(201).json({ success: true, message: 'Tạo giải đấu thành công.', tournamentId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});
// Cập nhật giải đấu
router.put('/tournaments/:id', async (req, res) => {
    const { id } = req.params;
    const { name, description, logo, max_teams, is_active } = req.body;

    const fields = [];
    const params = [];

    if (name !== undefined) {
        fields.push('name = ?');
        params.push(name);
    }
    if (description !== undefined) {
        fields.push('description = ?');
        params.push(description);
    }
    if (logo !== undefined) {
        fields.push('logo = ?');
        params.push(logo);
    }
    if (is_active !== undefined) {
        fields.push('is_active = ?');
        params.push(is_active ? 1 : 0);
    }

    if (fields.length === 0 && max_teams === undefined) {
        return res.status(400).json({ success: false, message: 'Không có trường nào để cập nhật.' });
    }

    try {
        let result = { affectedRows: 1 };

        if (fields.length > 0) {
            params.push(id);
            const [updateResult] = await pool.execute(
                `UPDATE tournaments SET ${fields.join(', ')}, updated_at = NOW() WHERE id = ?`,
                params
            );
            result = updateResult;
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy giải đấu.' });
        }

        if (max_teams !== undefined) {
            const [existingRule] = await pool.execute(
                'SELECT id FROM tournament_rules WHERE tournament_id = ? AND deleted_at IS NULL',
                [id]
            );

            if (existingRule.length > 0) {
                await pool.execute(
                    `UPDATE tournament_rules
                     SET max_players_per_team = ?, updated_at = NOW()
                     WHERE id = ?`,
                    [max_teams, existingRule[0].id]
                );
            } else {
                await pool.execute(
                    `INSERT INTO tournament_rules (
                        tournament_id, points_per_win, points_per_draw, points_per_loss,
                        yellow_cards_suspension, max_players_per_team, min_players_per_team,
                        teams_advance_per_group, tiebreaker_order, created_at, updated_at, deleted_at, user_id
                     ) VALUES (?, 3, 1, 0, 3, ?, 11, 2, '["goal_difference","goals_scored","head_to_head"]', NOW(), NOW(), NULL, NULL)`,
                    [id, max_teams]
                );
            }
        }

        res.json({ success: true, message: 'Cập nhật giải đấu thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Tạo mùa giải (season) cho một giải đấu
router.post('/seasons', async (req, res) => {
    const { name, description, start_date, end_date, registration_deadline, tournament_id, is_registration_open, user_id, max_teams, registration_fee } = req.body;

    if (!name || !start_date || !end_date || !registration_deadline || !tournament_id || max_teams === undefined) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp tên, ngày bắt đầu, ngày kết thúc, hạn đăng ký, tournament_id và max_teams.' });
    }

    try {
        const [tournamentRows] = await pool.execute(
            'SELECT id FROM tournaments WHERE id = ? AND deleted_at IS NULL',
            [tournament_id]
        );

        if (tournamentRows.length === 0) {
            return res.status(404).json({ success: false, message: 'Giải đấu không tồn tại.' });
        }

        const [result] = await pool.execute(
            `INSERT INTO seasons (name, description, status, start_date, end_date, registration_deadline, is_registration_open, is_active, created_at, updated_at, deleted_at, tournament_id, user_id, max_teams, registration_fee)
             VALUES (?, ?, 'upcoming', ?, ?, ?, ?, 1, NOW(), NOW(), NULL, ?, ?, ?, ?)`,
            [name, description || null, start_date, end_date, registration_deadline, is_registration_open ? 1 : 0, tournament_id, user_id || null, max_teams, registration_fee || 0.00]
        );

        const seasonId = result.insertId;

        // If caller provided phases to create immediately, forward them to the phases API
        const createdPhases = [];
        if (Array.isArray(req.body.phases) && req.body.phases.length > 0) {
            const phasesModule = require('../phases');

            for (const phaseData of req.body.phases) {
                const connection = await pool.getConnection();
                try {
                    const {
                        name: pName,
                        type: pType,
                        format: pFormat,
                        order: pOrder,
                        start_date: pStartDate,
                        end_date: pEndDate,
                        group_count: pGroupCount,
                        groupCount: pGroupCountAlt,
                        group_names: pGroupNames,
                        groupNames: pGroupNamesAlt,
                        team_ids: pTeamIds,
                        teamIds: pTeamIdsAlt
                    } = phaseData;

                    const effectiveGroupCount = pGroupCount || pGroupCountAlt;
                    const effectiveGroupNames = pGroupNames || pGroupNamesAlt;
                    const effectiveTeamIds = pTeamIds || pTeamIdsAlt;

                    const [phaseResult] = await connection.execute(
                        `INSERT INTO phases (season_id, name, type, format, \`order\`, start_date, end_date, is_active, created_at, updated_at, status)
                         VALUES (?, ?, ?, ?, ?, ?, ?, 1, NOW(), NOW(), 'draft')`,
                        [seasonId, pName, pType, pFormat, pOrder, pStartDate || null, pEndDate || null]
                    );

                    const createdPhaseId = phaseResult.insertId;
                    const created = { input: phaseData, phaseId: createdPhaseId };

                    if (pFormat === 'round_robin') {
                        const { groupIds, teamIds: assignedTeams } = await phasesModule.createGroupsAndAssignTeams(connection, seasonId, createdPhaseId, effectiveGroupCount, effectiveGroupNames);
                        created.groups = groupIds;

                        if (assignedTeams && assignedTeams.length > 0) {
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

                    if (pFormat === 'knockout') {
                        const teamIdsList = Array.isArray(effectiveTeamIds) ? effectiveTeamIds : [];
                        if (teamIdsList.length >= 2) {
                            await phasesModule.createBracketSlots(connection, createdPhaseId, teamIdsList);
                        }
                    }

                    // Optionally auto-schedule this phase now
                    if (phaseData.autoSchedule && typeof phasesModule.generateScheduleForPhase === 'function') {
                        try {
                            await phasesModule.generateScheduleForPhase(createdPhaseId, phaseData.scheduleOptions || {});
                        } catch (schedErr) {
                            created.scheduleError = String(schedErr.message || schedErr);
                        }
                    }

                    createdPhases.push(created);
                } catch (e) {
                    createdPhases.push({ input: phaseData, error: e.message || String(e) });
                } finally {
                    try { connection.release(); } catch (_) {}
                }
            }
        }

        res.status(201).json({ success: true, message: 'Tạo mùa giải thành công.', seasonId, phases: createdPhases });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.post('/tournaments/:id/seasons', async (req, res) => {
    const { id } = req.params;
    const { name, description, start_date, end_date, registration_deadline, is_registration_open, user_id } = req.body;

    if (!name || !start_date || !end_date || !registration_deadline) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp tên, ngày bắt đầu, ngày kết thúc và hạn đăng ký.' });
    }

    try {
        const [result] = await pool.execute(
            `INSERT INTO seasons (name, description, status, start_date, end_date, registration_deadline, is_registration_open, is_active, created_at, updated_at, deleted_at, tournament_id, user_id)
             VALUES (?, ?, 'upcoming', ?, ?, ?, ?, 1, NOW(), NOW(), NULL, ?, ?)`,
            [name, description || null, start_date, end_date, registration_deadline, is_registration_open ? 1 : 0, id, user_id || null]
        );

        res.status(201).json({ success: true, message: 'Tạo mùa giải thành công.', seasonId: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Cấu hình rule cho mùa giải
router.put('/tournaments/:tournamentId/seasons/:seasonId/rules', async (req, res) => {
    const { seasonId } = req.params;
    const {
        points_per_win,
        points_per_draw,
        points_per_loss,
        yellow_cards_suspension,
        max_players_per_team,
        min_players_per_team,
        registration_fee,
        forfeit_score,
        teams_advance_per_group,
        tiebreaker_order,
        user_id
    } = req.body;

    try {
        const [existingRules] = await pool.execute(
            'SELECT id FROM season_rules WHERE season_id = ? AND deleted_at IS NULL',
            [seasonId]
        );

        const tiebreakerJson = tiebreaker_order ? JSON.stringify(tiebreaker_order) : JSON.stringify(['goal_difference', 'goals_scored', 'head_to_head']);

        if (existingRules.length > 0) {
            const ruleId = existingRules[0].id;
            const fields = [];
            const params = [];

            if (points_per_win !== undefined) { fields.push('points_per_win = ?'); params.push(points_per_win); }
            if (points_per_draw !== undefined) { fields.push('points_per_draw = ?'); params.push(points_per_draw); }
            if (points_per_loss !== undefined) { fields.push('points_per_loss = ?'); params.push(points_per_loss); }
            if (yellow_cards_suspension !== undefined) { fields.push('yellow_cards_suspension = ?'); params.push(yellow_cards_suspension); }
            if (max_players_per_team !== undefined) { fields.push('max_players_per_team = ?'); params.push(max_players_per_team); }
            if (min_players_per_team !== undefined) { fields.push('min_players_per_team = ?'); params.push(min_players_per_team); }
            if (registration_fee !== undefined) { fields.push('registration_fee = ?'); params.push(registration_fee); }
            if (forfeit_score !== undefined) { fields.push('forfeit_score = ?'); params.push(forfeit_score); }
            if (teams_advance_per_group !== undefined) { fields.push('teams_advance_per_group = ?'); params.push(teams_advance_per_group); }
            if (tiebreaker_order !== undefined) { fields.push('tiebreaker_order = ?'); params.push(tiebreakerJson); }
            if (user_id !== undefined) { fields.push('updated_by_id = ?'); params.push(user_id); }

            if (fields.length === 0) {
                return res.status(400).json({ success: false, message: 'Không có trường nào để cập nhật.' });
            }

            fields.push('updated_at = NOW()');
            params.push(ruleId);

            await pool.execute(
                `UPDATE season_rules SET ${fields.join(', ')} WHERE id = ?`,
                params
            );
        } else {
            await pool.execute(
                `INSERT INTO season_rules (
                    season_id, points_per_win, points_per_draw, points_per_loss,
                    yellow_cards_suspension, max_players_per_team, min_players_per_team,
                    registration_fee, forfeit_score, teams_advance_per_group,
                    tiebreaker_order, created_at, updated_at, deleted_at, user_id)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), NULL, ?)`,
                [seasonId,
                    points_per_win || 3,
                    points_per_draw || 1,
                    points_per_loss || 0,
                    yellow_cards_suspension || 3,
                    max_players_per_team || 25,
                    min_players_per_team || 11,
                    registration_fee || 0.00,
                    forfeit_score || 3,
                    teams_advance_per_group || 2,
                    tiebreakerJson,
                    user_id || null]
            );
        }

        res.json({ success: true, message: 'Cập nhật quy tắc mùa giải thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Mở đăng ký cho mùa giải
router.patch('/tournaments/:tournamentId/seasons/:seasonId/open-registration', async (req, res) => {
    const { seasonId } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE seasons SET is_registration_open = 1, status = 'registration_open', updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
            [seasonId]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy mùa giải.' });
        }

        res.json({ success: true, message: 'Mở đăng ký mùa giải thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Đóng đăng ký cho mùa giải
router.patch('/tournaments/:tournamentId/seasons/:seasonId/close-registration', async (req, res) => {
    const { seasonId } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE seasons SET is_registration_open = 0, status = 'upcoming', updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
            [seasonId]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy mùa giải.' });
        }

        res.json({ success: true, message: 'Đóng đăng ký mùa giải thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
