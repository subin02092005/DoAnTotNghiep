const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

// Cấu hình kết nối Database chung cho nhánh này
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// Route xử lý Đăng Nhập
router.post('/login', async (req, res) => {
    const email = req.body.email?.trim();
    const password = req.body.password?.trim();

    if (!email || !password) {
        return res.status(400).json({ success: false, message: 'Vui lòng nhập đầy đủ email và mật khẩu!' });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);
        const [rows] = await connection.execute('SELECT * FROM users WHERE email = ?', [email]);
        await connection.end();

        if (rows.length === 0) {
            return res.status(404).json({ success: false, message: 'Email không tồn tại!' });
        }

        const user = rows[0];

        if (user.is_active !== 1) {
            return res.status(403).json({ success: false, message: 'Tài khoản của bạn đã bị khóa!' });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(400).json({ success: false, message: 'Mật khẩu không chính xác!' });
        }

        const token = jwt.sign(
            { id: user.id, email: user.email, name: user.name },
            'YOUR_JWT_SECRET_KEY',
            { expiresIn: '1d' }
        );

        return res.status(200).json({
            success: true,
            message: 'Đăng nhập thành công!',
            token: token,
            user: {
                id: user.id,
                name: user.name,
                email: user.email,
                phone: user.phone,
                email_verified: user.email_verified
            }
        });

    } catch (error) {
        console.error("Lỗi đăng nhập:", error);
        return res.status(500).json({ success: false, message: 'Lỗi server hệ thống!' });
    }
});

// Xuất router ra để file server.js tổng dùng
module.exports = router;