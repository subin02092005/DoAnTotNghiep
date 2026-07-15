const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const { updateGroupStandings } = require('./standingsHelper');

// Cấu hình kết nối Database
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

/**
 * 1️⃣ CẬP NHẬT TỈ SỐ
 * PUT /api/match-events/:matchId/score
 * Body: { home_score, away_score, status }
 */
router.put('/match-events/:matchId/score', async (req, res) => {
    const { matchId } = req.params;
    const { home_score, away_score, status } = req.body;

    if (home_score === undefined || away_score === undefined) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp home_score và away_score.' });
    }

    // 🌟 RÀNG BUỘC: Tỉ số không được âm
    if (home_score < 0 || away_score < 0) {
        return res.json({ success: false, message: 'Tỉ số không được là số âm.' });
    }

    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        // 1. Lấy thông tin cơ bản của trận đấu
        const [matchRows] = await connection.execute(
            'SELECT home_team_id, away_team_id, group_id FROM matches WHERE id = ?',
            [matchId]
        );

        if (matchRows.length === 0) {
            await connection.rollback();
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        const { home_team_id, away_team_id, group_id } = matchRows[0];

        // 2. Xác định winner_team_id
        let winner_team_id = null;
        if (home_score > away_score) {
            winner_team_id = home_team_id;
        } else if (away_score > home_score) {
            winner_team_id = away_team_id;
        }

        // 3. Cập nhật bảng matches
        await connection.execute(
            `UPDATE matches 
             SET home_score = ?, away_score = ?, status = ?, updated_at = NOW() 
             WHERE id = ?`,
            [home_score, away_score, status || 'ongoing', matchId]
        );

        // 4. Kiểm tra xem đã có bản ghi trong match_results chưa
        const [resultRows] = await connection.execute(
            'SELECT id FROM match_results WHERE match_id = ?',
            [matchId]
        );

        if (resultRows.length > 0) {
            await connection.execute(
                `UPDATE match_results
                 SET home_score = ?, away_score = ?, home_final_score = ?, away_final_score = ?, winner_team_id = ?, updated_at = NOW()
                 WHERE match_id = ?`,
                [home_score, away_score, home_score, away_score, winner_team_id, matchId]
            );
        } else {
            await connection.execute(
                `INSERT INTO match_results (match_id, home_score, away_score, home_final_score, away_final_score, winner_team_id, result_type, status, created_at, updated_at)
                 VALUES (?, ?, ?, ?, ?, ?, 'full_time', 'official', NOW(), NOW())`,
                [matchId, home_score, away_score, home_score, away_score, winner_team_id]
            );
        }

        // 5. Nếu kết thúc trận đấu, cập nhật bảng xếp hạng
        if (status === 'finished' && group_id) {
            await updateGroupStandings(connection, group_id);
        }

        await connection.commit();
        res.json({
            success: true, 
            message: 'Cập nhật tỉ số và bảng xếp hạng thành công.',
            data: { matchId, home_score, away_score, status, winner_team_id }
        });
    } catch (error) {
        await connection.rollback();
        res.status(500).json({ success: false, message: error.message });
    } finally {
        connection.release();
    }
});

/**
 * 2️⃣ THAY CẦU THỦ
 * POST /api/match-events/:matchId/substitution
 * Body: { team_id, jersey_in, jersey_out, minute, period }
 */
router.post('/match-events/:matchId/substitution', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_in_id, player_out_id, jersey_in, jersey_out, minute, period } = req.body;

    if (!team_id || (!player_in_id && !jersey_in) || (!player_out_id && !jersey_out) || !minute || !period) {
        return res.status(400).json({ 
            success: false, 
            message: 'Vui lòng cung cấp team_id, thông tin cầu thủ vào/ra, phút và hiệp.'
        });
    }

    try {
        let finalInId = player_in_id;
        let finalOutId = player_out_id;

        // Tìm ID cầu thủ vào theo số áo
        if (!finalInId && jersey_in) {
            const [rows] = await pool.execute(
                'SELECT player_id FROM team_players WHERE team_id = ? AND jersey_number = ? AND approval_status = "approved"',
                [team_id, jersey_in]
            );
            if (rows.length > 0) finalInId = rows[0].player_id;
        }

        // Tìm ID cầu thủ ra theo số áo
        if (!finalOutId && jersey_out) {
            const [rows] = await pool.execute(
                'SELECT player_id FROM team_players WHERE team_id = ? AND jersey_number = ? AND approval_status = "approved"',
                [team_id, jersey_out]
            );
            if (rows.length > 0) finalOutId = rows[0].player_id;
        }

        if (!finalInId || !finalOutId) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy cầu thủ vào hoặc ra với số áo đã cung cấp.' });
        }

        // Kiểm tra trận đấu có tồn tại không
        const [match] = await pool.execute('SELECT id FROM matches WHERE id = ?', [matchId]);
        if (match.length === 0) return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });

        // Ghi nhận cầu thủ rời sân
        await pool.execute(
            `INSERT INTO match_events (match_id, player_id, team_id, type, minute, period, created_at)
             VALUES (?, ?, ?, 'substitution_out', ?, ?, NOW())`,
            [matchId, finalOutId, team_id, minute, period]
        );

        // Ghi nhận cầu thủ vào sân
        await pool.execute(
            `INSERT INTO match_events (match_id, player_id, team_id, type, minute, period, sub_out_player_id, created_at)
             VALUES (?, ?, ?, 'substitution_in', ?, ?, ?, NOW())`,
            [matchId, finalInId, team_id, minute, period, finalOutId]
        );

        res.status(201).json({ success: true, message: 'Ghi nhận thay cầu thủ thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

/**
 * 3️⃣ GHI NHẬN THẺ VÀNG
 * POST /api/match-events/:matchId/yellow-card
 * Body: { team_id, player_id, jersey_number, minute, period, note }
 */
router.post('/match-events/:matchId/yellow-card', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_id, jersey_number, minute, period, note } = req.body;

    if (!team_id || (!player_id && !jersey_number) || !minute || !period) {
        return res.status(400).json({ 
            success: false, 
            message: 'Vui lòng cung cấp team_id, số áo (hoặc player_id), phút và hiệp.'
        });
    }

    try {
        // 1. Tìm player_id theo số áo nếu không được gửi player_id trực tiếp
        let targetPlayerId = player_id;
        if (!targetPlayerId && jersey_number) {
            const [playerRows] = await pool.execute(
                'SELECT player_id FROM team_players WHERE team_id = ? AND jersey_number = ? AND approval_status = "approved"',
                [team_id, jersey_number]
            );
            if (playerRows.length === 0) {
                return res.status(404).json({ success: false, message: `Không tìm thấy cầu thủ số áo ${jersey_number} trong đội này.` });
            }
            targetPlayerId = playerRows[0].player_id;
        }

        // 2. Kiểm tra trận đấu
        const [match] = await pool.execute('SELECT id FROM matches WHERE id = ?', [matchId]);
        if (match.length === 0) return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });

        // 3. Kiểm tra xem cầu thủ đã có thẻ vàng nào trong trận này chưa
        const [existingYellows] = await pool.execute(
            'SELECT id FROM match_events WHERE match_id = ? AND player_id = ? AND type = "yellow_card"',
            [matchId, targetPlayerId]
        );

        if (existingYellows.length >= 1) {
            // Đây là thẻ vàng thứ 2 -> Tự động chuyển thành thẻ đỏ (second_yellow)
            await pool.execute(
                `INSERT INTO match_events
                 (match_id, player_id, team_id, type, card_color, minute, period, note, created_at)
                 VALUES (?, ?, ?, 'second_yellow', 'red', ?, ?, ?, NOW())`,
                [matchId, targetPlayerId, team_id, minute, period, note || 'Thẻ vàng thứ 2 -> Thẻ đỏ']
            );
            return res.status(201).json({ success: true, message: 'Cầu thủ nhận thẻ vàng thứ 2 và bị truất quyền thi đấu.' });
        }

        // 4. Ghi nhận thẻ vàng đầu tiên
        await pool.execute(
            `INSERT INTO match_events 
             (match_id, player_id, team_id, type, card_color, minute, period, note, created_at) 
             VALUES (?, ?, ?, 'yellow_card', 'yellow', ?, ?, ?, NOW())`,
            [matchId, targetPlayerId, team_id, minute, period, note || null]
        );

        res.status(201).json({ success: true, message: 'Ghi nhận thẻ vàng thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

/**
 * 4️⃣ GHI NHẬN THẺ ĐỎ
 * POST /api/match-events/:matchId/red-card
 * Body: { team_id, player_id, jersey_number, minute, period, note }
 */
router.post('/match-events/:matchId/red-card', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_id, jersey_number, minute, period, note } = req.body;

    if (!team_id || (!player_id && !jersey_number) || !minute || !period) {
        return res.status(400).json({ 
            success: false, 
            message: 'Vui lòng cung cấp team_id, số áo (hoặc player_id), phút và hiệp.'
        });
    }

    try {
        // 1. Tìm player_id theo số áo
        let targetPlayerId = player_id;
        if (!targetPlayerId && jersey_number) {
            const [playerRows] = await pool.execute(
                'SELECT player_id FROM team_players WHERE team_id = ? AND jersey_number = ? AND approval_status = "approved"',
                [team_id, jersey_number]
            );
            if (playerRows.length === 0) {
                return res.status(404).json({ success: false, message: `Không tìm thấy cầu thủ số áo ${jersey_number} trong đội này.` });
            }
            targetPlayerId = playerRows[0].player_id;
        }

        // 2. Ghi nhận thẻ đỏ
        const [result] = await pool.execute(
            `INSERT INTO match_events 
             (match_id, player_id, team_id, type, card_color, minute, period, note, created_at) 
             VALUES (?, ?, ?, 'red_card', 'red', ?, ?, ?, NOW())`,
            [matchId, targetPlayerId, team_id, minute, period, note || null]
        );

        res.status(201).json({ success: true, message: 'Ghi nhận thẻ đỏ thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

/**
 * 4.5️⃣ GHI NHẬN BÀN THẮNG
 * POST /api/match-events/:matchId/goal
 * Body: { team_id, player_id, jersey_number, minute, period, note }
 */
router.post('/match-events/:matchId/goal', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_id, jersey_number, minute, period, note } = req.body;

    if (!team_id || (!player_id && !jersey_number) || !minute || !period) {
        return res.status(400).json({
            success: false,
            message: 'Vui lòng cung cấp team_id, số áo (hoặc player_id), phút và hiệp.'
        });
    }

    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        // 1. Tìm player_id theo số áo
        let targetPlayerId = player_id;
        if (!targetPlayerId && jersey_number) {
            const [playerRows] = await connection.execute(
                'SELECT player_id FROM team_players WHERE team_id = ? AND jersey_number = ?',
                [team_id, jersey_number]
            );
            if (playerRows.length > 0) {
                targetPlayerId = playerRows[0].player_id;
            }
        }

        // 2. Ghi nhận sự kiện bàn thắng
        await connection.execute(
            `INSERT INTO match_events
             (match_id, player_id, team_id, type, minute, period, note, created_at)
             VALUES (?, ?, ?, 'goal', ?, ?, ?, NOW())`,
            [matchId, targetPlayerId || null, team_id, minute, period, note || null]
        );

        // 3. Tự động cập nhật tỉ số trong bảng matches
        const [matchRows] = await connection.execute(
            'SELECT home_team_id, away_team_id, home_score, away_score, status FROM matches WHERE id = ?',
            [matchId]
        );

        if (matchRows.length > 0) {
            let home_score = Number(matchRows[0].home_score) || 0;
            let away_score = Number(matchRows[0].away_score) || 0;
            let currentStatus = matchRows[0].status;
            const home_team_id = matchRows[0].home_team_id;
            const away_team_id = matchRows[0].away_team_id;

            if (Number(team_id) === Number(home_team_id)) {
                home_score += 1;
            } else if (Number(team_id) === Number(away_team_id)) {
                away_score += 1;
            }

            // Tự động chuyển sang ongoing nếu đang scheduled
            let nextStatus = currentStatus;
            if (currentStatus === 'scheduled') nextStatus = 'ongoing';

            await connection.execute(
                'UPDATE matches SET home_score = ?, away_score = ?, status = ?, updated_at = NOW() WHERE id = ?',
                [home_score, away_score, nextStatus, matchId]
            );

            // 3.5. Cập nhật hoặc Thêm mới vào bảng match_results
            const [resultRows] = await connection.execute(
                'SELECT id FROM match_results WHERE match_id = ?',
                [matchId]
            );

            // Xác định winner_team_id sơ bộ
            let winnerId = null;
            if (home_score > away_score) winnerId = home_team_id;
            else if (away_score > home_score) winnerId = away_team_id;

            if (resultRows.length > 0) {
                await connection.execute(
                    `UPDATE match_results
                     SET home_score = ?, away_score = ?, home_final_score = ?, away_final_score = ?, winner_team_id = ?, updated_at = NOW()
                     WHERE match_id = ?`,
                    [home_score, away_score, home_score, away_score, winnerId, matchId]
                );
            } else {
                await connection.execute(
                    `INSERT INTO match_results (match_id, winner_team_id, home_score, away_score, home_final_score, away_final_score, result_type, status, created_at, updated_at)
                     VALUES (?, ?, ?, ?, ?, ?, 'full_time', 'official', NOW(), NOW())`,
                    [matchId, winnerId, home_score, away_score, home_score, away_score]
                );
            }
        }

        

        await connection.commit();
        res.status(201).json({ success: true, message: 'Ghi nhận bàn thắng và cập nhật tỉ số thành công.' });
    } catch (error) {
        await connection.rollback();
        res.status(500).json({ success: false, message: error.message });
    } finally {
        connection.release();
    }
});

/**
 * 5️⃣ LẤY DANH SÁCH SỰ KIỆN TRONG TRẬN
 * GET /api/match-events/:matchId
 */
router.get('/match-events/:matchId', async (req, res) => {
    const { matchId } = req.params;

    try {
        const [events] = await pool.execute(
            `SELECT me.id, me.match_id, me.player_id, me.team_id, me.type, me.minute, 
                    me.period, me.note, me.card_color, me.sub_out_player_id,
                    u.name AS player_name, t.name AS team_name,
                    u_out.name AS sub_out_player_name,
                    me.created_at
             FROM match_events me
             LEFT JOIN players p ON me.player_id = p.id
             LEFT JOIN users u ON p.user_id = u.id
             LEFT JOIN teams t ON me.team_id = t.id
             LEFT JOIN players p_out ON me.sub_out_player_id = p_out.id
             LEFT JOIN users u_out ON p_out.user_id = u_out.id
             WHERE me.match_id = ?
             ORDER BY me.minute ASC, me.created_at ASC`,
            [matchId]
        );

        res.json({ 
            success: true, 
            data: events 
        });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

/**
 * 6️⃣ XÓA SỰ KIỆN
 * DELETE /api/match-events/:eventId
 */
router.delete('/match-events/:eventId', async (req, res) => {
    const { eventId } = req.params;

    try {
        const [result] = await pool.execute(
            'DELETE FROM match_events WHERE id = ?',
            [eventId]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy sự kiện.' });
        }

        res.json({ 
            success: true, 
            message: 'Xóa sự kiện thành công.',
            eventId
        });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
