const { initializeApp, cert, getApps } = require('firebase-admin/app');
const { getMessaging } = require('firebase-admin/messaging');
const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
});

// KHỞI TẠO CHUẨN V14+
// Khởi tạo an toàn
let messaging;
try {
    const serviceAccount = require('../../config/service-account.json');
    
    // Kiểm tra danh sách app đang chạy
    if (!getApps().length) {
        initializeApp({
            credential: cert(serviceAccount)
        });
    }
    messaging = getMessaging();
    console.log("Firebase đã khởi tạo thành công!");
} catch (error) {
    console.error("LỖI KHỞI TẠO FIREBASE:", error.message);
}

async function sendFCMNotification(token, title, body) {
    if (!messaging) return;
    try {
        await messaging.send({ notification: { title, body }, token: token });
        console.log("Gửi thành công tới:", token);
    } catch (error) {
        console.error("Lỗi gửi FCM:", error.message);
    }
}

async function processNotificationForUsers(users, title, body) {
    const promises = users.map(async (user) => {
        await pool.query("INSERT INTO notifications (title, content, type, recipient_user_id, source) VALUES (?, ?, 'general', ?, 'system')", [title, body, user.id]);
        if (user.fcm_token) return sendFCMNotification(user.fcm_token, title, body);
    });
    await Promise.all(promises);
}

async function createAndSendNotification(teamId, title, content, type) {
    await pool.query("INSERT INTO notifications (title, content, type, source, target_team_id, is_active) VALUES (?, ?, ?, 'system', ?, 1)", [title, content, type, teamId]);
    const [users] = await pool.query("SELECT id, fcm_token FROM users WHERE team_id = ? AND fcm_token IS NOT NULL", [teamId]);
    await processNotificationForUsers(users, title, content);
}
async function sendRegistrationNotification(teamId, seasonId, message) {
    try {
        // 1. Lấy thông tin tên đội và tên giải
        const [[team]] = await pool.query("SELECT name FROM teams WHERE id = ?", [teamId]);
        const [[season]] = await pool.query("SELECT name FROM seasons WHERE id = ?", [seasonId]);

        const title = "Thông báo đăng ký giải đấu";
        const content = `Đội ${team.name} đã đăng ký thành công giải ${season.name}. ${message}`;

        // 2. Lưu vào DB thông báo (cho cả đội)
        // Lấy danh sách user trong đội
        const [users] = await pool.query("SELECT id, fcm_token FROM users WHERE team_id = ?", [teamId]);
        
        await processNotificationForUsers(users, title, content);
        
        console.log("Đã gửi thông báo đăng ký cho đội:", team.name);
    } catch (error) {
        console.error("Lỗi gửi thông báo đăng ký:", error.message);
    }
}

module.exports = { sendFCMNotification, createAndSendNotification, sendRegistrationNotification };