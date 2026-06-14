package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.StandingItem // 🌟 ĐÃ THÊM: Import Model chuẩn nhận từ API MySQL của bạn

val DarkBackground = Color(0xFF0A0A0A)
val CardBackground = Color(0xFF121212)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    team: StandingItem, // 🌟 ĐÃ SỬA: Nhận trực tiếp gói dữ liệu động tải từ MySQL về thay vì truyền rời rạc các biến tĩnh
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("CHI TIẾT ĐỘI BÓNG", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- TÊN ĐỘI BÓNG KHỔ LỚN ---
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(NeonGreen.copy(alpha = 0.1f), CircleShape)
                            .border(BorderStroke(2.dp, NeonGreen), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = team.teamName.take(2).uppercase(),
                            color = NeonGreen,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = team.teamName.uppercase(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "MÙA GIẢI 2026",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // --- KHU VỰC THÔNG TIN BAN QUẢN LÝ / CHỦ CHỐT (HLV & ĐỘI TRƯỞNG) ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Thẻ hiển thị Huấn Luyện Viên
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, Color(0xFF333333))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "HLV",
                                tint = Color.Yellow,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("HUẤN LUYỆN VIÊN", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (team.coachName.trim().isEmpty()) "Chưa cập nhật" else team.coachName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }

                    // Thẻ hiển thị Đội Trưởng (Captain)
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, Color(0xFF333333))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Đội trưởng",
                                tint = NeonGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("ĐỘI TRƯỞNG (C)", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (team.captainName.trim().isEmpty()) "Chưa cập nhật" else team.captainName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // --- TIÊU ĐỀ DANH SÁCH THÀNH VIÊN ---
            item {
                Text(
                    text = "DANH SÁCH CẦU THỦ (${team.players.size})",
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Nếu đội bóng chưa có cầu thủ nào đăng ký từ Database
            if (team.players.isEmpty()) {
                item {
                    Text(
                        text = "Danh sách cầu thủ đang được cập nhật...",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- DANH SÁCH CẦU THỦ CHI TIẾT ---
            items(team.players) { player ->
                val isCaptain = player.name == team.captainName
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBackground, RoundedCornerShape(8.dp))
                        .border(
                            border = if (isCaptain) BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)) else BorderStroke(1.dp, Color(0xFF1F1F1F)),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Số áo cầu thủ
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(NeonGreen, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = player.number, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Tên và vị trí
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = player.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

                            if (isCaptain) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "C",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(NeonGreen, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(text = "Vị trí: ${player.position}", color = Color.Gray, fontSize = 12.sp)
                    }

                    // Hiển thị tag vị trí bên góc phải màu sắc sinh động (Tiền đạo - FW, Tiền vệ - MF, v.v)
                    Text(
                        text = when(player.position) {
                            "Tiền đạo", "FW" -> "FW"
                            "Tiền vệ", "MF" -> "MF"
                            "Hậu vệ", "DF" -> "DF"
                            else -> "GK"
                        },
                        color = when(player.position) {
                            "Tiền đạo", "FW" -> Color(0xFFFF5252)
                            "Tiền vệ", "MF" -> Color(0xFF69F0AE)
                            "Hậu vệ", "DF" -> Color(0xFF40C4FF)
                            else -> Color(0xFFFFD740)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}