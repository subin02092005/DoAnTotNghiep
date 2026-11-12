package com.example.qlbongda.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils
import com.example.qlbongda.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleTabContent(
    viewModel: HomeViewModel,
    onMatchClick: (FullMatchDetail) -> Unit
) {
    val matchList by viewModel.allMatchesList.collectAsStateWithLifecycle()
    var tick by remember { mutableStateOf(0L) }
    val gridState = rememberLazyGridState()

    // 🌟 1. Biến trạng thái lưu từ khóa tìm kiếm
    var searchQuery by remember { mutableStateOf("") }

    // 🌟 2. Lọc danh sách trận đấu dựa theo tên đội nhà (teamA) hoặc đội khách (teamB)
    val filteredMatches = remember(searchQuery, matchList) {
        if (searchQuery.isBlank()) {
            matchList
        } else {
            matchList.filter { match ->
                match.teamA.contains(searchQuery, ignoreCase = true) ||
                        match.teamB.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60000)
            tick++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
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

        // 🌟 3. Ô NHẬP TÌM KIẾM (Search Bar) TÍCH HỢP TRỰC TIẾP
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Nhập tên đội bóng cần tìm...", color = Color.Gray) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Tìm kiếm", tint = NeonGreen)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Xóa", tint = Color.Gray)
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = NeonGreen
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🌟 4. Hiển thị thông báo nếu không tìm thấy trận nào phù hợp với từ khóa
        if (filteredMatches.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không tìm thấy trận đấu nào cho từ khóa \"$searchQuery\"",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        } else {
            // Danh sách hiển thị dạng lưới (Grid) 2 cột
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(filteredMatches) { match ->
                    val isFeatured = match.isHot

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clickable { onMatchClick(match) },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            width = if (isFeatured) 2.dp else 1.dp,
                            color = if (isFeatured) Color.Red else NeonGreen
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Tên đội nhà
                                Text(
                                    text = match.teamA,
                                    color = if (isFeatured) Color(0xFFFFD700) else NeonGreen,
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

                                                val (statusLabel, timeLabel) = when (timeDisplay) {
                                                    "HT" -> "LIVE" to "HT"
                                                    "FT" -> "FT" to "Đang cập nhật tỉ số"
                                                    else -> "LIVE" to "$timeDisplay'"
                                                }

                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = statusLabel,
                                                        color = Color.Red,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = timeLabel,
                                                        color = if (timeDisplay == "FT") Color.LightGray else Color.Red,
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
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 8.dp, end = 8.dp)
                                )
                            }
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