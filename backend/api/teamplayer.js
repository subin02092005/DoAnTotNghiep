const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// 1. Tạo Pool một lần duy nhất ở ngoài router
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
   
});

router.get('/team/:id/detail', async (req, res) => {
    const teamId = req.params.id;
    try {
        // 1. Lấy thông tin đội bóng
        const [teamRows] = await pool.query(
            'SELECT id, name, coach_name FROM teams WHERE id = ?', 
            [teamId]
        );

        if (teamRows.length === 0) {
            return res.status(404).json({ status: "error", message: "Không tìm thấy đội bóng" });
        }

        // 2. Lấy thông tin đội trưởng (Captain)
        const [captainRows] = await pool.query(
            `SELECT u.name 
             FROM team_players tp
             JOIN players p ON tp.player_id = p.id
             JOIN users u ON p.user_id = u.id
             WHERE tp.team_id = ? AND tp.role = 'captain' AND tp.is_active = 1 LIMIT 1`, 
            [teamId]
        );

        // 3. Lấy danh sách cầu thủ
        const [playerRows] = await pool.query(
            `SELECT tp.player_id as id, u.name as name, tp.position, tp.jersey_number, tp.role 
             FROM team_players tp
             JOIN players p ON tp.player_id = p.id
             JOIN users u ON p.user_id = u.id
             WHERE tp.team_id = ? AND tp.is_active = 1`, 
            [teamId]
        );

        return res.status(200).json({
            status: "success",
            message: "Lấy dữ liệu thành công",
           data: {
        id: teamRows[0].id,
        teamName: teamRows[0].name, // Ánh xạ từ cột name trong DB
        coachName: teamRows[0].coach_name,
        captainName: captainRows.length > 0 ? captainRows[0].name : "Chưa cập nhật",
        players: playerRows 
    }
        });

    } catch (error) {
        console.error("Lỗi API:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;