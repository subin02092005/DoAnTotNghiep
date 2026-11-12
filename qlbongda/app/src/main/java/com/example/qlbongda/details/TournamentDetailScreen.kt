package com.example.qlbongda.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils
import com.example.qlbongda.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(
    seasonName: String,
    isKnockout: Boolean,
    viewModel: HomeViewModel,
    currentTab: Int,                   // 🌟 Nhận tab hiện tại từ MainActivity
    onTabSelected: (Int) -> Unit, // 🌟 Hoặc (Int) -> Unit để cập nhật tab
    onBack: () -> Unit,
    onTeamClick: (Int) -> Unit,
    onNavigateToStandingDetail: () -> Unit
) {
    // 💡 Dùng trực tiếp `currentTab` thay cho `mutableIntStateOf(0)` để giữ trạng thái khi đổi màn hình
    val matchList by viewModel.matchList.collectAsState()
    val standings by viewModel.standingList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(seasonName.uppercase(), color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            ScrollableTabRow(
                selectedTabIndex = currentTab, // 🌟 Đồng bộ với tab hiện tại
                containerColor = Color(0xFF121212),
                contentColor = NeonGreen,
                edgePadding = 16.dp
            ) {
                Tab(selected = currentTab == 0, onClick = { onTabSelected(0) }, text = { Text("Lịch thi đấu", color = if (currentTab == 0) NeonGreen else Color.Gray) })
                Tab(selected = currentTab == 1, onClick = { onTabSelected(1) }, text = { Text("Kết quả", color = if (currentTab == 1) NeonGreen else Color.Gray) })
                Tab(selected = currentTab == 2, onClick = { onTabSelected(2) }, text = { Text("Đội tham gia", color = if (currentTab == 2) NeonGreen else Color.Gray) })
                Tab(selected = currentTab == 3, onClick = { onTabSelected(3) }, text = { Text(if (isKnockout) "Nhánh đấu trực tiếp" else "Bảng xếp hạng", color = if (currentTab == 3) NeonGreen else Color.Gray) })
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (currentTab) { // 🌟 Hiển thị nội dung tương ứng theo tab được chọn
                0 -> ApiScheduledMatchesView(matchList, onTeamClick)
                1 -> ApiFinishedMatchesView(matchList, onTeamClick)
                2 -> ApiParticipatingTeamsView(standings, matchList, onTeamClick)
                3 -> {
                    if (isKnockout) {
                        KnockoutBracketView()
                    } else {
                        ApiGroupStageStandingView(
                            standings = standings,
                            onStandingClick = onNavigateToStandingDetail,
                            onTeamClick = onTeamClick
                        )
                    }
                }
            }
        }
    }
}

// Hiển thị danh sách trận đấu sắp đá từ API
@Composable
fun ApiScheduledMatchesView(matchList: List<FullMatchDetail>, onTeamClick: (Int) -> Unit) {
    val upcomingMatches = matchList.filter { it.status != "finished" }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("LỊCH THI ĐẤU SẮP TỚI", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp) }

        if (upcomingMatches.isEmpty()) {
            item { Text("Chưa có lịch thi đấu cho giải này", color = Color.Gray) }
        } else {
            items(upcomingMatches) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = BorderStroke(0.5.dp, Color.DarkGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${DateUtils.formatTime(match.time)} - ${DateUtils.formatDate(match.date)}", color = NeonGreen, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            // 🌟 Bấm vào tên đội nhà
                            Text(
                                match.teamA,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).clickable { onTeamClick(match.teamAId) }
                            )
                            Text("VS", color = Color.Gray, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 8.dp))
                            // 🌟 Bấm vào tên đội khách
                            Text(
                                match.teamB,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).clickable { onTeamClick(match.teamBId) },
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApiFinishedMatchesView(matchList: List<FullMatchDetail>, onTeamClick: (Int) -> Unit) {
    val finishedMatches = matchList.filter { it.status == "finished" }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("KẾT QUẢ TRẬN ĐẤU", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 13.sp) }

        if (finishedMatches.isEmpty()) {
            item { Text("Chưa có kết quả trận đấu nào", color = Color.Gray) }
        } else {
            items(finishedMatches) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = BorderStroke(0.5.dp, Color.DarkGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Đã kết thúc", color = Color.Gray, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                match.teamA,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).clickable { onTeamClick(match.teamAId) }
                            )
                            Text("${match.scoreA} - ${match.scoreB}", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            Text(
                                match.teamB,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f).clickable { onTeamClick(match.teamBId) },
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}

// 🌟 Bảng xếp hạng thu gọn nhưng cho phép bấm vào để mở bảng hoàn chỉnh
@Composable
fun ApiGroupStageStandingView(
    standings: List<GroupStanding>,
    onStandingClick: () -> Unit,
    onTeamClick: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("BẢNG XẾP HẠNG VÒNG BẢNG", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                // Nút bấm mở rộng xem chi tiết đầy đủ
                TextButton(onClick = onStandingClick) {
                    Text("Xem đầy đủ ➔", color = Color.Yellow, fontSize = 12.sp)
                }
            }
        }

        if (standings.isEmpty()) {
            item { Text("Chưa có dữ liệu bảng xếp hạng", color = Color.Gray) }
        } else {
            items(standings) { group ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStandingClick() }, // 🌟 Bấm vào nguyên thẻ bảng xếp hạng sẽ mở bảng chi tiết hoàn chỉnh
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = BorderStroke(1.dp, Color.DarkGray)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(group.groupName, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("#", color = Color.Gray, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center, fontSize = 12.sp)
                            Text("Đội bóng", color = Color.Gray, modifier = Modifier.weight(1f), fontSize = 12.sp)
                            Text("Trận", color = Color.Gray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center, fontSize = 12.sp)
                            Text("HS", color = Color.Gray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center, fontSize = 12.sp)
                            Text("Đ", color = Color.Gray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))

                        group.standings.forEach { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { onTeamClick(row.id) }, // 🌟 Bấm trực tiếp vào từng đội để xem chi tiết đội
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(row.rank.toString(), color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                                Text(row.teamName, color = Color.White, modifier = Modifier.weight(1f), fontSize = 14.sp)
                                Text(row.played.toString(), color = Color.LightGray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                                Text(row.goalDifference, color = Color.LightGray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                                Text(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun KnockoutBracketView() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("VÒNG BÁN KẾT", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        item {
            KnockoutMatchCard(teamA = "FC Phoenix", scoreA = "2", teamB = "FC Tiger", scoreB = "1", status = "Đã đá")
        }
        item {
            KnockoutMatchCard(teamA = "FC Lion", scoreA = "3", teamB = "FC Dragon", scoreB = "0", status = "Đã đá")
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("TRẬN CHUNG KẾT", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        item {
            KnockoutMatchCard(teamA = "FC Phoenix", scoreA = "-", teamB = "FC Lion", scoreB = "-", status = "Sắp diễn ra (20:00)")
        }
    }
}

@Composable
fun KnockoutMatchCard(teamA: String, scoreA: String, teamB: String, scoreB: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
        border = BorderStroke(1.dp, Color.DarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(status, color = Color.Gray, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(teamA, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("$scoreA - $scoreB", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
                Text(teamB, color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
fun ApiParticipatingTeamsView(
    standings: List<GroupStanding>,
    matchList: List<FullMatchDetail>,
    onTeamClick: (Int) -> Unit
) {
    val teamsMap = mutableMapOf<Int, String>()
    standings.forEach { group ->
        group.standings.forEach { row ->
            teamsMap[row.id] = row.teamName
        }
    }
    matchList.forEach { match ->
        if (match.teamAId != 0) teamsMap[match.teamAId] = match.teamA
        if (match.teamBId != 0) teamsMap[match.teamBId] = match.teamB
    }
    val participatingTeams = teamsMap.entries.toList()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("DANH SÁCH ĐỘI BÓNG THAM GIA (${participatingTeams.size})", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp) }

        if (participatingTeams.isEmpty()) {
            item { Text("Chưa có đội bóng nào tham gia giải này", color = Color.Gray) }
        } else {
            items(participatingTeams) { entry ->
                val teamId = entry.key
                val teamName = entry.value
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTeamClick(teamId) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                    border = BorderStroke(0.5.dp, Color.DarkGray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonGreen.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(teamName.take(2).uppercase(), color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(teamName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Text("Xem chi tiết ➔", color = Color.Yellow, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
