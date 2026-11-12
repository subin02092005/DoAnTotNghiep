package com.example.qlbongda

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.QlbongdaTheme
import com.example.qlbongda.viewmodel.AdminViewModel
import com.example.qlbongda.viewmodel.HomeViewModel
import com.example.qlbongda.auth.*
import com.example.qlbongda.home.*
import com.example.qlbongda.details.*
import com.example.qlbongda.admin.AdminScreen
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()

    ) { isGranted: Boolean ->
        if (!isGranted) {
            // Bạn có thể hiện một Toast thông báo nếu người dùng từ chối quyền
            Log.d("FCM_PERMISSION", "Người dùng từ chối cấp quyền thông báo")
        }
    }
    private var fcmToken: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        val apiService = RetrofitClient.getClient(this@MainActivity)
        val homeViewModel = HomeViewModel(apiService)
        val adminViewModel = AdminViewModel(apiService)

        // Tìm đoạn này trong MainActivity.onCreate
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                Log.w("FCM_TOKEN", "Lấy token thất bại", task.exception)
                return@addOnCompleteListener
            }
            // Chỉ lấy result khi thành công
            fcmToken = task.result
            Log.d("FCM_TOKEN", "Token của tôi là: $fcmToken")
        }
        setContent {
            QlbongdaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var currentScreen by remember { mutableStateOf("login") }


                    var selectedSeasonForDetail by remember { mutableStateOf<SeasonWithPhases?>(null) } // 🌟 Lưu giải đấu đang chọn
                    var standingDetailSource by remember { mutableStateOf("home") } // Lưu nơi gọi bảng xếp hạng chi tiết
                    var teamDetailSource by remember { mutableStateOf("home") }     // Lưu nơi gọi chi tiết đội bóng
                    var previousScreen by remember { mutableStateOf("home") }
                    var selectedTab by remember { mutableIntStateOf(0) }
                    var currentStandingTabIndex by remember { mutableIntStateOf(0) }

                    // 🌟 LẮNG NGHE DÒNG DỮ LIỆU ĐỘNG TỪ VIEWMODEL TRÊN ĐÂY
                    val seasonList by homeViewModel.seasonsWithPhases.collectAsState(initial = emptyList())
                    val matchList by homeViewModel.matchList.collectAsState()
                    // val hotMatchList by homeViewModel.hotMatchList.collectAsState()x
                    val phaseList by homeViewModel.phases.collectAsState()
                    val standingList by homeViewModel.standingList.collectAsState()
                    val isLoading by homeViewModel.isLoading.collectAsState()
                    val selectedMatch by homeViewModel.selectedMatchDetail.collectAsState()
                    var currentTeamId by remember { mutableIntStateOf(0) }
                    var selectedTeamObjectForDetail by remember { mutableStateOf<StandingItem?>(null) }
                    var isTeamRegistered by remember { mutableStateOf(false) }
                    var currentUserRole by remember { mutableStateOf("") }
                    var teamName by remember { mutableStateOf("") }
                    var leaderName by remember { mutableStateOf("") }
                    var coachName by remember { mutableStateOf("") }
                    var isLeagueRegistered by remember { mutableStateOf(false) }

                    val playerList = remember { mutableStateListOf<PlayerInfo>() }
                    var selectedTournamentTab by remember { mutableIntStateOf(0) }
                    val homeListState = rememberLazyListState()
                        when (currentScreen) {
                            "admin" -> {
                                AdminScreen(
                                    onLogout = {
                                        Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT)
                                            .show()
                                        currentScreen = "login"
                                    },
                                    adminViewModel = adminViewModel
                                )
                            }

                            "login" -> {
                                LoginScreen(
                                    onLoginSuccess = { role -> // 🌟 Nhận role từ LoginScreen
                                        currentUserRole =
                                            role // 🌟 Cập nhật vào state để Compose re-render
                                        selectedTab = 0
                                        currentScreen = "home"
                                    },
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
                                HomeScreen(
                                    homeViewModel = homeViewModel,
                                    selectedTab = selectedTab,
                                    onTabSelected = { selectedTab = it },
                                    seasonList = seasonList, // 🌟 TRUYỀN THAM SỐ GIẢI ĐẤU XUỐNG ĐÂY
                                    phaseList = phaseList,
                                    matchList = matchList,
                                    // 🌟 TRUYỀN DÒNG NÀY
                                    onNavigateToMatchDetail = { match ->
                                        homeViewModel.setSelectedMatch(match)
                                        homeViewModel.fetchMatchDetail(match.id)
                                        currentScreen = "match_detail"
                                    },
                                    standingList = standingList,
                                    selectedStandingTab = currentStandingTabIndex,
                                    onStandingTabSelected = { currentStandingTabIndex = it },
                                    onNavigateToStandingDetail = {
                                        standingDetailSource = "home"
                                        currentScreen = "standing_detail"
                                    },
                                    onLogout = {
                                        currentScreen = "login"
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Đã đăng xuất thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onTeamClick = { teamIdClicked ->
                                        currentTeamId = teamIdClicked
                                        teamDetailSource =
                                            "home" // Đánh dấu đi từ home sang chi tiết đội
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
                                    currentUserRole = currentUserRole, // 🌟 Truyền xuống
                                    playerList = playerList,
                                    onNavigateToDetail = { season ->
                                        // 1. Lưu giải đấu được chọn
                                        selectedSeasonForDetail = season
                                        // 2. Gọi ViewModel load dữ liệu của giải này (bảng xếp hạng, v.v.)
                                        homeViewModel.selectSeason(season)
                                        // 3. Chuyển sang màn hình chi tiết giải đấu
                                        currentScreen = "tournament_detail"
                                    },homeListState = homeListState
                                )

                                // Chỉ hiện overlay nếu đang load lần đầu hoặc đang chuyển đổi quan trọng
                                if (isLoading && (seasonList.isEmpty() || standingList.isEmpty())) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.5f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = Color(0xFF00FF66))
                                    }
                                }
                            }

                            "tournament_detail" -> {
                                selectedSeasonForDetail?.let { season ->
                                    TournamentDetailScreen(
                                        seasonName = season.name,
                                        isKnockout = false,
                                        viewModel = homeViewModel,
                                        currentTab = selectedTournamentTab, // 🌟 Truyền tab hiện tại vào
                                        onTabSelected = {
                                            selectedTournamentTab = it
                                        }, // 🌟 Cập nhật lại khi người dùng đổi tab
                                        onBack = {
                                            currentScreen = "home"
                                            selectedTournamentTab =
                                                0 // Reset về tab đầu nếu thoát hẳn ra ngoài màn hình chính (tuỳ chọn)
                                        },
                                        onTeamClick = { teamId ->
                                            homeViewModel.fetchTeamDetail(teamId)
                                            currentTeamId = teamId
                                            teamDetailSource =
                                                "tournament_detail" // Đánh dấu đi từ tournament_detail sang chi tiết đội
                                            currentScreen = "team_detail"
                                        },
                                        onNavigateToStandingDetail = {
                                            standingDetailSource = "tournament_detail"
                                            currentScreen = "standing_detail"
                                        }
                                    )
                                } ?: run {
                                    currentScreen = "home"
                                }
                            }


                            "match_detail" -> {
                                selectedMatch?.let { match ->
                                    MatchDetailScreen(
                                        match = match,
                                        onBack = { currentScreen = "home" },
                                        onTeamClick = { teamIdClicked ->
                                            // 🌟 Gọi API lấy dữ liệu đội bóng trước, sau đó chuyển màn hình
                                            homeViewModel.fetchTeamDetail(teamIdClicked)
                                            currentTeamId = teamIdClicked
                                            teamDetailSource = "match_detail" // Lưu lại nguồn để nút back quay lại đúng chỗ
                                            currentScreen = "team_detail"
                                        }
                                    )
                                } ?: Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Color(0xFF00FF66))
                                }
                            }

                            "standing_detail" -> {
                                StandingDetailScreen(
                                    standings = standingList,
                                    initialTabIndex = currentStandingTabIndex,
                                    onBack = {
                                        currentScreen = standingDetailSource
                                    }, // Quay về đúng màn hình đã gọi nó (home hoặc tournament_detail)
                                    onTeamClick = { teamIdClicked ->
                                        homeViewModel.fetchTeamDetail(teamIdClicked)
                                        currentTeamId = teamIdClicked
                                        teamDetailSource =
                                            "standing_detail" // Đánh dấu là đi từ bảng xếp hạng chi tiết sang chi tiết đội
                                        currentScreen = "team_detail"
                                    }
                                )
                            }

                            "team_detail" -> {
                                if (currentTeamId != 0) {
                                    TeamDetailContainer(
                                        teamId = currentTeamId,
                                        viewModel = homeViewModel,
                                        onBackClick = {
                                            currentScreen = teamDetailSource
                                        } // Quay lại đúng màn hình trước đó (có thể là home, tournament_detail hoặc standing_detail)
                                    )
                                } else {
                                    currentScreen = "home"
                                }
                            }
                        }
                    }
                }
        }
    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}