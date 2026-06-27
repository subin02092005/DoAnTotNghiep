const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy danh sách bài viết (Thông báo)
router.get('/notifications', async (req, res) => {
    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // Lấy các thông tin cần thiết từ bảng articles
        // status = 'published' (giả sử bài viết phải được xuất bản mới hiện)
        const [rows] = await connection.execute(
            'SELECT id, title, content, cover_image FROM articles ORDER BY id DESC'
        );
        
        await connection.end();

        // Map dữ liệu khớp với Model Notification bên Android
        const formattedData = rows.map(item => ({
            id: item.id,
            title: item.title,          // Tiêu đề
            content: item.content,      // Nội dung
            imageUrl: item.cover_image || "", // Ảnh bìa (nếu có)
            time: "Mới cập nhật"        // Bạn có thể sửa thành cột created_at nếu DB có
        }));

        return res.status(200).json({
            status: "success",
            message: "Tải danh sách bài viết thành công!",
            data: formattedData
        });

    } catch (error) {
        console.error("Lỗi API bài viết:", error);
        return res.status(500).json({ 
            status: "error", 
            message: 'Lỗi kết nối Server!' 
        });
    }
});

module.exports = router;