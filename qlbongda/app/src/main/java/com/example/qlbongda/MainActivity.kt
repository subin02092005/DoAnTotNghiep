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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.data.model.MatchEvent
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.StandingItem

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
                    var currentScreen by remember { mutableStateOf("login") }

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
                    val detailedStandingsList = remember { mutableStateListOf<GroupStanding>() }



                    var isStandingLoading by remember { mutableStateOf(false) }


                    val globalMatchList = remember {
                        listOf(  com.example.qlbongda.data.model.FullMatchDetail(
                        id = 1,
                        teamA = "Arsenal",
                        teamB = "Man City",
                        isStarted = true,
                        scoreA = "2",
                        scoreB = "1",
                        time = "22:00",
                        date = "07/06/2026",
                        stadium = "Emirates Stadium",
                        events = listOf(
                            MatchEvent("15", "Arsenal", "Ghi bàn", "Bukayo Saka"),
                            MatchEvent("42", "Man City", "Thẻ Vàng", "Ruben Dias"),
                            MatchEvent("55", "Man City", "Ghi bàn", "Erling Haaland"),
                            MatchEvent("89", "Arsenal", "Ghi bàn", "Martin Odegaard")
                        ),
                        lineupA = listOf(
                            PlayerInfo("22", "Raya", "GK"),
                            PlayerInfo("2", "Saliba", "DF"),
                            PlayerInfo("6", "Gabriel", "DF"),
                            PlayerInfo("4", "White", "DF"),
                            PlayerInfo("41", "Rice", "MF"),
                            PlayerInfo("8", "Odegaard", "MF"),
                            PlayerInfo("7", "Saka", "FW")
                        ),
                        lineupB = listOf(
                            PlayerInfo("31", "Ederson", "GK"),
                            PlayerInfo("3", "Dias", "DF"),
                            PlayerInfo("25", "Akanji", "DF"),
                            PlayerInfo("16", "Rodri", "MF"),
                            PlayerInfo("17", "De Bruyne", "MF"),
                            PlayerInfo("47", "Foden", "FW"),
                            PlayerInfo("9", "Haaland", "FW")
                        ),
                        subsA = listOf(
                            PlayerInfo("29", "Havertz", "FW"),
                            PlayerInfo("11", "Martinelli", "FW")
                        ),
                        subsB = listOf(
                            PlayerInfo("10", "Grealish", "FW"),
                            PlayerInfo("19", "Alvarez", "FW")
                        ),
                        PossessionA = "45%", PossessionB = "55%",
                        ShotsA = "12", ShotsB = "14",
                        mvp = "Martin Odegaard (Arsenal)",
                                isHot = false
                        ))
                    }
                    var selectedTab by remember { mutableIntStateOf(0) }
                    var selectedMatch by remember { mutableStateOf<FullMatchDetail?>(null) }
                    var selectedTeamObjectForDetail by remember { mutableStateOf<StandingItem?>(null) }
                    val apiService = remember { RetrofitClient.getClient(this@MainActivity) }
                    val homeViewModel = remember { HomeViewModel(apiService) }
                    val adminViewModel = remember { AdminViewModel(apiService) }

                    var currentStandingTabIndex by remember { mutableIntStateOf(0) }
                    when (currentScreen) {
                        "admin" -> {
                            AdminScreen(
                                matchList =globalMatchList,
                                onLogout = {
                                    Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                                    currentScreen = "login"
                                },
                                adminViewModel = adminViewModel
                            )
                        }

                        "login" -> {

                            LoginScreen(

                                onLoginSuccess = { currentScreen = "home" },
                                onLoginAdminSuccess = { currentScreen = "admin" },
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
                            LaunchedEffect(Unit) {
                                isStandingLoading = true
                                try {
                                    val response = RetrofitClient.getClient(this@MainActivity).getDetailedStandings()
                                    if (response.isSuccessful && response.body()?.status == "success") {
                                        detailedStandingsList.clear()
                                        response.body()?.data?.let { detailedStandingsList.addAll(it) }
                                    }
                                } catch (e: Exception) {
                                    android.util.Log.e("API_ERROR", "Lỗi tải bảng xếp hạng: ${e.message}")
                                } finally {
                                    isStandingLoading = false
                                }
                            }
                            val phaseList by homeViewModel.phases.collectAsState()
                            val isLoading by homeViewModel.isLoading.collectAsState()

                            if (isLoading) {
                                Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = Color(0xFF00FF66))
                                }
                            } else {
                                HomeScreen(
                                    selectedTab = selectedTab, // Truyền biến state vừa tạo
                                    onTabSelected = { selectedTab = it }, // Truyền hàm xử lý khi click tab
                                    phaseList = phaseList,
                                    matchList = globalMatchList,
                                    onNavigateToMatchDetail = { match ->
                                        // 1. Lưu trận đấu vào biến state
                                        selectedMatch = match
                                        // 2. Chuyển màn hình
                                        currentScreen = "match_detail"

                                        // 3. Gọi API lấy dữ liệu chi tiết
                                        scope.launch {
                                            try {
                                                val response = apiService.getMatchDetail(match.id)
                                                if (response.isSuccessful && response.body()?.status == "success") {
                                                    // Cập nhật lại với dữ liệu chi tiết từ server
                                                    selectedMatch = response.body()?.data
                                                }
                                            } catch (e: Exception) {
                                                android.util.Log.e("API_ERROR", "Không thể lấy chi tiết trận đấu: ${e.message}")
                                            }
                                        }
                                    },
                                    standingList = detailedStandingsList,
                                    selectedStandingTab = currentStandingTabIndex,
                                    onStandingTabSelected = { currentStandingTabIndex = it },
                                    onNavigateToStandingDetail = {
                                        previousScreen = "home"
                                        isStandingLoading = true
                                        currentScreen = "standing_detail"

                                        scope.launch {
                                            try {
                                                val response = RetrofitClient.getClient(this@MainActivity).getDetailedStandings()
                                                if (response.isSuccessful && response.body()?.status == "success") {
                                                    detailedStandingsList.clear()
                                                    response.body()?.data?.let { detailedStandingsList.addAll(it) }
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(this@MainActivity, "Không thể tải bảng xếp hạng!", Toast.LENGTH_SHORT).show()
                                            } finally {
                                                isStandingLoading = false
                                            }
                                        }
                                    },
                                    onLogout = {
                                        selectedMatch = null
                                        selectedTeamObjectForDetail = null
                                        selectedTab = 0
                                        // 3. Chuyển màn hình về login
                                        currentScreen = "login"

                                        Toast.makeText(this@MainActivity, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show()
                                     },

                                    onTeamClick = {teamNameClicked ->
                                        val clickedTeam = detailedStandingsList.flatMap { it.standings }
                                        .find { it.teamName.equals(teamNameClicked, ignoreCase = true) }
                                        selectedTeamObjectForDetail = if (clickedTeam != null) {
                                            StandingItem(
                                                rank = clickedTeam.rank,
                                                teamName = clickedTeam.teamName,
                                                played = clickedTeam.played,
                                                goalDifference = clickedTeam.goalDifference,
                                                points = clickedTeam.points,
                                                coachName = "Đang cập nhật",
                                                captainName = "Đang cập nhật",
                                                players = emptyList()
                                            )
                                        } else {
                                            // Trường hợp dự phòng nếu không tìm thấy trong list
                                            StandingItem(
                                                rank = 0, teamName = teamNameClicked, played = 0, goalDifference = "0", points = 0,
                                                coachName = "Đang cập nhật", captainName = "Đang cập nhật", players = emptyList()
                                            )
                                        }
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
                                    playerList = playerList,

                                )
                            }
}
                        "match_detail" -> {
                            selectedMatch?.let { match ->
                                MatchDetailScreen(
                                    match = match,
                                    onBack = { currentScreen = "home" }
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
                                    initialTabIndex = currentStandingTabIndex, // <--- Truyền đúng tab đó vào
                                    onBack = { currentScreen = "home" },
                                    onTeamClick = { teamName ->
                                        // 1. TÌM DỮ LIỆU ĐỘI TRONG LIST
                                        val clickedTeam = detailedStandingsList.flatMap { it.standings }
                                            .find { it.teamName.equals(teamName, ignoreCase = true) }

                                        // 2. GÁN DỮ LIỆU VÀO STATE ĐỂ TRANG TEAM CHI TIẾT HIỂN THỊ
                                        if (clickedTeam != null) {
                                            // Lưu ý: Map dữ liệu từ DetailedStanding sang StandingItem
                                            selectedTeamObjectForDetail = StandingItem(
                                                rank = clickedTeam.rank,
                                                teamName = clickedTeam.teamName,
                                                played = clickedTeam.played,
                                                goalDifference = clickedTeam.goalDifference,
                                                points = clickedTeam.points,
                                                coachName = "Đang cập nhật", // Hoặc gọi API lấy chi tiết
                                                captainName = "Đang cập nhật",
                                                players = emptyList()
                                            )
                                            // 3. CHUYỂN TRANG
                                            previousScreen = "standing_detail"
                                            currentScreen = "team_detail"
                                        }
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