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

import com.example.qlbongda.data.model.TournamentPhase // 🌟 THÊM IMPORT NÀY
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlbongda.data.api.HomeViewModel
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.GroupStanding

import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    selectedTab: Int, // Nhận từ MainActivity
    onTabSelected: (Int) -> Unit, // Nhận hàm callback[cite: 2]
    phaseList: List<TournamentPhase>, // 🌟 1. THÊM THAM SỐ ĐỂ ĐÓN MẢNG VÒNG ĐẤU ĐỘNG TỪ MAINACTIVITY
    matchList: List<FullMatchDetail>,
  //  hotMatchList: List<FullMatchDetail> = emptyList(), // 🌟 THÊM DÒNG NÀY
    onNavigateToMatchDetail: (FullMatchDetail) -> Unit,
    standingList: List<GroupStanding>, // 🌟 Thêm tham số này
    selectedStandingTab: Int,           // Thêm tham số
    onStandingTabSelected: (Int) -> Unit, // Thêm tham số
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
                        phaseList = phaseList,
                        //hotMatches = hotMatchList, // 🌟 TRUYỀN XUỐNG
                        standings = standingList,
                        selectedTabIndex = selectedStandingTab, // Truyền xuống
                        onTabSelected = onStandingTabSelected,  // Truyền xuống
                        onNavigateToStandingDetail = onNavigateToStandingDetail,
                        onTeamClick ={ teamId: Int ->
                            // Đây là nơi nhận ID từ HomeTabContent và chuyển tiếp lên
                            onTeamClick(teamId)}
                    )
                    1 -> ScheduleTabContent(
                        viewModel = homeViewModel,
                        matchList = matchList,
                        onMatchClick = { clickedMatch -> // 🌟 Khai báo biến 'clickedMatch' ở đây
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
                        currentUserRole = currentUserRole, // Biến này cần được khai báo trong HomeScreen
                    )
                    4 -> ProfileTabContent(onLogout = onLogout
                    )// Thay đổi biến này để kích hoạt lại các nơi khá)
                }
            }
        }
    }
