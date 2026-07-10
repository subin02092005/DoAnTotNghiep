const mysql = require('mysql2/promise');

/**
 * Tái tính toán bảng xếp hạng cho một bảng đấu (group) dựa trên kết quả các trận đấu
 * @param {Object} connection - Kết nối MySQL đang sử dụng (Transaction)
 * @param {number} groupId - ID của bảng đấu cần cập nhật
 */
async function updateGroupStandings(connection, groupId) {
    if (!groupId) return;

    // 1. Lấy luật tính điểm của mùa giải/giải đấu
    const [rulesRows] = await connection.execute(
        `SELECT tr.points_per_win, tr.points_per_draw, tr.points_per_loss
         FROM \`groups\` g
         JOIN phases p ON g.phase_id = p.id
         JOIN seasons s ON p.season_id = s.id
         JOIN tournament_rules tr ON s.tournament_id = tr.tournament_id
         WHERE g.id = ?`,
        [groupId]
    );

    const rules = rulesRows[0] || { points_per_win: 3, points_per_draw: 1, points_per_loss: 0 };

    // 2. Lấy danh sách đội bóng trong bảng đấu này từ team_standings
    const [teams] = await connection.execute(
        `SELECT team_id FROM team_standings WHERE group_id = ?`,
        [groupId]
    );

    const teamStats = {};
    teams.forEach(t => {
        teamStats[t.team_id] = {
            played: 0, wins: 0, draws: 0, losses: 0,
            gf: 0, ga: 0, pts: 0
        };
    });

    // 3. Lấy tất cả các trận đấu ĐÃ KẾT THÚC của bảng này từ bảng match_results
    const [matches] = await connection.execute(
        `SELECT m.home_team_id, m.away_team_id, mr.home_final_score, mr.away_final_score, mr.winner_team_id
         FROM matches m
         JOIN match_results mr ON m.id = mr.match_id
         WHERE m.group_id = ? AND m.status = 'finished' AND m.deleted_at IS NULL`,
        [groupId]
    );

    // 4. Tính toán số liệu thống kê cho từng đội
    matches.forEach(m => {
        const hId = m.home_team_id;
        const aId = m.away_team_id;
        const hScore = m.home_final_score || 0;
        const aScore = m.away_final_score || 0;

        if (teamStats[hId]) {
            teamStats[hId].played++;
            teamStats[hId].gf += hScore;
            teamStats[hId].ga += aScore;
        }
        if (teamStats[aId]) {
            teamStats[aId].played++;
            teamStats[aId].gf += aScore;
            teamStats[aId].ga += hScore;
        }

        if (hScore > aScore) {
            if (teamStats[hId]) { teamStats[hId].wins++; teamStats[hId].pts += rules.points_per_win; }
            if (teamStats[aId]) { teamStats[aId].losses++; teamStats[aId].pts += rules.points_per_loss; }
        } else if (hScore < aScore) {
            if (teamStats[aId]) { teamStats[aId].wins++; teamStats[aId].pts += rules.points_per_win; }
            if (teamStats[hId]) { teamStats[hId].losses++; teamStats[hId].pts += rules.points_per_loss; }
        } else {
            if (teamStats[hId]) { teamStats[hId].draws++; teamStats[hId].pts += rules.points_per_draw; }
            if (teamStats[aId]) { teamStats[aId].draws++; teamStats[aId].pts += rules.points_per_draw; }
        }
    });

    // 5. Cập nhật lại bảng team_standings
    for (const teamId in teamStats) {
        const s = teamStats[teamId];
        await connection.execute(
            `UPDATE team_standings
             SET matches_played = ?, wins = ?, draws = ?, losses = ?,
                 goals_for = ?, goals_against = ?, points = ?, updated_at = NOW()
             WHERE team_id = ? AND group_id = ?`,
            [s.played, s.wins, s.draws, s.losses, s.gf, s.ga, s.pts, teamId, groupId]
        );
    }
}

module.exports = { updateGroupStandings };
