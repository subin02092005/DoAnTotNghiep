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

    // ==========================================
    // 1. ĐỊNH NGHĨA DATA MODELS VÀ MÀU SẮC CHỦ ĐẠO
    // ==========================================



    // ==========================================
    // 2. MÀN HÌNH CHÍNH (HOME SCREEN)
    // ==========================================

    @Composable
    fun HomeScreen(
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
        val matchList = remember {
            listOf(
                FullMatchDetail(
                    id = 1,
                    teamA = "Arsenal",
                    teamB = "Chelsea",
                    isStarted = true,
                    scoreA = "2",
                    scoreB = "1",
                    time = "19:00",
                    date = "06/06",
                    stadium = "Emirates Stadium",
                    events = listOf(
                        MatchEvent("23", "Arsenal", "Ghi bàn ⚽", "Bukayo Saka"),
                        MatchEvent("45", "Chelsea", "Thẻ vàng 🟨", "Enzo"),
                        MatchEvent("78", "Chelsea", "Ghi bàn ⚽", "Cole Palmer")
                    ),
                    lineupA = listOf(
                        PlayerInfo("1", "Raya", "GK"),
                        PlayerInfo("7", "Saka", "FW"),
                        PlayerInfo("8", "Odegaard", "MF")
                    ),
                    lineupB = listOf(
                        PlayerInfo("1", "Sanchez", "GK"),
                        PlayerInfo("20", "Palmer", "MF"),
                        PlayerInfo("8", "Enzo", "MF")
                    ),
                    subsA = listOf(PlayerInfo("14", "Nketiah", "FW")),
                    subsB = listOf(PlayerInfo("15", "Mudryk", "FW")),
                    PossessionA = "55%",
                    PossessionB = "45%",
                    ShotsA = "14",
                    ShotsB = "9",
                    mvp = "Bukayo Saka (Arsenal)"
                ),
                FullMatchDetail(
                    id = 2, teamA = "MU", teamB = "Man City", isStarted = false, scoreA = "0", scoreB = "0", time = "21:30", date = "07/06", stadium = "Old Trafford",
                    events = emptyList(), lineupA = emptyList(), lineupB = emptyList(), subsA = emptyList(), subsB = emptyList(),
                    PossessionA = "0%", PossessionB = "0%", ShotsA = "0", ShotsB = "0",
                    mvp = "Trận đấu chưa diễn ra"
                )
            )
        }

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
                                        Card(
                                            modifier = Modifier.fillMaxWidth().clickable { activeDetailMatch = match },
                                            shape = RoundedCornerShape(12.dp),
                                            border = BorderStroke(1.dp, NeonGreen),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(text = match.teamA, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                                Text(text = "VS", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(vertical = 2.dp))
                                                Text(text = match.teamB, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                                Spacer(modifier = Modifier.height(8.dp))
                                                HorizontalDivider(color = Color(0xFF222222))
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(text = match.time, color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                                Text(text = match.date, color = Color.LightGray, fontSize = 11.sp)
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
                            Column(
                                modifier = Modifier.fillMaxSize().padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(modifier = Modifier.size(90.dp).background(Color(0xFF121212), RoundedCornerShape(45.dp)).border(2.dp, NeonGreen, RoundedCornerShape(45.dp)), contentAlignment = Alignment.Center) {
                                    Text(text = "TTTQ", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Black)
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Ban Tổ Chức / Quản Trị Viên", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "admin@tttqshop.com", color = Color.Gray, fontSize = 13.sp)

                                Spacer(modifier = Modifier.height(30.dp))
                                HorizontalDivider(color = Color(0xFF222222))
                                Spacer(modifier = Modifier.height(20.dp))

                                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)), border = BorderStroke(0.5.dp, Color.DarkGray)) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Phiên bản ứng dụng", color = Color.LightGray, fontSize = 13.sp)
                                            Text("v2.6.0 (Mùa giải 2026)", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Trạng thái máy chủ", color = Color.LightGray, fontSize = 13.sp)
                                            Text("Ổn định 🟢", color = Color.White, fontSize = 13.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(40.dp))

                                Button(
                                    onClick = { onLogout() },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(text = "ĐĂNG XUẤT TÀI KHOẢN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
        // Khai báo danh sách dữ liệu tin tức ở đây
        val newsList = remember {
            listOf(
                FootballNews(1, "Arsenal giành chiến thắng kịch tính phút bù giờ cuối cùng", "Trận đấu nghẹt thở kết thúc với tỷ số 3-2 nghiêng về Pháo thủ.", "10 phút trước", "Sky Sports"),
                FootballNews(2, "Siêu máy tính dự đoán nhà vô địch Ngoại Hạng Anh 2026", "Tỷ lệ vô địch của Man City giảm mạnh sau chuỗi trận hòa.", "1 giờ trước", "BBC Sport"),
                FootballNews(3, "Chấn thương của ngôi sao MU nghiêm trọng hơn dự kiến", "Tiền đạo chủ lực phải nghỉ thi đấu ít nhất 3 tuần.", "3 giờ trước", "ESPN"),
                FootballNews(4, "Thị trường chuyển nhượng: Chelsea nhắm bom tấn 100 triệu Euro", "Đội bóng quyết tâm bổ sung một trung phong đẳng cấp.", "5 giờ trước", "Romano")
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "TIN TỨC HOT",
                color = Color(0xFF39FF14), // Màu xanh Neon của app
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Cuộn danh sách tin tức mượt mà
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(newsList) { item ->
                    // Gọi hàm NewsItemRow ở dưới để vẽ từng dòng
                    NewsItemRow(news = item)
                }
            }
        }
    }
    @Composable
    fun NewsItemRow(news: FootballNews) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
            border = BorderStroke(1.dp, Color(0xFF222222))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Khối giả lập ảnh thu nhỏ của bài báo
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

                // Nội dung tiêu đề tin tức
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
                            color = NeonGreen, // Dùng biến NeonGreen sẵn có trong dự án của bạn
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
