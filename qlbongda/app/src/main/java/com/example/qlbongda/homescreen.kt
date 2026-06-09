    package com.example.qlbongda

    import android.widget.Toast
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Delete
    import androidx.compose.material.icons.filled.DateRange
    import androidx.compose.material.icons.filled.Edit
    import androidx.compose.material.icons.filled.Home
    import androidx.compose.material.icons.filled.Info
    import androidx.compose.material.icons.filled.List
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.qlbongda.data.model.FootballNews
    import com.example.qlbongda.data.model.FullMatchDetail
    import com.example.qlbongda.data.model.MatchEvent
    import com.example.qlbongda.data.model.PlayerInfo
    import com.example.qlbongda.data.model.StandingRow
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material3.ModalBottomSheet
    import androidx.compose.foundation.layout.navigationBarsPadding
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material.icons.automirrored.filled.ArrowBack
    import androidx.compose.material.icons.filled.Visibility
    import androidx.compose.material.icons.filled.VisibilityOff
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.text.input.VisualTransformation


    // ==========================================
    // 1. ĐỊNH NGHĨA DATA MODELS VÀ MÀU SẮC CHỦ ĐẠO
    // ==========================================



    // ==========================================
    // 2. MÀN HÌNH CHÍNH (HOME SCREEN)
    // ==========================================

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(matchList: List<FullMatchDetail>,
        onLogout: () -> Unit,
        onNavigateToStandingDetail: () -> Unit // 🌟 Nhận Lambda điều hướng từ MainActivity chuyển qua
    ) {
        val context = LocalContext.current
        var selectedTab by remember { mutableStateOf(0) }
        var activeDetailMatch by remember { mutableStateOf<FullMatchDetail?>(null) }

        // ---- DỮ LIỆU QUẢN LÝ ĐỘI BÓNG (TAB 2) ----
        var isTeamRegistered by remember { mutableStateOf(false) }
        var teamName by remember { mutableStateOf("") }
        var leaderName by remember { mutableStateOf("") }
        var isLeagueRegistered by remember { mutableStateOf(false) }

        val playerList = remember { mutableStateListOf<PlayerInfo>() }

        var isEditingPlayer by remember { mutableStateOf(false) }
        var editingPlayerIndex by remember { mutableStateOf(-1) }
        var inputPlayerNumber by remember { mutableStateOf("") }
        var inputPlayerName by remember { mutableStateOf("") }
        var inputPlayerPosition by remember { mutableStateOf("FW") }

        // ---- DỮ LIỆU MẪU BẢNG XẾP HẠNG THU NHỎ (TAB 0) ----
        val standingList = remember {
            listOf(
                StandingRow(1, "Arsenal", 1, "+1", 3),
                StandingRow(2, "Man City", 0, "0", 0),
                StandingRow(3, "MU", 0, "0", 0),
                StandingRow(4, "Chelsea", 1, "-1", 0),
                StandingRow(5, "Liverpool", 0, "0", 0),
                StandingRow(6, "Tottenham", 0, "0", 0),
                StandingRow(7, "Aston Villa", 0, "0", 0),
                StandingRow(8, "Newcastle", 0, "0", 0)
            )
        }
        val newsList = remember {
            listOf(
                FootballNews(
                    1,
                    "Arsenal giành chiến thắng kịch tính phút bù giờ cuối cùng",
                    "Trận đấu nghẹt thở kết thúc với tỷ số 3-2 nghiêng về Pháo thủ.",
                    "10 phút trước",
                    "Sky Sports"
                ),
                FootballNews(2, "Siêu máy tính dự đoán nhà vô địch Ngoại Hạng Anh 2026", "Tỷ lệ vô địch của Man City giảm mạnh sau chuỗi trận hòa.", "1 giờ trước", "BBC Sport"),
                FootballNews(3, "Chấn thương của ngôi sao MU nghiêm trọng hơn dự kiến", "Tiền đạo chủ lực phải nghỉ thi đấu ít nhất 3 tuần.", "3 giờ trước", "ESPN"),
                FootballNews(4, "Thị trường chuyển nhượng: Chelsea nhắm bom tấn 100 triệu Euro", "Đội bóng quyết tâm bổ sung một trung phong đẳng cấp.", "5 giờ trước", "Romano")
            )
        }
        // ---- DANH SÁCH LỊCH THI ĐẤU MẪU (TAB 1) ----


        // Kiểm tra và hiển thị màn hình chi tiết trận đấu khi bấm từ Tab 1
        if (activeDetailMatch != null) {
            MatchDetailScreen(
                match = activeDetailMatch!!,
                onBack = { activeDetailMatch = null }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(containerColor = Color.Black, tonalElevation = 0.dp) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            label = { Text("Trang chủ", color = if (selectedTab == 0) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            label = { Text("Lịch đấu", color = if (selectedTab == 1) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                            icon = { Icon(Icons.Default.DateRange, contentDescription = "Lịch thi đấu") },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                        )
                        NavigationBarItem(
                            selected = selectedTab == 4,
                            onClick = { selectedTab = 4 },
                            label = { Text("Tin tức", color = if (selectedTab == 4) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                            icon = { Text("📰", fontSize = 20.sp) }, // Bạn có thể đổi thành Icons.Default.List hoặc icon tùy ý
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            label = { Text("Đội bóng", color = if (selectedTab == 2) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                            icon = { Icon(Icons.Default.List, contentDescription = "Chi tiết đội bóng") },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                        )
                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            label = { Text("Cá nhân", color = if (selectedTab == 3) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                            icon = { Icon(Icons.Default.Info, contentDescription = "Trang cá nhân") },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(innerPadding)
                ) {
                    when (selectedTab) {
                        0 -> {
                            // 🌟 TAB 0: TRANG CHỦ + KHỐI BẢNG XẾP HẠNG (ĐÃ PHẲNG HOÁ & CÓ THỂ CLICK)
                            LazyColumn(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                            ) {
                                // Banner Khởi động Welcome
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth().height(130.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                                        border = BorderStroke(1.dp, NeonGreen)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(text = "WELCOME TO TTTQ SHOP", color = NeonGreen, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(text = "Ứng dụng quản lý giải đấu bóng đá chuyên nghiệp", color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                // Tiêu đề khối dữ liệu bảng xếp hạng
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToStandingDetail() }, // 🌟 Click vào tiêu đề để xem chi tiết
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Column {
                                            Text(text = "BẢNG XẾP HẠNG", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "Cập nhật giải đấu mùa xuân 2026", color = Color.LightGray, fontSize = 11.sp)
                                        }
                                        Text(text = "Xem chi tiết ➔", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                // Cấu trúc 1: Khối Card Header của bảng xếp hạng
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onNavigateToStandingDetail() }, // 🌟 Click vào Header cũng chuyển trang
                                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                                        border = BorderStroke(1.dp, Color(0xFF222222))
                                    ) {
                                        Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "STT", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                                                Text(text = "ĐỘI BÓNG", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                                Text(text = "TRẬN", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                                Text(text = "HS", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                                Text(text = "ĐIỂM", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                            }
                                            HorizontalDivider(color = Color(0xFF222222), thickness = 1.dp)
                                        }
                                    }
                                }

                                // Cấu trúc 2: Danh sách phẳng items của từng câu lạc bộ (Dễ dàng lướt và có thể bấm chọn)
                                items(standingList) { row ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onNavigateToStandingDetail() }, // 🌟 Click vào bất kỳ hàng nào cũng chuyển sang trang chi tiết đầy đủ
                                        shape = RoundedCornerShape(0.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                                        border = BorderStroke(1.dp, Color(0xFF222222))
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = row.rank.toString(),
                                                    color = if (row.rank == 1) NeonGreen else Color.White,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.width(40.dp),
                                                    textAlign = TextAlign.Center
                                                )
                                                Text(text = row.teamName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                                Text(text = row.played.toString(), color = Color.White, fontSize = 14.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                                Text(text = row.goalDifference, color = Color.LightGray, fontSize = 14.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                                Text(text = row.points.toString(), color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                            }
                                            HorizontalDivider(color = Color(0xFF1C1C1C), thickness = 0.5.dp)
                                        }
                                    }
                                }

                                // Cấu trúc 3: Đáy kết thúc khối card bo viền góc dưới
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clickable { onNavigateToStandingDetail() },
                                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                                        border = BorderStroke(1.dp, Color(0xFF222222))
                                    ) {}
                                }
                            }
                        }
                        1 -> {
                            // 🌟 TAB 1: LỊCH THI ĐẤU GIẢI ĐẤU
                            Column(
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "LỊCH THI ĐẤU", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(matchList) { match ->
                                        // 🌟 KIỂM TRA ĐIỀU KIỆN TRẬN ĐẤU NỔI BẬT
                                        val isFeatured = match.isHot

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { activeDetailMatch = match },
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(
                                                width = if (isFeatured) 2.dp else 1.dp,
                                                color = if (isFeatured) Color.Red else NeonGreen
                                            ),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                                        ) {
                                            // 🌟 BỌC BOX Ở ĐÂY ĐỂ ĐÓNG ĐINH NGÔI SAO LÊN GÓC TRÊN BÊN PHẢI
                                            Box(modifier = Modifier.fillMaxWidth()) {

                                                // Khối nội dung dọc của bạn giữ nguyên, chỉ bỏ ngôi sao ở text đi
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    // Đội A: Đã xóa ngôi sao dính liền chữ để text căn giữa chuẩn chỉ
                                                    Text(
                                                        text = match.teamA,
                                                        color = if (isFeatured) Color(0xFFFFD700) else Color.White,
                                                        fontSize = 15.sp,
                                                        fontWeight = if (isFeatured) FontWeight.Black else FontWeight.Bold
                                                    )

                                                    Text(
                                                        text = "VS",
                                                        color = NeonGreen,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Black,
                                                        modifier = Modifier.padding(vertical = 2.dp)
                                                    )

                                                    // Đội B
                                                    Text(
                                                        text = match.teamB,
                                                        color = if (isFeatured) Color(0xFFFFD700) else Color.White,
                                                        fontSize = 15.sp,
                                                        fontWeight = if (isFeatured) FontWeight.Black else FontWeight.Bold
                                                    )

                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    HorizontalDivider(color = Color(0xFF222222))
                                                    Spacer(modifier = Modifier.height(6.dp))

                                                    // Giờ thi đấu
                                                    Text(
                                                        text = match.time,
                                                        color = if (isFeatured) Color.Red else NeonGreen,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Black
                                                    )

                                                    // Ngày thi đấu
                                                    Text(
                                                        text = match.date,
                                                        color = Color.LightGray,
                                                        fontSize = 11.sp
                                                    )
                                                }

                                                // 🌟 ĐƯA NGÔI SAO RA GÓC (Chỉ hiển thị khi trận HOT)
                                                if (isFeatured) {
                                                    Text(
                                                        text = "⭐",
                                                        fontSize = 12.sp,
                                                        modifier = Modifier
                                                            .align(Alignment.TopEnd) // Đẩy thẳng lên góc trên cùng bên phải
                                                            .padding(top = 10.dp, end = 10.dp) // Cách lề trên và lề phải 10dp cho đẹp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        2 -> {
                            // 🌟 TAB 2: ĐỘI BÓNG CỦA TÔI & THÀNH VIÊN
                            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                Text(text = "QUẢN LÝ ĐỘI BÓNG", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                                Text(text = "— MÙA GIẢI 2026 —", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp), textAlign = TextAlign.Center)

                                if (!isTeamRegistered) {
                                    // Giao diện đăng ký đội tham gia giải ban đầu
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                                        border = BorderStroke(1.dp, NeonGreen),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Đăng Ký Đội Bóng Thành Viên", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(16.dp))

                                            OutlinedTextField(
                                                value = teamName,
                                                onValueChange = { teamName = it },
                                                label = { Text("Tên đội bóng", color = Color.White) },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))

                                            OutlinedTextField(
                                                value = leaderName,
                                                onValueChange = { leaderName = it },
                                                label = { Text("Tên Captain / Đại diện", color = Color.White) },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                            )
                                            Spacer(modifier = Modifier.height(20.dp))

                                            Button(
                                                onClick = {
                                                    if (teamName.trim().isEmpty() || leaderName.trim().isEmpty()) {
                                                        Toast.makeText(context, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        isTeamRegistered = true
                                                        Toast.makeText(context, "Đăng ký thành viên thành công!", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("ĐĂNG KÝ THÀNH VIÊN GIẢI", color = Color.Black, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                } else {
                                    // Giao diện CRUD quản lý danh sách cầu thủ của câu lạc bộ
                                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                        item {
                                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)), border = BorderStroke(1.dp, NeonGreen)) {
                                                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                    Column {
                                                        Text(text = "Đội: ${teamName.uppercase()}", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                                        Text(text = "Đại diện: $leaderName", color = Color.White, fontSize = 13.sp)
                                                    }
                                                    Button(
                                                        onClick = {
                                                            isLeagueRegistered = !isLeagueRegistered
                                                            val msg = if(isLeagueRegistered) "Đã nộp đơn đăng ký tham gia Giải đấu! 🏆" else "Đã hủy đơn đăng ký giải đấu!"
                                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = if(isLeagueRegistered) Color.DarkGray else Color.Red),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text(text = if (isLeagueRegistered) "ĐÃ ĐĂNG KÝ GIẢI" else "ĐĂNG KÝ GIẢI", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }

                                        item {
                                            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F0F)), border = BorderStroke(1.dp, NeonGreen)) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Text(text = if (isEditingPlayer) "CẬP NHẬT CẦU THỦ" else "THÊM CẦU THỦ MỚI", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                        OutlinedTextField(value = inputPlayerNumber, onValueChange = { inputPlayerNumber = it }, label = { Text("Số áo", color = Color.White, fontSize = 11.sp) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                                                        OutlinedTextField(value = inputPlayerName, onValueChange = { inputPlayerName = it }, label = { Text("Họ tên", color = Color.White, fontSize = 11.sp) }, modifier = Modifier.weight(2.5f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                                                    }
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                            listOf("GK", "DF", "MF", "FW").forEach { pos ->
                                                                FilterChip(
                                                                    selected = inputPlayerPosition == pos,
                                                                    onClick = { inputPlayerPosition = pos },
                                                                    label = { Text(pos, fontSize = 10.sp, color = if(inputPlayerPosition == pos) Color.Black else Color.White) },
                                                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NeonGreen, selectedLabelColor = Color.Black, containerColor = Color.Black)
                                                                )
                                                            }
                                                        }

                                                        Button(
                                                            onClick = {
                                                                if (inputPlayerNumber.trim().isEmpty() || inputPlayerName.trim().isEmpty()) {
                                                                    Toast.makeText(context, "Thiếu số áo hoặc họ tên!", Toast.LENGTH_SHORT).show()
                                                                } else {
                                                                    val newPlayer = PlayerInfo(inputPlayerNumber.trim(), inputPlayerName.trim(), inputPlayerPosition)
                                                                    if (isEditingPlayer && editingPlayerIndex != -1) {
                                                                        playerList[editingPlayerIndex] = newPlayer
                                                                        isEditingPlayer = false
                                                                        editingPlayerIndex = -1
                                                                        Toast.makeText(context, "Đã sửa cầu thủ!", Toast.LENGTH_SHORT).show()
                                                                    } else {
                                                                        playerList.add(newPlayer)
                                                                        Toast.makeText(context, "Đã thêm cầu thủ!", Toast.LENGTH_SHORT).show()
                                                                    }
                                                                    inputPlayerNumber = ""
                                                                    inputPlayerName = ""
                                                                    inputPlayerPosition = "FW"
                                                                }
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                                            shape = RoundedCornerShape(4.dp)
                                                        ) {
                                                            Text(if (isEditingPlayer) "CẬP NHẬT" else "THÊM", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        item {
                                            Text("DANH SÁCH CẦU THỦ THÀNH VIÊN (${playerList.size})", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }

                                        if(playerList.isEmpty()) {
                                            item {
                                                Text("Chưa có cầu thủ nào. Vui lòng thêm ở form trên!", color = Color.White, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                                            }
                                        }

                                        items(playerList.size) { index ->
                                            val player = playerList[index]
                                            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF121212), RoundedCornerShape(8.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Box(modifier = Modifier.size(32.dp).background(NeonGreen, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                                                    Text(text = player.number, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(text = player.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                                    Text(text = "Vị trí: ${player.position}", color = Color.LightGray, fontSize = 11.sp)
                                                }
                                                IconButton(onClick = {
                                                    isEditingPlayer = true
                                                    editingPlayerIndex = index
                                                    inputPlayerNumber = player.number
                                                    inputPlayerName = player.name
                                                    inputPlayerPosition = player.position
                                                }) {
                                                    Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color.LightGray)
                                                }
                                                IconButton(onClick = {
                                                    playerList.removeAt(index)
                                                    Toast.makeText(context, "Đã xóa cầu thủ!", Toast.LENGTH_SHORT).show()
                                                }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        3 -> {
                            // 🌟 TAB 3: TÀI KHOẢN CÁ NHÂN & CÀI ĐẶT HỆ THỐNG
                            val context = LocalContext.current
                            val scrollState = rememberScrollState()

                            // Khởi tạo SharedPreferences để quản lý dữ liệu cấu hình tài khoản dưới máy
                            val sharedPref = remember { context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE) }

                            // 🌟 CÁC BIẾN THÔNG TIN CÁ NHÂN CÓ THỂ CHỈNH SỬA
                            var userName by remember { mutableStateOf(sharedPref.getString("USER_NAME", "Quản trị viên") ?: "Quản trị viên") }
                            var userEmail by remember { mutableStateOf(sharedPref.getString("REMEMBERED_EMAIL", "admin@tttqshop.com") ?: "admin@tttqshop.com") }
                            var userPhone by remember { mutableStateOf(sharedPref.getString("USER_PHONE", "0123456789") ?: "0123456789") }
                            var userPassword by remember { mutableStateOf(sharedPref.getString("REMEMBERED_PASSWORD", "123456") ?: "123456") }

                            // Trạng thái đóng/mở BottomSheet chỉnh sửa thông tin
                            var showEditSheet by remember { mutableStateOf(false) }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Khối Avatar dạng chữ viết tắt của Tên (Tự động cập nhật theo tên mới)
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .background(Color(0xFF121212), RoundedCornerShape(45.dp))
                                        .border(2.dp, NeonGreen, RoundedCornerShape(45.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if(userName.trim().length >= 2) userName.trim().take(2).uppercase() else "AD",
                                        color = NeonGreen,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Câu chào mừng động
                                Text(
                                    text = "Chào mừng, $userName!",
                                    color = NeonGreen,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Email & Số điện thoại hiển thị phụ phía dưới Avatar
                                Text(
                                    text = "$userEmail  |  $userPhone",
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Nút mở bảng chỉnh sửa thông tin cá nhân
                                OutlinedButton(
                                    onClick = { showEditSheet = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(horizontal = 32.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    border = BorderStroke(1.dp, NeonGreen),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                                ) {
                                    Text(text = "CHỈNH SỬA THÔNG TIN", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider(color = Color(0xFF222222))
                                Spacer(modifier = Modifier.height(20.dp))

                                // Khung thông tin đơn vị cố định của hệ thống
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                                    border = BorderStroke(0.5.dp, Color.DarkGray)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Đơn vị cố định
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Đơn vị", color = Color.LightGray, fontSize = 13.sp)
                                            Text("Trường CĐ KT Cao Thắng", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Khoa cố định
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Khoa", color = Color.LightGray, fontSize = 13.sp)
                                            Text("Công nghệ Thông tin", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Vai trò hệ thống cố định
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Vai trò", color = Color.LightGray, fontSize = 13.sp)
                                            Text("Ban Tổ Chức", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        HorizontalDivider(color = Color(0xFF222222))
                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Phiên bản ứng dụng
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Phiên bản ứng dụng", color = Color.LightGray, fontSize = 13.sp)
                                            Text("v2.6.0 (Mùa giải 2026)", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Trạng thái máy chủ
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Trạng thái máy chủ", color = Color.LightGray, fontSize = 13.sp)
                                            Text("Ổn định 🟢", color = Color.White, fontSize = 13.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                                // Nút Đăng xuất
                                Button(
                                    onClick = { onLogout() },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(text = "ĐĂNG XUẤT TÀI KHOẢN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }

                            // 🌟 GIAO DIỆN BẢNG TRƯỢT SỬA TÊN, EMAIL, SĐT, MẬT KHẨU 🌟
                            if (showEditSheet) {
                                var editName by remember { mutableStateOf(userName) }
                                var editEmail by remember { mutableStateOf(userEmail) }
                                var editPhone by remember { mutableStateOf(userPhone) }

                                // Trạng thái để mở giao diện Đổi Mật Khẩu phụ
                                var showChangePasswordDialog by remember { mutableStateOf(false) }

                                ModalBottomSheet(
                                    onDismissRequest = { showEditSheet = false },
                                    containerColor = Color(0xFF0F0F0F),
                                    scrimColor = Color.Black.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp, vertical = 8.dp)
                                            .verticalScroll(rememberScrollState())
                                            .navigationBarsPadding()
                                    ) {
                                        Text(
                                            text = "Thay đổi thông tin cá nhân",
                                            color = NeonGreen,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 20.dp)
                                        )

                                        // 1. Ô sửa Họ và Tên
                                        OutlinedTextField(
                                            value = editName,
                                            onValueChange = { editName = it },
                                            label = { Text("Họ và Tên", color = Color.Gray) },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = NeonGreen,
                                                unfocusedBorderColor = Color.DarkGray,
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // 2. Ô sửa Email
                                        OutlinedTextField(
                                            value = editEmail,
                                            onValueChange = { editEmail = it },
                                            label = { Text("Địa chỉ Email", color = Color.Gray) },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            singleLine = true,
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = NeonGreen,
                                                unfocusedBorderColor = Color.DarkGray,
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // 3. Ô sửa Số điện thoại
                                        OutlinedTextField(
                                            value = editPhone,
                                            onValueChange = { editPhone = it },
                                            label = { Text("Số điện thoại", color = Color.Gray) },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = NeonGreen,
                                                unfocusedBorderColor = Color.DarkGray,
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        // 🌟 NÚT ĐỂ MỞ TRANG ĐỔI MẬT KHẨU RIÊNG BIỆT 🌟
                                        TextButton(
                                            onClick = { showChangePasswordDialog = true },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text(
                                                text = "Đổi mật khẩu tài khoản?",
                                                color = NeonGreen,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Hàng điều khiển Hủy bỏ / Lưu lại thông tin cá nhân
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            OutlinedButton(
                                                onClick = { showEditSheet = false },
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                border = BorderStroke(1.dp, Color.Gray),
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                            ) {
                                                Text("HỦY BỎ", fontWeight = FontWeight.Bold)
                                            }

                                            Spacer(modifier = Modifier.width(16.dp))

                                            Button(
                                                onClick = {
                                                    if (editName.trim().isEmpty() || editEmail.trim().isEmpty() || editPhone.trim().isEmpty()) {
                                                        Toast.makeText(context, "Vui lòng không để trống thông tin!", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        userName = editName
                                                        userEmail = editEmail
                                                        userPhone = editPhone

                                                        sharedPref.edit().apply {
                                                            putString("USER_NAME", editName)
                                                            putString("REMEMBERED_EMAIL", editEmail)
                                                            putString("USER_PHONE", editPhone)
                                                            apply()
                                                        }

                                                        Toast.makeText(context, "Đã lưu thay đổi thông tin!", Toast.LENGTH_SHORT).show()
                                                        showEditSheet = false
                                                    }
                                                },
                                                modifier = Modifier.weight(1f).height(48.dp),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                                            ) {
                                                Text("LƯU LẠI", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                // 🌟 GIAO DIỆN PHỤ: TRANG ĐỔI MẬT KHẨU (3 Ô NHẬP) 🌟
                                if (showChangePasswordDialog) {
                                    var oldPasswordInput by remember { mutableStateOf("") }
                                    var newPasswordInput by remember { mutableStateOf("") }
                                    var confirmPasswordInput by remember { mutableStateOf("") }

                                    var isOldVisible by remember { mutableStateOf(false) }
                                    var isNewVisible by remember { mutableStateOf(false) }
                                    var isConfirmVisible by remember { mutableStateOf(false) }

                                    AlertDialog(
                                        onDismissRequest = { showChangePasswordDialog = false },
                                        containerColor = Color(0xFF121212),
                                        shape = RoundedCornerShape(16.dp),
                                        title = {
                                            Text("Đổi mật khẩu mới", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        },
                                        text = {
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Text("Nhập đầy đủ thông tin bảo mật để thiết lập mật khẩu mới.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 16.dp))

                                                // 1. Ô nhập Mật khẩu cũ
                                                OutlinedTextField(
                                                    value = oldPasswordInput,
                                                    onValueChange = { oldPasswordInput = it },
                                                    label = { Text("Mật khẩu hiện tại", color = Color.Gray) },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    singleLine = true,
                                                    visualTransformation = if (isOldVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                                    trailingIcon = {
                                                        IconButton(onClick = { isOldVisible = !isOldVisible }) {
                                                            Icon(imageVector = if (isOldVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen)
                                                        }
                                                    },
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                                )

                                                Spacer(modifier = Modifier.height(12.dp))

                                                // 2. Ô nhập Mật khẩu mới
                                                OutlinedTextField(
                                                    value = newPasswordInput,
                                                    onValueChange = { newPasswordInput = it },
                                                    label = { Text("Mật khẩu mới", color = Color.Gray) },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    singleLine = true,
                                                    visualTransformation = if (isNewVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                                    trailingIcon = {
                                                        IconButton(onClick = { isNewVisible = !isNewVisible }) {
                                                            Icon(imageVector = if (isNewVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen)
                                                        }
                                                    },
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                                )

                                                Spacer(modifier = Modifier.height(12.dp))

                                                // 3. Ô Xác nhận mật khẩu mới
                                                OutlinedTextField(
                                                    value = confirmPasswordInput,
                                                    onValueChange = { confirmPasswordInput = it },
                                                    label = { Text("Xác nhận mật khẩu mới", color = Color.Gray) },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    singleLine = true,
                                                    visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                                    trailingIcon = {
                                                        IconButton(onClick = { isConfirmVisible = !isConfirmVisible }) {
                                                            Icon(imageVector = if (isConfirmVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen)
                                                        }
                                                    },
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                                )
                                            }
                                        },
                                        confirmButton = {
                                            Button(
                                                onClick = {
                                                    when {
                                                        oldPasswordInput.isEmpty() || newPasswordInput.isEmpty() || confirmPasswordInput.isEmpty() -> {
                                                            Toast.makeText(context, "Vui lòng nhập đầy đủ 3 ô!", Toast.LENGTH_SHORT).show()
                                                        }
                                                        oldPasswordInput != userPassword -> {
                                                            Toast.makeText(context, "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show()
                                                        }
                                                        newPasswordInput == oldPasswordInput -> {
                                                            Toast.makeText(context, "Mật khẩu mới không được trùng mật khẩu cũ!", Toast.LENGTH_SHORT).show()
                                                        }
                                                        newPasswordInput != confirmPasswordInput -> {
                                                            Toast.makeText(context, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show()
                                                        }
                                                        else -> {
                                                            // Thực hiện đổi mật khẩu thành công
                                                            userPassword = newPasswordInput

                                                            // Lưu mật khẩu mới đè vào máy
                                                            sharedPref.edit().putString("REMEMBERED_PASSWORD", newPasswordInput).apply()

                                                            Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                                                            showChangePasswordDialog = false // Đóng trang đổi mật khẩu
                                                        }
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                                            ) {
                                                Text("XÁC NHẬN", fontWeight = FontWeight.Bold)
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showChangePasswordDialog = false }) {
                                                Text("HỦY BỎ", color = Color.Gray)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        4 -> {
                            // Gọi hàm NewsScreen() từ file NewsScreen.kt riêng của bạn
                            // Jetpack Compose sẽ tự động nhận diện nếu chung một package com.example.qlbongda
                            NewsScreen()
                        }
                    }
                }
            }
        }
    }
    @Composable
    fun NewsScreen() {
        // 🌟 Trạng thái lưu bài báo đang được chọn để xem chi tiết
        var activeDetailNews by remember { mutableStateOf<FootballNews?>(null) }

        // Cập nhật lại danh sách dữ liệu mẫu đầy đủ thông tin chi tiết
        val newsList = remember {
            listOf(
                FootballNews(
                    id = 1,
                    title = "Arsenal giành chiến thắng kịch tính phút bù giờ cuối cùng",
                    summary = "Trận đấu nghẹt thở kết thúc với tỷ số 3-2 nghiêng về Pháo thủ.",
                    time = "10 phút trước",
                    source = "Sky Sports",
                    category = "Ngoại Hạng Anh",
                    content = "Trận đấu muộn vòng 34 đã diễn ra vô cùng kịch tính tại thánh địa Emirates. Dù bị dẫn trước từ sớm, các cầu thủ Arsenal vẫn kiên trì lội ngược dòng thành công. Bàn thắng quyết định được ghi do công của đội trưởng ở phút bù giờ thứ 5, đem về 3 điểm quý giá giữ vững ngôi đầu bảng cho Pháo Thủ."
                ),
                FootballNews(
                    id = 2,
                    title = "Siêu máy tính dự đoán nhà vô địch Ngoại Hạng Anh 2026",
                    summary = "Tỷ lệ vô địch của Man City giảm mạnh sau chuỗi trận hòa.",
                    time = "1 giờ trước",
                    source = "BBC Sport",
                    category = "Phân Tích",
                    content = "Theo mô phỏng mới nhất từ siêu máy tính Opta, tỷ lệ bảo vệ ngôi vương của Manchester City đã sụt giảm nghiêm trọng xuống còn 42.5%. Trong khi đó, phong độ hủy diệt của các câu lạc bộ bám đuổi đã đẩy cục diện cuộc đua vô địch năm nay trở nên căng thẳng và khó lường hơn bao giờ hết."
                ),
                FootballNews(
                    id = 3,
                    title = "Chấn thương của ngôi sao MU nghiêm trọng hơn dự kiến",
                    summary = "Tiền đạo chủ lực phải nghỉ thi đấu ít nhất 3 tuần.",
                    time = "3 giờ trước",
                    source = "ESPN",
                    category = "Chấn Thương",
                    content = "Tin không vui dành cho người hâm mộ Quỷ Đỏ khi bộ phận y tế xác nhận tiền đạo chủ lực đã gặp phải một chấn thương gân khoeo cấp độ 2. Anh chắc chắn sẽ vắng mặt trong chuỗi 4 trận đấu quan trọng sắp tới, bao gồm cả trận derby rực lửa vào tuần sau."
                ),
                FootballNews(
                    id = 4,
                    title = "Thị trường chuyển nhượng: Chelsea nhắm bom tấn 100 triệu Euro",
                    summary = "Đội bóng quyết tâm bổ sung một trung phong đẳng cấp.",
                    time = "5 giờ trước",
                    source = "Romano",
                    category = "Chuyển Nhượng",
                    content = "Chuyên gia săn tin chuyển nhượng Fabrizio Romano vừa độc quyền tiết lộ ban lãnh đạo Chelsea đang rục rịch đàm phán kích hoạt điều khoản giải phóng hợp đồng của tiền đạo hot nhất châu Âu hiện tại. Dự kiến thương vụ bom tấn này có mức phí không dưới 100 triệu Euro nhằm sửa chữa hàng công cho câu lạc bộ."
                )
            )
        }

        // 🌟 LOGIC ĐIỀU HƯỚNG TẠI CHỖ
        if (activeDetailNews != null) {
            // Nếu có bài báo đang được chọn -> Hiển thị màn hình chi tiết bài báo đó
            NewsDetailScreen(news = activeDetailNews!!) {
                activeDetailNews = null // Khi bấm Back, reset về null để quay lại danh sách
            }
        } else {
            // Nếu chưa chọn bài viết nào -> Hiển thị danh sách tổng quát
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "TIN TỨC HOT",
                    color = NeonGreen,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(newsList) { item ->
                        // Thêm sự kiện clickable truyền bài viết được click lên State
                        NewsItemRow(news = item, onClick = { activeDetailNews = item })
                    }
                }
            }
        }
    }

    @Composable
    fun NewsItemRow(news: FootballNews, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { onClick() }, // 🌟 THÊM SỰ KIỆN CLICK VÀO CARD
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
            border = BorderStroke(1.dp, Color(0xFF222222))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color(0xFF1F1F1F), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📰", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = news.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = news.source,
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " • ${news.time}",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NewsDetailScreen(news: FootballNews, onBack: () -> Unit) { // 🌟 SỬA KIỂU DỮ LIỆU SANG FootballNews
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            // Nút quay lại danh sách tin tức
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBack() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quay lại tin tức", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Nhãn chuyên mục bài viết lấy từ thuộc tính mới thêm
                    Surface(
                        color = NeonGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, NeonGreen)
                    ) {
                        Text(
                            text = news.category.uppercase(),
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = news.title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Tác giả: ${news.author}", color = Color.LightGray, fontSize = 12.sp)
                        Text(text = news.time, color = Color.Gray, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFF222222))
                }

                item {
                    Text(
                        text = news.content,
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }