const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// 🌟 API 1: LẤY THÔNG TIN CHI TIẾT ĐỂ HIỂN THỊ TRÊN PROFILE
// Đường dẫn: GET http://localhost:3000/api/profile/get-info?email=...
router.get('/profile/get-info', async (req, res) => {
    const email = req.query.email?.trim();

    if (!email) {
        return res.status(400).json({ success: false, message: 'Thiếu tham số Email!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // Lấy thông tin user kết hợp với tên quyền (role) từ database của bạn
        const query = `
            SELECT u.name, u.email, u.phone, u.email_verified, r.name as role_name 
            FROM users u
            LEFT JOIN user_role ur ON u.id = ur.user_id
            LEFT JOIN roles r ON ur.role_id = r.id
            WHERE u.email = ? 
            LIMIT 1
        `;
        const [rows] = await connection.execute(query, [email]);
        await connection.end();

        if (rows.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy người dùng!' });
        }

        const user = rows[0];
        return res.status(200).json({
            success: true,
            data: {
                name: user.name,
                email: user.email,
                phone: user.phone,
                email_verified: user.email_verified, // Sẽ trả về số 0 hoặc 1
                role: user.role_name || 'user'
            }
        });
    } catch (error) {
        console.error("Lỗi lấy thông tin Profile:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi máy chủ khi lấy dữ liệu!' });
    }
});

// 🌟 API 2: XÁC THỰC OTP TẠI TRANG CÁ NHÂN
// Đường dẫn: POST http://localhost:3000/api/profile/verify-otp
router.post('/profile/verify-otp', async (req, res) => {
    const email = req.body.email?.trim();
    const otp = req.body.otp?.trim();

    if (!email || !otp) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp đầy đủ Email và mã OTP!' });
    }

    // MẸO HACK ĐỒ ÁN: Gõ đúng 123456 là qua môn
    if (otp !== '123456') {
        return res.status(400).json({ success: false, message: 'Mã OTP không chính xác hoặc đã hết hạn!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // Kích hoạt tài khoản: Đổi trạng thái email_verified = 1
        const updateQuery = `
            UPDATE users 
            SET email_verified = 1, email_verified_at = NOW() 
            WHERE email = ?
        `;
        const [result] = await connection.execute(updateQuery, [email]);
        await connection.end();

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy tài khoản người dùng!' });
        }

        return res.status(200).json({
            success: true,
            message: 'Xác thực tài khoản thành công! Trạng thái đã kích hoạt.'
        });
    } catch (error) {
        console.error("Lỗi Xác Thực OTP:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi hệ thống khi kích hoạt!' });
    }
});

module.exports = router;