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

// Lấy danh sách đội bóng hoặc tra cứu theo tên/HLV/trạng thái
router.get('/teams', async (req, res) => {
    try {
        const { name, coach_name, is_active } = req.query;
        let query = `
            SELECT id, name, coach_name, logo, description, is_active,
                   created_at, updated_at
            FROM teams
            WHERE is_deleted = 0
        `;
        const params = [];

        if (name) {
            query += ' AND name LIKE ?';
            params.push(`%${name}%`);
        }

        if (coach_name) {
            query += ' AND coach_name LIKE ?';
            params.push(`%${coach_name}%`);
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

// Xem chi tiết đội bóng theo id, kèm leader và cầu thủ
router.get('/teams/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const [teams] = await pool.execute(
            `SELECT id, name, coach_name, logo, description, is_active, created_at, updated_at, deleted_at, user_id
             FROM teams
             WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (teams.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng.' });
        }

        const team = teams[0];

        const [leaders] = await pool.execute(
            `SELECT tl.id, tl.user_id, u.name, u.email, u.phone, tl.created_at
             FROM team_leaders tl
             JOIN users u ON tl.user_id = u.id
             WHERE tl.team_id = ? AND tl.is_deleted = 0`,
            [id]
        );

        const [players] = await pool.execute(
            `SELECT tp.id, tp.jersey_number, tp.position, tp.role, tp.status, tp.approval_status,
                    pl.id AS player_id, pl.user_id AS player_user_id,
                    u.name AS player_name, u.email AS player_email, u.phone AS player_phone
             FROM team_players tp
             JOIN players pl ON tp.player_id = pl.id
             LEFT JOIN users u ON pl.user_id = u.id
             WHERE tp.team_id = ? AND tp.is_deleted = 0`,
            [id]
        );

        res.json({ success: true, data: { team, leaders, players } });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Duyệt đăng ký đội bóng
router.patch('/teams/:id/approve', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE teams
             SET is_active = 1, updated_at = NOW()
             WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng hoặc đội đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã duyệt đăng ký đội bóng.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Từ chối đăng ký đội bóng
router.patch('/teams/:id/reject', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE teams
             SET is_deleted = 1, updated_at = NOW()
             WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng hoặc đội đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã từ chối đăng ký đội bóng.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;

