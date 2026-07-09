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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlbongda.data.api.HomeViewModel
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils

@Composable
fun ScheduleTabContent(
    viewModel: HomeViewModel, // Nhận ViewModel từ ngoài vào
    matchList: List<FullMatchDetail>,
    onMatchClick: (FullMatchDetail) -> Unit
) {var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while(true) {
            kotlinx.coroutines.delay(60000) // Đợi 1 phút
            tick++ // Cập nhật state để Trigger Recompose
        }
    }
    LaunchedEffect(Unit) {
        viewModel.loadMatches() // Hoặc hàm load dữ liệu của bạn
    }
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
                .background(Color.White))
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
                    modifier = Modifier.fillMaxWidth().clickable { onMatchClick(match) },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = if (isFeatured) 2.dp else 1.dp,
                        color = if (isFeatured) Color.Red else NeonGreen
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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

                            Text(
                                text = match.teamB,
                                color = if (isFeatured) Color(0xFFFFD700) else Color.White,
                                fontSize = 15.sp,
                                fontWeight = if (isFeatured) FontWeight.Black else FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFF222222))
                            Spacer(modifier = Modifier.height(6.dp))

                            when {
                                // 1. TRẬN ĐANG DIỄN RA
                                match.status == "ongoing" -> {
                                    Text(
                                        text = "LIVE",
                                        color = Color.Red,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${getMinuteElapsed(match.time)}'",
                                        color = Color.Red,fontSize = 12.sp)
                                    Text(
                                        text = "${match.scoreA} - ${match.scoreB}",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                // 2. TRẬN ĐÃ KẾT THÚC
                                match.status == "finished" -> {
                                    Text(
                                        text = "${match.scoreA} - ${match.scoreB}",
                                        color = NeonGreen,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = DateUtils.formatDate(match.date),
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                }

                                // 3. TRẬN CHƯA ĐÁ (PENDING)
                                else -> {
                                    Text(
                                        text = DateUtils.formatTime(match.time),
                                        color = NeonGreen,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = DateUtils.formatDate(match.date),
                                        color = Color.LightGray,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                        if (isFeatured) {
                            Text(
                                text = "⭐",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.TopEnd).padding(top = 10.dp, end = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewScheduleTabContent() {
//    val mockMatchList = listOf(
//        FullMatchDetail(
//            id = 1,
//            teamA = "Arsenal",
//            teamB = "Man City",status="",
//            isStarted = true,
//            scoreA = 1,
//            scoreB = 2,
//            time = "2026-06-28T22:00:00.000Z",
//            date = "2026-06-28T22:00:00.000Z",
//            stadium = "Emirates Stadium",
//            events = emptyList(),
//            lineupA = emptyList(),
//            lineupB = emptyList(),
//            subsA = emptyList(),
//            subsB = emptyList(),
//            PossessionA = "50%",
//            PossessionB = "50%",
//            ShotsA = "10",
//            ShotsB = "12",
//            mvp = "",
//            isHot = true
//        ),
//        FullMatchDetail(
//            id = 2,
//            teamA = "Liverpool",
//            teamB = "Chelsea",
//            status="",
//            isStarted = false,
//            scoreA = 0,
//            scoreB = 0,
//            time = "2026-06-29T19:30:00.000Z",
//            date = "2026-06-29T19:30:00.000Z",
//            stadium = "Anfield",
//            events = emptyList(),
//            lineupA = emptyList(),
//            lineupB = emptyList(),
//            subsA = emptyList(),
//            subsB = emptyList(),
//            PossessionA = "0%",
//            PossessionB = "0%",
//            ShotsA = "0",
//            ShotsB = "0",
//            mvp = "",
//            isHot = false
//        )
//    )
//
//    ScheduleTabContent(
//
//        matchList = mockMatchList,
//        onMatchClick = {}
//    )
//}
@Composable
fun getMinuteElapsed(actualStartTime: String?): String {
    if (actualStartTime == null) return ""
    // Sử dụng DateUtils để tính khoảng cách giữa thời gian hiện tại và actualStartTime
    // Trả về dạng: "15'"
    return DateUtils.calculateMinutes(actualStartTime)
}