package com.example.qlbongda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.StandingRow
import com.example.qlbongda.data.model.TournamentPhase // 🌟 THÊM IMPORT NÀY
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    phaseList: List<TournamentPhase>, // 🌟 1. THÊM THAM SỐ ĐỂ ĐÓN MẢNG VÒNG ĐẤU ĐỘNG TỪ MAINACTIVITY
    matchList: List<FullMatchDetail>,
    onNavigateToMatchDetail: (FullMatchDetail) -> Unit,
    standingList: List<GroupStanding>, // 🌟 Thêm tham số này
    onNavigateToStandingDetail: () -> Unit,
    onLogout: () -> Unit,
    onTeamClick: (String) -> Unit,
    // Nhận dữ liệu Hoisted từ MainActivity truyền xuống
    isTeamRegistered: Boolean,
    onTeamRegisteredChange: (Boolean) -> Unit,
    teamName: String,
    onTeamNameChange: (String) -> Unit,
    leaderName: String,
    onLeaderNameChange: (String) -> Unit,
    coachName: String,
    onCoachNameChange: (String) -> Unit,
    isLeagueRegistered: Boolean,
    onLeagueRegisteredChange: (Boolean) -> Unit,
    playerList: SnapshotStateList<PlayerInfo>
) {
    var selectedTab by remember { mutableStateOf(0) }
    var activeDetailMatch by remember { mutableStateOf<FullMatchDetail?>(null) }
    if (activeDetailMatch != null) {
        MatchDetailScreen(
            match = activeDetailMatch!!,
            onBack = { activeDetailMatch = null }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(containerColor = Color.Black, tonalElevation = 0.dp) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = { Text("Trang chủ", color = if (selectedTab == 0) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = { Text("Lịch đấu", color = if (selectedTab == 1) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Lịch thi đấu") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        label = { Text("Tin tức", color = if (selectedTab == 2) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Text("📰", fontSize = 20.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        label = { Text("Đội bóng", color = if (selectedTab == 3) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.List, contentDescription = "Chi tiết đội bóng") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        label = { Text("Cá nhân", color = if (selectedTab == 4) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Trang cá nhân") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> HomeTabContent(
                        phaseList = phaseList, // 🌟 2. TIẾP TỤC ĐẨY PHACELIST VÀO ĐỂ KHỚP LƯỜNG VỚI HOMETABCONTENT
                        standings = standingList,
                        onNavigateToStandingDetail = onNavigateToStandingDetail,
                        onTeamClick = onTeamClick
                    )
                    1 -> ScheduleTabContent(
                        matchList = matchList,
                        onMatchClick = { clickedMatch -> // 🌟 Khai báo biến 'clickedMatch' ở đây
                            onNavigateToMatchDetail(clickedMatch)
                        }
                    )
                    2 -> NewsTabContent()
                    3 -> TeamTabContent(
                        playerList = playerList,
                        isTeamRegistered = isTeamRegistered,
                        onTeamRegisteredChange = onTeamRegisteredChange,
                        teamName = teamName,
                        onTeamNameChange = onTeamNameChange,
                        leaderName = leaderName,
                        onLeaderNameChange = onLeaderNameChange,
                        coachName = coachName,
                        onCoachNameChange = onCoachNameChange,
                        isLeagueRegistered = isLeagueRegistered,
                        onLeagueRegisteredChange = onLeagueRegisteredChange
                    )
                    4 -> ProfileTabContent(onLogout = onLogout)
                }
            }
        }
    }
}