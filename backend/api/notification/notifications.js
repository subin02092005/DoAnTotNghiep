const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy danh sách bài viết (Thông báo)
router.get('/my_notifications', async (req, res) => {
    const { userId, teamId } = req.query; // Nhận thêm teamId từ client

    if (!userId) {
        return res.status(400).json({ status: "error", message: "Thiếu userId" });
    }

    try {
        // Lấy thông báo cho CÁ NHÂN HOẶC cho ĐỘI của họ
        const query = `
            SELECT * FROM notifications 
            WHERE is_active = 1 
            AND (recipient_user_id = ? OR target_team_id = ?)
            ORDER BY created_at DESC
        `;
        const [notifications] = await pool.query(query, [userId, teamId || 0]);

        res.status(200).json({
            status: "success",
            data: notifications
        });
    } catch (error) {
        console.error("Lỗi lấy thông báo:", error);
        res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});
module.exports = router;