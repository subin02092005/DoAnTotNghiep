const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');
const { createAndSendNotification } = require('../notification/notifications');
const { updateGroupStandings } = require('./standingsHelper');
// Cấu hình kết nối Database
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

// Lấy danh sách lịch thi đấu
router.get('/matchesadmin', async (req, res) => {
    try {
        const { teamId, status, phaseId, seasonId, dateFrom, dateTo } = req.query;
        let query = `
            SELECT m.id, m.phase_id, m.group_id, m.home_team_id, ht.name AS home_team_name,
                   m.away_team_id, at.name AS away_team_name, m.scheduled_at, m.played_at,
                   m.home_score, m.away_score, m.status, m.round, m.leg, m.venue_id,
                   m.referee, m.season_id, m.is_published, m.created_at, m.updated_at
            FROM matches m
            LEFT JOIN teams ht ON m.home_team_id = ht.id
            LEFT JOIN teams at ON m.away_team_id = at.id
            WHERE m.deleted_at IS NULL AND m.is_featured = 0
        `;
        const params = [];

        if (teamId) {
            query += ' AND (m.home_team_id = ? OR m.away_team_id = ?)';
            params.push(teamId, teamId);
        }

        if (status) {
            query += ' AND m.status = ?';
            params.push(status);
        }

        if (phaseId) {
            query += ' AND m.phase_id = ?';
            params.push(phaseId);
        }

        if (seasonId) {
            query += ' AND m.season_id = ?';
            params.push(seasonId);
        }

        if (dateFrom) {
            query += ' AND m.scheduled_at >= ?';
            params.push(dateFrom);
        }

        if (dateTo) {
            query += ' AND m.scheduled_at <= ?';
            params.push(dateTo);
        }

        query += ' ORDER BY m.scheduled_at ASC';

        const [rows] = await pool.execute(query, params);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Xem chi tiết một trận đấu
router.get('/matches/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const [matches] = await pool.execute(
            `SELECT m.id, m.phase_id, m.group_id, 
                    m.home_team_id AS teamAId, 
                    ht.name AS teamA,
                    m.away_team_id AS teamBId, 
                    at.name AS teamB, 
                    m.scheduled_at, m.played_at,
                    m.home_score AS scoreA, 
                    m.away_score AS scoreB, 
                    m.status, m.round, m.leg, m.venue_id,
                    m.referee, m.season_id, m.is_published, m.created_at, m.updated_at
             FROM matches m
             LEFT JOIN teams ht ON m.home_team_id = ht.id
             LEFT JOIN teams at ON m.away_team_id = at.id
             WHERE m.id = ? AND m.deleted_at IS NULL`,
            [id]
        );

        if (matches.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        // Trả về dữ liệu đã được đổi tên key chuẩn theo FullMatchDetail của Android
        res.json({ success: true, data: matches[0] });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Tạo lịch thi đấu mới
router.post('/matchesadd', async (req, res) => {
    const {
        phase_id,
        group_id,
        home_team_id,
        away_team_id,
        scheduled_at,
        venue_id,
        round,
        leg,
        referee,
        season_id,
        is_published
    } = req.body;

    if (!home_team_id || !away_team_id || !scheduled_at) {
        return res.status(400).json({ success: false, message: 'Vui lòng cung cấp home_team_id, away_team_id và scheduled_at.' });
    }

    if (home_team_id === away_team_id) {
        return res.status(400).json({ success: false, message: 'Đội nhà và đội khách không được giống nhau.' });
    }

    try {
        const [result] = await pool.execute(
            `INSERT INTO matches (
                phase_id, group_id, home_team_id, away_team_id, scheduled_at,
                venue_id, round, leg, referee, season_id, is_published, created_at, updated_at
             ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())`,
            [phase_id || null, group_id || null, home_team_id, away_team_id, scheduled_at,
                venue_id || null, round || null, leg || null, referee || null, season_id || null,
                is_published ? 1 : 0]
        );
// Gửi cho đội A
        const matchTitle = `Trận đấu giữa đội nhà và đội khách`; 

        // Gửi cho đội nhà (home_team_id)
        await createAndSendNotification(
            home_team_id, 
            "Lịch thi đấu mới", 
            `Bạn có trận đấu sắp tới vào lúc ${scheduled_at}`, 
            'match_schedule'
        );

        // Gửi cho đội khách (away_team_id)
        await createAndSendNotification(
            away_team_id, 
            "Lịch thi đấu mới", 
            `Bạn có trận đấu sắp tới vào lúc ${scheduled_at}`, 
            'match_schedule'
        );
        res.status(201).json({ success: true, message: 'Tạo lịch thi đấu thành công.', matchId: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Chỉnh sửa thông tin trận đấu
router.put('/matches/:id', async (req, res) => {
    const { id } = req.params;
    const {
        phase_id, group_id, home_team_id, away_team_id,
        scheduled_at, played_at, home_score, away_score,
        winner_team_id, status, round, leg, venue_id,
        referee, season_id, is_published
    } = req.body;

    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        const fields = [];
        const params = [];

        if (phase_id !== undefined) { fields.push('phase_id = ?'); params.push(phase_id); }
        if (group_id !== undefined) { fields.push('group_id = ?'); params.push(group_id); }
        if (home_team_id !== undefined) { fields.push('home_team_id = ?'); params.push(home_team_id); }
        if (away_team_id !== undefined) { fields.push('away_team_id = ?'); params.push(away_team_id); }
        if (scheduled_at !== undefined) { fields.push('scheduled_at = ?'); params.push(scheduled_at); }
        if (played_at !== undefined) { fields.push('played_at = ?'); params.push(played_at); }
        if (home_score !== undefined) { fields.push('home_score = ?'); params.push(home_score); }
        if (away_score !== undefined) { fields.push('away_score = ?'); params.push(away_score); }
        if (status !== undefined) { fields.push('status = ?'); params.push(status); }
        if (round !== undefined) { fields.push('round = ?'); params.push(round); }
        if (leg !== undefined) { fields.push('leg = ?'); params.push(leg); }
        if (venue_id !== undefined) { fields.push('venue_id = ?'); params.push(venue_id); }
        if (referee !== undefined) { fields.push('referee = ?'); params.push(referee); }
        if (season_id !== undefined) { fields.push('season_id = ?'); params.push(season_id); }
        if (is_published !== undefined) { fields.push('is_published = ?'); params.push(is_published ? 1 : 0); }

        if (fields.length > 0) {
            params.push(id);
            await connection.execute(
                `UPDATE matches SET ${fields.join(', ')}, updated_at = NOW() WHERE id = ?`,
                params
            );
        }

        // XỬ LÝ KẾT QUẢ VÀ BẢNG XẾP HẠNG KHI KẾT THÚC
        if (status === 'finished') {
            // Lấy dữ liệu đầy đủ sau khi đã update ở trên
            const [current] = await connection.execute(
                'SELECT home_team_id, away_team_id, home_score, away_score, group_id FROM matches WHERE id = ?',
                [id]
            );
            const m = current[0];
            const hScore = m.home_score || 0;
            const aScore = m.away_score || 0;
            const gId = m.group_id;

            // Xác định winnerId
            let finalWinnerId = winner_team_id;
            if (finalWinnerId === undefined) {
                if (hScore > aScore) finalWinnerId = m.home_team_id;
                else if (aScore > hScore) finalWinnerId = m.away_team_id;
                else finalWinnerId = null;
            }

            // Cập nhật/Chèn vào match_results
            const [results] = await connection.execute('SELECT id FROM match_results WHERE match_id = ?', [id]);
            if (results.length > 0) {
                await connection.execute(
                    `UPDATE match_results
                     SET home_score = ?, away_score = ?, home_final_score = ?, away_final_score = ?, winner_team_id = ?, updated_at = NOW()
                     WHERE match_id = ?`,
                    [hScore, aScore, hScore, aScore, finalWinnerId, id]
                );
            } else {
                await connection.execute(
                    `INSERT INTO match_results (match_id, winner_team_id, home_score, away_score, home_final_score, away_final_score, result_type, status)
                     VALUES (?, ?, ?, ?, ?, ?, 'full_time', 'official')`,
                    [id, finalWinnerId, hScore, aScore, hScore, aScore]
                );
            }

            // Cập nhật bảng xếp hạng nếu có group_id
            if (gId) {
                await updateGroupStandings(connection, gId);
            }
        }

        await connection.commit();
        res.json({ success: true, message: 'Cập nhật trận đấu và kết quả thành công.' });
    } catch (error) {
        if (connection) await connection.rollback();
        console.error("Error in PUT /matches/:id:", error);
        res.status(500).json({ success: false, message: error.message });
    } finally {
        if (connection) connection.release();
    }
});

// Hủy trận đấu
router.patch('/matches/:id/cancel', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE matches SET status = 'cancelled', updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu hoặc đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã hủy trận đấu.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Bắt đầu trận đấu
router.patch('/matches/:id/start', async (req, res) => {
    const { id } = req.params;
    try {
        const [matches] = await pool.execute(
            'SELECT status FROM matches WHERE id = ? AND deleted_at IS NULL',
            [id]
        );

        if (matches.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu hoặc đã bị xóa.' });
        }

        if (matches[0].status === 'ongoing') {
            return res.status(400).json({ success: false, message: 'Trận đấu đã bắt đầu.' });
        }

        await pool.execute(
            `UPDATE matches
             SET status = 'ongoing', played_at = COALESCE(played_at, NOW()), updated_at = NOW()
             WHERE id = ? AND deleted_at IS NULL`,
            [id]
        );

        res.json({ success: true, message: 'Đã chuyển trạng thái trận đấu sang ongoing.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Kết thúc trận đấu
router.patch('/matches/:id/finish', async (req, res) => {
    const { id } = req.params;
    const connection = await pool.getConnection(); 
    
    try {
        await connection.beginTransaction();

        // 1. Lấy TOÀN BỘ thông tin liên quan của trận đấu từ bảng matches
        const [matchRows] = await connection.execute(
            `SELECT home_team_id, away_team_id, home_score, away_score, season_id, phase_id, group_id 
             FROM matches WHERE id = ?`,
            [id]
        );

        if (matchRows.length === 0) {
            await connection.rollback();
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu.' });
        }

        const { home_team_id, away_team_id, home_score, away_score, season_id, phase_id, group_id } = matchRows[0];

        // Tự động xác định đội thắng
        let winner_team_id = null;
        if (home_score > away_score) {
            winner_team_id = home_team_id;
        } else if (away_score > home_score) {
            winner_team_id = away_team_id;
        }

        // 2. Cập nhật trạng thái trận đấu thành 'finished' trong bảng matches
        await connection.execute(
            `UPDATE matches SET status = 'finished', updated_at = NOW() WHERE id = ?`,
            [id]
        );

        // 3. Kiểm tra xem trận đấu này đã từng có bản ghi trong match_results chưa
        const [resultRows] = await connection.execute(
            `SELECT id FROM match_results WHERE match_id = ?`,
            [id]
        );

        if (resultRows.length > 0) {
            // NẾU ĐÃ CÓ: Cập nhật tỉ số và winner_team_id
            await connection.execute(
                `UPDATE match_results 
                 SET home_score = ?, away_score = ?, home_final_score = ?, away_final_score = ?, winner_team_id = ?, updated_at = NOW()
                 WHERE match_id = ?`,
                [home_score, away_score, home_score, away_score, winner_team_id, id]
            );
        } else {
            // NẾU CHƯA CÓ: INSERT mới kèm winner_team_id
           await connection.execute(
    `INSERT INTO match_results (
        match_id, 
        winner_team_id,
        home_score,
        away_score, 
        home_final_score, 
        away_final_score, 
        result_type, 
        status, 
        created_at, 
        updated_at
    ) VALUES (?, ?, ?, ?, ?, ?, 'full_time', 'official', NOW(), NOW())`,
    [
        id,             // match_id
        winner_team_id, // winner_team_id
        home_score,     // home_score
        away_score,     // away_score
        home_score,     // home_final_score
        away_score      // away_final_score
    ]
);
        }

        // 4. Tự động cập nhật bảng xếp hạng (Standings) nếu trận đấu thuộc một bảng đấu (groupId)
        if (group_id) {
            await updateGroupStandings(connection, group_id);
        }

        await connection.commit();
        res.json({ success: true, message: 'Trận đấu đã kết thúc và bảng xếp hạng đã được cập nhật!' });

    } catch (error) {
        await connection.rollback();
        // In lỗi chi tiết ra màn hình terminal để kiểm tra chính xác tên cột bị thiếu nếu vẫn lỗi
        console.error("❌ LỖI DATABASE TẠI FINISH-MATCH:", error.message);
        res.status(500).json({ success: false, message: error.message });
    } finally {
        connection.release();
    }
});

// Dời trận đấu
router.patch('/matches/:id/reschedule', async (req, res) => {
    const { id } = req.params;
    const { scheduled_at, venue_id } = req.body;

    if (!scheduled_at && !venue_id) {
        return res.status(400).json({ success: false, message: 'Vui lòng gửi scheduled_at hoặc venue_id để dời trận.' });
    }

    try {
        const fields = [];
        const params = [];

        if (scheduled_at !== undefined) {
            fields.push('scheduled_at = ?');
            params.push(scheduled_at);
            fields.push('status = ?');
            params.push('scheduled');
        }
        if (venue_id !== undefined) {
            fields.push('venue_id = ?');
            params.push(venue_id);
        }

        params.push(id);

        const [result] = await pool.execute(
            `UPDATE matches SET ${fields.join(', ')}, updated_at = NOW() WHERE id = ? AND deleted_at IS NULL`,
            params
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy trận đấu hoặc đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã dời trận đấu thành công.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;
