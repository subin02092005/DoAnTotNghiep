const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình pool kết nối
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
   
});

router.get('/standings', async (req, res) => {
    try {
        const query = `
            SELECT 
                s.team_id,
                s.matches_played AS played, 
                s.wins AS won, 
                s.draws AS drawn, 
                s.losses AS lost, 
                s.goals_for, 
                s.goals_against, 
                (s.goals_for - s.goals_against) AS goal_difference, 
                s.points,
                t.name AS team_name, 
                g.name AS group_name
            FROM team_standings s
            JOIN teams t ON s.team_id = t.id
            JOIN \`groups\` g ON s.group_id = g.id
            WHERE s.is_active = 1
            ORDER BY g.name ASC, s.points DESC, goal_difference DESC
        `;
        
        const [rows] = await pool.query(query);
        const formattedData = [];
        const groups = {};

        for (const row of rows) {
            // Lấy 5 trận gần nhất đã kết thúc
            const [recentMatches] = await pool.query(
                `SELECT home_team_id, away_team_id, home_score, away_score 
                 FROM matches 
                 WHERE (home_team_id = ? OR away_team_id = ?) 
                 AND status = 'finished' 
                 ORDER BY played_at DESC LIMIT 5`,
                [row.team_id, row.team_id]
            );

            // Tính toán W, D, L cho đội dựa trên home_score và away_score
            const formArray = recentMatches.map(m => {
                const isHome = (m.home_team_id === row.team_id);
                const teamScore = isHome ? m.home_score : m.away_score;
                const oppScore = isHome ? m.away_score : m.home_score;

                if (teamScore > oppScore) return 'W';
                if (teamScore < oppScore) return 'L';
                return 'D';
            });

            if (!groups[row.group_name]) {
                groups[row.group_name] = { groupName: row.group_name, standings: [] };
                formattedData.push(groups[row.group_name]);
            }

            groups[row.group_name].standings.push({
                rank: groups[row.group_name].standings.length + 1,
                teamName: row.team_name,
                played: row.played,
                won: row.won,
                drawn: row.drawn,
                lost: row.lost,
                goalsFor: row.goals_for,
                goalsAgainst: row.goals_against,
                goalDifference: row.goal_difference.toString(),
                points: row.points,
                form: formArray // Dữ liệu phong độ đã chuẩn
            });
        }

        return res.status(200).json({ status: "success", data: formattedData });
    } catch (error) {
        res.status(500).json({ status: "error", message: error.message });
    }
});

module.exports = router;