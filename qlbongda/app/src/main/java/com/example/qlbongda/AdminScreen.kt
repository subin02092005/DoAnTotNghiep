package com.example.qlbongda

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
<<<<<<< HEAD
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
=======
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
<<<<<<< HEAD
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.ui.text.TextStyle

import com.example.qlbongda.data.model.AdminMatchItem
import com.example.qlbongda.data.model.MatchDto
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
=======
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList // 🌟 THÊM IMPORT NÀY ĐỂ QUẢN LÝ DANH SÁCH STATE
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.ClipData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.data.model.AdminMatchItem
import com.example.qlbongda.data.model.MatchDto
import com.example.qlbongda.data.model.AdminPlayerItem
import com.example.qlbongda.data.model.AdminTeamItem
import com.example.qlbongda.data.model.CreateNotificationRequest
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.GroupItem
import com.example.qlbongda.data.model.NotificationItem
import com.example.qlbongda.data.model.PhaseItem
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.TeamItem
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils
import kotlin.collections.emptyList

val NeonBlack = Color(0xFF0A0A0A)      // Đen sâu
val NeonSurface = Color(0xFF161616)    // Xám rất tối (cho Card & Tab)
val NeonGray = Color(0xFF666666)       // Xám cho chữ phụ
val NeonRed = Color(0xFFFF003C)
val NeonWhite = Color(0xFFFFFFFF) // <--- THÊM DÒNG NÀY VÀO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onLogout: () -> Unit,
    adminViewModel: AdminViewModel // 🌟 THÊM VIEWMODEL VÀO ĐÂY
) {
    var currentSection by remember { mutableStateOf(AdminSection.DASHBOARD) }
    var selectedMatchId by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    val message by adminViewModel.message.collectAsStateWithLifecycle()
    
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            adminViewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "HỆ THỐNG QUẢN TRỊ (ADMIN)",
                        color = NeonGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    if (currentSection != AdminSection.DASHBOARD) {
                        IconButton(onClick = { currentSection = AdminSection.DASHBOARD }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSection) {
                AdminSection.DASHBOARD -> AdminDashboard(onSectionSelect = { currentSection = it })
                AdminSection.TEAMS -> TeamManagementScreen(adminViewModel)
                AdminSection.LEAGUES -> LeagueManagementScreen(adminViewModel)
                AdminSection.PLAYERS -> PlayerManagementAdminScreen(adminViewModel)
                // 🌟 TRUYỀN DANH SÁCH VÀO MÀN HÌNH QUẢN LÝ LỊCH ĐẤU ĐỂ SWITCH
                AdminSection.SCHEDULE -> MatchScheduleManagementScreen(adminViewModel, onManageMatch = { matchId ->
                    selectedMatchId = matchId
                    currentSection = AdminSection.MATCH_DETAIL_MANAGE
                })
                AdminSection.STATS -> NotificationManagementScreen(adminViewModel)
                AdminSection.PAYMENTS -> PaymentConfirmationScreen()
                AdminSection.MATCH_DETAIL_MANAGE -> {
                    if (selectedMatchId != -1) {
                        MatchEventManagementScreen(
                            matchId = selectedMatchId,
                            viewModel = adminViewModel,
                            onBack = { currentSection = AdminSection.SCHEDULE }
                        )
                    }
                }
            }
        }
    }
}

enum class AdminSection {
    DASHBOARD, TEAMS, LEAGUES, PLAYERS, SCHEDULE, STATS, PAYMENTS, MATCH_DETAIL_MANAGE
}

data class AdminMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val section: AdminSection,
    val color: Color = NeonGreen
)

@Composable
fun AdminDashboard(onSectionSelect: (AdminSection) -> Unit) {
    val menuItems = listOf(
        AdminMenuItem("Quản lý Đội bóng", "Duyệt và chỉnh sửa các đội tham gia", Icons.Default.Groups, AdminSection.TEAMS),
        AdminMenuItem("Quản lý Giải đấu", "Thiết lập mùa giải và vòng đấu", Icons.Default.EmojiEvents, AdminSection.LEAGUES),
        AdminMenuItem("Quản lý Cầu thủ", "Cơ sở dữ liệu cầu thủ toàn hệ thống", Icons.Default.Person, AdminSection.PLAYERS),
        AdminMenuItem("Lịch thi đấu", "Sắp xếp và cập nhật trạng thái HOT", Icons.Default.CalendarMonth, AdminSection.SCHEDULE),
        AdminMenuItem("Thông Báo", "Xem hiệu suất và dữ liệu giải đấu", Icons.Default.Assessment, AdminSection.STATS),
        AdminMenuItem("Xác nhận Thanh toán", "Phê duyệt lệ phí tham gia của các đội", Icons.Default.Payments, AdminSection.PAYMENTS, Color.Yellow)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(menuItems) { item ->
            AdminMenuCard(item) { onSectionSelect(item.section) }
        }
    }
}

@Composable
fun AdminMenuCard(item: AdminMenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, item.color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

// ==========================================
// THIẾT LẬP MÀN HÌNH QUẢN LÝ LỊCH ĐẤU (BẬT/TẮT HOT THẬT)
// ==========================================
@Composable
fun MatchScheduleManagementScreen(viewModel: AdminViewModel, onManageMatch: (Int) -> Unit) {
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val featuredMatches by viewModel.featuredMatches.collectAsStateWithLifecycle()
    val publicMatches by viewModel.publicMatches.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchMatches()
        viewModel.fetchFeaturedMatches()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "QUẢN LÝ LỊCH THI ĐẤU", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Cập nhật kết quả và trạng thái trận đấu", color = Color.LightGray, fontSize = 13.sp)
            }

            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.background(NeonGreen, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Match", tint = Color.Black)
            }
        }


        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Black,
            contentColor = NeonGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonGreen
                )
            }
        ) {
            val tabTitles = listOf("Tất cả", "Sắp diễn ra", "Đang diễn ra", "Đã kết thúc", "Nổi bật")
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
<<<<<<< HEAD
            val displayList = if (selectedTab == 0) matches else featuredMatches

=======
            // Gộp các nguồn lại để hiển thị đầy đủ thông tin
            val allMatchesCombined = (matches + featuredMatches).distinctBy { it.id }
                .sortedByDescending { it.scheduledAt }

            val displayList = when (selectedTab) {
                1 -> allMatchesCombined.filter { it.status != "finished" && it.status != "ongoing" && it.status != "live" && it.status != "cancelled" }
                2 -> allMatchesCombined.filter { it.status == "ongoing" || it.status == "live" }
                3 -> publicMatches.filter { it.status == "finished" }.map { dto ->
                    // Convert MatchDto to AdminMatchItem to show scores from public API
                    AdminMatchItem(
                        id = dto.id,
                        homeTeamName = dto.teamA,
                        awayTeamName = dto.teamB,
                        homeScore = dto.home_score,
                        awayScore = dto.away_score,
                        status = dto.status,
                        scheduledAt = dto.scheduled_at ?: "",
                        isFeatured = dto.is_featured ?: 0,
                        isPublished = 1,
                        phaseId = null, groupId = null, homeTeamId = 0, awayTeamId = 0,
                        homeTeamLogo = null, awayTeamLogo = null, playedAt = null,
                        round = null, leg = null, venueId = null, referee = null,
                        seasonId = null, createdAt = null, updatedAt = null
                    )
                }
                4 -> featuredMatches
                else -> allMatchesCombined
            }
            
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
            if (displayList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có trận đấu nào", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(displayList) { match ->
                        MatchAdminCard(
                            match = match,
                            onStart = { viewModel.startMatch(match.id) },
                            onFinish = { viewModel.finishMatch(match.id) },
                            onToggleFeatured = { viewModel.toggleFeatured(match.id, match.isFeatured == 0) },
                            onManage = { onManageMatch(match.id) },
                            onCancel = { viewModel.cancelMatch(match.id) },
                            onReschedule = { newTime -> viewModel.rescheduleMatch(match.id, newTime) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMatchDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { request ->
                viewModel.createMatch(request)
                showAddDialog = false
            }
        )
    }
}


@Composable
fun MatchAdminCard(
    match: com.example.qlbongda.data.model.AdminMatchItem, 
    onStart: () -> Unit,
    onFinish: () -> Unit,
    onToggleFeatured: () -> Unit,
    onManage: () -> Unit,
    onCancel: () -> Unit,
    onReschedule: (String) -> Unit
) {
    var showRescheduleDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, if (match.status == "ongoing" || match.status == "live") Color.Red else if (match.status == "cancelled") Color.Gray else Color(0xFF222222))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = match.homeTeamName,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        
                        if (match.status == "finished" || match.status == "ongoing" || match.status == "live") {
                            Text(
                                text = " ${match.homeScore ?: 0} : ${match.awayScore ?: 0} ",
                                color = if (match.status == "finished") NeonWhite else NeonGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        } else {
                            Text(
                                text = " VS ",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        Text(
                            text = match.awayTeamName,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                    Text(
                        text = "Thời gian: ${match.scheduledAt}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Trạng thái: ${match.status.uppercase()}",
                        color = when(match.status) {
                            "ongoing", "live" -> Color.Red
                            "cancelled" -> Color.Gray
                            else -> NeonGreen
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (match.status != "finished" && match.status != "cancelled") {
                        IconButton(onClick = onManage) {
                            Icon(Icons.Default.Edit, contentDescription = "Manage Events", tint = Color.White)
                        }
                    }
                    
                    IconButton(onClick = onToggleFeatured) {
                        Icon(
                            imageVector = if (match.isFeatured == 1) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Toggle Featured",
                            tint = NeonGreen
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (match.status != "cancelled" && match.status != "finished") {
                    TextButton(onClick = onCancel) {
                        Text("HỦY", color = Color.Red, fontSize = 12.sp)
                    }
                    TextButton(onClick = { showRescheduleDialog = true }) {
                        Text("DỜI LỊCH", color = NeonGreen, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (match.status == "scheduled") {
                    Button(
                        onClick = {
                            println("DEBUG: Starting match with ID ${match.id}")
                            onStart() 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("BẮT ĐẦU", color = Color.White, fontSize = 10.sp)
                    }
                } else if (match.status == "ongoing") {
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text("KẾT THÚC", color = Color.Black, fontSize = 10.sp)
                    }
                }
            }
        }
    }

    if (showRescheduleDialog) {
        RescheduleDialog(
            currentDateTime = match.scheduledAt,
            onDismiss = { showRescheduleDialog = false },
            onConfirm = { newTime ->
                onReschedule(newTime)
                showRescheduleDialog = false
            }
        )
    }
}

@Composable
fun AddMatchDialog(onDismiss: () -> Unit, onConfirm: (com.example.qlbongda.data.model.CreateMatchRequest) -> Unit) {
    var homeId by remember { mutableStateOf("") }
    var awayId by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("2025-06-01 19:00:00") }
    var seasonId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo trận đấu mới") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = homeId, onValueChange = { homeId = it }, label = { Text("ID Đội nhà") })
                OutlinedTextField(value = awayId, onValueChange = { awayId = it }, label = { Text("ID Đội khách") })
                OutlinedTextField(value = dateTime, onValueChange = { dateTime = it }, label = { Text("Thời gian (YYYY-MM-DD HH:MM:SS)") })
                OutlinedTextField(value = seasonId, onValueChange = { seasonId = it }, label = { Text("ID Mùa giải") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(com.example.qlbongda.data.model.CreateMatchRequest(
                    homeTeamId = homeId.toIntOrNull() ?: 0,
                    awayTeamId = awayId.toIntOrNull() ?: 0,
                    scheduledAt = dateTime,
                    seasonId = seasonId.toIntOrNull()
                ))
            }) {
                Text("Tạo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun RescheduleDialog(currentDateTime: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var newDateTime by remember { mutableStateOf(currentDateTime) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dời lịch thi đấu") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Nhập thời gian mới:")
                OutlinedTextField(value = newDateTime, onValueChange = { newDateTime = it }, label = { Text("YYYY-MM-DD HH:MM:SS") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(newDateTime) }) {
                Text("Cập nhật")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

// Các màn hình con (Placeholders & Hoàn thiện UI)

@Composable
fun PlayerManagementAdminScreen(viewModel: AdminViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val players by viewModel.players.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchPlayers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ManagementHeader("QUẢN LÝ CẦU THỦ", "Tìm kiếm và quản lý thông tin cầu thủ")

        // Thanh tìm kiếm
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                viewModel.fetchPlayers(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Tìm tên cầu thủ...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonGreen) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(players) { player ->
                    PlayerAdminCard(
                        player = player,
                        onLockClick = { player.userId?.let { viewModel.lockAccount(it) } },
                        onUnlockClick = { player.userId?.let { viewModel.unlockAccount(it) } }
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerAdminCard(player: AdminPlayerItem, onLockClick: () -> Unit, onUnlockClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, if (player.userActive == 0) Color.Red else Color(0xFF222222))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.name ?: "N/A",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Email: ${player.email ?: "N/A"}",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                Text(
                    text = "Vị trí: ${player.position ?: "N/A"} - QG: ${player.nationality ?: "N/A"}",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                if (player.userActive == 0) {
                    Text(
                        text = "TÀI KHOẢN ĐANG BỊ KHÓA",
                        color = Color.Red,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (player.userActive == 0) {
                // Tài khoản đang khóa -> Hiện nút Mở khóa
                IconButton(onClick = onUnlockClick) {
                    Icon(
                        imageVector = Icons.Default.LockOpen, 
                        contentDescription = "Unlock", 
                        tint = NeonGreen
                    )
                }
            } else {
                // Tài khoản đang hoạt động -> Hiện nút Khóa
                IconButton(onClick = onLockClick) {
                    Icon(
                        imageVector = Icons.Default.Lock, 
                        contentDescription = "Lock", 
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun TeamManagementScreen(viewModel: AdminViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val teams by viewModel.teams.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchTeams()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ManagementHeader("QUẢN LÝ ĐỘI BÓNG", "Danh sách các đội bóng đã đăng ký hệ thống")

        // Thanh tìm kiếm
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.fetchTeams(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Tìm tên đội bóng...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonGreen) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(teams) { team ->
                    TeamAdminCard(
                        team = team,
                        onApprove = { team.id?.let { viewModel.approveTeam(it) } },
                        onReject = { team.id?.let { viewModel.rejectTeam(it) } }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamAdminCard(team: AdminTeamItem, onApprove: () -> Unit, onReject: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, if (team.isActive == 0) Color.Yellow else Color(0xFF222222))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = team.name ?: "Đội bóng không tên",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "HLV: ${team.coachName ?: "Chưa cập nhật"}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Mô tả: ${team.description ?: "Không có"}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                
                if (team.isActive == 0) {
                    Text(
                        text = "CHỜ DUYỆT",
                        color = Color.Yellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (team.isActive == 0) {
                Row {
                    IconButton(onClick = onApprove) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Approve", tint = NeonGreen)
                    }
                    IconButton(onClick = onReject) {
                        Icon(Icons.Default.Cancel, contentDescription = "Reject", tint = Color.Red)
                    }
                }
            } else {
                IconButton(onClick = onReject) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete/Deactivate", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun LeagueManagementScreen(viewModel: AdminViewModel) {
    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val tournamentDetail by viewModel.tournamentDetail.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    var showAddTournamentDialog by remember { mutableStateOf(false) }
    var showAddSeasonDialog by remember { mutableStateOf(false) }
    var selectedTournamentId by remember { mutableIntStateOf(-1) }
    var selectedSeasonIdForPhase by remember { mutableIntStateOf(-1) }
    var selectedSeasonId by remember { mutableIntStateOf(-1) }
    LaunchedEffect(Unit) {
        viewModel.fetchTournaments()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedTournamentId == -1) {
            // DANH SÁCH GIẢI ĐẤU
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "QUẢN LÝ GIẢI ĐẤU", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Chọn giải đấu để quản lý", color = Color.LightGray, fontSize = 13.sp)
                }
                IconButton(
                    onClick = { showAddTournamentDialog = true },
                    modifier = Modifier.background(NeonGreen, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Tournament", tint = Color.Black)
                }
            }
            HorizontalDivider(color = Color(0xFF222222))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tournaments) { tournament ->
                        TournamentAdminCard(
                            tournament = tournament,
                            onToggleStatus = { viewModel.updateTournamentStatus(tournament.id, tournament.isActive == 0) },
                            onClick = {
                                if (tournament.isActive == 0) {
                                    // Hiển thị thông báo thay vì gọi API

                                    viewModel.updateMessage("Giải đấu đang tạm dừng, vui lòng kích hoạt trước.")
// Sử dụng:

                                } else {
                                    // Cho phép đi tiếp
                                    selectedTournamentId = tournament.id
                                    viewModel.fetchTournamentDetail(tournament.id)
                                }
                            }
                        )
                    }
                }
            }
        } else {
            // CHI TIẾT GIẢI ĐẤU & CÁC MÙA GIẢI
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { selectedTournamentId = -1 }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                    Text(text = tournamentDetail?.tournament?.name ?: "Mùa giải", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                
                IconButton(
                    onClick = { showAddSeasonDialog = true },
                    modifier = Modifier.background(NeonGreen, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Season", tint = Color.Black)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val seasons = tournamentDetail?.seasons ?: emptyList()
                    items(seasons) { season ->
                        SeasonAdminCard(
                            season = season,
                            onToggleRegistration = { open ->
                                viewModel.toggleRegistration(selectedTournamentId, season.id, open)
                            },
                            onClick = { selectedSeasonId = season.id }
                        )
                    }
                }
            }
        }
    }

    if (showAddSeasonDialog && selectedTournamentId != -1) {
        AddSeasonDialog(
            onDismiss = { showAddSeasonDialog = false },
            onConfirm = { request ->
                viewModel.createSeason(selectedTournamentId, request)
                showAddSeasonDialog = false
            }
        )
    }

<<<<<<< HEAD

=======
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
    if (showAddTournamentDialog) {
        AddTournamentDialog(
            onDismiss = { showAddTournamentDialog = false },
            onConfirm = { name, desc, max,min ->
                viewModel.createTournament(name, desc, max, min)
                showAddTournamentDialog = false
            }
        )
    }

    if (selectedSeasonIdForPhase != -1) {
        AddPhaseDialog(
            onDismiss = { selectedSeasonIdForPhase = -1 },
            onConfirm = { request ->
                viewModel.createPhase(selectedSeasonIdForPhase, request)
                selectedSeasonIdForPhase = -1
            }
        )
    }
    if (selectedSeasonId != -1) {
        SeasonDetailScreen(
            seasonId = selectedSeasonId,
            viewModel = viewModel,
            onBack = { selectedSeasonId = -1 } // Hàm để quay lại danh sách mùa giải
        )
    }
}
// -------------------------------------------------------------
//
// -------------------------------------------------------------
// 1. ĐỊNH NGHĨA TRẠNG THÁI MÀN HÌNH (ROUTER)
sealed class Screen {
    object PhaseList : Screen()
    data class PhaseDetail(val phaseId: Int, val phaseType: String) : Screen()
}

// 2. MÀN HÌNH GỐC (Quản lý việc chuyển đổi giữa Tab và Chi tiết)
@Composable
<<<<<<< HEAD
fun SeasonDetailScreen(seasonId: Int, viewModel: AdminViewModel, onBack: () -> Unit) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.PhaseList) }

=======
fun SeasonDetailScreen(seasonId: Int, viewModel: AdminViewModel, onBack: () -> Unit)  {
    // Gọi load dữ liệu mỗi khi màn hình này xuất hiện
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
    LaunchedEffect(seasonId) {
        viewModel.fetchSeasonDetails(seasonId) // Hàm này sẽ lấy danh sách đội và các vòng đấu của mùa giải
    }

<<<<<<< HEAD
    when (val screen = currentScreen) {
        is Screen.PhaseList -> {
            // Hiển thị giao diện 2 Tabs
            SeasonDetailContent(
                seasonId = seasonId,
                viewModel = viewModel,
                onBack = onBack,
                onNavigateToPhaseDetail = { phaseId, phaseType ->
                    // Điều hướng sang màn hình chi tiết, truyền theo loại vòng đấu
                    currentScreen = Screen.PhaseDetail(phaseId, phaseType)
                }
            )
        }
        is Screen.PhaseDetail -> {
            // Hiển thị màn hình chi tiết (Bảng hoặc Loại trực tiếp)
            PhaseDetailScreen(
                phaseId = screen.phaseId,
                phaseType = screen.phaseType,
                viewModel = viewModel,
                onBack = { currentScreen = Screen.PhaseList } // Quay lại danh sách tab
            )
        }
    }
}

// 3. MÀN HÌNH TABS (Danh sách đội / Vòng đấu)
@Composable
fun SeasonDetailContent(
    seasonId: Int,
    viewModel: AdminViewModel,
    onBack: () -> Unit,
    onNavigateToPhaseDetail: (Int, String) -> Unit // Callback nhận 2 tham số
) {
=======

>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Danh sách đội", "Bảng đấu & Vòng đấu")

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
<<<<<<< HEAD
        // Header
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
            }
            Text("Chi tiết Mùa giải", color = NeonGreen, fontWeight = FontWeight.Bold)
        }

        // Tab Row
=======
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
            }
            Text("Chi tiết Mùa giải", color = Color.White, fontWeight = FontWeight.Bold)
        }
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Black,
            contentColor = NeonGreen
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Nội dung Tab
        when (selectedTabIndex) {
<<<<<<< HEAD
            0 -> SeasonTeamListContent(seasonId, viewModel)
            1 -> SeasonStageContent(
                seasonId = seasonId,
                viewModel = viewModel,
                onNavigateToBracket = onNavigateToPhaseDetail // Truyền thẳng callback xuống đây
            )
        }
    }
}

// 4. MÀN HÌNH CHI TIẾT TỪNG VÒNG ĐẤU (Gộp chung logic)
@Composable
fun PhaseDetailScreen(
    phaseId: Int,
    phaseType: String, // "group" hoặc "knockout"
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val seasonDetail by viewModel.seasonDetail.collectAsState()
    val currentPhase = seasonDetail?.phases?.find { it.id == phaseId }
    val teamsInPhase = currentPhase?.teams ?: emptyList()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color(0xFF121212))) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
            }
            Text(
                text = if (phaseType == "round_robin") "Chi tiết Vòng Bảng" else "Cặp đấu Loại Trực Tiếp",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- NÚT ACTION THEO LOẠI VÒNG ĐẤU ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            if (phaseType == "round_robin") {
                Button(
                    onClick = { /* TODO: Auto Chia Bảng */ },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("Auto Chia Bảng", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { /* TODO: Auto Xếp Lịch */ },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("Auto Xếp Lịch", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { /* TODO: Auto Bốc Thăm */ },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("Auto Bốc Thăm Cặp Đấu", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- HIỂN THỊ DỮ LIỆU ---
        // --- 2. KHU VỰC HIỂN THỊ DỮ LIỆU THẬT ---
        if (currentPhase == null) {
            Text("Đang tải dữ liệu...", color = Color.Gray)
        } else {
            if (phaseType == "round_robin") {
                RoundRobinContent(currentPhase)
            } else {
                KnockoutContent(currentPhase)
            }
        }
    }
}
@Composable
fun RoundRobinContent(phase: PhaseItem) {
    val groups = phase.groups ?: emptyList()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(groups) { group ->Log.d("DEBUG_TEAM", "Đang lọc cho bảng: ${group.name} (ID: ${group.id})")
            phase.teams?.forEach { t ->
                Log.d("DEBUG_TEAM", "Đội: ${t.name}, Team.groupId: ${t.groupId}")
            }
            val groupTeams = phase.teams?.filter { it.groupId == group.id } ?: emptyList()
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(group.name, color = NeonGreen, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    // LẤY TEAM TỪ API ĐÃ GẮN VÀO PHASE
                    val groupTeams = phase.teams?.filter { it.groupId == group.id } ?: emptyList()

                    if (groupTeams.isEmpty()) {
                        Text("Chưa có đội trong bảng", color = Color.Gray, fontSize = 12.sp)
                    } else {
                        groupTeams.forEach { team ->
                            Text(" ${team.name}", color = Color.White, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KnockoutContent(phase: PhaseItem) {
    val teams = phase.teams ?: emptyList()

    if (teams.isEmpty()) {
        Text("Chưa có đội nào trong vòng này.", color = Color.Gray)
    } else {
        Text("Các đội tham gia:", color = NeonGreen, fontWeight = FontWeight.Bold)
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
            items(teams) { team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚽", modifier = Modifier.padding(end = 8.dp))
                    Text(team.name, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                }
                HorizontalDivider(color = Color(0xFF333333)) // Đường kẻ giữa các đội
            }

        }
    }
}
=======
            0 -> SeasonTeamListContent(viewModel)
            1 -> SeasonStageContent(seasonId, viewModel)
        }
    }
}
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
@Composable
fun SeasonTeamListContent(viewModel: AdminViewModel) {
    // Lấy toàn bộ detail từ StateFlow
    val seasonDetail by viewModel.seasonDetail.collectAsState()

    // Lấy danh sách đội từ thuộc tính 'teams' bên trong seasonDetail
    // Sử dụng toán tử ?. để tránh crash nếu dữ liệu chưa tải về (vẫn là null)
    val teamList = seasonDetail?.teams ?: emptyList()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(teamList) { team ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Đảm bảo Row chiếm hết chiều ngang
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Đẩy các phần tử ra hai mép
                    verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
                ) {
                    Text(
                        text = team.name,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Status sẽ tự động nhảy sang bên phải vì ta đã dùng SpaceBetween
                    Text(
                        text = team.status.uppercase(),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End // Căn lề phải cho chữ
                    )
                }
            }
        }
    }
}
<<<<<<< HEAD
// -------------------------------------------------------------
//
// -------------------------------------------------------------
// 1. ĐỊNH NGHĨA TRẠNG THÁI MÀN HÌNH (ROUTER)



// -------------------------------------------------------------
// CÁC HÀM COMPOSABLE PHỤ TRỢ (TÁCH RA TỪ HÀM CHÍNH)
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeasonStageContent(
    seasonId: Int,
    viewModel: AdminViewModel,
    onNavigateToBracket: (Int, String) -> Unit
) {
=======
@Composable
fun SeasonStageContent(seasonId: Int, viewModel: AdminViewModel) {
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
    var showDialog by remember { mutableStateOf(false) }
    val seasonDetail by viewModel.seasonDetail.collectAsState()
    val phases = seasonDetail?.phases ?: emptyList()
    val allTeams = seasonDetail?.teams ?: emptyList()

    var selectedTeamToMove by remember { mutableStateOf<TeamItem?>(null) }
    var sourcePhaseIdForMove by remember { mutableIntStateOf(-1) }

    var showAddTeamDialog by remember { mutableStateOf(false) }
    var selectedPhaseForTeam by remember { mutableStateOf<PhaseItem?>(null) }

<<<<<<< HEAD
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // --- HEADER BUTTONS ---
        HeaderActionButtons(
            onAddPhaseClick = { showDialog = true },
            onAutoScheduleClick = { /* Gọi API Auto Schedule */ }
        )
=======
    var showAutoScheduleDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // --- KHU VỰC ĐỘI BÓNG CHƯA CÓ BẢNG (Dùng để chọn và di chuyển) ---
        val unassignedTeams = allTeams.filter { it.groupId == null }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (selectedTeamToMove == null) "ĐỘI CHƯA XẾP BẢNG (${unassignedTeams.size})" else "ĐANG CHỌN: ${selectedTeamToMove?.name?.uppercase()}",
                color = if (selectedTeamToMove == null) NeonGreen else Color.Yellow,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            // ... (nút gỡ bỏ nếu đang chọn)
        }
        
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (unassignedTeams.isEmpty()) {
                item { Text("Tất cả các đội đã được xếp bảng", color = Color.Gray, fontSize = 11.sp) }
            }
            items(unassignedTeams) { team ->
                val isSelected = selectedTeamToMove?.team_id == team.team_id
                Box(
                    modifier = Modifier
                        .background(if (isSelected) Color.Yellow else Color(0xFF333333), RoundedCornerShape(16.dp))
                        .clickable { 
                            selectedTeamToMove = if (isSelected) null else team
                            sourcePhaseIdForMove = -1
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(team.name, color = if (isSelected) Color.Black else Color.White, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- HEADER BUTTONS ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { showDialog = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
                Text("Thêm vòng đấu", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { showAutoScheduleDialog = true }, 
                modifier = Modifier.weight(1f), 
                border = BorderStroke(1.dp, NeonGreen)
            ) {
                Text("Auto Xếp Lịch", color = NeonGreen, fontWeight = FontWeight.Bold)
            }
        }
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd

        Spacer(modifier = Modifier.height(16.dp))

        // --- DANH SÁCH CÁC VÒNG ĐẤU ---
<<<<<<< HEAD
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(phases) { phase ->
                PhaseCard(
                    phase = phase,
                    allTeams = allTeams,
                    onCardClick = { onNavigateToBracket(phase.id, phase.format) },
                    onAddTeamClick = {
                        selectedPhaseForTeam = phase
                        showAddTeamDialog = true
                    },
                    onMoveTeam = { teamId, targetGroupId ->
                        viewModel.assignTeamToGroup(phase.id, teamId, targetGroupId)
=======
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(phases) { phase ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(text = phase.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                                Text(text = "Định dạng: ${if(phase.format == "round_robin") "Vòng tròn" else "Loại trực tiếp"}",
                                    style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            IconButton(onClick = { selectedPhaseForTeam = phase; showAddTeamDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Thêm đội", tint = NeonGreen)
                            }
                        }
                        val groups = phase.groups ?: emptyList()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState) // Cho phép cuộn ngang
                                .padding(vertical = 8.dp)
                        ) {
                            groups.forEach { group ->
                                GroupCard(
                                    group = group,
                                    allTeams = allTeams,
                                    phaseId = phase.id,
                                    isMoveActive = selectedTeamToMove != null,
                                    onRemoveTeam = { teamId ->
                                        viewModel.removeTeamFromPhase(phase.id, teamId)
                                    },
                                    onSelectTeam = { team ->
                                        selectedTeamToMove = team
                                        sourcePhaseIdForMove = phase.id
                                    },
                                    onMoveHere = {
                                        selectedTeamToMove?.let { team ->
                                            // Truyền team.id (ID của đội trong mùa giải)
                                            viewModel.assignTeamToGroup(phase.id, team.id, group.id)
                                            selectedTeamToMove = null
                                        }
                                    }
                                )
                            }
                        }
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
                    }
                )
            }
        }
    }

    // --- DIALOG THÊM ĐỘI ---
    if (showAddTeamDialog && selectedPhaseForTeam != null) {
        AddTeamToPhaseDialog(
            teams = allTeams,
            groups = selectedPhaseForTeam!!.groups ?: emptyList(),
            onDismiss = { showAddTeamDialog = false },
<<<<<<< HEAD
            onTeamsSelected = { selectedTeams, group ->
                selectedTeams.forEach { team ->
                    viewModel.assignTeamToGroup(selectedPhaseForTeam!!.id, team.team_id, group?.id)
                }
=======
            onTeamSelected = { team, group ->
                viewModel.addTeamToPhase(
                    phaseId = selectedPhaseForTeam!!.id,
                    teamItem = team,
                    groupId = group?.id
                )
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
                showAddTeamDialog = false
            }
        )
    }

    // --- DIALOG THÊM VÒNG ĐẤU ---
    if (showDialog) {
        AddPhaseDialog(
            onDismiss = { showDialog = false },
            onConfirm = { request ->
                viewModel.createPhase(seasonId, request)
                showDialog = false
            }
        )
    }
<<<<<<< HEAD
}
@Composable
fun HeaderActionButtons(onAddPhaseClick: () -> Unit, onAutoScheduleClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onAddPhaseClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
        ) {
            Text("Thêm vòng đấu", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        OutlinedButton(
            onClick = onAutoScheduleClick,
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.dp, NeonGreen)
        ) {
            Text("Auto Xếp Lịch", color = NeonGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PhaseCard(
    phase: PhaseItem,
    allTeams: List<TeamItem>,
    onCardClick: () -> Unit,
    onAddTeamClick: () -> Unit,
    onMoveTeam: (Int, Int?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onCardClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header của Card (Tên vòng, Định dạng, Nút thêm)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = phase.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Text(
                        text = "Định dạng: ${if (phase.format == "round_robin") "Vòng tròn" else "Loại trực tiếp"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                IconButton(onClick = onAddTeamClick) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm đội", tint = NeonGreen)
                }
            }

            // Hiển thị nội dung dựa trên định dạng
            if (phase.format == "round_robin") {
                PhaseGroupView(phase, allTeams, onMoveTeam)
            } else {
                PhaseKnockoutView(phase)
            }
        }
    }
}

@Composable
fun PhaseGroupView(
    phase: PhaseItem,
    allTeams: List<TeamItem>,
    onMoveTeam: (Int, Int?) -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val groups = phase.groups ?: emptyList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(vertical = 8.dp)
    ) {
        groups.forEach { group ->
            GroupCard(
                group = group,
                coroutineScope = scope,
                allTeams = allTeams,
                allGroups = groups,
                onMoveTeam = onMoveTeam,
                modifier = Modifier
                    .width(200.dp)
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun PhaseKnockoutView(phase: PhaseItem) {
    val teamsInPhase = phase.teams ?: emptyList()

    if (teamsInPhase.isEmpty()) {
        Text("Chưa có đội nào trong vòng này", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = NeonGreen)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Đã có ${teamsInPhase.size} đội", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Bấm vào đây để xem các nhánh đấu", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
///////////////////////////////////////////////
///////////////////////////////////////////////
///////////////////////////////////////////////
@OptIn(ExperimentalFoundationApi::class)
=======

    if (showAutoScheduleDialog) {
        AutoSchedulePhaseSelectorDialog(
            phases = phases,
            seasonStartDate = seasonDetail?.season?.startDate,
            onDismiss = { showAutoScheduleDialog = false },
            onConfirm = { phaseId, options ->
                viewModel.autoGenerateSchedule(phaseId, options)
                showAutoScheduleDialog = false
            }
        )
    }
}
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
@Composable
fun GroupCard(
    group: GroupItem, 
    allTeams: List<TeamItem>, 
    phaseId: Int,
    isMoveActive: Boolean,
    onRemoveTeam: (Int) -> Unit,
    onSelectTeam: (TeamItem) -> Unit,
    onMoveHere: () -> Unit
) {
    val teamsInGroup = allTeams.filter { it.groupId == group.id }

    Column(
        modifier = Modifier
            .width(160.dp)
            .padding(end = 8.dp)
            .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
            .clickable(enabled = isMoveActive) { onMoveHere() }
            .border(
                width = if (isMoveActive) 2.dp else 0.dp,
                color = if (isMoveActive) NeonGreen else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = group.name.uppercase(), color = NeonGreen, fontWeight = FontWeight.Bold)
            if (isMoveActive) {
                Icon(Icons.Default.ArrowDownward, contentDescription = "Thả vào đây", tint = NeonGreen, modifier = Modifier.size(16.dp))
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFF333333))

        if (teamsInGroup.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp), contentAlignment = Alignment.Center) {
                Text(if (isMoveActive) "Chạm để thả" else "Chưa có đội", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        } else {
            teamsInGroup.forEach { team ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable { onSelectTeam(team) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = team.name, style = MaterialTheme.typography.bodyMedium, color = Color.White, maxLines = 1)
                    }
                    IconButton(
                        onClick = { onRemoveTeam(team.team_id) },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Xóa đội",
                            tint = Color.Red,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun AddTeamToPhaseDialog(
    teams: List<TeamItem>,
    groups: List<GroupItem>, // Danh sách nhóm từ phase
    onDismiss: () -> Unit,
    onTeamSelected: (TeamItem, GroupItem?) -> Unit
) {
    var selectedTeam by remember { mutableStateOf<TeamItem?>(null) }
    var selectedGroup by remember { mutableStateOf<GroupItem?>(null) }

    // Ràng buộc điều kiện:
    // - Phải chọn Team.
    // - NẾU có danh sách bảng đấu (groups.isNotEmpty()) thì PHẢI chọn cả bảng đấu.
    val isConfirmEnabled = selectedTeam != null && (groups.isEmpty() || selectedGroup != null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm đội vào vòng đấu", color = Color.White) },
        text = {
            Column {
                // 1. Chọn Team
                Text("Chọn đội:", color = Color.Gray)
                LazyColumn(modifier = Modifier.height(150.dp)) {
                    items(teams) { team ->
                        TextButton(onClick = { selectedTeam = team }) {
                            Text(team.name, color = if (selectedTeam == team) NeonGreen else Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Chọn Group (Chỉ hiện nếu có danh sách nhóm)
                if (groups.isNotEmpty()) {
                    Text("Chọn bảng đấu:", color = Color.Gray)
                    LazyRow {
                        items(groups) { group ->
                            TextButton(
                                onClick = { selectedGroup = group },
                                // Thêm background để dễ nhận diện đang chọn bảng nào
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (selectedGroup == group) Color.DarkGray else Color.Transparent
                                )
                            ) {
                                Text(group.name, color = if (selectedGroup == group) NeonGreen else Color.White)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isConfirmEnabled) {
                        onTeamSelected(selectedTeam!!, selectedGroup)
                        onDismiss()
                    }
                },
                enabled = isConfirmEnabled // Nút chỉ sáng khi chọn đủ điều kiện
            ) {
                Text("Xác nhận", color = if (isConfirmEnabled) NeonGreen else Color.Gray)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.White)
            }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}


@Composable
fun AddSeasonDialog(onDismiss: () -> Unit, onConfirm: (com.example.qlbongda.data.model.CreateSeasonRequest) -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("2025-01-01") }
    var endDate by remember { mutableStateOf("2025-06-30") }
    var deadline by remember { mutableStateOf("2024-12-25") }
    var maxTeams by remember { mutableStateOf("16") }
    var fee by remember { mutableStateOf("0") }
    var isRegOpen by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Mùa giải mới") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên mùa giải (VD: Mùa Xuân 2025)") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Mô tả ngắn") })
                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Ngày bắt đầu (YYYY-MM-DD)") })
                OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("Ngày kết thúc (YYYY-MM-DD)") })
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Hạn đăng ký (YYYY-MM-DD)") })
                OutlinedTextField(value = maxTeams, onValueChange = { maxTeams = it }, label = { Text("Số đội tối đa") })
                OutlinedTextField(value = fee, onValueChange = { fee = it }, label = { Text("Lệ phí đăng ký (VNĐ)") })
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isRegOpen, onCheckedChange = { isRegOpen = it })
                    Text("Mở cổng đăng ký ngay")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(com.example.qlbongda.data.model.CreateSeasonRequest(
                            name = name,
                            description = desc.ifBlank { null },
                            startDate = startDate,
                            endDate = endDate,
                            registrationDeadline = deadline,
                            isRegistrationOpen = isRegOpen,
                            userId = null,
                            maxTeams = maxTeams.toIntOrNull() ?: 16,
                            registrationFee = fee.toDoubleOrNull() ?: 0.0
                        ))
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Tạo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun SeasonAdminCard(
    season: com.example.qlbongda.data.model.SeasonAdminItem, 
    onToggleRegistration: (Boolean) -> Unit,
    onClick: () -> Unit //
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(1.dp, Color(0xFF333333))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = season.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Trạng thái: ${season.status.uppercase()}", color = NeonGreen, fontSize = 12.sp)
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text("Cổng đăng ký", color = Color.Gray, fontSize = 10.sp)
                    Switch(
                        checked = season.isRegistrationOpen == 1,
                        onCheckedChange = onToggleRegistration,
                        colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen)
                    )
                }
            }

            Text(
                text = "Hạn đăng ký: ${DateUtils.formatTime(season.registrationDeadline ?: "")}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhaseDialog(
    onDismiss: () -> Unit,
    onConfirm: (com.example.qlbongda.data.model.CreatePhaseRequest) -> Unit,

) {
    val phaseMapping = mapOf(
        "Vòng Bảng" to "group_stage",
        "Vòng 1/16" to "round_of_16",
        "Tứ Kết" to "quarter_final",
        "Bán Kết" to "semi_final",
        "Chung Kết" to "final"
    )

    val formatMap = mapOf(
        "round_robin" to "Vòng tròn tính điểm",
        "knockout" to "Loại trực tiếp"
    )

    var selectedName by remember { mutableStateOf("Vòng Bảng") }
    var selectedFormat by remember { mutableStateOf("round_robin") }
    var order by remember { mutableStateOf("1") }
    var groupCount by remember { mutableStateOf("1") }

    var expandedName by remember { mutableStateOf(false) }
    var expandedFormat by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Vòng đấu") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // 1. Dropdown Tên
                ExposedDropdownMenuBox(expanded = expandedName, onExpandedChange = { expandedName = !expandedName }) {
                    OutlinedTextField(
                        value = selectedName, onValueChange = {}, readOnly = true, label = { Text("Tên vòng đấu") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedName) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedName, onDismissRequest = { expandedName = false }) {
                        phaseMapping.keys.forEach { name ->
                            DropdownMenuItem(text = { Text(name) }, onClick = { selectedName = name; expandedName = false })
                        }
                    }
                }

                // 2. Type (Hiện ra nhưng không sửa được)
                OutlinedTextField(
                    value = phaseMapping[selectedName] ?: "",
                    onValueChange = {}, enabled = false, label = { Text("Kiểu vòng (Auto)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // 3. Order (Thứ tự)
                OutlinedTextField(value = order, onValueChange = { order = it }, label = { Text("Thứ tự (Order)") })

                // 4. Dropdown Định dạng
                ExposedDropdownMenuBox(
                    expanded = expandedFormat,
                    onExpandedChange = { expandedFormat = !expandedFormat }
                ) {
                    OutlinedTextField(
                        // Hiển thị giá trị tiếng Việt tương ứng với key (selectedFormat)
                        value = formatMap[selectedFormat] ?: selectedFormat,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Định dạng") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFormat) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFormat,
                        onDismissRequest = { expandedFormat = false }
                    ) {
                        formatMap.forEach { (key, label) ->
                            DropdownMenuItem(
                                text = { Text(label) }, // Hiển thị "Vòng tròn tính điểm"
                                onClick = {
                                    selectedFormat = key // Gán "round_robin" vào biến selectedFormat
                                    expandedFormat = false
                                }
                            )
                        }
                    }
                }

                if (selectedFormat == "round_robin") {
                    OutlinedTextField(value = groupCount, onValueChange = { groupCount = it }, label = { Text("Số lượng bảng") })
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(com.example.qlbongda.data.model.CreatePhaseRequest(
                    name = selectedName,
                    type = phaseMapping[selectedName] ?: "group_stage",
                    format = selectedFormat,
                    order = order.toIntOrNull() ?: 1,
                    groupCount = if (selectedFormat == "round_robin") groupCount.toIntOrNull() else null
                ))
            }) { Text("Tạo") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}
@Composable
<<<<<<< HEAD
=======
fun AutoSchedulePhaseSelectorDialog(
    phases: List<com.example.qlbongda.data.model.PhaseItem>,
    seasonStartDate: String?,
    onDismiss: () -> Unit,
    onConfirm: (Int, com.example.qlbongda.data.model.ScheduleOptionsRequest) -> Unit
) {
    var selectedPhaseId by remember { mutableIntStateOf(-1) }
    var isAutoDate by remember { mutableStateOf(true) }
    var isAutoTime by remember { mutableStateOf(true) }
    
    // Format YYYY-MM-DD
    var startDate by remember(seasonStartDate) { 
        mutableStateOf(seasonStartDate?.split("T")?.get(0) ?: "2025-06-01") 
    }
    var startTime by remember { mutableStateOf("18:00") }
    var intervalHours by remember { mutableIntStateOf(2) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Cấu hình xếp lịch tự động", color = Color.White) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("1. Chọn Vòng đấu:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGreen)
                if (phases.isEmpty()) {
                    Text("Không có vòng đấu nào. Hãy thêm vòng đấu trước.", color = Color.Red)
                } else {
                    phases.forEach { phase ->
                        val isScheduled = !phase.matches.isNullOrEmpty()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .clickable(enabled = !isScheduled) { selectedPhaseId = phase.id }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedPhaseId == phase.id, 
                                onClick = { if (!isScheduled) selectedPhaseId = phase.id },
                                enabled = !isScheduled,
                                colors = RadioButtonDefaults.colors(selectedColor = NeonGreen)
                            )
                            Column {
                                Text(phase.name, fontSize = 14.sp, color = if (isScheduled) Color.Gray else Color.White)
                                if (isScheduled) {
                                    Text("(Đã có lịch đấu)", fontSize = 10.sp, color = Color.Red)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("2. Tùy chọn thời gian:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGreen)
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAutoDate, onCheckedChange = { isAutoDate = it }, colors = CheckboxDefaults.colors(checkedColor = NeonGreen))
                    Text("Tự động chọn ngày (Bắt đầu từ ngày mùa giải)", color = Color.White, fontSize = 12.sp)
                }
                if (!isAutoDate) {
                    OutlinedTextField(
                        value = startDate, 
                        onValueChange = { startDate = it }, 
                        label = { Text("Ngày bắt đầu (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAutoTime, onCheckedChange = { isAutoTime = it }, colors = CheckboxDefaults.colors(checkedColor = NeonGreen))
                    Text("Tự động chọn giờ (Mặc định 18:00)", color = Color.White, fontSize = 12.sp)
                }
                if (!isAutoTime) {
                    OutlinedTextField(
                        value = startTime, 
                        onValueChange = { startTime = it }, 
                        label = { Text("Giờ bắt đầu (HH:MM)") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("3. Khoảng cách giữa các trận (giờ):", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGreen)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..10).forEach { hour ->
                        FilterChip(
                            selected = intervalHours == hour,
                            onClick = { intervalHours = hour },
                            label = { Text(hour.toString()) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonGreen,
                                labelColor = Color.White,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        selectedPhaseId,
                        com.example.qlbongda.data.model.ScheduleOptionsRequest(
                            startDate = if (isAutoDate) null else startDate.ifBlank { null },
                            startTime = if (isAutoTime) null else startTime.ifBlank { null },
                            intervalHours = intervalHours
                        )
                    )
                },
                enabled = selectedPhaseId != -1,
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Chạy tự động", color = Color.Black)
            }
        },
        dismissButton = { 
            TextButton(onClick = onDismiss) { 
                Text("Hủy", color = Color.Gray) 
            } 
        }
    )
}

@Composable
>>>>>>> 43d5a9cebcb60ec5d8bc6dbd01ae01a3fadbacfd
fun TournamentAdminCard(
    tournament: com.example.qlbongda.data.model.TournamentItem, 
    onToggleStatus: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, if (tournament.isActive == 1) NeonGreen.copy(alpha = 0.5f) else Color.Gray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tournament.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                tournament.description?.let {
                    Text(it, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
                }
               Text("Số cầu thủ tối đa: ${tournament.maxPlayers ?: "Chưa thiết lập"}", color = Color.LightGray, fontSize = 12.sp)
                Text("Số cầu thủ tối thiểu: ${tournament.minPlayers ?: "Chưa thiết lập"}", color = Color.LightGray, fontSize = 12.sp)
            }
            Switch(
                checked = tournament.isActive == 1,
                onCheckedChange = { onToggleStatus() },
                colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen, checkedTrackColor = NeonGreen.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun AddTournamentDialog(
    onDismiss: () -> Unit,
    // Cập nhật onConfirm để nhận thêm tham số: (Tên, Mô tả, Cầu thủ tối đa, Cầu thủ tối thiểu)
    onConfirm: (String, String, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("20") } // Mặc định là 20
    var minPlayers by remember { mutableStateOf("7") }  // Mặc định là 7

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo giải đấu mới") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên giải đấu") }
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Mô tả") }
                )
                // Ô nhập cầu thủ tối đa
                OutlinedTextField(
                    value = maxPlayers,
                    onValueChange = { maxPlayers = it },
                    label = { Text("Cầu thủ tối đa/đội") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                // Ô nhập cầu thủ tối thiểu
                OutlinedTextField(
                    value = minPlayers,
                    onValueChange = { minPlayers = it },
                    label = { Text("Cầu thủ tối thiểu/đội") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                // Chuyển đổi String sang Int khi xác nhận
                onConfirm(
                    name,
                    desc,
                    maxPlayers.toIntOrNull() ?: 20,
                    minPlayers.toIntOrNull() ?: 7
                )
            }) {
                Text("Tạo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun NotificationManagementScreen(viewModel: AdminViewModel) {
    // Biến nhớ tab hiện tại
    var selectedTab by remember { mutableIntStateOf(0) }

    // Thu thập state từ ViewModel
    val selectedNotification by viewModel.selectedNotification.collectAsStateWithLifecycle()
    val showCleanupDialog by viewModel.showCleanupDialog.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()

    val generalList by viewModel.generalNotifications.collectAsStateWithLifecycle()
    val teamList by viewModel.teamNotifications.collectAsStateWithLifecycle()
    val personalList by viewModel.personalNotifications.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.fetchNotifications() // Đảm bảo hàm này trong ViewModel gọi 3 API của bạn
    }
    // Lọc dữ liệu dựa trên tab đang chọn
    val displayList = remember(selectedTab, generalList, teamList, personalList) {
        when (selectedTab) {
            0 -> generalList
            1 -> teamList
            else -> personalList
        }
    }
    if (showAddDialog) {
        AddNotificationDialog(
            onDismiss = { viewModel.setShowAddDialog(false) },
            onConfirm = { title, content, type, teamId, userId ->
                viewModel.createNotification(title, content, type, teamId, userId)
                viewModel.setShowAddDialog(false)
            }
        )
    }

    // Dialog chỉnh sửa (sẽ hiển thị khi selectedNotification khác null)
    if (selectedNotification != null) {
        EditNotificationDialog(
            notification = selectedNotification!!,
            onDismiss = { viewModel.setSelectedNotification(null) },
            onConfirm = { title, content, type, isActive ->
                viewModel.updateNotification(selectedNotification!!.id, title, content, type, isActive)
                viewModel.setSelectedNotification(null)
            }
        )
    }
    if (showCleanupDialog) {
        CleanupConfirmationDialog(
            onDismiss = { viewModel.setShowCleanupDialog(false) },
            onConfirm = {
                viewModel.triggerCleanupNotifications()
                viewModel.setShowCleanupDialog(false)
            }
        )
    }

    // Màn hình chính
    Scaffold(
        containerColor = NeonBlack,
        floatingActionButton = {
            Column {
                // Nút Dọn dẹp: Đỏ Neon + Icon Đen
                // Nút Dọn dẹp: Bấm vào chỉ làm hiện Dialog
                FloatingActionButton(
                    onClick = { viewModel.setShowCleanupDialog(true) },
                    containerColor = NeonRed,
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Dọn dẹp")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút Thêm: Xanh Neon + Icon Đen
                FloatingActionButton(
                    onClick = { viewModel.setShowAddDialog(true) },
                    containerColor = NeonGreen, // Dùng biến NeonGreen của bạn
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(NeonBlack)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NeonSurface,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator( // Sử dụng SecondaryIndicator cho Compose mới
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonGreen,
                        height = 3.dp
                    )
                }
            ) {
                val tabs = listOf("Chung", "Theo Team", "Cá nhân")
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                // Hiệu ứng đổi màu chữ dựa trên trạng thái chọn
                                color = if (selectedTab == index) NeonGreen else NeonGray,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 160.dp)
            ) {
                items(displayList) { item ->
                    // Card sẽ tự hòa vào nền đen vì dùng NeonSurface
                    NotificationItemCard(
                        item = item,
                        onDelete = { viewModel.deleteNotification(item.id) },
                        onEdit = { viewModel.setSelectedNotification(item) }
                    )
                }
            }
        }
    }
}
@Composable
fun AddNotificationDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int?, Int?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("general") }
    var targetTeamId by remember { mutableStateOf("") }
    var recipientUserId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeonSurface,
        title = { Text("Thêm thông báo mới", color = NeonWhite) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Tiêu đề") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Nội dung") })

                // Chọn loại thông báo
                Text("Loại: $type", color = NeonGreen)
                Row {
                    RadioButton(selected = type == "general", onClick = { type = "general" })
                    Text("Chung", color = NeonWhite)
                    RadioButton(selected = type == "match_schedule", onClick = { type = "match_schedule" })
                    Text("Đội", color = NeonWhite)
                    RadioButton(selected = type == "player_approved", onClick = { type = "player_approved" })
                    Text("Cá nhân", color = NeonWhite)
                }

                if (type == "match_schedule") {
                    OutlinedTextField(value = targetTeamId, onValueChange = { targetTeamId = it }, label = { Text("ID Team") })
                }
                if (type == "player_approved") {
                    OutlinedTextField(value = recipientUserId, onValueChange = { recipientUserId = it }, label = { Text("ID Người nhận") })
                }
                errorMessage?.let {
                    Text(it, color = NeonRed, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // LOGIC VALIDATE
                when {
                    title.isBlank() -> errorMessage = "Vui lòng nhập tiêu đề"
                    content.isBlank() -> errorMessage = "Vui lòng nhập nội dung"
                    type == "match_schedule" && targetTeamId.isBlank() -> errorMessage = "Vui lòng nhập ID Team"
                    type == "player_approved" && recipientUserId.isBlank() -> errorMessage = "Vui lòng nhập ID Người nhận"
                    else -> {
                        // Nếu mọi thứ đều ổn
                        onConfirm(title, content, type, targetTeamId.toIntOrNull(), recipientUserId.toIntOrNull())
                    }
                }
            }) {
                Text("Gửi", color = NeonGreen)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy", color = NeonGray) } }
    )
}
@Composable
fun CleanupConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeonSurface, // Màu nền đen xám của bạn
        title = { Text("Xác nhận dọn dẹp", color = NeonRed) },
        text = {
            Text(
                "Dọn dẹp tất cả thông báo cũ hơn 30 ngày? Hành động này không thể hoàn tác.",
                color = NeonGray
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Xác nhận", color = NeonRed) // Màu đỏ Neon cho hành động nguy hiểm
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = NeonGreen)
            }
        }
    )
}
@Composable
fun EditNotificationDialog(
    notification: NotificationItem,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf(notification.title) }
    var content by remember { mutableStateOf(notification.content) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chỉnh sửa thông báo") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Tiêu đề") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Nội dung") })
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(title, content, notification.type, 1) }) {
                Text("Lưu")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}
@Composable
fun NotificationItemCard(item: NotificationItem, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), // Chiếm hết chiều rộng
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.content, maxLines = 2, fontSize = 14.sp)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Sửa") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Xóa") }
        }
    }
}
@Composable
fun PaymentConfirmationScreen() {
    ManagementHeader("XÁC NHẬN THANH TOÁN", "Danh sách các giao dịch chờ phê duyệt")

    val pendingPayments = remember {
        listOf(
            "Đội Arsenal - Lệ phí: 5.000.000đ",
            "Đội Chelsea - Lệ phí: 5.000.000đ",
            "Đội MU - Lệ phí: 5.000.000đ"
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(pendingPayments) { payment ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(payment, color = Color.White, fontSize = 14.sp)
                    Button(
                        onClick = { /* Logic phê duyệt */ },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Duyệt", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchEventManagementScreen(matchId: Int, viewModel: AdminViewModel, onBack: () -> Unit) {
    val matchDetail by viewModel.matchDetail.collectAsStateWithLifecycle()
    val events by viewModel.matchEvents.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var showScoreDialog by remember { mutableStateOf(false) }
    var showSubDialog by remember { mutableStateOf(false) }
    var showCardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(matchId) {
        viewModel.fetchMatchDetail(matchId)
        viewModel.fetchMatchEvents(matchId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý sự kiện trận đấu", color = NeonGreen) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                matchDetail?.let { match ->
                    // Score Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(match.teamA, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("${match.scoreA}", color = NeonGreen, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Text("VS", color = Color.Gray)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(match.teamB, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("${match.scoreB}", color = NeonGreen, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                            Button(
                                onClick = { showScoreDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                            ) {
                                Text("Cập nhật tỉ số", color = Color.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { showSubDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("Thay người", color = Color.White, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { showCardDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("Thẻ phạt", color = Color.White, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Danh sách sự kiện", color = NeonGreen, fontWeight = FontWeight.Bold)
                    
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
                        items(events) { event ->
                            EventAdminItem(event, onDelete = { viewModel.deleteEvent(matchId, event.id) })
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showScoreDialog && matchDetail != null) {
        UpdateScoreDialog(
            currentScoreA = matchDetail!!.scoreA,
            currentScoreB = matchDetail!!.scoreB,
            onDismiss = { showScoreDialog = false },
            onConfirm = { home, away ->
                viewModel.updateScore(matchId, home, away)
                showScoreDialog = false
            }
        )
    }

    if (showSubDialog && matchDetail != null) {
        SubstitutionDialog(
            matchDetail = matchDetail!!,
            onDismiss = { showSubDialog = false },
            onConfirm = { teamId, pIn, pOut, min, period ->
                viewModel.addSubstitution(matchId, teamId, pIn, pOut, min, period)
                showSubDialog = false
            }
        )
    }

    if (showCardDialog && matchDetail != null) {
        CardDialog(
            matchDetail = matchDetail!!,
            onDismiss = { showCardDialog = false },
            onConfirm = { teamId, playerId, min, period, isRed ->
                viewModel.addCard(matchId, teamId, playerId, min, period, isRed)
                showCardDialog = false
            }
        )
    }
}

@Composable
fun EventAdminItem(event: com.example.qlbongda.data.model.MatchEventDetailed, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(NeonGreen.copy(alpha = 0.2f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("${event.minute}'", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                val typeText = when(event.type) {
                    "yellow_card" -> "Thẻ vàng"
                    "red_card" -> "Thẻ đỏ"
                    "substitution_in" -> "Vào sân: ${event.playerName}"
                    "substitution_out" -> "Rời sân: ${event.playerName}"
                    else -> event.type
                }
                Text(typeText, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (event.type == "substitution_in" && event.subOutPlayerName != null) {
                    Text("Thay cho: ${event.subOutPlayerName}", color = Color.Gray, fontSize = 12.sp)
                } else if (event.playerName != null && !event.type.startsWith("sub")) {
                    Text(event.playerName, color = Color.Gray, fontSize = 12.sp)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun UpdateScoreDialog(currentScoreA: Int, currentScoreB: Int, onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit) {
    var scoreA by remember { mutableStateOf(currentScoreA.toString()) }
    var scoreB by remember { mutableStateOf(currentScoreB.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cập nhật tỉ số") },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                OutlinedTextField(
                    value = scoreA,
                    onValueChange = { scoreA = it },
                    modifier = Modifier.width(60.dp),
                    label = { Text("Home") }
                )
                Text(" - ", modifier = Modifier.padding(horizontal = 8.dp))
                OutlinedTextField(
                    value = scoreB,
                    onValueChange = { scoreB = it },
                    modifier = Modifier.width(60.dp),
                    label = { Text("Away") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(scoreA.toIntOrNull() ?: 0, scoreB.toIntOrNull() ?: 0) }) {
                Text("Cập nhật")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun SubstitutionDialog(
    matchDetail: FullMatchDetail,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, Int, String) -> Unit
) {
    var selectedTeamId by remember { mutableIntStateOf(0) }
    var playerInId by remember { mutableIntStateOf(0) }
    var playerOutId by remember { mutableIntStateOf(0) }
    var minute by remember { mutableStateOf("45") }
    var period by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay người") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Chọn đội bóng:", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedTeamId == 0, onClick = { selectedTeamId = 0 })
                    Text(matchDetail.teamA, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = selectedTeamId == 1, onClick = { selectedTeamId = 1 })
                    Text(matchDetail.teamB, fontSize = 14.sp)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = minute, onValueChange = { minute = it }, label = { Text("Phút thay người") })
                OutlinedTextField(value = period, onValueChange = { period = it }, label = { Text("Hiệp (1 hoặc 2)") })
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Nhập ID Cầu thủ (Primary Key từ DB):", fontSize = 12.sp, color = Color.Gray)
                OutlinedTextField(value = playerInId.toString(), onValueChange = { playerInId = it.toIntOrNull() ?: 0 }, label = { Text("ID Cầu thủ VÀO") })
                OutlinedTextField(value = playerOutId.toString(), onValueChange = { playerOutId = it.toIntOrNull() ?: 0 }, label = { Text("ID Cầu thủ RA") })
            }
        },
        confirmButton = {
            val teamId = if (selectedTeamId == 0) matchDetail.teamAId else matchDetail.teamBId
            Button(onClick = { 
                if (teamId != 0 && playerInId != 0 && playerOutId != 0) {
                    onConfirm(teamId, playerInId, playerOutId, minute.toIntOrNull() ?: 0, period) 
                }
            }) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun CardDialog(
    matchDetail: FullMatchDetail,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, String, Boolean) -> Unit
) {
    var selectedTeamId by remember { mutableIntStateOf(0) }
    var playerId by remember { mutableIntStateOf(0) }
    var minute by remember { mutableStateOf("45") }
    var period by remember { mutableStateOf("1") }
    var isRed by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ghi nhận thẻ phạt") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Chọn đội bóng:", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedTeamId == 0, onClick = { selectedTeamId = 0 })
                    Text(matchDetail.teamA, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = selectedTeamId == 1, onClick = { selectedTeamId = 1 })
                    Text(matchDetail.teamB, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = playerId.toString(), onValueChange = { playerId = it.toIntOrNull() ?: 0 }, label = { Text("ID Cầu thủ nhận thẻ") })
                OutlinedTextField(value = minute, onValueChange = { minute = it }, label = { Text("Phút") })
                OutlinedTextField(value = period, onValueChange = { period = it }, label = { Text("Hiệp") })
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isRed, onCheckedChange = { isRed = it })
                    Text("Thẻ đỏ")
                }
            }
        },
        confirmButton = {
            val teamId = if (selectedTeamId == 0) matchDetail.teamAId else matchDetail.teamBId
            Button(onClick = { 
                if (teamId != 0 && playerId != 0) {
                    onConfirm(teamId, playerId, minute.toIntOrNull() ?: 0, period, isRed) 
                }
            }) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun ManagementHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title, color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = subtitle, color = Color.LightGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFF222222))
    }
}
