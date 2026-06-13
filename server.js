const express = require('express');
require('dotenv').config();

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// 🌟 NẠP CÁC FILE CODE CON VÀO ĐÂY 🌟
const loginApi = require('./api/login');
const registerApi = require('./api/register');
const profileApi = require('./api/profile'); // <-- ĐÃ THÊM FILE MỚI VÀO ĐÂY
const teamApi = require('./api/admin/teamController');
const playerApi = require('./api/admin/playerController');
const matchApi = require('./api/admin/matchController');
const tournamentApi = require('./api/admin/tournamentController');

// 🌟 KẾT NỐI CHÚNG VÀO HỆ THỐNG ROUTING 🌟
app.use('/api', loginApi);    
app.use('/api', registerApi); 
app.use('/api', profileApi);  // <-- ĐÃ KẾT NỐI ĐƯỜNG DẪN PROFILE CHO ANDROID GỌI
app.use('/api', teamApi);     
app.use('/api', playerApi);   
app.use('/api', matchApi);    
app.use('/api', tournamentApi); 

// Khởi chạy server duy nhất trên Port 3000
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`=== SERVER TỔNG ĐANG CHẠY TRÊN PORT ${PORT} ===`);
    console.log(`-> Đã nạp API Đăng nhập: http://localhost:${PORT}/api/login`);
    console.log(`-> Đã nạp API Đăng ký : http://localhost:${PORT}/api/register`);
    console.log(`-> Đã nạp API Profile  : http://localhost:${PORT}/api/profile/...`);
});