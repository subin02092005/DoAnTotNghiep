const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

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
        let query = `SELECT id, name, description, logo, max_teams, is_active, created_at, updated_at FROM tournaments WHERE is_active = 1`;
        const params = [];

        if (name) {
            query += ' AND name LIKE ?';
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
            `SELECT id, name, description, logo, max_teams, is_active, created_at, updated_at
             FROM tournaments
             WHERE id = ?`,
            [id]
        );

        if (tournaments.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy giải đấu.' });
        }

        const tournament = tournaments[0];

        const [seasons] = await pool.execute(
            `SELECT id, name, description, status, start_date, end_date, registration_deadline, is_registration_open, is_active
             FROM seasons
             WHERE tournament_id = ? AND is_deleted = 0
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
    const { name, description, logo, max_teams, user_id } = req.body;

    if (!name || !max_teams) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp tên giải đấu và số đội tối đa.' });
    }

    try {
        const [result] = await pool.execute(
            `INSERT INTO tournaments (name, description, logo, max_teams, is_active, created_at, updated_at, user_id)
             VALUES (?, ?, ?, ?, 1, NOW(), NOW(), ?)`,
            [name, description || null, logo || null, max_teams, user_id || null]
        );

        res.status(201).json({ success: true, message: 'Tạo giải đấu thành công.', tournamentId: result.insertId });
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
    if (max_teams !== undefined) {
        fields.push('max_teams = ?');
        params.push(max_teams);
    }
    if (is_active !== undefined) {
        fields.push('is_active = ?');
        params.push(is_active ? 1 : 0);
    }

    if (fields.length === 0) {
        return res.status(400).json({ success: false, message: 'Không có trường nào để cập nhật.' });
    }

    params.push(id);

    try {
        const [result] = await pool.execute(
            `UPDATE tournaments SET ${fields.join(', ')}, updated_at = NOW() WHERE id = ?`,
            params
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy giải đấu.' });
        }

        res.json({ success: true, message: 'Cập nhật giải đấu thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Tạo mùa giải (season) cho một giải đấu
router.post('/tournaments/:id/seasons', async (req, res) => {
    const { id } = req.params;
    const { name, description, start_date, end_date, registration_deadline, is_registration_open, user_id } = req.body;

    if (!name || !start_date || !end_date || !registration_deadline) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp tên, ngày bắt đầu, ngày kết thúc và hạn đăng ký.' });
    }

    try {
        const [result] = await pool.execute(
            `INSERT INTO seasons (name, description, status, start_date, end_date, registration_deadline, is_registration_open, is_active, created_at, updated_at, is_deleted, tournament_id, user_id)
             VALUES (?, ?, 'upcoming', ?, ?, ?, ?, 1, NOW(), NOW(), 0, ?, ?)`,
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
            'SELECT id FROM season_rules WHERE season_id = ? AND is_deleted = 0',
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
                    tiebreaker_order, created_at, updated_at, is_deleted, user_id)
                 VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), 0, ?)`,
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
            `UPDATE seasons SET is_registration_open = 1, status = 'registration_open', updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
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
            `UPDATE seasons SET is_registration_open = 0, status = 'upcoming', updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
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
