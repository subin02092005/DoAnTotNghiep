package com.example.qlbongda.home

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

import com.example.qlbongda.data.model.TournamentPhase 
import com.example.qlbongda.data.model.SeasonWithPhases 
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.qlbongda.viewmodel.HomeViewModel
import com.example.qlbongda.data.model.GroupStanding

import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    seasonList: List<SeasonWithPhases>,
    phaseList: List<TournamentPhase>,
    matchList: List<FullMatchDetail>,
    onNavigateToMatchDetail: (FullMatchDetail) -> Unit,
    standingList: List<GroupStanding>,
    selectedStandingTab: Int,
    onStandingTabSelected: (Int) -> Unit,
    onNavigateToStandingDetail: () -> Unit,
    onLogout: () -> Unit,
    onTeamClick: (Int) -> Unit,
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
    currentUserRole: String,
    playerList: SnapshotStateList<PlayerInfo>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color.Black, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    label = { Text("Trang chủ", color = if (selectedTab == 0) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    label = { Text("Lịch đấu", color = if (selectedTab == 1) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Lịch thi đấu") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { onTabSelected(2)},
                    label = { Text("Tin tức", color = if (selectedTab == 2) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                    icon = { Text("📰", fontSize = 20.sp) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { onTabSelected(3)},
                    label = { Text("Đội bóng", color = if (selectedTab == 3) NeonGreen else Color.LightGray, fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.List, contentDescription = "Chi tiết đội bóng") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, unselectedIconColor = Color.LightGray, indicatorColor = NeonGreen)
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { onTabSelected(4)},
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
        ) { when (selectedTab) {
            0 -> HomeTabContent(
                viewModel = homeViewModel,
                seasonList = seasonList,
                phaseList = phaseList,
                standings = standingList,
                selectedTabIndex = selectedStandingTab,
                onTabSelected = onStandingTabSelected,
                onNavigateToStandingDetail = onNavigateToStandingDetail,
                onTeamClick ={ teamId: Int ->
                    onTeamClick(teamId)}
            )
            1 -> ScheduleTabContent(
                viewModel = homeViewModel,
                onMatchClick = { clickedMatch ->
                    onNavigateToMatchDetail(clickedMatch)
                }
            )
            2 -> NewsTabContent()
            3 ->  TeamTabContent(
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
                onLeagueRegisteredChange = onLeagueRegisteredChange,
                currentUserRole = currentUserRole,
            )
            4 -> ProfileTabContent(onLogout = onLogout)
        }
        }
    }
}
