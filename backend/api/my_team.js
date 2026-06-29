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

// Tìm đội trưởng từ danh sách players
const captain = players.find(p => p.role === 'captain');

return res.status(200).json({
    status: "success",
    hasTeam: true,
    data: {
        teamId: teamId,
        teamName: rows[0].team_name,
        coachName: rows[0].coach_name,
        captainName: captain ? captain.name : "Chưa xác định", // 🌟 Lấy tên đội trưởng
        currentUserRole: rows[0].currentUserRole,
        players: players
    }
});

    } catch (error) {
        console.error("Lỗi API my_team:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server nội bộ" });
    }
});


///////////////////////////////////
router.post('/update_player', async (req, res) => {
    const { team_id, player_id, new_name, jersey_number, position } = req.body;

    try {
        // 1. Cập nhật tên trong bảng users (liên kết qua player_id -> p.user_id -> u.id)
        const updateNameQuery = `
            UPDATE users u
            JOIN players p ON u.id = p.user_id
            SET u.name = ?
            WHERE p.id = ?
        `;
        await pool.query(updateNameQuery, [new_name, player_id]);

        // 2. Cập nhật số áo và vị trí trong bảng team_players
        const updateInfoQuery = `
            UPDATE team_players 
            SET jersey_number = ?, position = ? 
            WHERE team_id = ? AND player_id = ?
        `;
        await pool.query(updateInfoQuery, [jersey_number, position, team_id, player_id]);

        res.status(200).json({ status: "success", message: "Đã cập nhật thông tin cầu thủ!" });
    } catch (error) {
        console.error("Lỗi cập nhật:", error);
        res.status(500).json({ status: "error", message: "Không thể cập nhật cầu thủ" });
    }
});

///////////////////////////////////
router.post('/remove_player', async (req, res) => {
    const { team_id, player_id } = req.body;

    try {
        const query = `
            UPDATE team_players 
            SET is_active = 0 
            WHERE team_id = ? AND player_id = ?
        `;
        await pool.query(query, [team_id, player_id]);

        res.status(200).json({ status: "success", message: "Cầu thủ đã rời đội" });
    } catch (error) {
        console.error("Lỗi xóa cầu thủ:", error);
        res.status(500).json({ status: "error", message: "Lỗi hệ thống" });
    }
});

/////////////////////////////////
router.post('/add_player_by_email', async (req, res) => {
    const { team_id, email, jersey_number, position } = req.body;

    try {
        // 1. Tìm user_id và lấy TÊN dựa trên email
        const [users] = await pool.query("SELECT id, name FROM users WHERE email = ?", [email]);
        if (users.length === 0) {
            return res.status(404).json({ status: "error", message: "Email không tồn tại" });
        }
        const user_id = users[0].id;
        const user_name = users[0].name; // 🌟 Lấy tên để trả về

        // 2. Tìm hoặc tạo player_id
        let [players] = await pool.query("SELECT id FROM players WHERE user_id = ?", [user_id]);
        let player_id;
        if (players.length === 0) {
            const [result] = await pool.query("INSERT INTO players (user_id) VALUES (?)", [user_id]);
            player_id = result.insertId;
        } else {
            player_id = players[0].id;
        }

        // 3. Kiểm tra đã có trong team chưa
        const [existing] = await pool.query(
            "SELECT * FROM team_players WHERE team_id = ? AND player_id = ?", 
            [team_id, player_id]
        );

        if (existing.length > 0) {
            return res.status(400).json({ status: "error", message: "Cầu thủ đã có trong đội rồi!" });
        }

        // 4. Thêm vào đội
        await pool.query(
            "INSERT INTO team_players (team_id, player_id, jersey_number, position, role, is_active) VALUES (?, ?, ?, ?, 'player', 1)",
            [team_id, player_id, jersey_number, position]
        );

        // 5. TRẢ VỀ THÔNG TIN CẦU THỦ VỪA THÊM
        res.status(200).json({ 
            status: "success", 
            message: "Thêm thành công!",
            data: {
                name: user_name,
                jersey_number: jersey_number,
                position: position,
                role: 'player'
            }
        });

    } catch (error) {
        console.error("Lỗi API add_player_by_email:", error);
        res.status(500).json({ status: "error", message: "Lỗi hệ thống" });
    }
});
module.exports = router;