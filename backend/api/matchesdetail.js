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
        const [matchRows] = await pool.execute(`
            SELECT m.*, mr.home_final_score, mr.away_final_score,
                   t1.name AS teamA, t2.name AS teamB,
                   ph.type AS phase_type,
                   v.name AS venue_name,
                   COALESCE(tr.min_players_per_team, 7) AS format_size,
                   CASE 
                       WHEN mr.home_final_score IS NOT NULL OR mr.away_final_score IS NOT NULL THEN 'finished'
                       WHEN m.scheduled_at <= NOW() AND DATE_ADD(m.scheduled_at, INTERVAL 90 MINUTE) >= NOW() THEN 'ongoing'
                       WHEN m.scheduled_at <= NOW() THEN 'finished' 
                       ELSE 'pending'
                   END AS status,
                   TIMESTAMPDIFF(MINUTE, m.scheduled_at, NOW()) AS current_minute
            FROM matches m
            LEFT JOIN match_results mr ON m.id = mr.match_id
            JOIN teams t1 ON m.home_team_id = t1.id
            JOIN teams t2 ON m.away_team_id = t2.id
            LEFT JOIN phases ph ON m.phase_id = ph.id
            LEFT JOIN venues v ON m.venue_id = v.id
            LEFT JOIN seasons s ON m.season_id = s.id
            LEFT JOIN tournaments t ON s.tournament_id = t.id
            LEFT JOIN tournament_rules tr ON t.id = tr.tournament_id
            WHERE m.id = ?
        `, [matchId]);

        if (matchRows.length === 0) return res.status(404).json({ status: "error", message: "Không tìm thấy trận đấu" });
        const matchData = matchRows[0];

        // 2. Lấy sự kiện trận đấu
        const [events] = await pool.execute(`
            SELECT e.minute, e.type, e.team_id, t.name AS team, u.name AS playerName, e.player_id, e.sub_out_player_id
            FROM match_events e
            LEFT JOIN teams t ON e.team_id = t.id
            LEFT JOIN players p ON e.player_id = p.id
            LEFT JOIN users u ON p.user_id = u.id
            WHERE e.match_id = ? 
            ORDER BY e.minute ASC
        `, [matchId]);

        // 3. Đếm số thẻ vàng / đỏ trực tiếp từ match_events cho từng đội
        const [cardCounts] = await pool.execute(`
            SELECT team_id,
                   SUM(CASE WHEN type IN ('yellow_card', 'second_yellow') THEN 1 ELSE 0 END) AS yellow_count,
                   SUM(CASE WHEN type IN ('red_card', 'second_yellow') THEN 1 ELSE 0 END) AS red_count
            FROM match_events
            WHERE match_id = ?
            GROUP BY team_id
        `, [matchId]);

        const cardsMap = {};
        cardCounts.forEach(row => {
            cardsMap[row.team_id] = {
                yellow: row.yellow_count || 0,
                red: row.red_count || 0
            };
        });

        const cardsA = cardsMap[matchData.home_team_id] || { yellow: 0, red: 0 };
        const cardsB = cardsMap[matchData.away_team_id] || { yellow: 0, red: 0 };

        // 4. Lấy đội hình
       // 4. Lấy đội hình và ép is_starter về boolean (true / false)
        const [allPlayers] = await pool.execute(`
            SELECT tp.player_id, tp.jersey_number, u.name, tp.position, tp.team_id, 
                   CAST(tp.is_starter AS UNSIGNED) AS is_starter
            FROM team_players tp
            JOIN players p ON tp.player_id = p.id
            JOIN users u ON p.user_id = u.id
            WHERE tp.team_id IN (?, ?) AND tp.is_active = 1
            ORDER BY tp.jersey_number ASC
        `, [matchData.home_team_id, matchData.away_team_id]);

        const processTeamPlayers = (teamId) => {
            const players = allPlayers.filter(p => p.team_id == teamId).map(p => ({
                ...p,
                is_starter: p.is_starter === 1 // 🌟 Chuyển thành true / false ở đây
            }));
            
            const lineup = players.filter(p => p.is_starter === true);
            const subs = players.filter(p => p.is_starter === false);
            
            if (lineup.length === 0) {
                return { lineup: players.slice(0, 11), subs: players.slice(11) };
            }
            return { lineup, subs };
        };

        const teamAData = processTeamPlayers(matchData.home_team_id);
        const teamBData = processTeamPlayers(matchData.away_team_id);

        const scoreA = matchData.home_final_score !== null ? matchData.home_final_score : (matchData.home_score || 0);
        const scoreB = matchData.away_final_score !== null ? matchData.away_final_score : (matchData.away_score || 0);

        // 🌟 Tự động giả lập chỉ số Kiểm soát bóng & Cú sút dựa vào tỷ số và ID trận (để số liệu luôn ổn định cho từng trận)
        let possessionA = 50, possessionB = 50;
        let shotsA = 5 + scoreA * 3, shotsB = 5 + scoreB * 3;
        if (scoreA > scoreB) {
            possessionA = 54; possessionB = 46;
        } else if (scoreA < scoreB) {
            possessionA = 46; possessionB = 54;
        } else {
            possessionA = 50; possessionB = 50;
            shotsA = 8; shotsB = 8;
        }

        // 5. Trả về JSON hoàn thiện
        const responseData = {
            id: matchData.id,
            teamA: matchData.teamA,
            teamB: matchData.teamB,
            home_team_id: matchData.home_team_id,
            away_team_id: matchData.away_team_id,
            status: matchData.status,
          isStarted: matchData.status !== 'pending' ? true : false,
            scoreA: scoreA,
            scoreB: scoreB,
            time: matchData.scheduled_at,
            date: matchData.scheduled_at,
            venue_name: matchData.venue_name || "Đang cập nhật",
            formatSize: matchData.format_size,
            events: events,
            lineupA: teamAData.lineup,
            subsA: teamAData.subs,
            lineupB: teamBData.lineup,
            subsB: teamBData.subs,
            // Thêm các thông số thống kê giả lập thông minh & đếm thẻ thực tế
            PossessionA: `${possessionA}%`,
            PossessionB: `${possessionB}%`,
            ShotsA: shotsA.toString(),
            ShotsB: shotsB.toString(),
            YellowCardsA: cardsA.yellow.toString(),
            YellowCardsB: cardsB.yellow.toString(),
            RedCardsA: cardsA.red.toString(),
            RedCardsB: cardsB.red.toString(),
            mvp: "Đang cập nhật",
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