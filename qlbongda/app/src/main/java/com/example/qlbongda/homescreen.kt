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
import androidx.compose.runtime.snapshots.SnapshotStateList



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    matchList: List<FullMatchDetail>,
    onLogout: () -> Unit,
    onNavigateToStandingDetail: () -> Unit,
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

    val standingList = remember {
        listOf(
            StandingRow(1, "Arsenal", 1, "+1", 3),
            StandingRow(2, "Man City", 0, "0", 0),
            StandingRow(3, "MU", 0, "0", 0),
            StandingRow(4, "Chelsea", 1, "-1", 0),
            StandingRow(5, "Liverpool", 0, "0", 0),
            StandingRow(6, "Tottenham", 0, "0", 0),
            StandingRow(7, "Aston Villa", 0, "0", 0),
            StandingRow(8, "Newcastle", 0, "0", 0)
        )
    }

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
                        label = { Text("Đội bóng", color = if (selectedTab == 2) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.List, contentDescription = "Chi tiết đội bóng") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        label = { Text("Cá nhân", color = if (selectedTab == 3) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Trang cá nhân") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
                    NavigationBarItem(
                            selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    label = { Text("Tin tức", color = if (selectedTab == 4) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                    icon = { Text("📰", fontSize = 20.sp) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                    )
//                    NavigationBarItem(
//                        selected = selectedTab == 5,
//                        onClick = { selectedTab = 5 },
//                        label = { Text("Giải Cúp", color = if (selectedTab == 5) NeonGreen else Color.LightGray, fontSize = 11.sp) },
//                        icon = { Text("🏆", fontSize = 20.sp) }, // Icon chiếc cúp
//                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
//                    )
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
                        standingList = standingList,
                        teamName = teamName,
                        coachName = coachName,
                        leaderName = leaderName,
                        playerList = playerList,
                        onNavigateToStandingDetail = onNavigateToStandingDetail,
                        onTeamClick = onTeamClick // 🌟 Thêm dòng này để truyền sự kiện lên MainActivity
                    )
                    1 -> ScheduleTabContent(
                        matchList = matchList,
                        onMatchClick = { activeDetailMatch = it }
                    )
                    2 -> TeamTabContent(
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
                    3 -> ProfileTabContent(onLogout = onLogout)
                    4 -> NewsTabContent()
                   // 5 -> KnockoutBracketScreen()
                }
            }
        }
    }
}