const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

// Cấu hình kết nối Database của bạn
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

// Lấy danh sách đội bóng hoặc tra cứu theo tên/HLV/trạng thái
router.get('/teams', async (req, res) => {
    try {
        const { name, coach_name, is_active } = req.query;
        console.log('DEBUG: teamController /teams route hit');
        let query = `
            SELECT id, name, coach_name, logo, description, is_active,
                   created_at, updated_at
            FROM teams
            WHERE deleted_at IS NULL
        `;
        const params = [];

        if (name) {
            query += ' AND name LIKE ?';
            params.push(`%${name}%`);
        }

        if (coach_name) {
            query += ' AND coach_name LIKE ?';
            params.push(`%${coach_name}%`);
        }

        if (is_active !== undefined) {
            query += ' AND is_active = ?';
            params.push(Number(is_active));
        }

        query += ' ORDER BY created_at DESC';

        const [rows] = await pool.execute(query, params);
        res.json({ success: true, data: rows });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Xem chi tiết đội bóng theo id, kèm leader và cầu thủ
router.get('/teams/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const [teams] = await pool.execute(
            `SELECT id, name, coach_name, logo, description, is_active, created_at, updated_at, deleted_at, user_id
             FROM teams
             WHERE id = ? AND deleted_at IS NULL`,
            [id]
        );

        if (teams.length === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng.' });
        }

        const team = teams[0];

        const [leaders] = await pool.execute(
            `SELECT tl.id, tl.user_id, u.name, u.email, u.phone, tl.created_at
             FROM team_leaders tl
             JOIN users u ON tl.user_id = u.id
             WHERE tl.team_id = ? AND tl.deleted_at IS NULL`,
            [id]
        );

        const [players] = await pool.execute(
            `SELECT tp.id, tp.jersey_number, tp.position, tp.role, tp.status, tp.approval_status,
                    pl.id AS player_id, pl.user_id AS player_user_id,
                    u.name AS player_name, u.email AS player_email, u.phone AS player_phone
             FROM team_players tp
             JOIN players pl ON tp.player_id = pl.id
             LEFT JOIN users u ON pl.user_id = u.id
             WHERE tp.team_id = ? AND tp.deleted_at IS NULL`,
            [id]
        );

        res.json({ success: true, data: { team, leaders, players } });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Duyệt đăng ký đội bóng
router.patch('/teams/:id/approve', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE teams
             SET is_active = 1, updated_at = NOW()
             WHERE id = ? AND deleted_at IS NULL`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng hoặc đội đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã duyệt đăng ký đội bóng.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Từ chối đăng ký đội bóng
router.patch('/teams/:id/reject', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE teams
             SET deleted_at = NOW(), updated_at = NOW()
             WHERE id = ? AND deleted_at IS NULL`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng hoặc đội đã bị xóa.' });
        }

        res.json({ success: true, message: 'Đã từ chối đăng ký đội bóng.' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Tạo đội bóng mới (Admin)
router.post('/teams', async (req, res) => {
    const { name, coach_name, description, user_id } = req.body;
    try {
        const [result] = await pool.execute(
            `INSERT INTO teams (name, coach_name, description, user_id, is_active, created_at)
             VALUES (?, ?, ?, ?, 1, NOW())`,
            [name, coach_name || 'Chưa cập nhật', description || null, user_id || null]
        );
        res.json({ success: true, message: 'Tạo đội bóng thành công!', teamId: result.insertId });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Chỉnh sửa thông tin đội bóng
router.put('/teams/:id', async (req, res) => {
    const { id } = req.params;
    const { name, coach_name, description } = req.body;
    try {
        const [result] = await pool.execute(
            `UPDATE teams
             SET name = ?, coach_name = ?, description = ?, updated_at = NOW()
             WHERE id = ? AND deleted_at IS NULL`,
            [name, coach_name, description, id]
        );
        if (result.affectedRows === 0) {
            return res.status(404).json({ success: false, message: 'Không tìm thấy đội bóng.' });
        }
        res.json({ success: true, message: 'Cập nhật thông tin đội bóng thành công!' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

// Thêm cầu thủ vào đội bằng email (Admin)
router.post('/teams/:teamId/players/add', async (req, res) => {
    const { teamId } = req.params;
    const { email, jersey_number, position } = req.body;
    try {
        // Tìm user/player
        const [users] = await pool.execute(
            `SELECT u.id as user_id, p.id as player_id
             FROM users u
             LEFT JOIN players p ON u.id = p.user_id
             WHERE u.email = ?`,
            [email]
        );

        if (users.length === 0) {
            return res.status(404).json({ success: false, message: 'Email không tồn tại.' });
        }

        let { user_id, player_id } = users[0];
        if (!player_id) {
            const [pResult] = await pool.execute("INSERT INTO players (user_id, is_active, created_at) VALUES (?, 1, NOW())", [user_id]);
            player_id = pResult.insertId;
        }

        // 🌟 RÀNG BUỘC: Kiểm tra xem cầu thủ đã có trong đội nào khác chưa
        const [otherTeam] = await pool.execute(
            `SELECT t.name
             FROM team_players tp
             JOIN teams t ON tp.team_id = t.id
             WHERE tp.player_id = ? AND tp.is_active = 1 AND tp.deleted_at IS NULL`,
            [player_id]
        );

        if (otherTeam.length > 0) {
            return res.json({
                success: false,
                message: `Cầu thủ này đã thuộc đội '${otherTeam[0].name}'. Vui lòng xóa khỏi đội cũ trước.`
            });
        }

        await pool.execute(
            `INSERT INTO team_players (team_id, player_id, jersey_number, position, role, approval_status, is_active, created_at)
             VALUES (?, ?, ?, ?, 'player', 'approved', 1, NOW())`,
            [teamId, player_id, jersey_number || 0, position || 'midfielder']
        );

        res.json({ success: true, message: 'Đã thêm cầu thủ vào đội thành công!' });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;

