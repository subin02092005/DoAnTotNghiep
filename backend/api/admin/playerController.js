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

// 1. TRA CỨU CẦU THỦ
// Lấy danh sách cầu thủ, có thể truyền query param ?name=...
// =========================================================
router.get('/players', async (req, res) => {
    try {
        const { name } = req.query;
        let query = `
            SELECT p.id as player_id, p.date_of_birth, p.position, p.height, p.weight, 
                   p.nationality, p.is_active as player_active,
                   u.id as user_id, u.name, u.email, u.phone, u.is_active as user_active
            FROM players p
            JOIN users u ON p.user_id = u.id
            WHERE p.is_deleted = 0
        `;
        const params = [];

        if (name) {
            query += ` AND u.name LIKE ?`;
            params.push(`%${name}%`);
        }

        const [rows] = await pool.execute(query, params);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =========================================================
// 2. KHÓA TÀI KHOẢN (CẦU THỦ/USER)
// Chuyển is_active = 0 trong bảng users và players
// =========================================================
router.patch('/players/:userId/lock', async (req, res) => {
    const { userId } = req.params;
    let connection;
    try {
        connection = await pool.getConnection();
        await connection.beginTransaction();

        // Khóa tài khoản user
        await connection.execute('UPDATE users SET is_active = 0 WHERE id = ?', [userId]);
        // Khóa thông tin player (nếu có)
        await connection.execute('UPDATE players SET is_active = 0 WHERE user_id = ?', [userId]);

        await connection.commit();
        res.json({ success: true, message: 'Đã khóa tài khoản thành công.' });
    } catch (error) {
        if (connection) await connection.rollback();
        res.status(500).json({ success: false, message: error.message });
    } finally {
        if (connection) connection.release();
    }
});

// =========================================================
// 3. PHÂN QUYỀN TÀI KHOẢN
// Thêm role vào bảng user_role
// =========================================================
router.post('/users/:userId/roles', async (req, res) => {
    const { userId } = req.params;
    const { roleId } = req.body; // Gửi roleId lên (VD: 3 là player, 1 là admin theo SQL của bạn)
    try {
        // Kiểm tra xem user đã có role này chưa
        const [existing] = await pool.execute(
            'SELECT * FROM user_role WHERE user_id = ? AND role_id = ?', 
            [userId, roleId]
        );

        if (existing.length > 0) {
            return res.status(400).json({ success: false, message: 'User đã có quyền này.' });
        }

        await pool.execute('INSERT INTO user_role (user_id, role_id) VALUES (?, ?)', [userId, roleId]);
        res.json({ success: true, message: 'Phân quyền thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =========================================================
// 4. DUYỆT THÊM CẦU THỦ MỚI (VÀO ĐỘI)
// Cập nhật approval_status = 'approved' trong bảng team_players
// =========================================================
router.patch('/team-players/:id/approve', async (req, res) => {
    const { id } = req.params; // id của bảng team_players
    try {
        const [result] = await pool.execute(
            `UPDATE team_players SET approval_status = 'approved', updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy yêu cầu hoặc đã bị xóa.' });
        }
        res.json({ success: true, message: 'Đã duyệt cầu thủ vào đội.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =========================================================
// 5. TỪ CHỐI THÊM CẦU THỦ (VÀO ĐỘI)
// Cập nhật approval_status = 'rejected' trong bảng team_players
// =========================================================
router.patch('/team-players/:id/reject', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE team_players SET approval_status = 'rejected', updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy yêu cầu hoặc đã bị xóa.' });
        }
        res.json({ success: true, message: 'Đã từ chối yêu cầu thêm cầu thủ.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// =========================================================
// 6. ĐÁNH DẤU CẦU THỦ CHẤN THƯƠNG
// Cập nhật status = 'injured' trong bảng team_players
// =========================================================
router.patch('/team-players/:id/status/injured', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE team_players SET status = 'injured', updated_at = NOW() WHERE id = ? AND is_deleted = 0`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy bản ghi cầu thủ trong đội.' });
        }
        res.json({ success: true, message: 'Đã cập nhật trạng thái chấn thương cho cầu thủ.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;