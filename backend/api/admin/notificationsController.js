const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const { sendFCMNotification } = require('../notification/notifications');

// Cấu hình kết nối Database
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};
const pool = mysql.createPool(dbConfig);

router.get('/all_notifications', async (req, res) => {
    try {
        // Lấy tất cả, sắp xếp theo cái mới nhất lên đầu
        const [notifications] = await pool.query("SELECT * FROM notifications ORDER BY created_at DESC");
        res.status(200).json({ status: "success", data: notifications });
    } catch (error) {
        res.status(500).json({ status: "error", message: error.message });
    }
});
router.post('/create_notification', async (req, res) => {
    const { title, content, type, target_team_id,    // Lấy đúng tên key từ Android
        recipient_user_id,source } = req.body;

    try {
        // 1. Lưu vào database
        const query = `INSERT INTO notifications (title, content, type, source, target_team_id, recipient_user_id) VALUES (?, ?, ?, 'manual', ?, ?)`;
        await pool.query(query, [title, content, type, target_team_id || null, recipient_user_id || null]);

        // 2. Lấy token để gửi thông báo đẩy (FCM)
        let fcmTokens = [];
if (type === 'general') {
    // Lấy tất cả token của tất cả người dùng
    const [users] = await pool.query("SELECT fcm_token FROM users WHERE fcm_token IS NOT NULL");
    fcmTokens = users.map(u => u.fcm_token);
} else if (recipient_user_id) {
    const [users] = await pool.query("SELECT fcm_token FROM users WHERE id = ? AND fcm_token IS NOT NULL", [recipient_user_id]);
    fcmTokens = users.map(u => u.fcm_token);
} else if (target_team_id) {
    const query = `
        SELECT u.fcm_token 
        FROM users u
        JOIN players p ON u.id = p.user_id
        JOIN team_players tp ON p.id = tp.player_id
        WHERE tp.team_id = ? 
        AND u.fcm_token IS NOT NULL
        AND tp.is_active = 1
    `;
    const [users] = await pool.query(query, [target_team_id]);
    fcmTokens = users.map(u => u.fcm_token);
} 

console.log("Tokens lấy được:", fcmTokens);
        // 3. Gửi FCM cho từng người
        for (const token of fcmTokens) {
            await sendFCMNotification(token, title, content);
        }

        res.status(200).json({ status: "success", message: "Đã tạo và gửi thông báo" });
    } catch (error) {
        console.error("LỖI INSERT SQL:", error.message);
        res.status(500).json({ status: "error", message: error.message });
    }
});
router.put('/update_notification/:id', async (req, res) => {
    const { id } = req.params;
    const { title, content, type, is_active } = req.body;

    try {
        const query = `
            UPDATE notifications 
            SET title = ?, content = ?, type = ?, is_active = ?, updated_at = NOW() 
            WHERE id = ?
        `;
        const [result] = await pool.query(query, [title, content, type, is_active, id]);
        
        if (result.affectedRows === 0) return res.status(404).json({ status: "error", message: "Không tìm thấy" });
        
        res.status(200).json({ status: "success", message: "Đã cập nhật thông báo" });
    } catch (error) {
        res.status(500).json({ status: "error", message: error.message });
    }
});
router.delete('/delete_notification/:id', async (req, res) => {
    const { id } = req.params;
    try {
        await pool.query("DELETE FROM notifications WHERE id = ?", [id]);
        res.status(200).json({ status: "success", message: "Đã xóa thông báo" });
    } catch (error) {
        res.status(500).json({ status: "error", message: error.message });
    }
});
router.post('/cleanup_notifications', async (req, res) => {
    try {
        // Vô hiệu hóa các thông báo được tạo cách đây hơn 30 ngày
        const query = `
            UPDATE notifications 
            SET is_active = 0 
            WHERE created_at < NOW() - INTERVAL 1 MONTH 
            AND is_active = 1
        `;
        const [result] = await pool.query(query);
        res.status(200).json({ 
            status: "success", 
            message: `Đã ẩn ${result.affectedRows} thông báo cũ.` 
        });
    } catch (error) {
        res.status(500).json({ status: "error", message: error.message });
    }
});
module.exports = router;