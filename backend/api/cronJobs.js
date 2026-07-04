const mysql = require('mysql2/promise');
const cron = require('node-cron');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management',
});

cron.schedule('* * * * *', async () => {
    console.log("Đang quét các trận đấu cần bắt đầu...");
    try {
        // Sử dụng played_at để ghi lại thời gian bắt đầu thực tế
        // Sử dụng trạng thái 'scheduled' thay vì 'pending'
        const [result] = await pool.query(`
            UPDATE matches 
            SET status = 'ongoing', 
                played_at = NOW(),
                updated_at = NOW()
            WHERE status = 'scheduled' 
            AND scheduled_at <= NOW()
        `);
        
        if (result.affectedRows > 0) {
            console.log(`Đã cập nhật trạng thái cho ${result.affectedRows} trận đấu sang 'ongoing'.`);
        }
    } catch (error) {
        console.error("Lỗi Cron Job:", error);
    }
});

module.exports = pool;