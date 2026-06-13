package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // 🌟 Cần import cái này để sử dụng items(playerList)
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.qlbongda.data.model.PlayerInfo
// Giả định màu NeonGreen được định nghĩa ở đâu đó trong project, ví dụ:


val DarkBackground = Color(0xFF0A0A0A)
val CardBackground = Color(0xFF121212)

@OptIn(ExperimentalMaterial3Api::class)// 🌟 1. Sửa lỗi viết sai từ khóa 'class' thành tên annotation chuẩn của Material 3
@Composable
fun TeamDetailScreen(
    teamName: String,
    coachName: String,
    captainName: String,
    playerList: List<PlayerInfo>,
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = NeonGreen)
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
                            text = teamName.take(2).uppercase(),
                            color = NeonGreen,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = teamName.uppercase(),
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
                        border = BorderStroke(1.dp, Color.Yellow)
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
                                text = if (coachName.trim().isEmpty()) "Chưa cập nhật" else coachName,
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
                        border = BorderStroke(1.dp, NeonGreen)
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
                                text = if (captainName.trim().isEmpty()) "Chưa cập nhật" else captainName,
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
                    text = "DANH SÁCH CẦU THỦ (${playerList.size})",
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Nếu đội bóng chưa có cầu thủ nào đăng ký
            if (playerList.isEmpty()) {
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
            // 🌟 2. Sửa lỗi ép kiểu: Sử dụng extension function `items(playerList)` đúng chuẩn
            items(playerList) { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBackground, RoundedCornerShape(8.dp))
                        .border(
                            border = if (player.name == captainName) BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)) else BorderStroke(0.dp, Color.Transparent),
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

                            if (player.name == captainName) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "C",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier // 🌟 3. SỬA LỖI CHÍNH: Thêm từ khóa Modifier gốc vào đây (Bạn viết thiếu chữ Modifier. khiến code bị crash)
                                        .background(NeonGreen, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(text = "Vị trí: ${player.position}", color = Color.LightGray, fontSize = 12.sp)
                    }

                    // Hiển thị tag vị trí bên góc phải (GK, DF, MF, FW)
                    Text(
                        text = player.position,
                        color = when(player.position) {
                            "GK" -> Color.Yellow
                            "DF" -> Color.Cyan
                            "MF" -> Color.Green
                            else -> Color.Red
                        }.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}