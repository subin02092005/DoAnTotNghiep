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
// Route xử lý Đăng Nhập
router.post('/login', async (req, res) => {
    const email = req.body.email?.trim();
    const password = req.body.password?.trim();
    const fcm_token = req.body.fcm_token?.trim();
    
    // 1. Kiểm tra chưa nhập email hoặc mật khẩu
    if (!email || !password) {
        return res.status(400).json({ success: false, message: 'Vui lòng nhập đầy đủ email và mật khẩu!' });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);
        const [rows] = await connection.execute('SELECT * FROM users WHERE email = ?', [email]);
        
        // 2. Kiểm tra email không tồn tại
        if (rows.length === 0) {
            await connection.end();
            return res.status(404).json({ success: false, message: 'Email không tồn tại!' });
        }

        const user = rows[0];

        // 3. Kiểm tra tài khoản bị khóa
        if (user.is_active !== 1) {
            await connection.end();
            return res.status(403).json({ success: false, message: 'Tài khoản của bạn đã bị khóa!' });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        
        // 4. Kiểm tra sai mật khẩu (dùng mã 401 chuẩn xác thực)
        if (!isMatch) {
            await connection.end();
            return res.status(401).json({ success: false, message: 'Mật khẩu không chính xác!' });
        }

        // 5. Kiểm tra quyền Admin
        const [roleRows] = await connection.execute(
            'SELECT r.name FROM user_role ur JOIN roles r ON ur.role_id = r.id WHERE ur.user_id = ?',
            [user.id]
        );
        const isAdmin = roleRows.some(r => r.name === 'admin');

        // 6. Lấy thông tin team_id và role trong đội bóng của người dùng
        const queryRole = `
            SELECT tp.role, tp.team_id 
            FROM team_players tp
            JOIN players p ON tp.player_id = p.id
            WHERE p.user_id = ? AND tp.is_active = 1 
            LIMIT 1
        `;
        const [teamData] = await connection.execute(queryRole, [user.id]);

        // Nếu không thuộc đội nào thì mặc định role='player' và team_id=0
        const userRole = teamData.length > 0 ? teamData[0].role : 'player';
        const teamId = teamData.length > 0 ? teamData[0].team_id : 0; 

        // 7. Cập nhật FCM Token nếu có
        if (fcm_token) {
            await connection.execute(
                'UPDATE users SET fcm_token = ?, updated_at = NOW() WHERE id = ?',
                [fcm_token, user.id]
            );
            console.log(`Đã gán fcm_token thành công cho user: ${user.name}`);
        }
        
        await connection.end();

        // 8. Tạo JWT Token
        const secret = process.env.JWT_SECRET || 'YOUR_JWT_SECRET_KEY';
        const token = jwt.sign(
            { id: user.id, email: user.email, name: user.name, is_admin: isAdmin },
            secret,
            { expiresIn: '1d' }
        );

        // 9. Trả về kết quả thành công kèm theo team_id
        return res.status(200).json({
            success: true,
            message: 'Đăng nhập thành công!',
            token: token,
            user: {
                id: user.id,
                name: user.name,
                email: user.email,
                phone: user.phone,
                role: userRole,
                team_id: teamId, // <--- Đã có team_id gửi về cho Android
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