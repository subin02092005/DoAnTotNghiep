package com.example.qlbongda



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.MatchEvent
import com.example.qlbongda.data.model.PlayerInfo

// Định nghĩa cấu trúc dữ liệu chi tiết cho trận đấu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(match: FullMatchDetail, onBack: () -> Unit) {
    // Quản lý 3 trạng thái của thẻ Tag nội dung (0: Diễn biến, 1: Sơ đồ/Đội hình, 2: Thống kê)
    var selectedSubTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi Tiết Trận Đấu", color = NeonGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 📊 [PHẦN 1]: BẢNG TỈ SỐ HOẶC GIỜ GIẤC THEO TRẠNG THÁI TRẬN ĐẤU
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, NeonGreen),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = match.teamA, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                        // Kiểm tra trạng thái trận đấu để hiển thị giao diện phù hợp
                        if (match.isStarted) {
                            // Nếu ĐÃ DIỄN RA -> Hiện tỉ số đậm chất thể thao
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = match.scoreA, color = NeonGreen, fontSize = 36.sp, fontWeight = FontWeight.Black)
                                Text(text = " - ", color = Color.Gray, fontSize = 28.sp)
                                Text(text = match.scoreB, color = NeonGreen, fontSize = 36.sp, fontWeight = FontWeight.Black)
                            }
                        } else {
                            // Nếu CHƯA DIỄN RA -> Hiện ngày giờ đếm ngược kèm chữ "CHƯA ĐÁ"
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = match.time, color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Text(text = match.date, color = Color.LightGray, fontSize = 14.sp)
                                Text(text = "CHƯA DIỄN RA", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
                            }
                        }

                        Text(text = match.teamB, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Sân vận động: ${match.stadium}", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🎯 [PHẦN 2]: THANH 3 TAGS ĐIỀU HƯỚNG CON
            TabRow(
                selectedTabIndex = selectedSubTab,
                containerColor = Color(0xFF121212),
                contentColor = NeonGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]), color = NeonGreen)
                }
            ) {
                Tab(selected = selectedSubTab == 0, onClick = { selectedSubTab = 0 }, text = { Text("Diễn biến", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if(selectedSubTab==0) NeonGreen else Color.Gray) })
                Tab(selected = selectedSubTab == 1, onClick = { selectedSubTab = 1 }, text = { Text("Đội hình", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if(selectedSubTab==1) NeonGreen else Color.Gray) })
                Tab(selected = selectedSubTab == 2, onClick = { selectedSubTab = 2 }, text = { Text("Thống kê", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if(selectedSubTab==2) NeonGreen else Color.Gray) })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📦 [PHẦN 3]: NỘI DUNG CHI TIẾT PHỤ THUỘC VÀO TAG ĐƯỢC CHỌN
            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                when (selectedSubTab) {
                    0 -> TagDiEnBien(match)
                    1 -> TagDoiHinh(match)
                    2 -> TagThongKe(match)
                }
            }
        }
    }
}

// ======================= THÀNH PHẦN CON CHO TAG 1 =======================
@Composable
fun TagDiEnBien(match: FullMatchDetail) {
    if (!match.isStarted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Trận đấu chưa diễn ra, chưa có dữ liệu diễn biến sự kiện.", color = Color.Gray, textAlign = TextAlign.Center)
        }
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(match.events) { event ->
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF0F0F0F), RoundedCornerShape(8.dp)).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${event.minute}'", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(45.dp))
                    Column {
                        Text(text = event.playerName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = "${event.type} (${event.team})", color = if(event.type.contains("Đỏ")) Color.Red else if(event.type.contains("Vàng")) Color.Yellow else Color.LightGray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// ======================= THÀNH PHẦN CON CHO TAG 2 =======================
@Composable
fun TagDoiHinh(match: FullMatchDetail) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("ĐỘI HÌNH RA SÂN CHÍNH THỨC", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold) }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Đội hình chính Đội A
                Column(modifier = Modifier.weight(1f)) {
                    Text(match.teamA, color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    match.lineupA.forEach { player ->
                        Text("${player.number}. ${player.name} (${player.position})", color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
                // Đội hình chính Đội B
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(match.teamB, color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    match.lineupB.forEach { player ->
                        Text("${player.name} .${player.number} (${player.position})", color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp), textAlign = TextAlign.End)
                    }
                }
            }
        }

        item { HorizontalDivider(color = Color.DarkGray) }
        item { Text("DANH SÁCH DỰ BỊ", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold) }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Dự bị Đội A
                Column(modifier = Modifier.weight(1f)) {
                    match.subsA.forEach { player ->
                        Text("${player.number}. ${player.name}", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
                // Dự bị Đội B
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    match.subsB.forEach { player ->
                        Text("${player.name} .${player.number}", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp), textAlign = TextAlign.End)
                    }
                }
            }
        }
    }
}

// ======================= THÀNH PHẦN CON CHO TAG 3 =======================
@Composable
fun TagThongKe(match: FullMatchDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
        Text("BIỂU ĐỒ THỐNG KÊ TRẬN ĐẤU", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        // Dòng Kiểm soát bóng
        ThongKeRow(title = "Kiểm soát bóng", valA = match.PossessionA, valB = match.PossessionB)
        // Dòng Sút khung thành
        ThongKeRow(title = "Tổng cú sút", valA = match.ShotsA, valB = match.ShotsB)

        HorizontalDivider(color = Color.DarkGray)

        // Mục danh hiệu cầu thủ xuất sắc nhất
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            border = BorderStroke(1.dp, NeonGreen)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CẦU THỦ XUẤT SẮC NHẤT (MVP)", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = match.mvp, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ThongKeRow(title: String, valA: String, valB: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(valA, color = Color.White, fontSize = 14.dp.value.sp, fontWeight = FontWeight.Bold)
            Text(title, color = Color.Gray, fontSize = 14.dp.value.sp)
            Text(valB, color = Color.White, fontSize = 14.dp.value.sp, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            progress = {
                val a = valA.replace("%", "").toFloatOrNull() ?: 0f
                val b = valB.replace("%", "").toFloatOrNull() ?: 0f
                if(a+b == 0f) 0.5f else a / (a + b)
            },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp),
            color = NeonGreen,
            trackColor = Color.Red,
        )
    }
}
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Trận đã diễn ra")
@Composable
fun MatchDetailScreenStartedPreview() {
    // Tạo dữ liệu giả lập cho trận đấu đã diễn ra
    val dummyMatchStarted = FullMatchDetail(
        id = 1,
        teamA = "Arsenal",
        teamB = "Man City",
        isStarted = true,
        scoreA = "2",
        scoreB = "1",
        time = "22:00",
        date = "07/06/2026",
        stadium = "Emirates Stadium",
        events = listOf(
            MatchEvent("15", "Arsenal", "Ghi bàn", "Bukayo Saka"),
            MatchEvent("42", "Man City", "Thẻ Vàng", "Ruben Dias"),
            MatchEvent("55", "Man City", "Ghi bàn", "Erling Haaland"),
            MatchEvent("89", "Arsenal", "Ghi bàn", "Martin Odegaard")
        ),
        lineupA = listOf(
            PlayerInfo("22", "Raya", "GK"),
            PlayerInfo("2", "Saliba", "DF"),
            PlayerInfo("6", "Gabriel", "DF"),
            PlayerInfo("4", "White", "DF"),
            PlayerInfo("41", "Rice", "MF"),
            PlayerInfo("8", "Odegaard", "MF"),
            PlayerInfo("7", "Saka", "FW")
        ),
        lineupB = listOf(
            PlayerInfo("31", "Ederson", "GK"),
            PlayerInfo("3", "Dias", "DF"),
            PlayerInfo("25", "Akanji", "DF"),
            PlayerInfo("16", "Rodri", "MF"),
            PlayerInfo("17", "De Bruyne", "MF"),
            PlayerInfo("47", "Foden", "FW"),
            PlayerInfo("9", "Haaland", "FW")
        ),
        subsA = listOf(PlayerInfo("29", "Havertz", "FW"), PlayerInfo("11", "Martinelli", "FW")),
        subsB = listOf(PlayerInfo("10", "Grealish", "FW"), PlayerInfo("19", "Alvarez", "FW")),
        PossessionA = "45%", PossessionB = "55%",
        ShotsA = "12", ShotsB = "14",
        mvp = "Martin Odegaard (Arsenal)"
    )

    // Gọi màn hình hiển thị dữ liệu test
    MatchDetailScreen(match = dummyMatchStarted, onBack = {})
}

