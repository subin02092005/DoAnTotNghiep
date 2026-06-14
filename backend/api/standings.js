const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình kết nối DB
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy BXH chi tiết
// URL: http://localhost:3000/api/standings/detailed
router.get('/detailed', async (req, res) => {
    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // 1. Truy vấn lấy dữ liệu (cần JOIN với bảng teams để lấy tên đội)
        const query = `
            SELECT s.*, t.name as team_name 
            FROM standings s 
            JOIN teams t ON s.team_id = t.id 
            ORDER BY s.group_name ASC, s.points DESC
        `;
        const [rows] = await connection.execute(query);
        await connection.end();

        // 2. Logic gom nhóm (như cũ)
        const groupedData = rows.reduce((acc, curr) => {
            let group = acc.find(g => g.groupName === curr.group_name);
            if (!group) {
                group = { groupName: curr.group_name, standings: [] };
                acc.push(group);
            }

            group.standings.push({
                rank: curr.rank,
                teamName: curr.team_name,
                played: curr.played,
                won: curr.won,
                drawn: curr.drawn,
                lost: curr.lost,
                goalsFor: curr.goals_for,
                goalsAgainst: curr.goals_against,
                goalDifference: (curr.goals_for - curr.goals_against).toString(),
                points: curr.points,
                form: curr.form ? curr.form.split(',') : []
            });
            return acc;
        }, []);

        return res.status(200).json({
            status: "success",
            data: groupedData
        });

    } catch (error) {
        console.error("Lỗi API standings:", error);
        return res.status(500).json({ 
            status: "error", 
            message: 'Lỗi Database!' 
        });
    }
});

module.exports = router;