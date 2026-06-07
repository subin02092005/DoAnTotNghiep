const express = require('express');
require('dotenv').config();

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// 🌟 NẠP 2 FILE CODE CON VÀO ĐÂY 🌟
const loginApi = require('./api/login');
const registerApi = require('./api/register');

// 🌟 KẾT NỐI CHÚNG VÀO HỆ THỐNG ROUTING 🌟
app.use('/api', loginApi);    // Tạo đường dẫn: /api/login
app.use('/api', registerApi); // Tạo đường dẫn: /api/register

// Khởi chạy server duy nhất trên Port 3000
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`=== SERVER TỔNG ĐANG CHẠY TRÊN PORT ${PORT} ===`);
    console.log(`-> Đã nạp API Đăng nhập: http://localhost:${PORT}/api/login`);
    console.log(`-> Đã nạp API Đăng ký : http://localhost:${PORT}/api/register`);
});