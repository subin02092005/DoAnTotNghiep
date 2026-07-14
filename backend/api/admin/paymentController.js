const express = require('express');
const router = express.Router();
const mysql = require('mysql2/promise');

const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'football_management'
};

const pool = mysql.createPool(dbConfig);

// Lấy danh sách thanh toán
router.get('/payments', async (req, res) => {
    try {
        const { status } = req.query;
        console.log(`[PAYMENT] Admin lấy danh sách, status: ${status || 'TẤT CẢ'}`);

        let query = `
            SELECT
                p.id,
                p.season_team_id,
                CAST(p.amount AS DOUBLE) as amount,
                p.status,
                p.transaction_ref,
                p.paid_at,
                p.confirmed_at,
                p.confirmed_by,
                p.created_at,
                t.name as team_name,
                s.name as season_name
            FROM payments p
            JOIN season_teams st ON p.season_team_id = st.id
            JOIN teams t ON st.team_id = t.id
            JOIN seasons s ON st.season_id = s.id
            WHERE p.deleted_at IS NULL
        `;
        const params = [];

        if (status && status !== 'null' && status !== 'undefined') {
            query += ' AND p.status = ?';
            params.push(status);
        }

        query += ' ORDER BY p.created_at DESC';

        const [rows] = await pool.execute(query, params);
        res.json({ success: true, data: rows });
    } catch (error) {
        console.error("[PAYMENT] Lỗi lấy danh sách:", error);
        res.status(500).json({ success: false, message: error.message });
    }
});

// Xác nhận thanh toán
router.patch('/payments/:id/confirm', async (req, res) => {
    const { id } = req.params;
    const confirmed_by = (req.body && req.body.confirmed_by) ? req.body.confirmed_by : null;

    console.log(`[PAYMENT] Admin đang xác nhận thanh toán ID: ${id}`);

    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        // 1. Cập nhật trạng thái payment
        // Trả về 200 kèm success: false để App Android đọc được message
        const [updatePayment] = await connection.execute(
            `UPDATE payments
             SET status = 'confirmed', confirmed_at = NOW(), confirmed_by = ?, updated_at = NOW()
             WHERE id = ? AND status = 'pending' AND deleted_at IS NULL`,
            [confirmed_by, id]
        );

        if (updatePayment.affectedRows === 0) {
            await connection.rollback();
            return res.json({
                success: false,
                message: 'Giao dịch không khả dụng (đã được xử lý hoặc đã xóa).'
            });
        }

        // 2. Lấy season_team_id
        const [paymentRows] = await connection.execute(
            'SELECT season_team_id FROM payments WHERE id = ?',
            [id]
        );

        if (paymentRows.length > 0) {
            const seasonTeamId = paymentRows[0].season_team_id;
            // 3. Kích hoạt đội bóng
            await connection.execute(
                `UPDATE season_teams SET status = 'active', updated_at = NOW() WHERE id = ?`,
                [seasonTeamId]
            );
        }

        await connection.commit();
        res.json({ success: true, message: 'Xác nhận thanh toán và kích hoạt đội thành công!' });

    } catch (error) {
        if (connection) await connection.rollback();
        console.error("[PAYMENT] Lỗi:", error);
        res.json({ success: false, message: 'Lỗi server: ' + error.message });
    } finally {
        if (connection) connection.release();
    }
});

// Từ chối thanh toán
router.patch('/payments/:id/reject', async (req, res) => {
    const { id } = req.params;
    try {
        const [result] = await pool.execute(
            `UPDATE payments
             SET status = 'rejected', updated_at = NOW()
             WHERE id = ? AND status = 'pending' AND deleted_at IS NULL`,
            [id]
        );

        if (result.affectedRows === 0) {
            return res.json({
                success: false,
                message: 'Giao dịch không khả dụng.'
            });
        }

        res.json({ success: true, message: 'Đã từ chối thanh toán.' });
    } catch (error) {
        res.json({ success: false, message: error.message });
    }
});

module.exports = router;
