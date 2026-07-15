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
            WHERE p.deleted_at IS NULL
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
// 2b. MỞ KHÓA TÀI KHOẢN (CẦU THỦ/USER)
// Chuyển is_active = 1 trong bảng users và players
// =========================================================
router.patch('/players/:userId/unlock', async (req, res) => {
    const { userId } = req.params;
    let connection;
    try {
        connection = await pool.getConnection();
        await connection.beginTransaction();

        // Mở khóa tài khoản user
        await connection.execute('UPDATE users SET is_active = 1 WHERE id = ?', [userId]);
        // Mở khóa thông tin player (nếu có)
        await connection.execute('UPDATE players SET is_active = 1 WHERE user_id = ?', [userId]);

        await connection.commit();
        res.json({ success: true, message: 'Đã mở khóa tài khoản thành công.' });
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
            `UPDATE team_players SET approval_status = 'approved', updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
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
            `UPDATE team_players SET approval_status = 'rejected', updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
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
            `UPDATE team_players SET status = 'injured', updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
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

// 7. THÊM CẦU THỦ MỚI (Admin)
// Tạo User account và Player profile đồng thời
router.post('/players', async (req, res) => {
    const { name, email, phone, position, date_of_birth, nationality, height, weight } = req.body;
    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        // 1. Tạo User (Mật khẩu mặc định là 123456)
        const [userResult] = await connection.execute(
            `INSERT INTO users (name, email, password, phone, is_active, created_at)
             VALUES (?, ?, '$2b$10$7Z2vL0n5h5v2G2.g1.h1.eQ1vL0n5h5v2G2.g1.h1.e', ?, 1, NOW())`,
            [name, email, phone]
        );
        const userId = userResult.insertId;

        // 2. Tạo Player Profile
        await connection.execute(
            `INSERT INTO players (user_id, date_of_birth, position, nationality, height, weight, is_active, created_at)
             VALUES (?, ?, ?, ?, ?, ?, 1, NOW())`,
            [userId, date_of_birth, position, nationality, height, weight]
        );

        // 3. Gán role 'player' (id=3)
        await connection.execute('INSERT INTO user_role (user_id, role_id) VALUES (?, 3)', [userId]);

        await connection.commit();
        res.json({ success: true, message: 'Tạo cầu thủ thành công! Mật khẩu mặc định là 123456.' });
    } catch (error) {
        await connection.rollback();
        res.status(500).json({ success: false, message: error.message });
    } finally {
        connection.release();
    }
});

// 8. CẬP NHẬT THÔNG TIN CẦU THỦ
router.put('/players/:playerId', async (req, res) => {
    const { playerId } = req.params;
    const { name, email, phone, position, date_of_birth, nationality, height, weight, userId } = req.body;

    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        // 1. Cập nhật bảng users
        await connection.execute(
            `UPDATE users SET name = ?, email = ?, phone = ?, updated_at = NOW() WHERE id = ?`,
            [name, email, phone, userId]
        );

        // 2. Cập nhật bảng players
        await connection.execute(
            `UPDATE players SET position = ?, date_of_birth = ?, nationality = ?, height = ?, weight = ?, updated_at = NOW()
             WHERE id = ?`,
            [position, date_of_birth, nationality, height, weight, playerId]
        );

        await connection.commit();
        res.json({ success: true, message: 'Cập nhật thông tin cầu thủ thành công!' });
    } catch (error) {
        await connection.rollback();
        res.status(500).json({ success: false, message: error.message });
    } finally {
        connection.release();
    }
});

module.exports = router;