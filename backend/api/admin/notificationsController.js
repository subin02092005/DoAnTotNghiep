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
        // 1. Lấy ID người nhận trước khi update để gửi thông báo
        const [rows] = await pool.query("SELECT recipient_user_id FROM notifications WHERE id = ?", [id]);
        if (rows.length === 0) {
            return res.status(404).json({ status: "error", message: "Không tìm thấy thông báo" });
        }
        const recipientId = rows[0].recipient_user_id;

        // 2. Thực hiện Update
        const query = `
            UPDATE notifications 
            SET title = ?, content = ?, type = ?, is_active = ?, updated_at = NOW() 
            WHERE id = ?
        `;
        const [result] = await pool.query(query, [title, content, type, is_active, id]);
        
        if (result.affectedRows === 0) {
            return res.status(404).json({ status: "error", message: "Không có thay đổi nào được thực hiện" });
        }

        // 3. Nếu update thành công, lấy FCM token và gửi thông báo mới
        if (recipientId) {
            const [users] = await pool.query("SELECT fcm_token FROM users WHERE id = ? AND fcm_token IS NOT NULL", [recipientId]);
            
            if (users.length > 0) {
                const token = users[0].fcm_token;
                // Gửi thông báo cho user biết là thông báo đã được cập nhật
                await sendFCMNotification(token, "Thông báo cập nhật", title);
                console.log(`Đã gửi thông báo update cho user ${recipientId}`);
            }
        }
        
        res.status(200).json({ status: "success", message: "Đã cập nhật và gửi thông báo cho người dùng" });
    } catch (error) {
        console.error("Lỗi update notification:", error.message);
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
router.post('/create_rules', async (req, res) => {
    console.log("Dữ liệu nhận từ Android:", req.body);
    const { 
        season_id, min_players, max_players, 
        points_win, points_draw, points_loss, forfeit_score, description 
    } = req.body;

    try {
        const connection = await pool.getConnection();
        await connection.beginTransaction();

        try {
            // 1. TÌM TOURNAMENT_ID TỪ SEASON_ID
          const [seasonRows] = await connection.query("SELECT tournament_id FROM seasons WHERE id = ?", [season_id]);
if (seasonRows.length === 0) throw new Error("Không tìm thấy giải đấu!");
const tournament_id = seasonRows[0].tournament_id;

// 2. THAY VÌ INSERT, TA SẼ UPDATE DÒNG ĐÃ TỒN TẠI
// Cấu trúc: Nếu tồn tại tournament_id này thì Update, không thì Insert
const updateQuery = `
    INSERT INTO tournament_rules 
    (tournament_id, min_players_per_team, max_players_per_team, points_per_win, points_per_draw, points_per_loss, forfeit_score, tiebreaker_order, description, is_active) 
    VALUES (?, ?, ?, ?, ?, ?, ?, '["goal_difference", "head_to_head"]', ?, 1)
    ON DUPLICATE KEY UPDATE 
    min_players_per_team = VALUES(min_players_per_team),
    max_players_per_team = VALUES(max_players_per_team),
    points_per_win = VALUES(points_per_win),
    points_per_draw = VALUES(points_per_draw),
    points_per_loss = VALUES(points_per_loss),
    forfeit_score = VALUES(forfeit_score),
    description = VALUES(description),
    is_active = 1
`;

await connection.query(updateQuery, [
    tournament_id, min_players, max_players, points_win, points_draw, points_loss, forfeit_score, description
]);

await connection.commit();
            connection.release();

            // 4. Gửi thông báo (Giữ nguyên logic dùng season_id)
            const notifyTitle = "Cập nhật luật giải đấu mới";
            const notifyContent = `Giải đấu đã cập nhật luật: Tối thiểu ${min_players}, Tối đa ${max_players} cầu thủ. Kiểm tra ngay!`;

            const [users] = await pool.query(`
                SELECT DISTINCT u.fcm_token 
                FROM users u
                JOIN team_players tp ON u.id = tp.user_id
                JOIN season_teams ts ON tp.team_id = ts.team_id
                WHERE ts.season_id = ? AND u.fcm_token IS NOT NULL
            `, [season_id]);

            const sendPromises = users.map(u => sendFCMNotification(u.fcm_token, notifyTitle, notifyContent));
            await Promise.all(sendPromises);

            res.status(200).json({ status: "success", message: "Đã áp dụng luật và thông báo cho người chơi" });

        } catch (dbError) {
            await connection.rollback();
            connection.release();
            throw dbError;
        }
    } catch (error) {
        console.error("Lỗi tạo luật:", error.message);
        res.status(500).json({ status: "error", message: error.message });
    }
});
module.exports = router;