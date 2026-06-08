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

// Route xử lý Đăng Ký
router.post('/register', async (req, res) => {
    const name = req.body.name?.trim();
    const email = req.body.email?.trim();
    const password = req.body.password?.trim();
    const phone = req.body.phone?.trim();

    // 🌟 1. KIỂM TRA TRỐNG DỮ LIỆU ĐẦU VÀO
    if (!name || !email || !password || !phone) {
        return res.status(400).json({ success: false, message: 'Vui lòng điền đầy đủ thông tin đăng ký!' });
    }

    // 🌟 2. KIỂM TRA ĐỊNH DẠNG EMAIL (Bắt buộc phải đúng form như abc@gmail.com)
    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!emailRegex.test(email)) {
        return res.status(400).json({ success: false, message: 'Định dạng Email không chính xác (Ví dụ: abc@gmail.com)!' });
    }

    // 🌟 3. KIỂM TRA SỐ ĐIỆN THOẠI (Phải đúng 10 chữ số và chỉ gồm ký tự số)
    const phoneRegex = /^0\d{9}$/;
if (!phoneRegex.test(phone)) {
    return res.status(400).json({ success: false, message: 'Số điện thoại không hợp lệ (Phải có 10 số và bắt đầu bằng số 0)!' });
}

    // 🌟 4. KIỂM TRA ĐỘ DÀI MẬT KHẨU BẢO MẬT
    if (password.length < 6) {
        return res.status(400).json({ success: false, message: 'Mật khẩu phải chứa ít nhất 6 ký tự!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // 1. Kiểm tra xem email đã tồn tại trong bảng `users` chưa
        const [existingUser] = await connection.execute('SELECT id FROM users WHERE email = ?', [email]);
        if (existingUser.length > 0) {
            await connection.end();
            return res.status(400).json({ success: false, message: 'Email này đã tồn tại trong hệ thống!' });
        }

        // 2. Tự động tìm ID của role "tài khoản được đăng ký" trong bảng roles (hoặc role)
        const [roleRows] = await connection.execute(
            'SELECT id FROM roles WHERE name = ? OR name = ?', 
            ['tài khoản được đăng ký', 'tài khoản đc đăng ký']
        );

        let targetRoleId = 3; // Giá trị ID mặc định phòng hờ nếu không tìm thấy trong DB

        if (roleRows.length > 0) {
            targetRoleId = roleRows[0].id; // Lấy đúng ID từ database ra
        } else {
            console.log("Cảnh báo: Không tìm thấy tên quyền 'tài khoản được đăng ký' trong bảng roles, đang dùng ID mặc định là 3.");
        }

        // 3. Tiến hành băm mật khẩu người dùng
        const saltRounds = 10;
        const hashedPassword = await bcrypt.hash(password, saltRounds);

        // 4. Thêm user mới vào bảng `users`
        const insertUserQuery = `
            INSERT INTO users (name, email, password, phone, is_active, email_verified, email_verified_at, created_at, updated_at) 
            VALUES (?, ?, ?, ?, 1, 1, NOW(), NOW(), NOW())
        `;
        const [userResult] = await connection.execute(insertUserQuery, [name, email, hashedPassword, phone]);
        const newUserId = userResult.insertId; // Lấy ID của user vừa tạo

        // 5. Thêm liên kết vào bảng trung gian `user_role` với ID quyền vừa tìm được
        try {
            await connection.execute(
                'INSERT INTO user_role (user_id, role_id) VALUES (?, ?)', 
                [newUserId, targetRoleId]
            );
            console.log(`Đã cấp quyền thành công cho User ID ${newUserId} với Role ID ${targetRoleId}`);
        } catch (roleError) {
            console.error("Lỗi khi chèn dữ liệu vào bảng user_role:", roleError.message);
        }

        await connection.end();

        return res.status(201).json({
            success: true,
            message: 'Đăng ký tài khoản và cấp quyền thành công!'
        });

    } catch (error) {
        console.error("Lỗi Đăng Ký:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi server hệ thống không thể đăng ký!' });
    }
});

module.exports = router;