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

import com.example.qlbongda.data.api.HomeViewModel
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.QlbongdaTheme
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
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
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    var currentScreen by remember { mutableStateOf("login") }
                    var previousScreen by remember { mutableStateOf("home") }
                    var selectedTab by remember { mutableIntStateOf(0) }
                    var currentStandingTabIndex by remember { mutableIntStateOf(0) }

                    val matchList by homeViewModel.matchList.collectAsState()
                   // val hotMatchList by homeViewModel.hotMatchList.collectAsState()
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

                    when (currentScreen) {
                        "admin" -> {
                            AdminScreen(
                                matchList = matchList,
                                onLogout = {
                                    Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                                    currentScreen = "login"
                                },
                                adminViewModel = adminViewModel
                            )
                        }

                        "login" -> {
                            LoginScreen(
                                onLoginSuccess = {role -> // 🌟 Nhận role từ LoginScreen
                                    currentUserRole = role // 🌟 Cập nhật vào state để Compose re-render
                                    selectedTab = 0
                                    currentScreen = "home" },
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
                            if (isLoading) {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = Color(0xFF00FF66))
                                }
                            } else {
                                HomeScreen(
                                    homeViewModel = homeViewModel,
                                    selectedTab = selectedTab,
                                    onTabSelected = { selectedTab = it },
                                    phaseList = phaseList,
                                    matchList = matchList,
                                    // 🌟 TRUYỀN DÒNG NÀY
                                    onNavigateToMatchDetail = { match ->
                                        homeViewModel.fetchMatchDetail(match.id)
                                        currentScreen = "match_detail"
                                    },
                                    standingList = standingList,
                                    selectedStandingTab = currentStandingTabIndex,
                                    onStandingTabSelected = { currentStandingTabIndex = it },
                                    onNavigateToStandingDetail = {
                                        previousScreen = "home"
                                        currentScreen = "standing_detail"
                                    },
                                    onLogout = {
                                        currentScreen = "login"
                                        Toast.makeText(this@MainActivity, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show()
                                    },
                                    onTeamClick = { teamIdClicked ->
                                        Log.d("DEBUG_MA", "Đã nhận ID: $teamIdClicked")

                                        // Cập nhật ID trực tiếp vào state
                                        currentTeamId = teamIdClicked
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
                                    currentUserRole = currentUserRole, // 🌟 Truyền xuống
                                    playerList = playerList
                                )
                            }
                        }

                        "match_detail" -> {
                            selectedMatch?.let { match ->
                                MatchDetailScreen(
                                    match = match,
                                    onBack = { currentScreen = "home" }
                                )
                            } ?: Box(modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFF00FF66))
                            }
                        }

                        "standing_detail" -> {
                            StandingDetailScreen(
                                standings = standingList,
                                initialTabIndex = currentStandingTabIndex,
                                onBack = { currentScreen = "home" },
                                onTeamClick = { teamName ->
                                    val clickedTeam = standingList.flatMap { it.standings }
                                        .find { it.teamName.equals(teamName, ignoreCase = true) }
                                    if (clickedTeam != null) {
                                       selectedTeamObjectForDetail = StandingItem(clickedTeam.id, clickedTeam.teamName,  clickedTeam.goalDifference,  "Đang cập nhật", emptyList())
                                        previousScreen = "standing_detail"
                                        currentScreen = "team_detail"
                                    }
                                }
                            )
                        }

                        "team_detail" -> {
                            if (currentTeamId != 0) {
                                TeamDetailContainer(
                                    teamId = currentTeamId,
                                    viewModel = homeViewModel,
                                    onBackClick = { currentScreen = previousScreen }
                                )
                            } else {
                                // Nếu ID là 0, đừng gọi Container mà hãy quay lại hoặc hiện lỗi
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