package com.example.qlbongda

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope // 🌟 SỬA ĐỔI: Import cái này để tạo Coroutine Scope chuẩn trong Compose
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.qlbongda.data.api.HomeViewModel
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.DetailedStanding
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.StandingItem
import com.example.qlbongda.data.model.StandingRow
import com.example.qlbongda.ui.theme.QlbongdaTheme
import kotlinx.coroutines.launch // 🌟 Giữ lại để dùng launch

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

                    // 🌟 KHỞI TẠO COROUTINE SCOPE CHUẨN TRONG COMPOSE
                    val scope = rememberCoroutineScope()

                    // HOISTING STATE
                    var isTeamRegistered by remember { mutableStateOf(false) }
                    var teamName by remember { mutableStateOf("") }
                    var leaderName by remember { mutableStateOf("") }
                    var coachName by remember { mutableStateOf("") }
                    var isLeagueRegistered by remember { mutableStateOf(false) }
                    val playerList = remember { mutableStateListOf<PlayerInfo>() }



                    // 🌟 DỮ LIỆU ẢO PHÂN CHIA BẢNG A & BẢNG B ĐỂ BẠN TEST GIAO DIỆN
                    val detailedStandingsList = remember {
                        mutableStateListOf(
                            // --- DỮ LIỆU CỦA BẢNG A ---
                            GroupStanding(
                                groupName = "Bảng A",
                                standings = listOf(
                                    DetailedStanding(1, "Arsenal", "", 38, 28, 5, 5, 91, 29, "+62", 89, listOf("W", "W", "W", "W", "W")),
                                    DetailedStanding(2, "Man City", "", 38, 27, 7, 4, 96, 34, "+62", 88, listOf("W", "W", "W", "D", "L")),
                                    DetailedStanding(3, "MU", "", 38, 24, 10, 4, 86, 41, "+45", 82, listOf("W", "D", "W", "L", "D")),
                                    DetailedStanding(4, "Chelsea", "", 38, 20, 11, 7, 77, 63, "+14", 71, listOf("W", "W", "W", "W", "W"))
                                )
                            ),
                            // --- DỮ LIỆU CỦA BẢNG B ---
                            GroupStanding(
                                groupName = "Bảng B",
                                standings = listOf(
                                    DetailedStanding(1, "Liverpool", "", 38, 24, 10, 4, 86, 41, "+45", 82, listOf("W", "D", "W", "L", "D")),
                                    DetailedStanding(2, "Tottenham", "", 38, 20, 6, 12, 74, 61, "+13", 66, listOf("W", "L", "W", "L", "L")),
                                    DetailedStanding(3, "Aston Villa", "", 38, 20, 8, 10, 76, 61, "+15", 68, listOf("L", "D", "L", "W", "W")),
                                    DetailedStanding(4, "Newcastle", "", 38, 18, 6, 14, 85, 62, "+23", 60, listOf("D", "W", "D", "L", "W"))
                                )
                            )
                        )
                    }

// Giữ nguyên trạng thái loading bằng false để dữ liệu ảo hiển thị lên luôn

                    var isStandingLoading by remember { mutableStateOf(false) }
                    var selectedTeamObjectForDetail by remember { mutableStateOf<StandingItem?>(null) }

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

                    val apiService = remember { RetrofitClient.getClient(this@MainActivity) }
                    val homeViewModel = remember { HomeViewModel(apiService) }

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
                            val phaseList by homeViewModel.phases.collectAsState()
                            val isLoading by homeViewModel.isLoading.collectAsState()

                            if (isLoading) {
                                Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = Color(0xFF00FF66))
                                }
                            } else {
                                HomeScreen(
                                    phaseList = phaseList,
                                    matchList = globalMatchList,
                                    onLogout = { currentScreen = "login" },
                                    onNavigateToStandingDetail = {
                                        previousScreen = "home"
                                        isStandingLoading = true
                                        currentScreen = "standing_detail"

                                        // 🌟 SỬA ĐOẠN NÀY: Thay coroutineScope thành scope kế thừa từ rememberCoroutineScope()
                                        scope.launch {
                                            try {
                                                // Bạn lưu ý sửa tên hàm api thành hàm lấy BXH chi tiết (ví dụ: getDetailedStandings())
                                                val response = RetrofitClient.getClient(this@MainActivity).getDetailedStandings()
                                                if (response.isSuccessful && response.body()?.status == "success") {
                                                    detailedStandingsList.clear()
                                                    response.body()?.data?.let { detailedStandingsList.addAll(it) }
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(this@MainActivity, "Không thể tải bảng xếp hạng chi tiết!", Toast.LENGTH_SHORT).show()
                                            } finally {
                                                isStandingLoading = false
                                            }
                                        }
                                    },
                                    onTeamClick = { _ ->
                                        selectedTeamObjectForDetail = StandingItem(
                                            rank = 1, teamName = "Man City", played = 38, goalDifference = "+62", points = 88,
                                            coachName = "Pep Guardiola", captainName = "Kyle Walker", players = emptyList()
                                        )
                                        previousScreen = "home"
                                        currentScreen = "team_detail"
                                    },
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
                        }

                        "standing_detail" -> {
                            if (isStandingLoading) {
                                Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = Color(0xFF00FF66))
                                }
                            } else {
                                StandingDetailScreen(
                                    standings = detailedStandingsList,
                                    teamName = teamName,
                                    coachName = coachName,
                                    leaderName = leaderName,
                                    playerList = playerList,
                                    onBack = {
                                        currentScreen = "home"
                                    },
                                    onTeamClick = { _ ->
                                        previousScreen = "standing_detail"
                                        currentScreen = "team_detail"
                                    }
                                )
                            }
                        }

                        "team_detail" -> {
                            val safeTeamData = selectedTeamObjectForDetail ?: StandingItem(
                                rank = 2,
                                teamName = "Manchester City",
                                played = 38,
                                goalDifference = "+62",
                                points = 88,
                                coachName = if (coachName.isNotEmpty()) coachName else "Pep Guardiola",
                                captainName = if (leaderName.isNotEmpty()) leaderName else "Kyle Walker",
                                players = if (playerList.isNotEmpty()) playerList else listOf(
                                    PlayerInfo("9", "Erling Haaland", "Tiền đạo"),
                                    PlayerInfo("17", "Kevin De Bruyne", "Tiền vệ"),
                                    PlayerInfo("31", "Ederson", "Thủ môn")
                                )
                            )

                            TeamDetailScreen(
                                team = safeTeamData,
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