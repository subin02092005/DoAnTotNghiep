package com.example.qlbongda

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.data.model.AdminPlayerItem
import com.example.qlbongda.data.model.AdminTeamItem
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.ui.theme.NeonGreen

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
                AdminSection.STATS -> StatisticsScreen()
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
        AdminMenuItem("Thống kê & Báo cáo", "Xem hiệu suất và dữ liệu giải đấu", Icons.Default.Assessment, AdminSection.STATS),
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
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchTournaments()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "QUẢN LÝ GIẢI ĐẤU", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Danh sách các giải đấu đang diễn ra", color = Color.LightGray, fontSize = 13.sp)
            }
            IconButton(
                onClick = { showAddDialog = true },
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
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tournaments) { tournament ->
                    TournamentAdminCard(
                        tournament = tournament,
                        onToggleStatus = { viewModel.updateTournamentStatus(tournament.id, tournament.isActive == 0) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddTournamentDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, desc, teams ->
                viewModel.createTournament(name, desc, null, teams, null)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TournamentAdminCard(tournament: com.example.qlbongda.data.model.TournamentItem, onToggleStatus: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                Text("Số đội tối đa: ${tournament.maxTeams ?: "Chưa thiết lập"}", color = Color.LightGray, fontSize = 12.sp)
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
fun AddTournamentDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var teams by remember { mutableStateOf("16") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo giải đấu mới") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên giải đấu") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = teams, onValueChange = { teams = it }, label = { Text("Số đội tối đa") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, desc, teams.toIntOrNull() ?: 16) }) {
                Text("Tạo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun StatisticsScreen() {
    ManagementHeader("THỐNG KÊ & BÁO CÁO", "Tổng quan doanh thu và dữ liệu chuyên môn")
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
            Column {
                Text("Đội bóng:")
                Row {
                    RadioButton(selected = selectedTeamId == 0, onClick = { selectedTeamId = 0 })
                    Text("Đội nhà")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = selectedTeamId == 1, onClick = { selectedTeamId = 1 })
                    Text("Đội khách")
                }
                
                // Demo purposes: normally we'd have a dropdown here
                Text("Minute:")
                OutlinedTextField(value = minute, onValueChange = { minute = it })
                
                Text("Cầu thủ vào (ID):")
                OutlinedTextField(value = playerInId.toString(), onValueChange = { playerInId = it.toIntOrNull() ?: 0 })
                
                Text("Cầu thủ ra (ID):")
                OutlinedTextField(value = playerOutId.toString(), onValueChange = { playerOutId = it.toIntOrNull() ?: 0 })
            }
        },
        confirmButton = {
            val teamId = if (selectedTeamId == 0) matchDetail.teamAId else matchDetail.teamBId
            Button(onClick = { onConfirm(teamId, playerInId, playerOutId, minute.toIntOrNull() ?: 0, period) }) {
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
    var isRed by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thẻ phạt") },
        text = {
            Column {
                Text("Đội bóng:")
                Row {
                    RadioButton(selected = selectedTeamId == 0, onClick = { selectedTeamId = 0 })
                    Text("Đội nhà")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = selectedTeamId == 1, onClick = { selectedTeamId = 1 })
                    Text("Đội khách")
                }

                Text("ID Cầu thủ:")
                OutlinedTextField(value = playerId.toString(), onValueChange = { playerId = it.toIntOrNull() ?: 0 })
                
                Text("Phút:")
                OutlinedTextField(value = minute, onValueChange = { minute = it })
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isRed, onCheckedChange = { isRed = it })
                    Text("Thẻ đỏ")
                }
            }
        },
        confirmButton = {
            val teamId = if (selectedTeamId == 0) matchDetail.teamAId else matchDetail.teamBId
            Button(onClick = { onConfirm(teamId, playerId, minute.toIntOrNull() ?: 0, "1", isRed) }) {
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
