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
    `SELECT p.id,p.user_id,u.name, tp.jersey_number, tp.position, tp.role
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
if (!user.date_of_birth) {
    return res.status(422).json({ 
        status: "error", 
        message: "Cảnh báo: Cầu thủ này chưa có ngày sinh trong hồ sơ!",
        code: "MISSING_DOB" 
    });
}

    } catch (error) {
        console.error("Lỗi API my_team:", error);
        return res.status(500).json({ status: "error", message: "Lỗi Server nội bộ" });
    }
});


///////////////////////////////////
router.post('/update_player', async (req, res) => {
    // Chỉ lấy các thông tin cần thiết
    const { team_id, id, jersey_number, position } = req.body;

    try {
        // Chỉ cập nhật bảng team_players
        const [result] = await pool.query(`
            UPDATE team_players 
            SET jersey_number = ?, position = ? 
            WHERE team_id = ? AND player_id = ?
        `, [jersey_number, position, team_id, id]);

        // Kiểm tra xem có dòng nào được cập nhật không
        if (result.affectedRows === 0) {
            return res.status(404).json({ 
                status: "error", 
                message: "Không tìm thấy cầu thủ trong đội này!" 
            });
        }

        res.status(200).json({ status: "success", message: "Đã cập nhật vị trí và số áo!" });
    } catch (error) {
        console.error("Lỗi cập nhật:", error);
        res.status(500).json({ status: "error", message: error.message });
    }
});

///////////////////////////////////
router.post('/remove_player', async (req, res) => {
    // 🌟 Sửa ở đây: Lấy currentUserId từ req.body
    const { team_id, player_id, currentUserId } = req.body;

    // Kiểm tra nếu thiếu dữ liệu
    if (!team_id || !player_id || !currentUserId) {
        return res.status(400).json({ status: "error", message: "Thiếu dữ liệu bắt buộc" });
    }

    try {
        // 🌟 Sửa ở đây: Sử dụng biến currentUserId đã lấy từ req.body
        // Chuyển đổi sang số để so sánh chính xác
        if (parseInt(player_id) === parseInt(currentUserId)) {
            return res.status(403).json({ 
                status: "error", 
                message: "Bạn không thể tự xóa chính mình khỏi đội!" 
            });
        }

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
//////////////////////////////////
router.post('/add_player_by_email', async (req, res) => {
    const { team_id, email, jersey_number, position } = req.body;

    try {
        // 1. Tìm thông tin user và ngày sinh từ bảng players
        const [users] = await pool.query(
            `SELECT u.id, u.name, p.id as player_id, p.date_of_birth 
             FROM users u 
             LEFT JOIN players p ON u.id = p.user_id 
             WHERE u.email = ?`, 
            [email]
        );

        if (users.length === 0) {
            return res.status(404).json({ status: "error", message: "Email không tồn tại" });
        }

        const user = users[0];

        // 🌟 KIỂM TRA NGÀY SINH: Phải có mới cho thêm vào đội
        if (!user.date_of_birth) {
            return res.status(400).json({ 
                status: "error", 
                message: "Cầu thủ này chưa cập nhật ngày sinh trong hồ sơ, không thể thêm vào đội!" 
            });
        }

        // 2. Xác định player_id
        // Nếu user đã tồn tại trong bảng players (có player_id) thì dùng nó, chưa có thì tạo mới
        let player_id = user.player_id;
        if (!player_id) {
            const [result] = await pool.query("INSERT INTO players (user_id) VALUES (?)", [user.id]);
            player_id = result.insertId;
        }

        // 3. Kiểm tra đã có trong team này chưa
       // 3. Kiểm tra đã có trong team này chưa (bao gồm cả những người đã bị xóa - is_active = 0)
const [existing] = await pool.query(
    "SELECT id, is_active FROM team_players WHERE team_id = ? AND player_id = ?", 
    [team_id, player_id]
);

if (existing.length > 0) {
    if (existing[0].is_active === 1) {
        // TH 1: Cầu thủ đang thực sự ở trong đội
        return res.status(400).json({ status: "error", message: "Cầu thủ đã có trong đội rồi!" });
    } else {
        // TH 2: Cầu thủ từng ở trong đội nhưng đã xóa, giờ thêm lại -> UPDATE is_active = 1
        await pool.query(
            "UPDATE team_players SET is_active = 1, jersey_number = ?, position = ? WHERE id = ?",
            [jersey_number, position, existing[0].id]
        );
        return res.status(200).json({ 
            status: "success", 
            message: "Thêm thành công!",
            data: { id: player_id, name: user.name, jersey_number, position, role: 'player' }
        });
    }
}

// 3.5. Kiểm tra trùng số áo (Chỉ kiểm tra người đang active)
const [checkJersey] = await pool.query(
    "SELECT jersey_number FROM team_players WHERE team_id = ? AND jersey_number = ? AND is_active = 1",
    [team_id, jersey_number]
);

if (checkJersey.length > 0) {
    return res.status(400).json({ status: "error", message: "Số áo này đã được đăng ký trong đội!" });
}

// 4. Thêm mới (Chỉ chạy khi cầu thủ chưa từng tồn tại trong team_players)
await pool.query(
    "INSERT INTO team_players (team_id, player_id, jersey_number, position, role, is_active) VALUES (?, ?, ?, ?, 'player', 1)",
    [team_id, player_id, jersey_number, position]
);

        res.status(200).json({ 
            status: "success", 
            message: "Thêm thành công!",
            data: {
                id: player_id, // Nên trả thêm ID để Android quản lý danh sách tốt hơn
                name: user.name,
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
/////////////////////////////
router.post('/add_coach_by_email', async (req, res) => {
    const { team_id, email } = req.body;

    // 1. Kiểm tra team_id hợp lệ trước khi thực hiện
    if (!team_id || team_id <= 0) {
        return res.status(400).json({ status: "error", message: "ID đội bóng không hợp lệ" });
    }

    try {
        // 1. Tìm user theo email và lấy tên của họ
        const [users] = await pool.query("SELECT id, name FROM users WHERE email = ?", [email]);
        if (users.length === 0) {
            return res.status(404).json({ status: "error", message: "Email không tồn tại trong hệ thống" });
        }
        const coach_user_id = users[0].id;
        const coach_name = users[0].name; // 🌟 Lấy tên để cập nhật vào bảng teams

        // 2. Kiểm tra xem đội đã có HLV chưa (tùy bạn, ở đây kiểm tra theo logic cũ)
        const [existingCoach] = await pool.query("SELECT id FROM team_leaders WHERE team_id = ?", [team_id]);
        if (existingCoach.length > 0) {
            return res.status(400).json({ status: "error", message: "Đội đã có HLV rồi!" });
        }

        // 3. Sử dụng Transaction để đảm bảo tính toàn vẹn dữ liệu
        // (Nếu update bảng teams lỗi thì không insert bảng team_leaders)
        await pool.query("START TRANSACTION");

        // Cập nhật tên HLV vào bảng teams (để API my_team hiển thị được)
        await pool.query("UPDATE teams SET coach_name = ? WHERE id = ?", [coach_name, team_id]);

        // Thêm vào bảng team_leaders
        await pool.query("INSERT INTO team_leaders (team_id, user_id) VALUES (?, ?)", [team_id, coach_user_id]);

        await pool.query("COMMIT");

        res.status(200).json({ status: "success", message: "Đã chỉ định HLV thành công!" });

    } catch (error) {
        await pool.query("ROLLBACK"); // Quay lại trạng thái cũ nếu lỗi
        console.error("Lỗi add_coach_by_email:", error);
        res.status(500).json({ status: "error", message: "Lỗi hệ thống: " + error.message });
    }
});
// router.post('/register_team')
router.post('/register_team', async (req, res) => {
    const { team_name, captain_name, user_id } = req.body;

    try {
        // 1. Kiểm tra ngày sinh của user trước (Phải có ngày sinh mới được tạo đội)
        const [users] = await pool.query(
            "SELECT p.date_of_birth, p.id as player_id FROM players p WHERE p.user_id = ?",
            [user_id]
        );

        if (users.length === 0 || !users[0].date_of_birth) {
            return res.status(400).json({ 
                status: "error", 
                message: "Bạn chưa cập nhật ngày sinh trong hồ sơ, không thể đăng ký đội!" 
            });
        }

        // 2. Kiểm tra xem user này đã có đội nào khác chưa
        const [checkMembership] = await pool.query(
            "SELECT team_id FROM team_players WHERE user_id = ? AND is_active = 1 LIMIT 1",
            [user_id]
        );

        if (checkMembership.length > 0) {
            return res.status(400).json({ status: "error", message: "Bạn đã là thành viên của một đội khác!" });
        }

        // 3. Insert đội bóng mới
        const [result] = await pool.query(
            "INSERT INTO teams (name, coach_name, user_id, created_at) VALUES (?, ?, ?, NOW())",
            [team_name, "Chưa cập nhật", user_id]
        );

        const newTeamId = result.insertId;

        // 4. Thêm người tạo đội vào bảng team_players (để xác định họ là đội trưởng)
       await pool.query(
    "INSERT INTO team_players (team_id, player_id, user_id, role, is_active, jersey_number) VALUES (?, ?, ?, 'captain', 1, ?)",
    [newTeamId, users[0].player_id, user_id, 1] // Thêm số 0 làm giá trị mặc định cho jersey_number
);

        res.status(200).json({ status: "success", teamId: newTeamId });

    } catch (error) {
        // Xử lý lỗi trùng tên đội
        if (error.code === 'ER_DUP_ENTRY') {
            return res.status(400).json({ status: "error", message: "Tên đội bóng này đã có người sử dụng!" });
        }
        console.error("Lỗi đăng ký đội:", error);
        res.status(500).json({ status: "error", message: "Lỗi hệ thống, vui lòng thử lại sau." });
    }
});
module.exports = router;