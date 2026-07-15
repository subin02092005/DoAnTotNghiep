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

// --- 1. HÀM GỬI FCM CƠ BẢN ---
async function sendFCMNotification(token, title, body) {
    if (!messaging) return;
    try {
        await messaging.send({ notification: { title, body }, token: token });
        console.log("Gửi FCM thành công tới:", token);
    } catch (error) {
        console.error("Lỗi gửi FCM:", error.message);
    }
}

// --- 2. HÀM ĐIỀU PHỐI TRUNG TÂM (Sửa lỗi trùng lặp) ---
// Hàm này thay thế cho processNotificationForUsers cũ
async function dispatchNotification(users, title, content, targetTeamId = null) {
    for (const user of users) {
        try {
            // A. Lưu vào database - ĐÃ SỬA: message thành content để khớp Schema
            await pool.query(
                `INSERT INTO notifications (recipient_user_id, target_team_id, title, content, is_read, created_at, source, type)
                 VALUES (?, ?, ?, ?, 0, NOW(), 'system', 'general')`,
                [user.id, targetTeamId, title, content]
            );

            // B. Gửi Firebase (nếu có token)
            if (user.fcm_token) {
                await sendFCMNotification(user.fcm_token, title, content);
            }
        } catch (err) {
            console.error(`Lỗi xử lý thông báo cho user ${user.id}:`, err.message);
        }
    }
}

// --- 3. CÁC HÀM API CHÍNH ---

// Dùng để gửi cho tất cả người dùng
async function sendToAll(title, body) {
    const [users] = await pool.query("SELECT id, fcm_token FROM users WHERE fcm_token IS NOT NULL");
    if (users.length > 0) {
        await dispatchNotification(users, title, body, null);
    }
}

// Dùng cho đăng ký giải đấu
async function sendRegistrationNotification(teamId, seasonId, message) {
    try {
        const [[team]] = await pool.query("SELECT name FROM teams WHERE id = ?", [teamId]);
        const [[season]] = await pool.query("SELECT name FROM seasons WHERE id = ?", [seasonId]);

        const title = "Thông báo đăng ký giải đấu";
        const content = `Đội ${team.name} đã đăng ký thành công giải ${season.name}. ${message}`;

        const query = `
            SELECT u.id, u.fcm_token 
            FROM users u
            JOIN players p ON u.id = p.user_id
            JOIN team_players tp ON p.id = tp.player_id
            WHERE tp.team_id = ?`;
        const [users] = await pool.query(query, [teamId]);
        
        await dispatchNotification(users, title, content, teamId);
    } catch (error) {
        console.error("Lỗi gửi thông báo đăng ký:", error.message);
    }
}

// Dùng cho các sự kiện thông báo chung
async function createAndSendNotification(teamId, recipientUserId, title, content, type) {
    let users = [];

    // Lấy danh sách user cần gửi
    if (recipientUserId) {
        [users] = await pool.query("SELECT id, fcm_token FROM users WHERE id = ?", [recipientUserId]);
    } else if (teamId) {
        const query = `SELECT u.id, u.fcm_token FROM users u JOIN players p ON u.id = p.user_id JOIN team_players tp ON p.id = tp.player_id WHERE tp.team_id = ?`;
        [users] = await pool.query(query, [teamId]);
    } else {
        [users] = await pool.query("SELECT id, fcm_token FROM users");
    }

    // Điều phối gửi
    if (users.length > 0) {
        await dispatchNotification(users, title, content, teamId);
    }
}

module.exports = { sendFCMNotification, createAndSendNotification, sendRegistrationNotification, sendToAll };

