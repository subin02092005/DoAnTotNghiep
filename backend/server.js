const express = require('express');
require('dotenv').config();

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// 🌟 NẠP CÁC FILE CODE CON VÀO ĐÂY 🌟
const loginApi = require('./api/login');
const registerApi = require('./api/register');
const profileApi = require('./api/profile/profile');
const teamApi = require('./api/admin/teamController');
const playerApi = require('./api/admin/playerController');
const matchApi = require('./api/admin/matchController');
const tournamentApi = require('./api/admin/tournamentController');
const forgotpasswordRouter = require('./api/forgotPassword'); // Đảm bảo đường dẫn đúng đến file này

// 🌟 ĐÃ SỬA: Đường dẫn nạp file phases nằm cùng cấp trong thư mục api
const phasesRouter = require('./api/phases');
const notificationsRouter = require('./api/notification/notifications'); // Nạp file news phẳng
const standingsRoutes = require('./api/standings');
const teamRoutes = require('./api/teamplayer');// import file bạn vừa tạo
const matchRoutes = require('./api/matches');
const matchDetailRoutes = require('./api/matchesdetail');

// 1. Thêm dòng require ở đầu file


// 2. Thêm dòng app.use ở phần đăng ký route



 // Đăng ký đường dẫn
// 🌟 KẾT NỐI CHÚNG VÀO HỆ THỐNG ROUTING 🌟
app.use('/api', loginApi);    
app.use('/api', registerApi); 
app.use('/api', profileApi);  
app.use('/api', teamApi);     
app.use('/api', playerApi);   
app.use('/api', matchApi);    
app.use('/api', tournamentApi);
app.use('/api', forgotpasswordRouter);

app.use('/api', phasesRouter);
app.use('/api', notificationsRouter);
app.use('/api', standingsRoutes);
app.use('/api', teamRoutes);
app.use('/api', matchRoutes);
app.use('/api', matchDetailRoutes);
// Khởi chạy server duy nhất trên Port 3000
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`=== SERVER TỔNG ĐANG CHẠY TRÊN PORT ${PORT} ===`);
    console.log(`-> Đã nạp API Đăng nhập: http://localhost:${PORT}/api/login`);
    console.log(`-> Đã nạp API Đăng ký : http://localhost:${PORT}/api/register`);
    console.log(`-> Đã nạp API Profile  : http://localhost:${PORT}/api/profile/...`);
    console.log(`-> Đã nạp API Vòng đấu : http://localhost:${PORT}/api/seasons/:seasonId/phases`); // Đường dẫn chuẩn chỉnh cho Android gọi!
});