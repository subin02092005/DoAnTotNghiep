const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
   
});

// API lấy "Đội bóng của chính tôi" dựa trên userId
router.get('/my_team', async (req, res) => {
    const userId = req.query.userId;
    if (!userId) {
        return res.status(400).json({ status: "error", message: "Thiếu userId" });
    }

    try {
        // 1. Lấy thông tin đội bóng và quyền của người dùng (coach/captain/player)
        // Lưu ý: Dùng LEFT JOIN để kiểm tra xem user có nằm trong team_leaders (coach) hay không
        const query = `
           SELECT 
    tp.team_id, 
    t.name AS team_name,
    t.coach_name,
    CASE 
        WHEN tl.user_id IS NOT NULL THEN 'coach' 
        WHEN tp.role = 'captain' THEN 'captain'  
        ELSE 'player'                            
    END AS currentUserRole
FROM team_players tp
JOIN players p ON tp.player_id = p.id
JOIN teams t ON tp.team_id = t.id
LEFT JOIN team_leaders tl ON tp.team_id = tl.team_id AND tl.user_id = p.user_id
WHERE p.user_id = ? AND tp.is_active = 1
LIMIT 1
        `;
        
        const [rows] = await pool.query(query, [userId, userId]);

        // 2. Nếu không tìm thấy đội
        if (rows.length === 0) {
            return res.status(200).json({ 
                status: "success", 
                hasTeam: false, 
                message: "Người dùng chưa thuộc đội bóng nào" 
            });
        }

        const teamId = rows[0].team_id;

        // 3. Lấy danh sách toàn bộ cầu thủ trong đội đó
        const [players] = await pool.query(
            `SELECT u.name, tp.jersey_number, tp.position, tp.role
             FROM team_players tp
             JOIN players p ON tp.player_id = p.id
             JOIN users u ON p.user_id = u.id
             WHERE tp.team_id = ? AND tp.is_active = 1`,
            [teamId]
        );

        // 4. Trả về dữ liệu đầy đủ
        return res.status(200).json({
            status: "success",
            hasTeam: true,
            data: {
                teamId: teamId,
                teamName: rows[0].team_name,
                coachName: rows[0].coach_name,
                currentUserRole: rows[0].currentUserRole, // Quyền để phân biệt màn hình
                players: players
            }
        });

    } catch (error) {
        console.error("Lỗi API my_team:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server nội bộ" });
    }
});
module.exports = router;