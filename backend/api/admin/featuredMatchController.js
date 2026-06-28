const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình kết nối Database
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

/**
 * @route   PATCH /api/admin/matches/:id/featured
 * @desc    Admin thiết lập hoặc hủy trận đấu nổi bật
 * @access  Private (Admin)
 * @param   {number} id - ID của trận đấu
 * @body    {boolean} is_featured - Trạng thái nổi bật (true/false)
 */
router.patch('/admin/matches/:id/featured', async (req, res) => {
    const matchId = parseInt(req.params.id);
    const { is_featured } = req.body;

    // Kiểm tra tính hợp lệ của tham số
    if (isNaN(matchId)) {
        return res.status(400).json({ 
            success: false, 
            message: 'ID trận đấu phải là một số hợp lệ.' 
        });
    }

    if (typeof is_featured !== 'boolean') {
        return res.status(400).json({ 
            success: false, 
            message: 'Trường is_featured phải là kiểu boolean (true/false).' 
        });
    }

    try {
        // Kiểm tra trận đấu có tồn tại không
        const [matches] = await pool.execute(
            `SELECT id, home_team_id, away_team_id, status, scheduled_at, is_featured
             FROM matches 
             WHERE id = ? AND deleted_at IS NULL`,
            [matchId]
        );

        if (matches.length === 0) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy trận đấu.' 
            });
        }

        // Cập nhật trạng thái nổi bật
        await pool.execute(
            `UPDATE matches 
             SET is_featured = ?, updated_at = NOW() 
             WHERE id = ? AND deleted_at IS NULL`,
            [is_featured ? 1 : 0, matchId]
        );

        // Lấy thông tin trận đấu đã cập nhật cùng với thông tin đội
        const [updatedMatches] = await pool.execute(
            `SELECT m.id, m.phase_id, m.group_id, 
                    m.home_team_id, ht.name AS home_team_name, ht.logo AS home_team_logo,
                    m.away_team_id, at.name AS away_team_name, at.logo AS away_team_logo,
                    m.scheduled_at, m.played_at, m.home_score, m.away_score, 
                    m.status, m.round, m.leg, m.venue_id, m.referee, m.season_id, 
                    m.is_published, m.is_featured, m.created_at, m.updated_at
             FROM matches m
             LEFT JOIN teams ht ON m.home_team_id = ht.id
             LEFT JOIN teams at ON m.away_team_id = at.id
             WHERE m.id = ? AND m.deleted_at IS NULL`,
            [matchId]
        );

        return res.status(200).json({
            success: true,
            message: is_featured 
                ? 'Đã thêm trận đấu vào danh sách nổi bật.' 
                : 'Đã xóa trận đấu khỏi danh sách nổi bật.',
            data: updatedMatches[0]
        });

    } catch (error) {
        console.error('Lỗi cập nhật trận đấu nổi bật:', error);
        return res.status(500).json({ 
            success: false, 
            message: 'Lỗi hệ thống khi cập nhật trận đấu nổi bật.',
            error: error.message 
        });
    }
});

/**
 * @route   GET /api/admin/matches/featured
 * @desc    Lấy danh sách các trận đấu nổi bật
 * @access  Public
 * @query   {number} limit - Số lượng trận đấu (default: 10)
 * @query   {number} offset - Vị trí bắt đầu (default: 0)
 */
router.get('/admin/matches/featured', async (req, res) => {
    try {
        const limit = Math.max(1, parseInt(req.query.limit, 10) || 10);
        const offset = Math.max(0, parseInt(req.query.offset, 10) || 0);

        const [featuredMatches] = await pool.query(
            `SELECT m.id, m.phase_id, m.group_id, 
                    m.home_team_id, ht.name AS home_team_name, ht.logo AS home_team_logo,
                    m.away_team_id, at.name AS away_team_name, at.logo AS away_team_logo,
                    m.scheduled_at, m.played_at, m.home_score, m.away_score, 
                    m.status, m.round, m.leg, m.venue_id, m.referee, m.season_id, 
                    m.is_published, m.is_featured, m.created_at, m.updated_at
             FROM matches m
             LEFT JOIN teams ht ON m.home_team_id = ht.id
             LEFT JOIN teams at ON m.away_team_id = at.id
             WHERE m.is_featured = 1 AND m.deleted_at IS NULL
             ORDER BY m.updated_at DESC
             LIMIT ${limit} OFFSET ${offset}`
        );

        // Lấy tổng số trận đấu nổi bật
        const [countResult] = await pool.execute(
            `SELECT COUNT(*) as total FROM matches WHERE is_featured = 1 AND deleted_at IS NULL`
        );

        res.json({
            success: true,
            data: featuredMatches,
            pagination: {
                total: countResult[0].total,
                limit: limit,
                offset: offset,
                hasMore: (offset + limit) < countResult[0].total
            }
        });

    } catch (error) {
        console.error('Lỗi lấy danh sách nổi bật:', error);
        return res.status(500).json({ 
            success: false, 
            message: 'Lỗi hệ thống khi lấy danh sách nổi bật.',
            error: error.message 
        });
    }
});

module.exports = router;
