const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình kết nối Database của bạn
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

// Lấy danh sách lịch thi đấu
router.get('/matches', async (req, res) => {
    try {
        const { teamId, status, phaseId, seasonId, dateFrom, dateTo } = req.query;
        let query = `
            SELECT m.id, m.phase_id, m.group_id, m.home_team_id, ht.name AS home_team_name,
                   m.away_team_id, at.name AS away_team_name, m.scheduled_at, m.played_at,
                   m.home_score, m.away_score, m.status, m.round, m.leg, m.venue_id,
                   m.referee, m.season_id, m.is_published, m.created_at, m.updated_at
            FROM matches m
            LEFT JOIN teams ht ON m.home_team_id = ht.id
            LEFT JOIN teams at ON m.away_team_id = at.id
            WHERE m.is_deleted = 0
        `;
        const params = [];

        if (teamId) {
            query += ' AND (m.home_team_id = ? OR m.away_team_id = ?)';
            params.push(teamId, teamId);
        }

        if (status) {
            query += ' AND m.status = ?';
            params.push(status);
        }

        if (phaseId) {
            query += ' AND m.phase_id = ?';
            params.push(phaseId);
        }

        if (seasonId) {
            query += ' AND m.season_id = ?';
            params.push(seasonId);
        }

        if (dateFrom) {
            query += ' AND m.scheduled_at >= ?';
            params.push(dateFrom);
        }

        if (dateTo) {
            query += ' AND m.scheduled_at <= ?';
            params.push(dateTo);
        }

        query += ' ORDER BY m.scheduled_at ASC';

        const [rows] = await pool.execute(query, params);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Xem chi tiết một trận đấu
router.get('/matches/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const [matches] = await pool.execute(
            `SELECT m.id, m.phase_id, m.group_id, m.home_team_id, ht.name AS home_team_name,
                    m.away_team_id, at.name AS away_team_name, m.scheduled_at, m.played_at,
                    m.home_score, m.away_score, m.status, m.round, m.leg, m.venue_id,
                    m.referee, m.season_id, m.is_published, m.created_at, m.updated_at
             FROM matches m
             LEFT JOIN teams ht ON m.home_team_id = ht.id
             LEFT JOIN teams at ON m.away_team_id = at.id
             WHERE m.id = ? AND m.is_deleted = 0`,
            [id]
        );

        if (matches.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        res.json({ success: true, data: matches[0] });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Tạo lịch thi đấu mới
router.post('/matches', async (req, res) => {
    const {
        phase_id,
        group_id,
        home_team_id,
        away_team_id,
        scheduled_at,
        venue_id,
        round,
        leg,
        referee,
        season_id,
        is_published
    } = req.body;

    if (!home_team_id || !away_team_id || !scheduled_at) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp home_team_id, away_team_id và scheduled_at.' });
    }

    if (home_team_id === away_team_id) {
        return res.status(400).json({ success: false, message: 'Đội nhà và đội khách không được giống nhau.' });
    }

    try {
        const [result] = await pool.execute(
            `INSERT INTO matches (
                phase_id, group_id, home_team_id, away_team_id, scheduled_at,
                venue_id, round, leg, referee, season_id, is_published, created_at, updated_at
             ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())`,
            [phase_id || null, group_id || null, home_team_id, away_team_id, scheduled_at,
                venue_id || null, round || null, leg || null, referee || null, season_id || null,
                is_published ? 1 : 0]
        );

        res.status(201).json({ success: true, message: 'Tạo lịch thi đấu thành công.', matchId: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Chỉnh sửa thông tin trận đấu
router.put('/matches/:id', async (req, res) => {
    const { id } = req.params;
    const {
        phase_id,
        group_id,
        home_team_id,
        away_team_id,
        scheduled_at,
        played_at,
        home_score,
        away_score,
        status,
        round,
        leg,
        venue_id,
        referee,
        season_id,
        is_published
    } = req.body;

    try {
        const fields = [];
        const params = [];

        if (phase_id !== undefined) {
            fields.push('phase_id = ?');
            params.push(phase_id);
        }
        if (group_id !== undefined) {
            fields.push('group_id = ?');
            params.push(group_id);
        }
        if (home_team_id !== undefined) {
            fields.push('home_team_id = ?');
            params.push(home_team_id);
        }
        if (away_team_id !== undefined) {
            fields.push('away_team_id = ?');
            params.push(away_team_id);
        }
        if (scheduled_at !== undefined) {
            fields.push('scheduled_at = ?');
            params.push(scheduled_at);
        }
        if (played_at !== undefined) {
            fields.push('played_at = ?');
            params.push(played_at);
        }
        if (home_score !== undefined) {
            fields.push('home_score = ?');
            params.push(home_score);
        }
        if (away_score !== undefined) {
            fields.push('away_score = ?');
            params.push(away_score);
        }
        if (status !== undefined) {
            fields.push('status = ?');
            params.push(status);
        }
        if (round !== undefined) {
            fields.push('round = ?');
            params.push(round);
        }
        if (leg !== undefined) {
            fields.push('leg = ?');
            params.push(leg);
        }
        if (venue_id !== undefined) {
            fields.push('venue_id = ?');
            params.push(venue_id);
        }
        if (referee !== undefined) {
            fields.push('referee = ?');
            params.push(referee);
        }
        if (season_id !== undefined) {
            fields.push('season_id = ?');
            params.push(season_id);
        }
        if (is_published !== undefined) {
            fields.push('is_published = ?');
            params.push(is_published ? 1 : 0);
        }

        if (fields.length === 0) {
            return res.status(400).json({ success: false, message: 'Không có trường nào để cập nhật.' });
        }

        params.push(id);

        const [result] = await pool.execute(
            `UPDATE matches SET ${fields.join(', ')}, updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
            params
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu hoặc đã bị xóa.' });
        }

        res.json({ success: true, message: 'Cập nhật lịch thi đấu thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Hủy trận đấu
router.patch('/matches/:id/cancel', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE matches SET status = 'cancelled', updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu hoặc đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã hủy trận đấu.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Dời trận đấu
router.patch('/matches/:id/reschedule', async (req, res) => {
    const { id } = req.params;
    const { scheduled_at, venue_id } = req.body;

    if (!scheduled_at && !venue_id) {
        return res.status(400).json({ success: false, message: 'Vui lòng gửi scheduled_at hoặc venue_id để dời trận.' });
    }

    try {
        const fields = [];
        const params = [];

        if (scheduled_at !== undefined) {
            fields.push('scheduled_at = ?');
            params.push(scheduled_at);
            fields.push('status = ?');
            params.push('scheduled');
        }
        if (venue_id !== undefined) {
            fields.push('venue_id = ?');
            params.push(venue_id);
        }

        params.push(id);

        const [result] = await pool.execute(
            `UPDATE matches SET ${fields.join(', ')}, updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
            params
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu hoặc đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã dời trận đấu thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
