const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// 1. Tạo Pool một lần duy nhất ở ngoài router
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
    waitForConnections: true,
    connectionLimit: 10 // Số kết nối tối đa
});

router.get('/team/:id/detail', async (req, res) => {
    const teamId = req.params.id;
    try {
        // 2. Sử dụng pool.query thay vì createConnection
        // Chạy song song 2 câu query bằng Promise.all để tăng tốc độ phản hồi
        const [teamRows] = await pool.query(
            'SELECT id, name, coach_name, description FROM teams WHERE id = ?', 
            [teamId]
        );

        if (teamRows.length === 0) {
            return res.status(404).json({ status: "error", message: "Không tìm thấy đội bóng" });
        }

        const [playerRows] = await pool.query(
            `SELECT tp.player_id, u.name as player_name, tp.position, tp.jersey_number, tp.role 
             FROM team_players tp
             JOIN players p ON tp.player_id = p.id
             JOIN users u ON p.user_id = u.id
             WHERE tp.team_id = ? AND tp.is_active = 1`, 
            [teamId]
        );

        return res.status(200).json({
            status: "success",
            data: {
                ...teamRows[0],
                players: playerRows 
            }
        });

    } catch (error) {
        console.error("Lỗi API lấy chi tiết đội:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;