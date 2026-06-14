const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình kết nối Database chung (Giữ nguyên từ login.js của bạn)
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// 🌟 ĐĂNG KÝ ĐƯỜNG DẪN TRỰC TIẾP: /seasons/:seasonId/phases
router.get('/seasons/:seasonId/phases', async (req, res) => {
    const seasonId = req.params.seasonId;

    if (!seasonId) {
        return res.status(400).json({ 
            status: "error", 
            message: 'Thiếu thông tin mã mùa giải (seasonId)!' 
        });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);
        const [rows] = await connection.execute(
            'SELECT id, name, type FROM tournament_phases WHERE season_id = ? ORDER BY id ASC', 
            [seasonId]
        );
        await connection.end();

        // Nếu database chưa có dữ liệu, trả về mock data để tránh lỗi app Android của bạn
        if (rows.length === 0) {
            return res.status(200).json({
                status: "success",
                message: "Tải danh sách vòng đấu mẫu thành công!",
                data: {
                    seasonId: parseInt(seasonId),
                    phases: [
                        { id: 1, name: "Vòng Bảng", type: "round_robin" },
                        { id: 2, name: "Tứ Kết", type: "knockout" },
                        { id: 3, name: "Bán Kết", type: "knockout" },
                        { id: 4, name: "Chung Kết", type: "knockout" }
                    ]
                }
            });
        }

        // Trả về dữ liệu thật từ DB
        return res.status(200).json({
            status: "success",
            message: "Tải danh sách vòng đấu thành công từ Database!",
            data: {
                seasonId: parseInt(seasonId),
                phases: rows.map(item => ({
                    id: item.id,
                    name: item.name, 
                    type: item.type  
                }))
            }
        });

    } catch (error) {
        console.error("Lỗi API lấy vòng đấu:", error);
        return res.status(500).json({ status: "error", message: 'Lỗi kết nối Server hoặc Database!' });
    }
});

module.exports = router;