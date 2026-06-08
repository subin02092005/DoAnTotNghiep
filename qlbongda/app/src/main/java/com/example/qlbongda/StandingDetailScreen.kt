package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.DetailedStanding

// Màu xanh Neon chủ đạo đồng bộ hệ thống


// Định nghĩa cấu trúc dữ liệu cho Bảng xếp hạng chi tiết


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingDetailScreen(
    standings: List<DetailedStanding>,
    onBack: () -> Unit
) {
    // Dùng chung DUY NHẤT 1 trạng thái cuộn ngang để Tiêu đề chữ và Số bên dưới khóa chặt, đi chung với nhau
    val sharedScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "BẢNG XẾP HẠNG CHI TIẾT",
                        color = NeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
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
                .padding(horizontal = 12.dp)
        ) {
            // Khối chú thích tiêu đề giải đấu nhỏ phía trên bảng
            Text(
                text = "Giải Bóng Đá Vô Địch Quốc Gia 2026",
                color = Color.LightGray,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            // ---- PHẦN 1: TIÊU ĐỀ CỘT CỦA BẢNG (HEADER) ----
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                border = BorderStroke(1.dp, Color(0xFF222222))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // [GIỮ NGUYÊN CỐ ĐỊNH] - Tiêu đề cột bên trái ngoài cùng
                    Text("STT", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                    Text("ĐỘI BÓNG", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))

                    // [CUỘN NGANG VỚI SHARED STATE] - Các nhãn chữ Trận, Thắng, Hòa, Thua...
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(sharedScrollState),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HeaderCell("TRẬN")
                        HeaderCell("THẮNG")
                        HeaderCell("HÒA")
                        HeaderCell("THUA")
                        HeaderCell("BT")
                        HeaderCell("BB")
                        HeaderCell("HS")
                        HeaderCell("ĐIỂM", isHighlight = true)
                        HeaderCell("5 TRẬN GẦN NHẤT", width = 130.dp)
                    }
                }
            }

            // ---- PHẦN 2: DANH SÁCH DÒNG DỮ LIỆU ĐỘI BÓNG (LAZY COLUMN) ----
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(standings) { row ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(1.dp, Color(0xFF1F1F1F))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // [GIỮ NGUYÊN CỐ ĐỊNH] - Số thứ tự STT kèm màu sắc huy chương Vàng, Bạc, Đồng
                            Text(
                                text = row.rank.toString(),
                                color = when (row.rank) {
                                    1 -> Color.Yellow         // Hạng 1: Vàng
                                    2 -> Color(0xFFB0BEC5)    // Hạng 2: Bạc
                                    3 -> Color(0xFFCA8A04)    // Hạng 3: Đồng
                                    else -> Color.White       // Còn lại: Trắng
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.width(40.dp),
                                textAlign = TextAlign.Center
                            )

                            // [GIỮ NGUYÊN CỐ ĐỊNH] - Tên đội bóng đứng yên bên trái
                            Text(
                                text = row.teamName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(120.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // [CUỘN NGANG VỚI SHARED STATE] - Các con số chỉ số kỹ thuật chạy đi chung với tiêu đề chữ phía trên
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .horizontalScroll(sharedScrollState),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DataCell(row.played.toString())
                                DataCell(row.won.toString())
                                DataCell(row.drawn.toString())
                                DataCell(row.lost.toString())
                                DataCell(row.goalsFor.toString())
                                DataCell(row.goalsAgainst.toString())
                                DataCell(row.goalDifference, color = if(row.goalDifference.startsWith("+")) NeonGreen else Color.Red)
                                DataCell(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold)

                                // Chuỗi phong độ 5 trận gần nhất ở đuôi hàng dữ liệu cùng cuộn đi chung
                                Row(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .padding(start = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    row.form.forEach { outcome ->
                                        val (bgColor, textColor) = when (outcome) {
                                            "W" -> Pair(Color(0xFF00C853), Color.Black)
                                            "D" -> Pair(Color.DarkGray, Color.White)
                                            else -> Pair(Color.Red, Color.White)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .background(bgColor, RoundedCornerShape(4.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = outcome,
                                                color = textColor,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ---- PHẦN 3: ĐÁY BẢNG BO TRÒN THẨM MỸ ----
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(10.dp),
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        border = BorderStroke(1.dp, Color(0xFF222222))
                    ) {}
                }
            }
        }
    }
}

// Hàm bổ trợ vẽ ô tiêu đề chữ xám nhỏ gọn
@Composable
fun HeaderCell(text: String, width: androidx.compose.ui.unit.Dp = 55.dp, isHighlight: Boolean = false) {
    Text(
        text = text,
        color = if (isHighlight) NeonGreen else Color.Gray,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(width),
        textAlign = TextAlign.Center
    )
}

// Hàm bổ trợ vẽ ô dữ liệu số rõ nét
@Composable
fun DataCell(
    text: String,
    width: androidx.compose.ui.unit.Dp = 55.dp,
    color: Color = Color.White,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        color = color,
        fontSize = 13.sp,
        fontWeight = fontWeight,
        modifier = Modifier.width(width),
        textAlign = TextAlign.Center
    )
}