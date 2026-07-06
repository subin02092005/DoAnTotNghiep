const admin = require('firebase-admin');
const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const { cert } = require('firebase-admin/app'); // 🌟 Import module 'cert' trực tiếp

let serviceAccount;
try {
    serviceAccount = require('../../config/service-account.json');
} catch (error) {
    if (error.code === 'MODULE_NOT_FOUND') {
        console.warn('Warning: service-account.json not found in backend/config. Firebase notifications will be disabled.');
    } else {
        throw error;
    }
}

// 1. Cấu hình Database Pool
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
});
console.log("DEBUG: Giá trị của admin là:", typeof admin); // Phải là 'object'
console.log("DEBUG: Nội dung của admin:", admin);

function isFirebaseInitialized() {
    return admin.getApps().length > 0;
}

// Kiểm tra xem admin đã được khởi tạo chưa
if (admin.getApps().length === 0 && serviceAccount) {
    try {
        admin.initializeApp({
            credential: cert(serviceAccount) // 🌟 Sử dụng hàm 'cert' đã import
        });
        console.log("Firebase Admin đã khởi tạo thành công!");
    } catch (error) {
        console.error("Lỗi khi khởi tạo Firebase Admin:", error);
    }
}

// 🌟 ĐỊNH NGHĨA HÀM GỬI FCM (Thiếu hàm này nên bạn bị lỗi)
async function sendFCMNotification(token, title, body) {
    if (!isFirebaseInitialized()) {
        console.warn('Firebase Admin chưa được khởi tạo. Bỏ qua gửi thông báo FCM.');
        return;
    }

    try {
        const message = {
            notification: {
                title: title,
                body: body
            },
            token: token
        };
        await admin.messaging().send(message);
        console.log("Đã gửi thông báo thành công cho:", token);
    } catch (error) {
        console.error("Lỗi gửi FCM:", error.message);
    }
}
// Thêm vào file này để dùng chung
async function sendToAll(title, body) {
    // 1. Lấy tất cả user có token
    const [users] = await pool.query("SELECT id, fcm_token FROM users WHERE fcm_token IS NOT NULL");
    
    for (const user of users) {
        // 2. LƯU VÀO DATABASE (Để API /my_notifications lấy ra hiển thị trong app)
        await pool.query(
            "INSERT INTO notifications (title, content, type, recipient_user_id, source) VALUES (?, ?, 'general', ?, 'system')",
            [title, body, user.id]
        );
        
        // 3. GỬI ĐẾN ĐIỆN THOẠI (Để hiện trên thanh thông báo)
        await sendFCMNotification(user.fcm_token, title, body);
    }
}

async function sendToTeam(teamId, title, body) {
    // Truy vấn token chỉ của đội đó
    const [users] = await pool.query("SELECT fcm_token FROM users WHERE team_id = ? AND fcm_token IS NOT NULL", [teamId]);
    for (const user of users) {
        await sendFCMNotification(user.fcm_token, title, body);
    }
}
async function createAndSendNotification(teamId, title, content, type) {
    // 1. Lưu vào bảng notifications để người dùng xem lại được
    const query = `INSERT INTO notifications (title, content, type, source, target_team_id, is_active) 
                   VALUES (?, ?, ?, 'system', ?, 1)`;
    await pool.query(query, [title, content, type, teamId]);

    // 2. Gửi FCM
    await sendToTeam(teamId, title, content);
}
// API lấy danh sách bài viết (Thông báo)
router.get('/my_notifications', async (req, res) => {
    console.log("Dữ liệu nhận được:", req.body);
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
   const [notifications] = await pool.query(query, [userId, teamId || null]);

        res.status(200).json({
            status: "success",
            data: notifications
        });
    } catch (error) {
        console.error("Lỗi lấy thông báo:", error);
        res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});
router.post('/mark_as_read/:id', async (req, res) => {
  const { id } = req.params;
    const { userId } = req.body; // CẦN LẤY userId từ body hoặc token

    try {
      const query = `UPDATE notifications 
               SET is_read = 1, updated_at = NOW() 
               WHERE id = ? AND recipient_user_id = ?`;
const [result] = await pool.query(query, [id, userId]); // Cần lấy userId từ session hoặc token

        if (result.affectedRows === 0) {
            return res.status(404).json({ status: "error", message: "Không tìm thấy thông báo" });
        }

        res.status(200).json({ status: "success", message: "Đã cập nhật trạng thái đọc" });
    } catch (error) {
        console.error("Lỗi cập nhật trạng thái đọc:", error);
        res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});
module.exports = { router, sendFCMNotification, createAndSendNotification };