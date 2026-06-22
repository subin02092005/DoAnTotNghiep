const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

// API lấy lịch thi đấu
// URL: http://localhost:3000/api/matches
router.get('/matches', async (req, res) => {
    try {
        const connection = await mysql.createConnection(dbConfig);
        
        // Truy vấn JOIN để lấy tên đội từ ID
       const query = `
        SELECT m.*, 
            t1.name AS teamA, 
            t2.name AS teamB
        FROM matches m
        JOIN teams t1 ON m.home_team_id = t1.id
        JOIN teams t2 ON m.away_team_id = t2.id
        ORDER BY m.match_date ASC
       `;
        
        const [rows] = await connection.execute(query);
        await connection.end();

        return res.status(200).json({
            status: "success",
            data: rows
        });
    } catch (error) {
        console.error("Lỗi API matches:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;