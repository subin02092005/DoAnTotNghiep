package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.data.api.HomeViewModel
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils

@Composable
fun ScheduleTabContent(
    viewModel: HomeViewModel,
    onMatchClick: (FullMatchDetail) -> Unit
) {
    val matchList by viewModel.matchList.collectAsStateWithLifecycle()
    var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60000)
            tick++
        }
    }

    /* Redundant call
    LaunchedEffect(Unit) {
        viewModel.loadFeaturedMatches()
    }
    */

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "LỊCH THI ĐẤU",
            color = NeonGreen,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 60.dp)
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(matchList) { match ->
                val isFeatured = match.isHot

                Card(
                    modifier = Modifier.fillMaxWidth().height(160.dp).clickable { onMatchClick(match) },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = if (isFeatured) 2.dp else 1.dp,
                        color = if (isFeatured) Color.Red else NeonGreen
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Tên đội nhà
                            Text(
                                text = match.teamA,
                                color = if (isFeatured) Color(0xFFFFD700) else  NeonGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )

                            // Khung tỉ số / Trạng thái ở giữa
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    when {
                                        match.status == "finished" -> {
                                            Text("FT", color = Color.Red, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            Text("${match.scoreA} - ${match.scoreB}", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                        }
                                        match.status == "ongoing" -> {
                                            val timeDisplay = getMinuteElapsed(match.time)

                                            // Tách làm 2 dòng để hiển thị trên/dưới
                                            val (statusLabel, timeLabel) = when (timeDisplay) {
                                                "HT" -> "LIVE" to "HT"
                                                "FT" -> "FT" to "Đang cập nhật tỉ số" // Trận đấu quá thời gian, ẩn chữ LIVE hiện chữ FT
                                                else -> "LIVE" to "$timeDisplay'" // Đang đá bình thường
                                            }

                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                // Dòng 1: LIVE hoặc FT
                                                Text(
                                                    text = statusLabel,
                                                    color = Color.Red,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Spacer(modifier = Modifier.height(2.dp)) // Khoảng cách nhỏ giữa 2 dòng

                                                // Dòng 2: Số phút (ví dụ: 45') hoặc trạng thái (HT / Đang cập nhật)
                                                Text(
                                                    text = timeLabel,
                                                    color = if (timeDisplay == "FT") Color.LightGray else Color.Red, // Nếu đang cập nhật thì cho màu xám dịu xuống
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        else -> {
                                            Text(DateUtils.formatTime(match.time), color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            Text(DateUtils.formatDate(match.date), color = Color.LightGray, fontSize = 9.sp)
                                        }
                                    }
                                    match.stadium?.let {
                                        Text(it, color = Color.Gray, fontSize = 8.sp, maxLines = 1)
                                    }
                                }
                            }

                            // Tên đội khách
                            Text(
                                text = match.teamB,
                                color = if (isFeatured) Color(0xFFFFD700) else NeonGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }

                        if (isFeatured) {
                            Text(
                                text = "⭐",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getMinuteElapsed(actualStartTime: String?): String {
    if (actualStartTime == null) return ""
    return DateUtils.calculateMinutes(actualStartTime)
}