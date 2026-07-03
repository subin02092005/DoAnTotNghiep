const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');


const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// 🌟 ĐĂNG KÝ ĐƯỜNG DẪN TRỰC TIẾP:
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
        
        // 1. Sửa tên bảng từ 'tournament_phases' thành 'phases'
        // 2. Chọn thêm cột 'type' và các trường cần thiết
        const [rows] = await connection.execute(
            'SELECT id, name, type, format FROM phases WHERE season_id = ? ORDER BY `order` ASC', 
            [seasonId]
        );
        await connection.end();

        // Trả về dữ liệu thật từ DB
        return res.status(200).json({
            status: "success",
            message: "Tải danh sách vòng đấu thành công từ Database!",
            data: {
                seasonId: parseInt(seasonId),
                phases: rows.map(item => ({
                    id: item.id,
                    name: item.name, 
                    type: item.type,
                    format: item.format  // Giá trị này sẽ khớp với ENUM trong DB
                }))
            }
        });

    } catch (error) {
        console.error("Lỗi API lấy vòng đấu:", error);
        return res.status(500).json({ status: "error", message: 'Lỗi kết nối Server hoặc Database!' });
    }
});

module.exports = router;