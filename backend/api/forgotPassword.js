const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const bcrypt = require('bcrypt');

// Cấu hình kết nối Database của bạn
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// ==========================================
// API BƯỚC 1: XÁC MINH EMAIL & TRẢ VỀ TÊN USER
// ==========================================
router.post('/verify-email', async (req, res) => {
    const email = req.body.email?.trim();

    // Kiểm tra dữ liệu đầu vào
    if (!email) {
        return res.status(400).json({ success: false, message: 'Vui lòng nhập Email cần xác nhận!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // Truy vấn tìm tên người dùng dựa vào Email
        const [rows] = await connection.execute('SELECT name FROM users WHERE email = ?', [email]);

        await connection.end();

        if (rows.length > 0) {
            // Nếu tìm thấy, trả về success và kèm theo tên người dùng công khai
            return res.status(200).json({
                success: true,
                name: rows[0].name,
                message: 'Xác thực Email thành công!'
            });
        } else {
            // Nếu không tìm thấy Email trong hệ thống
            return res.status(404).json({
                success: false,
                message: 'Email này không tồn tại trong hệ thống!'
            });
        }

    } catch (error) {
        console.error("Lỗi xác minh Email:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi server hệ thống không thể kiểm tra Email!' });
    }
});


// ==========================================
// API BƯỚC 2: CẬP NHẬT MẬT KHẨU MỚI VÀO CSDL
// ==========================================
router.post('/reset-password', async (req, res) => {
    const email = req.body.email?.trim();
    const password = req.body.password?.trim();

    // Kiểm tra dữ liệu đầu vào từ App gửi lên
    if (!email || !password) {
        return res.status(400).json({ success: false, message: 'Thiếu dữ liệu Email hoặc Mật khẩu mới!' });
    }

    if (password.length < 6) {
        return res.status(400).json({ success: false, message: 'Mật khẩu mới phải chứa ít nhất 6 ký tự!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // 1. Kiểm tra lại một lần nữa xem Email có tồn tại không trước khi đổi
        const [userRows] = await connection.execute('SELECT id FROM users WHERE email = ?', [email]);
        if (userRows.length === 0) {
            await connection.end();
            return res.status(404).json({ success: false, message: 'Không tìm thấy tài khoản để đổi mật khẩu!' });
        }

        // 2. Tiến hành băm mật khẩu mới bằng Bcrypt để lưu trữ bảo mật
        const saltRounds = 10;
        const hashedPassword = await bcrypt.hash(password, saltRounds);

        // 3. Thực hiện câu lệnh UPDATE mật khẩu mới và cập nhật thời gian updated_at vào bảng users
        const updateQuery = 'UPDATE users SET password = ?, updated_at = NOW() WHERE email = ?';
        await connection.execute(updateQuery, [hashedPassword, email]);

        await connection.end();

        return res.status(200).json({
            success: true,
            message: 'Đặt lại mật khẩu mới thành công!'
        });

    } catch (error) {
        console.error("Lỗi đặt lại Mật khẩu:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi server, cập nhật mật khẩu thất bại!' });
    }
});

module.exports = router;