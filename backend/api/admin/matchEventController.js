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
 * Body: { team_id, player_in_id, player_out_id, minute, period }
 */
router.post('/match-events/:matchId/substitution', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_in_id, player_out_id, minute, period } = req.body;

    if (!team_id || !player_in_id || !player_out_id || !minute || !period) {
        return res.status(400).json({ 
            success: false, 
            message: 'Vui lòng cung cấp team_id, player_in_id, player_out_id, minute và period.' 
        });
    }

    try {
        // Kiểm tra trận đấu có tồn tại không
        const [match] = await pool.execute(
            'SELECT id FROM matches WHERE id = ?',
            [matchId]
        );

        if (match.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        // Ghi nhận cầu thủ rời sân
        const [subOut] = await pool.execute(
            `INSERT INTO match_events 
             (match_id, player_id, team_id, type, minute, period, created_at) 
             VALUES (?, ?, ?, 'substitution_out', ?, ?, NOW())`,
            [matchId, player_out_id, team_id, minute, period]
        );

        // Ghi nhận cầu thủ vào sân
        const [subIn] = await pool.execute(
            `INSERT INTO match_events 
             (match_id, player_id, team_id, type, minute, period, sub_out_player_id, created_at) 
             VALUES (?, ?, ?, 'substitution_in', ?, ?, ?, NOW())`,
            [matchId, player_in_id, team_id, minute, period, player_out_id]
        );

        res.status(201).json({ 
            success: true, 
            message: 'Ghi nhận thay cầu thủ thành công.',
            data: {
                matchId,
                team_id,
                player_out_id,
                player_in_id,
                minute,
                period,
                eventIds: [subOut.insertId, subIn.insertId]
            }
        });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

/**
 * 3️⃣ GHI NHẬN THẺ VÀNG
 * POST /api/match-events/:matchId/yellow-card
 * Body: { team_id, player_id, minute, period, note }
 */
router.post('/match-events/:matchId/yellow-card', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_id, minute, period, note } = req.body;

    if (!team_id || !player_id || !minute || !period) {
        return res.status(400).json({ 
            success: false, 
            message: 'Vui lòng cung cấp team_id, player_id, minute và period.' 
        });
    }

    try {
        // Kiểm tra trận đấu có tồn tại không
        const [match] = await pool.execute(
            'SELECT id FROM matches WHERE id = ?',
            [matchId]
        );

        if (match.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        // Ghi nhận thẻ vàng
        const [result] = await pool.execute(
            `INSERT INTO match_events 
             (match_id, player_id, team_id, type, card_color, minute, period, note, created_at) 
             VALUES (?, ?, ?, 'yellow_card', 'yellow', ?, ?, ?, NOW())`,
            [matchId, player_id, team_id, minute, period, note || null]
        );

        res.status(201).json({ 
            success: true, 
            message: 'Ghi nhận thẻ vàng thành công.',
            data: {
                eventId: result.insertId,
                matchId,
                team_id,
                player_id,
                card_color: 'yellow',
                minute,
                period,
                note
            }
        });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

/**
 * 4️⃣ GHI NHẬN THẺ ĐỎ
 * POST /api/match-events/:matchId/red-card
 * Body: { team_id, player_id, minute, period, note }
 */
router.post('/match-events/:matchId/red-card', async (req, res) => {
    const { matchId } = req.params;
    const { team_id, player_id, minute, period, note } = req.body;

    if (!team_id || !player_id || !minute || !period) {
        return res.status(400).json({ 
            success: false, 
            message: 'Vui lòng cung cấp team_id, player_id, minute và period.' 
        });
    }

    try {
        // Kiểm tra trận đấu có tồn tại không
        const [match] = await pool.execute(
            'SELECT id FROM matches WHERE id = ?',
            [matchId]
        );

        if (match.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        // Ghi nhận thẻ đỏ
        const [result] = await pool.execute(
            `INSERT INTO match_events 
             (match_id, player_id, team_id, type, card_color, minute, period, note, created_at) 
             VALUES (?, ?, ?, 'red_card', 'red', ?, ?, ?, NOW())`,
            [matchId, player_id, team_id, minute, period, note || null]
        );

        res.status(201).json({ 
            success: true, 
            message: 'Ghi nhận thẻ đỏ thành công.',
            data: {
                eventId: result.insertId,
                matchId,
                team_id,
                player_id,
                card_color: 'red',
                minute,
                period,
                note
            }
        });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
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
                    p.name AS player_name, t.name AS team_name, 
                    p_out.name AS sub_out_player_name,
                    me.created_at
             FROM match_events me
             LEFT JOIN players p ON me.player_id = p.id
             LEFT JOIN teams t ON me.team_id = t.id
             LEFT JOIN players p_out ON me.sub_out_player_id = p_out.id
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
