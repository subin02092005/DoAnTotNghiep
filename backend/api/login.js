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
        if (rows.length === 0) {
            await connection.end();
            return res.status(404).json({ success: false, message: 'Email không tồn tại!' });
        }

        // Kiểm tra role của user
        const user = rows[0];
        const [roleRows] = await connection.execute(
            'SELECT r.name FROM user_role ur JOIN roles r ON ur.role_id = r.id WHERE ur.user_id = ?',
            [user.id]
        );
        const isAdmin = roleRows.some(r => r.name === 'admin');

        await connection.end();

        if (rows.length === 0) {
            return res.status(404).json({ success: false, message: 'Email không tồn tại!' });
        }

        if (user.is_active !== 1) {
            return res.status(403).json({ success: false, message: 'Tài khoản của bạn đã bị khóa!' });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(400).json({ success: false, message: 'Mật khẩu không chính xác!' });
        }

        const secret = process.env.JWT_SECRET || 'YOUR_JWT_SECRET_KEY';
        const token = jwt.sign(
            { id: user.id, email: user.email, name: user.name, is_admin: isAdmin },
            secret,
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
                email_verified: user.email_verified,
                is_admin: isAdmin
            }
        });

    } catch (error) {
        console.error("Lỗi đăng nhập:", error);
        return res.status(500).json({ success: false, message: 'Lỗi server hệ thống!' });
    }
});

// Kiểm tra nhanh: chỉ cần email để biết có phải admin hay không
router.post('/check-admin-email', async (req, res) => {
    const email = req.body.email?.trim();
    if (!email) return res.status(400).json({ success: false, message: 'Vui lòng gửi email.' });

    try {
        const connection = await mysql.createConnection(dbConfig);
        const [rows] = await connection.execute('SELECT id FROM users WHERE email = ?', [email]);
        if (rows.length === 0) {
            await connection.end();
            return res.status(404).json({ success: false, message: 'Email không tồn tại.' });
        }

        const userId = rows[0].id;
        const [roleRows] = await connection.execute(
            'SELECT r.name FROM user_role ur JOIN roles r ON ur.role_id = r.id WHERE ur.user_id = ?',
            [userId]
        );
        await connection.end();

        const isAdmin = roleRows.some(r => r.name === 'admin');
        return res.json({ success: true, is_admin: isAdmin });
    } catch (error) {
        console.error('Lỗi check-admin-email:', error);
        return res.status(500).json({ success: false, message: 'Lỗi server' });
    }
});

// Xuất router ra để file server.js tổng dùng
module.exports = router;