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
import com.example.qlbongda.data.model.TournamentPhase
@Composable
fun HomeTabContent(
    phaseList: List<TournamentPhase>, // 🌟 Nhận danh sách vòng đấu động từ API
    standingList: List<StandingRow>,  // Giữ nguyên để tránh lỗi đối chiếu ở các file khác
    teamName: String,
    coachName: String,
    leaderName: String,
    playerList: List<PlayerInfo>,
    onNavigateToStandingDetail: () -> Unit,
    onTeamClick: (String) -> Unit
) {
    // Tự chọn vòng đấu đầu tiên từ API làm mặc định khi mở app
    var selectedPhase by remember(phaseList) { mutableStateOf(phaseList.firstOrNull()) }
    var selectedGroupName by remember { mutableStateOf("BẢNG A") }

    // Bản đồ Mock dữ liệu nhóm bảng theo thiết kế cũ
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
                StandingRow(2, "Mỹ", 3, "+2", 5),
                StandingRow(3, "Iran", 3, "-3", 3),
                StandingRow(4, "Xứ Wales", 3, "-5", 1)
            )
        )
    }

    val currentGroupStandings = groupDataMap[selectedGroupName] ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // 🌟 1. THANH TABS CHỌN VÒNG ĐẤU ĐỘNG (ĐỒNG BỘ THEO ADMIN CHỌN)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(Color(0xFF121212), RoundedCornerShape(8.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(phaseList) { phase ->
                val isSelected = selectedPhase?.id == phase.id
                Button(
                    onClick = { selectedPhase = phase },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonGreen else Color.Transparent
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = phase.name, // "Vòng Bảng", "Tứ Kết", "Bán Kết", "Chung Kết"...
                        color = if (isSelected) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 🌟 2. PHÂN CHIA GIAO DIỆN THEO THỂ THỨC (FORMAT) ĐƯỢC CHỌN
        if (selectedPhase?.format == "knockout") {

            // 👉 NẾU ADMIN CÀI ĐẶT LÀ KNOCKOUT: Gọi trực tiếp màn hình cây nhánh đấu cúp của bạn
            Box(modifier = Modifier.fillMaxSize()) {
                KnockoutBracketScreen()
            }

        } else {

            // 👉 NẾU ADMIN CÀI ĐẶT LÀ ROUND_ROBIN (VÒNG TRÒN / BXH): Hiện bảng xếp hạng tính điểm
            // Thanh chọn danh sách các Bảng đấu (A, B, C...)
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

            // Danh sách hiển thị Bảng Xếp Hạng chi tiết
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Column {
                        // Tên tiêu đề đổi động theo Vòng đấu của Admin chọn
                        Text(
                            text = "${selectedPhase?.name?.uppercase()} - $selectedGroupName",
                            color = NeonGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Mỗi bảng đấu lấy 2 đội dẫn đầu đi tiếp", color = Color.LightGray, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Header tiêu đề cột dữ liệu của bảng xếp hạng
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(1.dp, Color(0xFF222222))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Hạng", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(40.dp))
                            Text(text = "Đội bóng", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1F))
                            Row(horizontalArrangement = Arrangement.End) {
                                Text(text = "ST", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                Text(text = "HS", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                Text(text = "Điểm", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                // Đổ các hàng dữ liệu đội bóng ra giao diện công khai
                items(currentGroupStandings) { row ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToStandingDetail() },
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(0.5.dp, Color(0xFF222222))
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = row.rank.toString(),
                                    color = if (row.rank <= 2) NeonGreen else Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(40.dp)
                                )
                                Text(
                                    text = row.teamName,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f).clickable { onTeamClick(row.teamName) }
                                )
                                Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = row.played.toString(), color = Color.White, fontSize = 14.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                    Text(text = row.goalDifference, color = Color.LightGray, fontSize = 14.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                    Text(text = row.points.toString(), color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                                }
                            }
                            HorizontalDivider(color = Color(0xFF1C1C1C), thickness = 0.5.dp)
                        }
                    }
                }

                // Đóng góc chân bảng xếp hạng bo tròn
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