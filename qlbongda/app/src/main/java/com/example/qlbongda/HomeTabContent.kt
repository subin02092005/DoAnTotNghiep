package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.StandingRow
import androidx.compose.foundation.lazy.LazyRow


@Composable
fun HomeTabContent(
    standingList: List<StandingRow>, // Giữ nguyên để tránh lỗi đối chiếu ở các file khác
    teamName: String,
    coachName: String,
    leaderName: String,
    playerList: List<PlayerInfo>,
    onNavigateToStandingDetail: () -> Unit,
    onTeamClick: (String) -> Unit
) {
    // 🌟 1. Biến quản lý chế độ chơi lớn: false = Hiện BXH, true = Hiện đấu Cúp
    var isCupMode by remember { mutableStateOf(false) }

    // 🌟 2. Biến quản lý tên Bảng đang được chọn xem (Mặc định hiển thị Bảng A)
    var selectedGroupName by remember { mutableStateOf("BẢNG A") }

    // 🌟 3. DỮ LIỆU MOCK THEO THỂ THỨC WORLD CUP (4 đội/bảng)
    val groupDataMap = remember {
        mapOf(
            "BẢNG A" to listOf(
                StandingRow(1, "Pháp", 3, "+5", 9),
                StandingRow(2, "Đan Mạch", 3, "+1", 4),
                StandingRow(3, "Tunisia", 3, "-2", 2),
                StandingRow(4, "Úc", 3, "-4", 1)
            ),
            "BẢNG B" to listOf(
                StandingRow(1, "Anh", 3, "+6", 7),
                StandingRow(2, "Mỹ", 3, "+1", 5),
                StandingRow(3, "Iran", 3, "-3", 3),
                StandingRow(4, "Xứ Wales", 3, "-4", 1)
            ),
            "BẢNG C" to listOf(
                StandingRow(1, "Argentina", 3, "+3", 6),
                StandingRow(2, "Ba Lan", 3, "0", 4),
                StandingRow(3, "Mexico", 3, "-1", 4),
                StandingRow(4, "Ả Rập Xê Út", 3, "-2", 3)
            ),
            "BẢNG D" to listOf(
                StandingRow(1, "Nhật Bản", 3, "+1", 6),
                StandingRow(2, "Tây Ban Nha", 3, "+6", 4),
                StandingRow(3, "Đức", 3, "+1", 4),
                StandingRow(4, "Costa Rica", 3, "-8", 3)
            )
        )
    }

    // Lấy ra danh sách 4 đội tương ứng với bảng đang được người dùng bấm chọn
    val currentGroupStandings = groupDataMap[selectedGroupName] ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {

        // ---- THANH CHỌN CHẾ ĐỘ ĐẤU CHÍNH ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(Color(0xFF121212), RoundedCornerShape(8.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { isCupMode = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = if (!isCupMode) NeonGreen else Color.Transparent),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Vòng Bảng", color = if (!isCupMode) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
                onClick = { isCupMode = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = if (isCupMode) NeonGreen else Color.Transparent),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Vòng Đo ván (Cup)", color = if (isCupMode) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // ---- HIỂN THỊ NỘI DUNG ----
        if (isCupMode) {
            Box(modifier = Modifier.fillMaxSize()) {
                KnockoutBracketScreen()
            }
        } else {
            // ---- CHẾ ĐỘ XEM CÁC BẢNG ĐẤU ----

            // 🌟 THANH CUỘN NGANG CHỌN BẢNG (Bảng A, B, C, D)
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(groupDataMap.keys.toList()) { groupName ->
                    val isSelected = groupName == selectedGroupName
                    Card(
                        modifier = Modifier.clickable { selectedGroupName = groupName },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NeonGreen else Color(0xFF1A1A1A)
                        ),
                        border = BorderStroke(1.dp, if (isSelected) NeonGreen else Color(0xFF333333))
                    ) {
                        Text(
                            text = groupName,
                            color = if (isSelected) Color.Black else Color.LightGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // BẢNG XẾP HẠNG CHI TIẾT CỦA BẢNG ĐƯỢC CHỌN
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(text = "BẢNG XẾP HẠNG $selectedGroupName", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Mỗi bảng đấu lấy 2 đội dẫn đầu đi tiếp", color = Color.LightGray, fontSize = 11.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Tiêu đề các cột
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(1.dp, Color(0xFF222222))
                    ) {
                        Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("STT", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                                Text("ĐỘI BÓNG", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                Text("TRẬN", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                Text("HS", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                Text("ĐIỂM", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                            }
                            HorizontalDivider(color = Color(0xFF222222), thickness = 1.dp)
                        }
                    }
                }

                // Danh sách các đội trong bảng hiện tại
                items(currentGroupStandings) { row ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(1.dp, Color(0xFF222222))
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = row.rank.toString(),
                                    color = if (row.rank <= 2) NeonGreen else Color.White, // Nhất nhì bảng tô xanh lá để biểu thị đi tiếp
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.width(40.dp).clickable { onNavigateToStandingDetail() },
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = row.teamName,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f).clickable { onTeamClick(row.teamName) }
                                )

                                Row(modifier = Modifier.wrapContentWidth().clickable { onNavigateToStandingDetail() }, verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = row.played.toString(), color = Color.White, fontSize = 14.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                    Text(text = row.goalDifference, color = Color.LightGray, fontSize = 14.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                    Text(text = row.points.toString(), color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                }
                            }
                            HorizontalDivider(color = Color(0xFF1C1C1C), thickness = 0.5.dp)
                        }
                    }
                }

                // Thẻ bo góc dưới
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(1.dp, Color(0xFF222222))
                    ) {}
                }
            }
        }
    }
}

// Hàm bọc bổ trợ để gọi màn hình nhánh đấu cúp mà không sợ bị lệch giao diện
@Composable
fun KnoutBracketScreenWrapper() {
    KnockoutBracketScreen()
}