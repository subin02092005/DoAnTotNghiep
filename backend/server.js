const express = require('express');
require('dotenv').config();

require('./api/cronJobs');
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use('/api', (req, res, next) => {
    console.log('API REQUEST:', req.method, req.originalUrl);
    next();
});

// 🌟 NẠP CÁC FILE CODE CON VÀO ĐÂY 🌟
const loginApi = require('./api/login');
const registerApi = require('./api/register');
const profileApi = require('./api/profile/profile');
const teamApi = require('./api/admin/teamController');
const playerApi = require('./api/admin/playerController');
const matchApi = require('./api/admin/matchController');
const matchEventApi = require('./api/admin/matchEventController');
const featuredMatchApi = require('./api/admin/featuredMatchController');
const tournamentApi = require('./api/admin/tournamentController');
const forgotpasswordRouter = require('./api/forgotPassword'); // Đảm bảo đường dẫn đúng đến file này
const adminNotificationsRouter = require('./api/admin/notificationsController');
// 🌟 ĐÃ SỬA: Đường dẫn nạp file phases nằm cùng cấp trong thư mục api
const phasesRouter = require('./api/phases');

const standingsRoutes = require('./api/standings');
const teamRoutes = require('./api/teamplayer');// import file bạn vừa tạo
const matchRoutes = require('./api/matches');
const matchDetailRoutes = require('./api/matchesdetail');
//doi bong cua minh
const myTeamRoutes = require('./api/my_team'); // import file my_team.js

// 1. Thêm dòng require ở đầu file


// 2. Thêm dòng app.use ở phần đăng ký route
const notificationsRouter = require('./api/notification/notificationsRouter'); 
app.use('/api', notificationsRouter);

 // Đăng ký đường dẫn
// 🌟 KẾT NỐI CHÚNG VÀO HỆ THỐNG ROUTING 🌟
app.use('/api', loginApi);    
app.use('/api', registerApi); 
app.use('/api', profileApi);  
app.use('/api', teamApi);     
app.use('/api', playerApi);   
app.use('/api', matchApi);
app.use('/api', matchEventApi);    
app.use('/api', featuredMatchApi);
app.use('/api', tournamentApi);
app.use('/api', forgotpasswordRouter);
app.use('/api', adminNotificationsRouter);
app.use('/api', phasesRouter);

app.use('/api', standingsRoutes);
app.use('/api', teamRoutes);
app.use('/api', matchRoutes);
app.use('/api', matchDetailRoutes);

app.use('/api', myTeamRoutes); // Đăng ký route cho my_team.js

// Khởi chạy server duy nhất trên Port 3000
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`=== SERVER TỔNG ĐANG CHẠY TRÊN PORT ${PORT} ===`);
    console.log(`-> Đã nạp API Đăng nhập: http://localhost:${PORT}/api/login`);
    console.log(`-> Đã nạp API Đăng ký : http://localhost:${PORT}/api/register`);
    console.log(`-> Đã nạp API Profile  : http://localhost:${PORT}/api/profile/...`);
    console.log(`-> Đã nạp API Vòng đấu : http://localhost:${PORT}/api/seasons/:seasonId/phases`); // Đường dẫn chuẩn chỉnh cho Android gọi!
});