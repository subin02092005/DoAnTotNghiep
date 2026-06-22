const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy chi tiết đội bóng và danh sách cầu thủ
router.get('/teams/:id/detail', async (req, res) => {
    const teamId = req.params.id;
    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // 1. Lấy thông tin đội bóng (bao gồm tên đội và HLV)
        const [teamRows] = await connection.execute(
            'SELECT id, name, coach_name, description FROM teams WHERE id = ?', 
            [teamId]
        );

        if (teamRows.length === 0) {
            await connection.end();
            return res.status(404).json({ status: "error", message: "Không tìm thấy đội bóng" });
        }

        // 2. Lấy danh sách cầu thủ thuộc đội bóng này 
        // Kết hợp bảng team_players và bảng users (để lấy tên người dùng) 
        // hoặc bảng players nếu cần thông tin chi tiết cầu thủ
        const [playerRows] = await connection.execute(
            `SELECT tp.player_id, u.name as player_name, tp.position, tp.jersey_number, tp.role 
             FROM team_players tp
             JOIN players p ON tp.player_id = p.id
             JOIN users u ON p.user_id = u.id
             WHERE tp.team_id = ? AND tp.is_active = 1`, 
            [teamId]
        );

        await connection.end();

        // 3. Trả về thông tin kết hợp
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