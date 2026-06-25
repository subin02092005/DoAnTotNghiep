const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};
// API lấy chi tiết trận đấu
// URL: http://localhost:3000/api/match/detail?id=1
router.get('/match/detail', async (req, res) => {
    const matchId = req.query.id;

    if (!matchId) {
        return res.status(400).json({ status: "error", message: "Thiếu ID trận đấu" });
    }

    try {
        const connection = await mysql.createConnection(dbConfig);

        // 1. Lấy thông tin trận đấu chính (JOIN với tên đội)
        const matchQuery = `
            SELECT m.*, t1.name AS teamA, t2.name AS teamB
            FROM matches m
            JOIN teams t1 ON m.home_team_id = t1.id
            JOIN teams t2 ON m.away_team_id = t2.id
            WHERE m.id = ?
        `;
        const [matchRows] = await connection.execute(matchQuery, [matchId]);

        if (matchRows.length === 0) {
            await connection.end();
            return res.status(404).json({ status: "error", message: "Không tìm thấy trận đấu" });
        }

        // 2. Lấy sự kiện (Events)
        const [events] = await connection.execute(
            "SELECT minute, team, type, playerName FROM match_events WHERE match_id = ? ORDER BY minute ASC",
            [matchId]
        );

        // 3. Lấy đội hình chính (Lineups) - ví dụ lấy cả A và B
        const [lineups] = await connection.execute(
            "SELECT number, name, position, team_type FROM lineups WHERE match_id = ?",
            [matchId]
        );

        await connection.end();

        // Gộp dữ liệu theo đúng cấu trúc FullMatchDetail trong App Android
        const matchData = matchRows[0];
        
        // Phân loại đội hình ra A và B
        const lineupA = lineups.filter(p => p.team_type === 'A');
        const lineupB = lineups.filter(p => p.team_type === 'B');

        const responseData = {
            ...matchData,
            events: events,
            lineupA: lineupA,
            lineupB: lineupB,
            // Nếu bạn có bảng subs và stats, hãy truy vấn tương tự ở trên
            subsA: [], 
            subsB: [],
            PossessionA: matchData.possession_a || "0%",
            PossessionB: matchData.possession_b || "0%",
            ShotsA: matchData.shots_a || "0",
            ShotsB: matchData.shots_b || "0",
            mvp: matchData.mvp_name || ""
        };

        return res.status(200).json({
            status: "success",
            data: responseData
        });

    } catch (error) {
        console.error("Lỗi API match/detail:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server" });
    }
});

module.exports = router;