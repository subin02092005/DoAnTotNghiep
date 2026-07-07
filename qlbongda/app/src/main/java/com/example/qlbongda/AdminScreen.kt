package com.example.qlbongda

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.data.model.AdminPlayerItem
import com.example.qlbongda.data.model.AdminTeamItem
import com.example.qlbongda.data.model.CreateNotificationRequest
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.NotificationItem
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.ui.theme.NeonGreen

val NeonBlack = Color(0xFF0A0A0A)      // Đen sâu
val NeonSurface = Color(0xFF161616)    // Xám rất tối (cho Card & Tab)
val NeonGray = Color(0xFF666666)       // Xám cho chữ phụ
val NeonRed = Color(0xFFFF003C)
val NeonWhite = Color(0xFFFFFFFF) // <--- THÊM DÒNG NÀY VÀO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    matchList: List<FullMatchDetail>, // 🌟 NHẬN DANH SÁCH DÙNG CHUNG TỪ NGOÀI TRUYỀN VÀO
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
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    var showAutoScheduleDialog by remember { mutableStateOf(false) }
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
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Tất cả", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Nổi bật", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
            val displayList = if (selectedTab == 0) matches else featuredMatches
            
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
    if (showAutoScheduleDialog) {
        var inputPhaseId by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAutoScheduleDialog = false },
            title = { Text("Tạo lịch thi đấu tự động") },
            text = {
                Column {
                    Text("Hệ thống sẽ tự động tạo cặp đấu vòng tròn cho tất cả các bảng trong Vòng đấu này.", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputPhaseId,
                        onValueChange = { inputPhaseId = it },
                        label = { Text("Nhập ID Vòng đấu (Phase ID)") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    inputPhaseId.toIntOrNull()?.let {
                        viewModel.autoGenerateSchedule(it)
                    }
                    showAutoScheduleDialog = false
                }) {
                    Text("Chạy tự động")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAutoScheduleDialog = false }) { Text("Hủy") }
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
                    Text(
                        text = "${match.homeTeamName} VS ${match.awayTeamName}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                    IconButton(onClick = onManage) {
                        Icon(Icons.Default.Edit, contentDescription = "Manage Events", tint = Color.White)
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
    var selectedSeasonIdForAutoSchedule by remember { mutableIntStateOf(-1) }

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
                                selectedTournamentId = tournament.id
                                viewModel.fetchTournamentDetail(tournament.id)
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
                            onAddPhase = { selectedSeasonIdForPhase = season.id },
                            onAutoSchedule = {
                                viewModel.fetchSeasonPhasesForAdmin(season.id)
                                selectedSeasonIdForAutoSchedule = season.id
                            },
                            onToggleRegistration = { open ->
                                viewModel.toggleRegistration(selectedTournamentId, season.id, open)
                            }
                        )
                    }
                }
            }
        }
    }

    val phasesOfSeason by viewModel.phasesOfSeason.collectAsStateWithLifecycle()

    if (showAddSeasonDialog && selectedTournamentId != -1) {
        AddSeasonDialog(
            onDismiss = { showAddSeasonDialog = false },
            onConfirm = { request ->
                viewModel.createSeason(selectedTournamentId, request)
                showAddSeasonDialog = false
            }
        )
    }

    if (selectedSeasonIdForAutoSchedule != -1) {
        AutoSchedulePhaseSelectorDialog(
            phases = phasesOfSeason,
            onDismiss = { selectedSeasonIdForAutoSchedule = -1 },
            onConfirm = { phaseId, options ->
                viewModel.autoGenerateSchedule(phaseId, options)
                selectedSeasonIdForAutoSchedule = -1
            }
        )
    }

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
            seasonId = selectedSeasonIdForPhase,
            onDismiss = { selectedSeasonIdForPhase = -1 },
            onConfirm = { request ->
                viewModel.createPhase(selectedSeasonIdForPhase, request)
                selectedSeasonIdForPhase = -1
            }
        )
    }
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
    onAddPhase: () -> Unit, 
    onAutoSchedule: () -> Unit,
    onToggleRegistration: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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

            Text(text = "Hạn đăng ký: ${season.registrationDeadline}", color = Color.Gray, fontSize = 12.sp)
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAddPhase,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Thêm Vòng", fontSize = 11.sp, color = NeonGreen)
                }
                Button(
                    onClick = onAutoSchedule,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Auto Xếp Lịch", fontSize = 11.sp, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun AddPhaseDialog(
    seasonId: Int,
    onDismiss: () -> Unit,
    onConfirm: (com.example.qlbongda.data.model.CreatePhaseRequest) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("group_stage") }
    var format by remember { mutableStateOf("round_robin") }
    var order by remember { mutableStateOf("1") }
    var groupCount by remember { mutableStateOf("1") }

    val phaseTypes = listOf("group_stage", "round_of_16", "quarter_final", "semi_final", "final")
    val formats = listOf("round_robin", "knockout")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Vòng đấu") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên vòng đấu (VD: Vòng Bảng)") })
                
                Text("Kiểu vòng:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    phaseTypes.take(3).forEach { pType ->
                        FilterChip(selected = type == pType, onClick = { type = pType }, label = { Text(pType, fontSize = 10.sp) })
                    }
                }

                Text("Định dạng:")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    formats.forEach { fmt ->
                        FilterChip(selected = format == fmt, onClick = { format = fmt }, label = { Text(fmt) })
                    }
                }

                OutlinedTextField(value = order, onValueChange = { order = it }, label = { Text("Thứ tự (Order)") })
                
                if (format == "round_robin") {
                    OutlinedTextField(value = groupCount, onValueChange = { groupCount = it }, label = { Text("Số lượng bảng") })
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(com.example.qlbongda.data.model.CreatePhaseRequest(
                    name = name,
                    type = type,
                    format = format,
                    order = order.toIntOrNull() ?: 1,
                    groupCount = if (format == "round_robin") groupCount.toIntOrNull() else null
                ))
            }) { Text("Tạo") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun AutoSchedulePhaseSelectorDialog(
    phases: List<com.example.qlbongda.data.model.TournamentPhase>,
    onDismiss: () -> Unit,
    onConfirm: (Int, com.example.qlbongda.data.model.ScheduleOptionsRequest) -> Unit
) {
    var selectedPhaseId by remember { mutableIntStateOf(-1) }
    var startDate by remember { mutableStateOf("2025-06-01") }
    var startTime by remember { mutableStateOf("18:00") }
    var intervalHours by remember { mutableStateOf("2") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cấu hình xếp lịch tự động") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("1. Chọn Vòng đấu:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if (phases.isEmpty()) {
                    Text("Không có vòng đấu nào. Hãy thêm vòng đấu trước.", color = Color.Red)
                } else {
                    phases.forEach { phase ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable { selectedPhaseId = phase.id }.padding(vertical = 4.dp)
                        ) {
                            RadioButton(selected = selectedPhaseId == phase.id, onClick = { selectedPhaseId = phase.id })
                            Text(phase.name, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("2. Tùy chọn thời gian:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Ngày bắt đầu (YYYY-MM-DD)") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Giờ bắt đầu (HH:MM)") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = intervalHours, onValueChange = { intervalHours = it }, label = { Text("Cách nhau (giờ)") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        selectedPhaseId,
                        com.example.qlbongda.data.model.ScheduleOptionsRequest(
                            startDate = startDate.ifBlank { null },
                            startTime = startTime.ifBlank { null },
                            intervalHours = intervalHours.toIntOrNull() ?: 2
                        )
                    )
                },
                enabled = selectedPhaseId != -1
            ) {
                Text("Chạy tự động")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
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
            // Cập nhật ở đây để nhận đủ 6 tham số
            onConfirm = { title, content, type, source, teamId, userId ->
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
    onConfirm: (String, String, String, String, Int?, Int?) -> Unit
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
                        onConfirm(title, content, type, "admin_panel", targetTeamId.toIntOrNull(), recipientUserId.toIntOrNull())
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
