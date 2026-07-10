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

        // 🌟 Sửa query: Join thêm bảng players
        const query = `
            SELECT u.name, u.email, u.phone, u.email_verified, r.name as role_name, 
                   p.date_of_birth
            FROM users u
            LEFT JOIN user_role ur ON u.id = ur.user_id
            LEFT JOIN roles r ON ur.role_id = r.id
            LEFT JOIN players p ON u.id = p.user_id
            WHERE u.email = ? 
            LIMIT 1
        `;
        
        const [rows] = await connection.execute(query, [email]);
        await connection.end();

        if (rows.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy người dùng!' });
        }

        const user = rows[0];
        
        // Trả về dữ liệu kèm ngày sinh
        return res.status(200).json({
            success: true,
            data: {
                name: user.name,
                email: user.email,
                phone: user.phone,
                email_verified: user.email_verified,
                role: user.role_name || 'user',
                // 🌟 Định dạng ngày sinh (nếu có dữ liệu)
               date_of_birth: user.date_of_birth ? new Date(user.date_of_birth.getTime() - (user.date_of_birth.getTimezoneOffset() * 60000)).toISOString().split('T')[0] : null
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
<<<<<<< HEAD
       const otp = req.body.otp ? String(req.body.otp).trim() : null;
=======
    const otp = req.body.otp ? String(req.body.otp).trim() : null;
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd

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

// 🌟 API 3: CẬP NHẬT THÔNG TIN CÁ NHÂN
// Đường dẫn: POST http://localhost:3000/api/profile/update-info
router.post('/profile/update-info', async (req, res) => {
    // 1. Nhận thêm date_of_birth từ request body
    const { email, name, phone, date_of_birth } = req.body;

    if (!email) {
        return res.status(400).json({ success: false, message: 'Email là bắt buộc để cập nhật!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // 2. Tìm user_id dựa trên email (để dùng cho bảng players)
        const [users] = await connection.execute('SELECT id FROM users WHERE email = ?', [email]);
        if (users.length === 0) {
            await connection.end();
            return res.status(404).json({ success: false, message: 'Không tìm thấy người dùng!' });
        }
        const userId = users[0].id;

        // 3. Cập nhật bảng users
        await connection.execute(
            'UPDATE users SET name = ?, phone = ? WHERE email = ?', 
            [name, phone, email]
        );

        // 4. Cập nhật ngày sinh vào bảng players
        // Lưu ý: Nếu user chưa tồn tại trong bảng players, bạn có thể cần lệnh INSERT hoặc UPDATE
        // Ở đây dùng UPDATE giả định dòng cầu thủ đã tồn tại
       let formattedDob = null;
if (date_of_birth) {
    // Nếu dữ liệu có chứa chữ 'T' (dạng ISO), lấy phần đầu: YYYY-MM-DD
    // Ví dụ: '1997-07-24T00:00Z[UTC]' -> '1997-07-24'
    formattedDob = date_of_birth.toString().split('T')[0];
}

const sql = `
    INSERT INTO players (user_id, date_of_birth) 
    VALUES (?, ?) 
    ON DUPLICATE KEY UPDATE date_of_birth = ?
`;
// TRUYỀN formattedDob VÀO THAY VÌ date_of_birth
await connection.execute(sql, [userId, formattedDob, formattedDob]);

        await connection.end();

        return res.status(200).json({
            success: true,
            message: 'Cập nhật thông tin thành công!'
        });
    } catch (error) {
        console.error("Lỗi cập nhật Profile:",  error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi hệ thống khi cập nhật!' });
    }
});
// 🌟 API 4: ĐỔI MẬT KHẨU
// Đường dẫn: POST http://localhost:3000/api/profile/change-password
router.post('/profile/change-password', async (req, res) => {
    const { email, oldPassword, newPassword } = req.body;

    if (!email || !oldPassword || !newPassword) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp đầy đủ thông tin!' });
    }

    let connection;
    try {
        connection = await mysql.createConnection(dbConfig);

        // 1. Lấy mật khẩu hiện tại trong DB
        const [users] = await connection.execute('SELECT password FROM users WHERE email = ?', [email]);
        
        if (users.length === 0) {
            await connection.end();
            return res.status(404).json({ success: false, message: 'Người dùng không tồn tại!' });
        }

        const currentPasswordHash = users[0].password;

        // 2. So sánh mật khẩu cũ
        // Nếu bạn đang lưu mật khẩu thô (text), hãy thay dòng dưới bằng: if (oldPassword !== currentPasswordHash)
        const isMatch = await bcrypt.compare(oldPassword, currentPasswordHash);
        
        if (!isMatch) {
            await connection.end();
            return res.status(400).json({ success: false, message: 'Mật khẩu cũ không chính xác!' });
        }

        // 3. Mã hóa mật khẩu mới
        const saltRounds = 10;
        const newPasswordHash = await bcrypt.hash(newPassword, saltRounds);

        // 4. Cập nhật mật khẩu mới vào database
        await connection.execute('UPDATE users SET password = ? WHERE email = ?', [newPasswordHash, email]);
        
        await connection.end();
        return res.status(200).json({ success: true, message: 'Đổi mật khẩu thành công!' });

    } catch (error) {
        console.error("Lỗi đổi mật khẩu:", error);
        if (connection) await connection.end();
        return res.status(500).json({ success: false, message: 'Lỗi hệ thống khi đổi mật khẩu!' });
    }
});
module.exports = router;