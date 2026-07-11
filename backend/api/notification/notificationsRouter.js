const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
});

router.get('/my_notifications', async (req, res) => {
    let { userId, teamId } = req.query;
    
    // Khởi tạo câu query cơ bản
    let query = `SELECT * FROM notifications WHERE is_active = 1 AND (type = 'general'`;
    const params = [];

    // Chỉ thêm điều kiện nếu userId tồn tại
    if (userId) {
        query += ` OR recipient_user_id = ?`;
        params.push(userId);
    }

    // Chỉ thêm điều kiện nếu teamId tồn tại
    if (teamId) {
        query += ` OR target_team_id = ?`;
        params.push(teamId);
    }

    // Đóng ngoặc và sắp xếp
    query += `) ORDER BY created_at DESC`;

    try {
        const [notifications] = await pool.query(query, params);
        res.status(200).json({ status: "success", data: notifications });
    } catch (error) {
        console.error("Lỗi lấy thông báo:", error);
        res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;