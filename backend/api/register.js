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

// Route xử lý Đăng Ký
router.post('/register', async (req, res) => {
    const name = req.body.name?.trim();
    const email = req.body.email?.trim();
    const password = req.body.password?.trim();
    const phone = req.body.phone?.trim();

    if (!name || !email || !password || !phone) {
        return res.status(400).json({ success: false, message: 'Vui lòng điền đầy đủ thông tin đăng ký!' });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);

        // Kiểm tra xem email đã tồn tại chưa
        const [existingUser] = await connection.execute('SELECT id FROM users WHERE email = ?', [email]);
        
        if (existingUser.length > 0) {
            await connection.end();
            return res.status(400).json({ success: false, message: 'Email này đã tồn tại trong hệ thống!' });
        }

        const saltRounds = 10;
        const hashedPassword = await bcrypt.hash(password, saltRounds);

        const insertQuery = `
            INSERT INTO users (name, email, password, phone, is_active, email_verified, email_verified_at, created_at, updated_at) 
            VALUES (?, ?, ?, ?, 1, 1, NOW(), NOW(), NOW())
        `;
        const [result] = await connection.execute(insertQuery, [name, email, hashedPassword, phone]);
        
        try {
            await connection.execute('INSERT INTO user_role (user_id, role_id) VALUES (?, ?)', [result.insertId, 3]);
        } catch (roleError) {
            console.log("Lỗi hoặc không bắt buộc user_role:", roleError.message);
        }

        await connection.end();

        return res.status(201).json({
            success: true,
            message: 'Đăng ký tài khoản thành công!'
        });

    } catch (error) {
        console.error("Lỗi Đăng Ký:", error);
        return res.status(500).json({ success: false, message: 'Lỗi server hệ thống không thể đăng ký!' });
    }
});

// Xuất router ra để file server.js tổng dùng
module.exports = router;