const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
});

router.get('/match/detail', async (req, res) => {
    const matchId = req.query.id;
    if (!matchId) return res.status(400).json({ status: "error", message: "Thiếu ID trận đấu" });

    try {
        // 1. Lấy thông tin trận đấu
       // 1. Lấy thông tin trận đấu (đã thêm JOIN venues)
const [matchRows] = await pool.execute(`
    SELECT m.*, mr.home_final_score, mr.away_final_score,
           t1.name AS teamA, t2.name AS teamB,
           ph.type AS phase_type,
           v.name AS venue_name,
           CASE 
               WHEN mr.home_final_score IS NOT NULL OR mr.away_final_score IS NOT NULL THEN 'finished'
               WHEN m.scheduled_at <= NOW() AND DATE_ADD(m.scheduled_at, INTERVAL 90 MINUTE) >= NOW() THEN 'ongoing'
               WHEN m.scheduled_at <= NOW() THEN 'finished' -- Nếu quá 90 phút mà admin chưa nhập kết quả thì coi như xong
               ELSE 'pending'
           END AS status,
           -- Tính phút của trận đấu (nếu đang đá)
           TIMESTAMPDIFF(MINUTE, m.scheduled_at, NOW()) AS current_minute
    FROM matches m
    LEFT JOIN match_results mr ON m.id = mr.match_id
    JOIN teams t1 ON m.home_team_id = t1.id
    JOIN teams t2 ON m.away_team_id = t2.id
    LEFT JOIN phases ph ON m.phase_id = ph.id
    LEFT JOIN venues v ON m.venue_id = v.id
    WHERE m.id = ?
`, [matchId]);

        if (matchRows.length === 0) return res.status(404).json({ status: "error", message: "Không tìm thấy trận đấu" });
        const matchData = matchRows[0];

        // 2. Lấy sự kiện trận đấu (có tên đội)
        const [events] = await pool.execute(`
            SELECT e.minute, e.type, e.team_id, t.name AS team, u.name AS playerName 
            FROM match_events e
            LEFT JOIN teams t ON e.team_id = t.id
            LEFT JOIN players p ON e.player_id = p.id
            LEFT JOIN users u ON p.user_id = u.id
            WHERE e.match_id = ? 
            ORDER BY e.minute ASC
        `, [matchId]);

        // 3. Lấy đội hình và phân loại thông minh
        const [allPlayers] = await pool.execute(`
            SELECT tp.player_id, tp.jersey_number, u.name, tp.position, tp.team_id
            FROM team_players tp
            JOIN players p ON tp.player_id = p.id
            JOIN users u ON p.user_id = u.id
            WHERE tp.team_id IN (?, ?) AND tp.is_active = 1
            ORDER BY tp.jersey_number ASC
        `, [matchData.home_team_id, matchData.away_team_id]);

        const processTeamPlayers = (teamId) => {
            const players = allPlayers.filter(p => p.team_id == teamId);

            // TẬN DỤNG: Mặc định 11 người đầu tiên (theo số áo) là đá chính, còn lại là dự bị
            const lineup = players.slice(0, 11);
            const subs = players.slice(11);

            return { lineup, subs };
        };

        const teamAData = processTeamPlayers(matchData.home_team_id);
        const teamBData = processTeamPlayers(matchData.away_team_id);

        // 4. Trả về JSON
        const responseData = {
            id: matchData.id,
            teamA: matchData.teamA,
            teamB: matchData.teamB,
            home_team_id: matchData.home_team_id,
            away_team_id: matchData.away_team_id,
            status: matchData.status,
            isStarted: matchData.status !== 'pending',
            scoreA: matchData.home_final_score !== null ? matchData.home_final_score : (matchData.home_score || 0),
            scoreB: matchData.away_final_score !== null ? matchData.away_final_score : (matchData.away_score || 0),
            time: matchData.scheduled_at,
            date: matchData.scheduled_at,
            venue_name: matchData.venue_name || "Đang cập nhật",
            events: events,
            lineupA: teamAData.lineup,
            subsA: teamAData.subs,
            lineupB: teamBData.lineup,
            subsB: teamBData.subs,
            PossessionA: "50%",
            PossessionB: "50%",
            ShotsA: "0",
            ShotsB: "0",
            mvp: "Chưa xác định",
            isHot: matchData.phase_type === 'semi_final' || matchData.phase_type === 'final'
        };

        return res.status(200).json({
            success: true,
            status: "success",
            data: responseData
        });

    } catch (error) {
        console.error("Lỗi API:", error);
        return res.status(500).json({ status: "error", message: error.message });
    }
});


module.exports = router;