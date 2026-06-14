const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy chi tiết đội bóng theo ID
// URL: http://localhost:3000/api/teams/:id
router.get('/teams/:id/detail', async (req, res) => {
    const teamId = req.params.id;
    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // 1. Lấy thông tin đội bóng
        const [teamRows] = await connection.execute(
            'SELECT * FROM teams WHERE id = ?', [teamId]
        );

        if (teamRows.length === 0) {
            await connection.end();
            return res.status(404).json({ status: "error", message: "Không tìm thấy đội bóng" });
        }

        // 2. Lấy danh sách cầu thủ thuộc đội bóng này
        const [playerRows] = await connection.execute(
            'SELECT * FROM players WHERE team_id = ?', [teamId]
        );

        await connection.end();

        // 3. Trả về thông tin kết hợp
        return res.status(200).json({
            status: "success",
            data: {
                ...teamRows[0],
                players: playerRows // Trả về danh sách cầu thủ bên trong object đội bóng
            }
        });

    } catch (error) {
        console.error("Lỗi API lấy chi tiết đội:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;