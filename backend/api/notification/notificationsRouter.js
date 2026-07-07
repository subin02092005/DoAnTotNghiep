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
    const { userId, teamId } = req.query;
    if (!userId) return res.status(400).json({ status: "error", message: "Thiếu userId" });

    try {
        const query = `SELECT * FROM notifications WHERE is_active = 1 AND (type = 'general' OR recipient_user_id = ? OR target_team_id = ?) ORDER BY created_at DESC`;
        const [notifications] = await pool.query(query, [userId, teamId || null]);
        res.status(200).json({ status: "success", data: notifications });
    } catch (error) {
        res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;