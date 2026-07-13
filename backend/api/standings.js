const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const { updateGroupStandings } = require('./admin/standingsHelper');

// Cấu hình pool kết nối
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
});
router.get('/all_seasons', async (req, res) => {
    let connection;
    try {
        connection = await pool.getConnection();

        // Sử dụng LEFT JOIN để lấy cả mùa giải chưa có vòng đấu nào
        const query = `
            SELECT 
                s.id AS season_id, 
                s.name AS season_name, 
                p.id AS phase_id, 
                p.name AS phase_name 
            FROM seasons s 
            LEFT JOIN phases p ON s.id = p.season_id
            ORDER BY s.id DESC, p.id ASC
        `;

        const [rows] = await connection.query(query);

        // Khởi tạo mảng để gom nhóm dữ liệu theo cấu trúc lồng nhau (Nested JSON)
        const formattedData = [];
        const seasonsMap = {};

        for (const row of rows) {
            // Nếu database trống hoàn toàn
            if (!row.season_id) continue;

            // Nếu mùa giải này chưa được tạo trong Map thì khởi tạo mới
            if (!seasonsMap[row.season_id]) {
                seasonsMap[row.season_id] = {
                    id: row.season_id,
                    name: row.season_name,
                    phases: [] // Mảng chứa các vòng đấu thuộc giải này
                };
                formattedData.push(seasonsMap[row.season_id]);
            }

            // Nếu mùa giải có vòng đấu đi kèm (phase_id không null) thì đẩy vào mảng phases
            if (row.phase_id) {
                seasonsMap[row.season_id].phases.push({
                    id: row.phase_id,
                    name: row.phase_name
                });
            }
        }

        // Trả về kết quả chuẩn trạng thái success như cấu trúc của bạn
        return res.status(200).json({
            success: true,
            status: "success",
            data: formattedData // Nếu rỗng sẽ là [], Android nhận vào sẽ biết là "Không có dữ liệu"
        });

    } catch (error) {
        console.error("Lỗi API Danh sách Mùa giải:", error.message);
        res.status(500).json({ success: false, message: error.message });
    } finally {
        // Luôn giải phóng kết nối về lại cho Pool
        if (connection) connection.release();
    }
});
router.get('/standings', async (req, res) => {
    const { seasonId } = req.query;
    if (!seasonId) {
        return res.status(400).json({ success: false, message: "Thiếu Season ID" });
    }

    let connection;
    try {
        connection = await pool.getConnection();
        await connection.beginTransaction();

        // 1. Lấy danh sách các bảng đấu (groups) thuộc mùa giải này
        const [groups] = await connection.execute(
            `SELECT g.id FROM \`groups\` g
             JOIN phases p ON g.phase_id = p.id
             WHERE p.season_id = ?`,
            [seasonId]
        );

        // 2. Đảm bảo dữ liệu được khởi tạo và cập nhật
        for (const group of groups) {
            const [teamsInGroup] = await connection.execute(
                `SELECT team_id FROM season_teams WHERE group_id = ? AND deleted_at IS NULL`,
                [group.id]
            );

            for (const t of teamsInGroup) {
                const [exists] = await connection.execute(
                    `SELECT id FROM team_standings WHERE team_id = ? AND group_id = ?`,
                    [t.team_id, group.id]
                );
                if (exists.length === 0) {
                    await connection.execute(
                        `INSERT INTO team_standings (team_id, group_id, position, matches_played, wins, draws, losses, goals_for, goals_against, points, is_active, created_at)
                         VALUES (?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 1, NOW())`,
                        [t.team_id, group.id]
                    );
                }
            }
            await updateGroupStandings(connection, group.id);
        }

        await connection.commit();

        // 3. Truy vấn dữ liệu trả về
        const query = `
            SELECT 
                st.team_id,
                st.group_id,
                g.name AS group_name,
                p.id AS phase_id,
                t.name AS team_name,
                t.logo AS team_logo,
                COALESCE(st.matches_played, 0) AS played,
                COALESCE(st.wins, 0) AS won,
                COALESCE(st.draws, 0) AS drawn,
                COALESCE(st.losses, 0) AS lost,
                COALESCE(st.goals_for, 0) AS goals_for,
                COALESCE(st.goals_against, 0) AS goals_against,
                (COALESCE(st.goals_for, 0) - COALESCE(st.goals_against, 0)) AS goal_difference,
                COALESCE(st.points, 0) AS points
            FROM team_standings st
            JOIN teams t ON st.team_id = t.id
            JOIN \`groups\` g ON st.group_id = g.id
            JOIN phases p ON g.phase_id = p.id
            WHERE p.season_id = ? AND st.is_active = 1
            ORDER BY p.id ASC, g.name ASC, st.points DESC, goal_difference DESC, st.goals_for DESC, t.name ASC
        `;

        const [rows] = await connection.query(query, [seasonId]);
        const formattedData = [];
        const groupsMap = {};

        for (const row of rows) {
            const groupKey = `${row.phase_id}_${row.group_id}`;

            // Lấy 5 trận gần nhất để hiển thị phong độ
            const [recentMatches] = await connection.query(
                `SELECT home_team_id, away_team_id, home_score, away_score
                 FROM matches
                 WHERE (home_team_id = ? OR away_team_id = ?)
                 AND status = 'finished'
                 ORDER BY played_at DESC LIMIT 5`,
                [row.team_id, row.team_id]
            );

            const formArray = recentMatches.map(m => {
                const isHome = (m.home_team_id === row.team_id);
                const teamScore = isHome ? m.home_score : m.away_score;
                const oppScore = isHome ? m.away_score : m.home_score;
                if (teamScore > oppScore) return 'W';
                if (teamScore < oppScore) return 'L';
                return 'D';
            });

            if (!groupsMap[groupKey]) {
                groupsMap[groupKey] = {
                    phaseId: row.phase_id,
                    groupId: row.group_id,
                    groupName: row.group_name,
                    standings: []
                };
                formattedData.push(groupsMap[groupKey]);
            }

            groupsMap[groupKey].standings.push({
                id: row.team_id,
                rank: groupsMap[groupKey].standings.length + 1,
                teamName: row.team_name,
                logoUrl: row.team_logo || "",
                played: row.played,
                won: row.won,
                drawn: row.drawn,
                lost: row.lost,
                goalsFor: row.goals_for,
                goalsAgainst: row.goals_against,
                goalDifference: row.goal_difference.toString(),
                points: row.points,
                form: formArray
            });
        }

        return res.status(200).json({
            success: true,
            status: "success",
            data: formattedData
        });

    } catch (error) {
        if (connection) await connection.rollback();
        console.error("Lỗi API Bảng xếp hạng:", error.message);
        res.status(500).json({ success: false, message: error.message });
    } finally {
        if (connection) connection.release();
    }
});

module.exports = router;
