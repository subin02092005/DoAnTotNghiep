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
    const userId = req.query.user_id || req.query.userId;
    const teamId = req.query.team_id || req.query.teamId;

    // 1. Mặc định luôn luôn lấy thông báo Chung (type = 'general')
    let query = `SELECT * FROM notifications WHERE is_active = 1 AND (type = 'general'`;
    const params = [];

    // 2. Nếu có teamId, lấy đúng thông báo loại Đội (match_schedule) của Team đó
    if (teamId && teamId !== 'null' && teamId !== 'undefined' && teamId !== '0') {
        query += ` OR (type = 'match_schedule' AND target_team_id = ?)`;
        params.push(teamId);
    }

    // 3. Nếu có userId, lấy đúng thông báo loại Cá nhân (player_approved) của riêng User đó
    if (userId && userId !== 'null' && userId !== 'undefined' && userId !== '0') {
        query += ` OR (type = 'player_approved' AND recipient_user_id = ?)`;
        params.push(userId);
    }

    // Đóng ngoặc điều kiện và sắp xếp theo thời gian mới nhất
    query += `) ORDER BY created_at DESC`;

    try {
        const [notifications] = await pool.query(query, params);
        res.status(200).json({ status: "success", data: notifications });
    } catch (error) {
        console.error("Lỗi lấy thông báo cho User:", error);
        res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});
module.exports = router;