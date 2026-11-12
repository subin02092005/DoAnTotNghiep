package com.example.qlbongda.home

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.qlbongda.viewmodel.HomeViewModel
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils
import com.example.qlbongda.R

@Composable
fun HotMatchCard(match: FullMatchDetail) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(match.teamA, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
                }
                Text("VS", color = NeonGreen, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 8.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text(match.teamB, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (match.status == "ongoing") {
                Text("ĐANG DIỄN RA", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                Text("${match.scoreA} - ${match.scoreB}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            } else if (match.status == "finished") {
                Text("KẾT THÚC", color = Color.Gray, fontSize = 10.sp)
                Text("${match.scoreA} - ${match.scoreB}", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            } else {
                Text(DateUtils.formatTime(match.time), color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(DateUtils.formatDate(match.date), color = Color.Gray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun HomeTabContent(
    viewModel: HomeViewModel,
    seasonList: List<SeasonWithPhases> = emptyList(),
    phaseList: List<TournamentPhase>,
    standings: List<GroupStanding>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onNavigateToStandingDetail: () -> Unit,
    onTeamClick: (Int) -> Unit,
    onNavigateToDetail: (SeasonWithPhases) -> Unit,
    listState: LazyListState = rememberLazyListState()// 👈 THÊM THAM SỐ NÀY
) {
    val hotMatches by viewModel.hotMatchList.collectAsState()
    val selectedSeason by viewModel.selectedSeason.collectAsState()
    val currentPhases = remember(selectedSeason, phaseList) {
        selectedSeason?.phases ?: phaseList
    }
    val selectedPhase by viewModel.selectedPhase.collectAsState()



    LaunchedEffect(selectedSeason?.id) {
        val seasonId = selectedSeason?.id
        if (seasonId != null) {
            Log.d("API_DEBUG", "--- BẮT ĐẦU LOAD THẬT --- ID: $seasonId")
            viewModel.loadStandings(seasonId)
        }
    }

    LaunchedEffect(currentPhases) {
        if (currentPhases.isNotEmpty()) {
            if (selectedPhase == null || !currentPhases.contains(selectedPhase)) {
                viewModel.selectPhase(currentPhases.firstOrNull()) // Lưu trực tiếp vào ViewModel
            }
        } else {
            viewModel.selectPhase(null)
        }
    }
    LaunchedEffect(selectedPhase) {
        selectedPhase?.let { viewModel.selectPhase(it.id) }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                tint = NeonGreen,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NEONBALL PRO",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Cập nhật giải đấu mới nhất",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        LazyColumn(state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (hotMatches.isNotEmpty()) {
                item {
                    Text(
                        text = "TRẬN ĐẤU NỔI BẬT",
                        color = NeonGreen,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(hotMatches,key = { it.id }) { match ->
                            HotMatchCard(match)
                        }
                    }
                }
            }

            if (seasonList.isNotEmpty()) {
                item {
                    Text(
                        text = "DANH SÁCH GIẢI ĐẤU",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize= 11.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
                    )
                }

                // Hiển thị danh sách các giải đấu dưới dạng các ô card bấm được
                items(seasonList,key = { it.id ?: it.name}) { season ->
                    val isSelected = selectedSeason?.id == season.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                // 1. Cập nhật giải đấu trong ViewModel
                                viewModel.selectSeason(season)
                                // 2. Chuyển sang trang chi tiết
                                onNavigateToDetail(season)
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF1B2E1B) else Color(0xFF161616)
                        ),
                        border = BorderStroke(1.dp, if (isSelected) NeonGreen else Color.DarkGray)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(if (isSelected) NeonGreen else Color(0xFF252525), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.Black else NeonGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = season.name.uppercase(),
                                        color = if (isSelected) NeonGreen else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Nhấn để xem lịch đấu, kết quả & bảng xếp hạng",
                                        color = Color.Gray,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = if (isSelected) NeonGreen else Color.Gray
                            )
                        }
                    }
                }
            }

            if (currentPhases.isNotEmpty()) {
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentPhases) { phase ->
                            val isSelected = selectedPhase?.id == phase.id
                            Button(
                                onClick = { viewModel.selectPhase(phase) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) NeonGreen else Color(0xFF2C2C2C)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = phase.name,
                                    color = if (isSelected) Color.Black else Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                if (standings.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(standings) { index, group ->
                            val isSelected = selectedTabIndex == index
                            FilterChip(
                                selected = isSelected,
                                onClick = { onTabSelected(index) },
                                label = { Text(group.groupName, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonGreen,
                                    containerColor = Color(0xFF161616),
                                    labelColor = Color.White,
                                    selectedLabelColor = Color.Black
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = Color.DarkGray,
                                    selectedBorderColor = NeonGreen
                                )
                            )
                        }
                    }
                }
            }

            val currentGroup = standings.getOrNull(selectedTabIndex)

            if (currentGroup != null) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "#",
                            color = Color.Gray,
                            modifier = Modifier.width(30.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp
                        )
                        Text("Đội bóng", color = Color.Gray, modifier = Modifier.weight(1f), fontSize = 13.sp)
                        Text(
                            "Trận",
                            color = Color.Gray,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp
                        )
                        Text(
                            "HS",
                            color = Color.Gray,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp
                        )
                        Text(
                            "Đ",
                            color = Color.Gray,
                            modifier = Modifier.width(40.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp
                        )
                    }
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                }

                items(currentGroup.standings, key = { it.id}) { row ->
                    val rankColor = when (row.rank) {
                        1, 2 -> NeonGreen
                        3 -> Color.White
                        else -> Color.Gray
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onNavigateToStandingDetail() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.5.dp, Color(0xFF222222))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                row.rank.toString(),
                                color = rankColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(30.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp
                            )
                            Text(
                                row.teamName,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        Log.d("DEBUG_ID", "ID của đội ${row.teamName} là: ${row.id}")
                                        onTeamClick(row.id)
                                    },
                                fontSize = 15.sp
                            )
                            Text(
                                row.played.toString(),
                                color = Color.White,
                                modifier = Modifier.width(40.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                row.goalDifference,
                                color = Color.LightGray,
                                modifier = Modifier.width(40.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                row.points.toString(),
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(40.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có dữ liệu bảng xếp hạng", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "Giao diện Trang Chủ (HomeTab)")
@Composable
fun HomeTabContentPreview() {
    // Dữ liệu giả lập các giải đấu để hiển thị trong Preview
    val sampleSeasons = listOf(
        SeasonWithPhases(
            id = 1,
            name = "V-LEAGUE MÙA HÈ 2026",
            phases = emptyList()
        ),
        SeasonWithPhases(
            id = 2,
            name = "CÚP VÀNG MÙA THU 2026",
            phases = emptyList()
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Header Logo & Tiêu đề ứng dụng
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF1E2E1E), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "NEONBALL PRO",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Cập nhật giải đấu mới nhất",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tiêu đề mục danh sách giải đấu
        Text(
            text = "DANH SÁCH GIẢI ĐẤU",
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Hiển thị danh sách các giải đấu dưới dạng ô Card bấm được
        sampleSeasons.forEachIndexed { index, season ->
            val isSelected = index == 0 // Giả lập giải đầu tiên đang được chọn

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF1B2E1B) else Color(0xFF161616)
                ),
                border = BorderStroke(1.dp, if (isSelected) NeonGreen else Color.DarkGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (isSelected) NeonGreen else Color(0xFF252525), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = if (isSelected) Color.Black else NeonGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = season.name.uppercase(),
                                color = if (isSelected) NeonGreen else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Nhấn để xem lịch đấu, kết quả & bảng xếp hạng",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = if (isSelected) NeonGreen else Color.Gray
                    )
                }
            }
        }
    }
}