package com.example.qlbongda

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

@Composable
fun HomeTabContent(
    phaseList: List<TournamentPhase>,
    standings: List<GroupStanding>, // 🌟 Dữ liệu nhận từ MainActivity
    onNavigateToStandingDetail: () -> Unit,
    onTeamClick: (String) -> Unit
) {
    // Tự chọn vòng đấu đầu tiên làm mặc định
    var selectedPhase by remember(phaseList) { mutableStateOf(phaseList.firstOrNull()) }

    // Lấy danh sách đội từ bảng đầu tiên trong danh sách
    val teamList = standings.firstOrNull()?.standings ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // 1. THANH CHỌN VÒNG ĐẤU
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
                        text = phase.name,
                        color = if (isSelected) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 2. PHÂN CHIA GIAO DIỆN (KNOCKOUT VS ROUND ROBIN)
        if (selectedPhase?.format == "knockout") {
            Box(modifier = Modifier.fillMaxSize()) {
                // Gọi màn hình Knockout của bạn ở đây
                // KnockoutBracketScreen()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Text(
                        text = (selectedPhase?.name ?: "BẢNG XẾP HẠNG").uppercase(),
                        color = NeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // HEADER BẢNG
                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Hạng", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(40.dp))
                            Text("Đội bóng", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1F))
                            Text("ST", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                            Text("HS", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                            Text("Điểm", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                        }
                    }
                }

                // DANH SÁCH ĐỘI (Dữ liệu thật từ API)
                items(teamList) { row ->
                    // Logic màu sắc đặc biệt cho Top 3
                    val rankColor = when (row.rank) {
                        1, 2 -> NeonGreen
                        3 -> Color.White
                        else -> Color.Gray
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp).clickable { onNavigateToStandingDetail() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Row(modifier = Modifier.padding(14.dp, 12.dp), verticalAlignment = Alignment.CenterVertically) {
                            // Số thứ hạng với màu đặc biệt
                            Text(
                                text = row.rank.toString(),
                                color = rankColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(40.dp)
                            )

                            // Tên đội bóng thay đổi màu và chữ đậm hơn nếu thuộc Top 3
                            Text(
                                text = row.teamName,
                                color = if (row.rank <= 3) rankColor else Color.White,
                                fontWeight = if (row.rank <= 3) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.weight(1f).clickable { onTeamClick(row.teamName) }
                            )

                            Text(row.played.toString(), color = Color.White, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                            Text(row.goalDifference, color = Color.LightGray, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                            Text(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp), textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}