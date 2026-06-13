const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const bcrypt = require('bcrypt');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API: ĐĂNG KÝ
router.post('/register', async (req, res) => {
    const name = req.body.name?.trim();
    const email = req.body.email?.trim();
    const password = req.body.password?.trim();
    const phone = req.body.phone?.trim();

    if (!name || !email || !password || !phone) {
        return res.status(400).json({ success: false, message: 'Vui lòng điền đầy đủ thông tin đăng ký!' });
    }

    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!emailRegex.test(email)) {
        return res.status(400).json({ success: false, message: 'Định dạng Email không chính xác!' });
    }

    const phoneRegex = /^0\d{9}$/;
    if (!phoneRegex.test(phone)) {
        return res.status(400).json({ success: false, message: 'Số điện thoại không hợp lệ!' });
    }

    if (password.length < 6) {
        return res.status(400).json({ success: false, message: 'Mật khẩu phải chứa ít nhất 6 ký tự!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // Kiểm tra trùng email
        const [existingUser] = await connection.execute('SELECT id FROM users WHERE email = ?', [email]);
        if (existingUser.length > 0) {
            await connection.end();
            return res.status(400).json({ success: false, message: 'Email này đã tồn tại trong hệ thống!' });
        }

        // Tìm quyền 'user' mặc định (ID = 5)
        const [roleRows] = await connection.execute('SELECT id FROM roles WHERE name = ?', ['user']);
        let targetRoleId = 5;
        if (roleRows.length > 0) {
            targetRoleId = roleRows[0].id;
        }

        const saltRounds = 10;
        const hashedPassword = await bcrypt.hash(password, saltRounds);

        // email_verified = 0 (Chưa xác thực để xác thực sau ở Profile)
        const insertUserQuery = `
            INSERT INTO users (name, email, password, phone, is_active, email_verified, email_verified_at, created_at, updated_at) 
            VALUES (?, ?, ?, ?, 1, 0, NULL, NOW(), NOW())
        `;
        const [userResult] = await connection.execute(insertUserQuery, [name, email, hashedPassword, phone]);
        const newUserId = userResult.insertId;

        try {
            await connection.execute('INSERT INTO user_role (user_id, role_id) VALUES (?, ?)', [newUserId, targetRoleId]);
        } catch (roleError) {
            console.error("Lỗi liên kết bảng user_role:", roleError.message);
        }

        await connection.end();
        return res.status(201).json({
            success: true,
            message: 'Đăng ký thành công! Bạn có thể vào trang cá nhân để kích hoạt tài khoản.'
        });

    } catch (error) {
        console.error("Lỗi Đăng Ký:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi server hệ thống!' });
    }
});

module.exports = router;