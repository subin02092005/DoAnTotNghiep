const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình kết nối Database chung của bạn
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy danh sách tin tức bóng đá
// URL: http://localhost:3000/api/news
router.get('/news', async (req, res) => {
    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // Truy vấn lấy toàn bộ tin tức mới nhất từ bảng football_news
        const [rows] = await connection.execute('SELECT * FROM football_news ORDER BY id DESC');
        
        await connection.end();

        // Định dạng biến trả về chuẩn hóa khớp 100% với Model Object bên Android
        const formattedNews = rows.map(item => ({
            id: item.id,
            title: item.title,
            summary: item.summary,
            time: item.time_posted, // Đổi tên hiển thị cho khớp thuộc tính "time" của Android
            source: item.source,
            category: item.category,
            content: item.content,
            author: item.author
        }));

        return res.status(200).json({
            status: "success",
            message: "Tải danh sách tin tức thành công!",
            data: formattedNews
        });

    } catch (error) {
        console.error("Lỗi API tin tức:", error);
        return res.status(500).json({ 
            status: "error", 
            message: 'Lỗi kết nối Server hoặc Database!' 
        });
    }
});

module.exports = router;