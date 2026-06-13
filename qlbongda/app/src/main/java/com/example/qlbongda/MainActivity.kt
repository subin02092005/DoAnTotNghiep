package com.example.qlbongda

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.qlbongda.data.model.DetailedStanding
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.ui.theme.QlbongdaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QlbongdaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("home") }
                   var previousScreen by remember { mutableStateOf("home") }

                    // 🌟 HOISTING STATE: Đưa toàn bộ trạng thái đội bóng lên MainActivity quản lý chung
                    var isTeamRegistered by remember { mutableStateOf(false) }
                    var teamName by remember { mutableStateOf("") }
                    var leaderName by remember { mutableStateOf("") }
                    var coachName by remember { mutableStateOf("") }
                    var isLeagueRegistered by remember { mutableStateOf(false) }
                    val playerList = remember { mutableStateListOf<PlayerInfo>() }

                    // DỮ LIỆU BẢNG XẾP HẠNG CHI TIẾT
                    val detailedStandingsList = remember {
                        listOf(
                            DetailedStanding(1, "Arsenal", "", 38, 28, 5, 5, 91, 29, "+62", 89, listOf("W", "W", "W", "W", "W")),
                            DetailedStanding(2, "Man City", "", 38, 27, 7, 4, 96, 34, "+62", 88, listOf("W", "W", "W", "D", "L")),
                            DetailedStanding(3, "MU", "", 38, 24, 10, 4, 86, 41, "+45", 82, listOf("W", "D", "W", "L", "D")),
                            DetailedStanding(4, "Chelsea", "", 38, 20, 11, 7, 77, 63, "+14", 71, listOf("W", "W", "W", "W", "W")),
                            DetailedStanding(5, "Liverpool", "", 38, 24, 10, 4, 86, 41, "+45", 82, listOf("W", "D", "W", "L", "D")),
                            DetailedStanding(6, "Tottenham", "", 38, 20, 6, 12, 74, 61, "+13", 66, listOf("W", "L", "W", "L", "L")),
                            DetailedStanding(7, "Aston Villa", "", 38, 20, 8, 10, 76, 61, "+15", 68, listOf("L", "D", "L", "W", "W")),
                            DetailedStanding(8, "Newcastle", "", 38, 18, 6, 14, 85, 62, "+23", 60, listOf("D", "W", "D", "L", "W"))
                        )
                    }

                    var selectedTeamNameForDetail by remember { mutableStateOf("") }

                    val globalMatchList = remember {
                        mutableStateListOf(
                            com.example.qlbongda.data.model.FullMatchDetail(
                                id = 1, teamA = "Arsenal", teamB = "Chelsea", isStarted = true, scoreA = "2", scoreB = "1", time = "19:00", date = "06/06", stadium = "Emirates Stadium",
                                events = emptyList(), lineupA = emptyList(), lineupB = emptyList(), subsA = emptyList(), subsB = emptyList(), PossessionA = "55%", PossessionB = "45%", ShotsA = "14", ShotsB = "9", mvp = "Saka", isHot = false
                            ),
                            com.example.qlbongda.data.model.FullMatchDetail(
                                id = 2, teamA = "MU", teamB = "Man City", isStarted = false, scoreA = "0", scoreB = "0", time = "21:30", date = "07/06", stadium = "Old Trafford",
                                events = emptyList(), lineupA = emptyList(), lineupB = emptyList(), subsA = emptyList(), subsB = emptyList(), PossessionA = "0%", PossessionB = "0%", ShotsA = "0", ShotsB = "0", mvp = "", isHot = true
                            )
                        )
                    }

                    when (currentScreen) {
                        "admin" -> {
                            AdminScreen(
                                matchList = globalMatchList,
                                onLogout = {
                                    Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                                    currentScreen = "login"
                                }
                            )
                        }

                        "login" -> {
                            LoginScreen(
                                onLoginSuccess = { currentScreen = "home" },
                                onNavigateToRegister = { currentScreen = "register" },
                                onForgotPasswordClick = { currentScreen = "forgot_password" }
                            )
                        }

                        "register" -> {
                            RegisterScreen(
                                onRegisterSuccess = { currentScreen = "login" },
                                onBackToLogin = { currentScreen = "login" }
                            )
                        }

                        "forgot_password" -> {
                            ForgotPasswordScreen(
                                onResetSuccess = { currentScreen = "login" },
                                onBackToLogin = { currentScreen = "login" }
                            )
                        }

                        "home" -> {
                            HomeScreen(
                                matchList = globalMatchList,
                                onLogout = {
                                    Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                                    currentScreen = "login"
                                },
                                onNavigateToStandingDetail = {
                                    currentScreen = "standing_detail"
                                },
                                onTeamClick = { clickedTeamName ->
                                    selectedTeamNameForDetail = clickedTeamName
                                    previousScreen = "home"
                                    currentScreen = "team_detail"
                                },
                                // Truyền tiếp các biến và hàm callback xuống HomeScreen
                                isTeamRegistered = isTeamRegistered,
                                onTeamRegisteredChange = { isTeamRegistered = it },
                                teamName = teamName,
                                onTeamNameChange = { teamName = it },
                                leaderName = leaderName,
                                onLeaderNameChange = { leaderName = it },
                                coachName = coachName,
                                onCoachNameChange = { coachName = it },
                                isLeagueRegistered = isLeagueRegistered,
                                onLeagueRegisteredChange = { isLeagueRegistered = it },
                                playerList = playerList
                            )
                        }

                        "standing_detail" -> {
                            StandingDetailScreen(
                                standings = detailedStandingsList,
                                teamName = teamName,
                                coachName = coachName,
                                leaderName = leaderName,
                                playerList = playerList,
                                onBack = {
                                    currentScreen = "home"
                                },
                                onTeamClick = { clickedTeamName ->
                                    selectedTeamNameForDetail = clickedTeamName
                                    previousScreen = "standing_detail"
                                    currentScreen = "team_detail"
                                }
                            )
                        }

                        "team_detail" -> {
                            // Kiểm tra xem đội được chọn có phải là đội do User tự đăng ký ở Tab 2 không
                            val isSelectedMyTeam =
                                selectedTeamNameForDetail.equals(teamName, ignoreCase = true)

                            // 🌟 ĐÃ SỬA: Chuẩn hóa về đúng 3 tham số (Số áo, Tên, Vị trí) khớp với Constructor của bạn
                            val mockPlayerList = listOf(
                                PlayerInfo("9", "Erling Haaland", "Tiền đạo"),
                                PlayerInfo("17", "Kevin De Bruyne", "Tiền vệ"),
                                PlayerInfo("47", "Phil Foden", "Tiền vệ"),
                                PlayerInfo("20", "Bernardo Silva", "Tiền vệ"),
                                PlayerInfo("16", "Rodri", "Tiền vệ"),
                                PlayerInfo("3", "Ruben Dias", "Hậu vệ"),
                                PlayerInfo("31", "Ederson", "Thủ môn"),
                                PlayerInfo("2", "Kyle Walker", "Hậu vệ"),
                                PlayerInfo("6", "Nathan Ake", "Hậu vệ"),
                                PlayerInfo("5", "John Stones", "Hậu vệ"),
                                PlayerInfo("11", "Jeremy Doku", "Tiền đạo"),
                                PlayerInfo("24", "Josko Gvardiol", "Hậu vệ")
                            )

                            TeamDetailScreen(
                                teamName = selectedTeamNameForDetail,
                                coachName = if (isSelectedMyTeam) coachName else "Pep Guardiola",
                                captainName = if (isSelectedMyTeam) leaderName else "Kyle Walker",
                                playerList = if (isSelectedMyTeam && playerList.isNotEmpty()) playerList else mockPlayerList,
                                onBackClick = {
                                    currentScreen = previousScreen
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}