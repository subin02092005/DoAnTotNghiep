const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// 1. Dùng Pool để quản lý kết nối hiệu quả hơn
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
  
});

router.get('/matches', async (req, res) => {
    try {
        const query = `
            SELECT m.id, m.scheduled_at, m.home_team_id, m.away_team_id, 
                   t1.name AS teamA, 
                   t2.name AS teamB,
                   IFNULL(mr.home_final_score, 0) AS home_final_score,
                   IFNULL(mr.away_final_score, 0) AS away_final_score,
                   m.is_featured,
                   CASE 
                       WHEN mr.home_final_score IS NOT NULL OR mr.away_final_score IS NOT NULL THEN 'finished'
                       WHEN m.scheduled_at <= NOW() THEN 'ongoing'
                       ELSE 'pending'
                   END AS status, 
                  CASE 
    WHEN m.is_featured = 1 OR ph.type IN ('semi_final', 'final') THEN 1 
    ELSE 0 
END AS isHot
            FROM matches m
            JOIN teams t1 ON m.home_team_id = t1.id
            JOIN teams t2 ON m.away_team_id = t2.id
            LEFT JOIN match_results mr ON m.id = mr.match_id
            LEFT JOIN phases ph ON m.phase_id = ph.id
            WHERE m.is_active = 1 
           /* AND m.scheduled_at BETWEEN DATE_SUB(NOW(), INTERVAL 7 DAY) 
                                   AND DATE_ADD(NOW(), INTERVAL 7 DAY) 
            */
            ORDER BY m.scheduled_at ASC
        `;
        
        const [rows] = await pool.execute(query);

        return res.status(200).json({
            status: "success",
            data: rows
        });
    } catch (error) {
        console.error("Lỗi API matches:", error);
        return res.status(500).json({ 
            status: "error", 
            message: "Lỗi kết nối database", 
            debug: error.message 
        });
    }
});
module.exports = router;