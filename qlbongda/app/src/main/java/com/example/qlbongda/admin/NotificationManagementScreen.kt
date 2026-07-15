package com.example.qlbongda.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.viewmodel.AdminViewModel
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

val NeonBlack = Color(0xFF0A0A0A)      // Đen sâu
val NeonSurface = Color(0xFF161616)    // Xám rất tối (cho Card & Tab)
val NeonGray = Color(0xFF666666)       // Xám cho chữ phụ
val NeonRed = Color(0xFFFF003C)
val NeonWhite = Color(0xFFFFFFFF)

@Composable
fun NotificationManagementScreen(viewModel: AdminViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val selectedNotification by viewModel.selectedNotification.collectAsStateWithLifecycle()
    val showCleanupDialog by viewModel.showCleanupDialog.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()
    val generalList by viewModel.generalNotifications.collectAsStateWithLifecycle()
    val teamList by viewModel.teamNotifications.collectAsStateWithLifecycle()
    val personalList by viewModel.personalNotifications.collectAsStateWithLifecycle()
    val showAddRuleDialog by viewModel.showAddRuleDialog.collectAsStateWithLifecycle()

    // 🌟 1. THÊM STATE NÀY ĐỂ QUẢN LÝ DIALOG SỬA LUẬT
    var editingRuleSeasonId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) { viewModel.fetchNotifications() }

    val displayList = remember(selectedTab, generalList, teamList, personalList) {
        when (selectedTab) { 0 -> generalList; 1 -> teamList; else -> personalList }
    }

    if (showAddRuleDialog) {
        AddRuleDialog(
            onDismiss = { viewModel.setShowAddRuleDialog(false) },
            onConfirm = { sId, min, max, win, draw, loss, forfeit, desc ->
                val request = CreateRuleRequest(season_id = sId, min_players = min, max_players = max, points_win = win, points_draw = draw, points_loss = loss, forfeit_score = forfeit, description = desc)
                viewModel.createRules(request)
                viewModel.setShowAddRuleDialog(false)
            }
        )
    }

    if (editingRuleSeasonId != null) {
        AddRuleDialog(
            onDismiss = { editingRuleSeasonId = null },
            onConfirm = { sId, min, max, win, draw, loss, forfeit, desc ->
                val request = CreateRuleRequest(
                    season_id = sId, 
                    min_players = min,
                    max_players = max,
                    points_win = win,
                    points_draw = draw,
                    points_loss = loss,
                    forfeit_score = forfeit,
                    description = desc
                )
                viewModel.createRules(request) 
                editingRuleSeasonId = null 
            }
        )
    }

    if (showAddDialog) {
        AddNotificationDialog(onDismiss = { viewModel.setShowAddDialog(false) }, onConfirm = { title, content, type, teamId, userId -> viewModel.createNotification(title, content, type, teamId, userId); viewModel.setShowAddDialog(false) })
    }
    if (selectedNotification != null) {
        EditNotificationDialog(notification = selectedNotification!!, onDismiss = { viewModel.setSelectedNotification(null) }, onConfirm = { title, content, type, isActive -> viewModel.updateNotification(selectedNotification!!.id, title, content, type, isActive); viewModel.setSelectedNotification(null) })
    }
    if (showCleanupDialog) {
        CleanupConfirmationDialog(onDismiss = { viewModel.setShowCleanupDialog(false) }, onConfirm = { viewModel.triggerCleanupNotifications(); viewModel.setShowCleanupDialog(false) })
    }

    Scaffold(
        containerColor = NeonBlack,
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = { viewModel.setShowCleanupDialog(true) }, containerColor = NeonRed, contentColor = Color.Black) { Icon(Icons.Default.Delete, contentDescription = "Dọn dẹp") }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = { viewModel.setShowAddRuleDialog(true) }, containerColor = Color.Yellow) { Icon(Icons.Default.Settings, contentDescription = "Luật") }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = { viewModel.setShowAddDialog(true) }, containerColor = NeonGreen, contentColor = Color.Black) { Icon(Icons.Default.Add, contentDescription = "Thêm") }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize().background(NeonBlack)) {
            ManagementHeader(title = "QUẢN LÝ THÔNG BÁO", subtitle = "Quản lý và gửi thông báo hệ thống")

            TabRow(selectedTabIndex = selectedTab, containerColor = NeonSurface, indicator = { tabPositions -> TabRowDefaults.SecondaryIndicator(modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]), color = NeonGreen, height = 3.dp) }) {
                listOf("Chung", "Theo Team", "Cá nhân").forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(text = title, color = if (selectedTab == index) NeonGreen else NeonGray, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) })
                }
            }

            LazyColumn {
                items(displayList) { notification ->
                    NotificationItemCard(
                        item = notification,
                        onDelete = { viewModel.deleteNotification(notification.id) },
                        onEdit = { item ->
                            if (item.source == "manual") {
                                viewModel.setSelectedNotification(item)
                            } else if (item.ref_entity_type == "tournament_rules") {
                                editingRuleSeasonId = item.season_id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddNotificationDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, Int?, Int?) -> Unit) {
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
                Row {
                    RadioButton(selected = type == "general", onClick = { type = "general" }); Text("Chung", color = NeonWhite)
                    RadioButton(selected = type == "match_schedule", onClick = { type = "match_schedule" }); Text("Đội", color = NeonWhite)
                    RadioButton(selected = type == "player_approved", onClick = { type = "player_approved" }); Text("Cá nhân", color = NeonWhite)
                }

                if (type == "match_schedule") {
                    OutlinedTextField(
                        value = targetTeamId,
                        onValueChange = { newValue -> targetTeamId = newValue.filter { it.isDigit() } },
                        label = { Text("ID Team") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                if (type == "player_approved") {
                    OutlinedTextField(
                        value = recipientUserId,
                        onValueChange = { newValue -> recipientUserId = newValue.filter { it.isDigit() } },
                        label = { Text("ID Người nhận") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                errorMessage?.let { Text(it, color = NeonRed, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    title.isBlank() -> errorMessage = "Vui lòng nhập tiêu đề"
                    content.isBlank() -> errorMessage = "Vui lòng nhập nội dung"
                    else -> onConfirm(title, content, type, targetTeamId.toIntOrNull(), recipientUserId.toIntOrNull())
                }
            }) { Text("Gửi", color = NeonGreen) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy", color = NeonGray) } }
    )
}

@Composable
fun AddRuleDialog(onDismiss: () -> Unit, onConfirm: (Int, Int, Int, Int, Int, Int, Int, String) -> Unit) {
    var seasonId by remember { mutableStateOf("") }
    var minPlayers by remember { mutableStateOf("7") }
    var maxPlayers by remember { mutableStateOf("11") }
    var ptsWin by remember { mutableStateOf("3") }
    var ptsDraw by remember { mutableStateOf("1") }
    var ptsLoss by remember { mutableStateOf("0") }
    var forfeitScore by remember { mutableStateOf("3") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeonSurface,
        title = { Text("Thêm Luật Giải Đấu", color = NeonWhite) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                val fields = listOf(
                    "ID Mùa giải" to seasonId, "Tối thiểu cầu thủ" to minPlayers,
                    "Tối đa cầu thủ" to maxPlayers, "Điểm thắng" to ptsWin,
                    "Điểm hòa" to ptsDraw, "Điểm thua" to ptsLoss, "Điểm xử thua" to forfeitScore,
                    "Mô tả" to description
                )

                fields.forEach { (label, value) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            val filteredValue = if (label != "Mô tả") newValue.filter { it.isDigit() } else newValue

                            when(label) {
                                "ID Mùa giải" -> seasonId = filteredValue
                                "Tối thiểu cầu thủ" -> minPlayers = filteredValue
                                "Tối đa cầu thủ" -> maxPlayers = filteredValue
                                "Điểm thắng" -> ptsWin = filteredValue
                                "Điểm hòa" -> ptsDraw = filteredValue
                                "Điểm thua" -> ptsLoss = filteredValue
                                "Điểm xử thua" -> forfeitScore = filteredValue
                                "Mô tả" -> description = filteredValue
                            }
                        },
                        label = { Text(label, color = NeonGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = if (label == "Mô tả") KeyboardType.Text else KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    seasonId.toIntOrNull() ?: 0,
                    minPlayers.toIntOrNull() ?: 7,
                    maxPlayers.toIntOrNull() ?: 11,
                    ptsWin.toIntOrNull() ?: 3,
                    ptsDraw.toIntOrNull() ?: 1,
                    ptsLoss.toIntOrNull() ?: 0,
                    forfeitScore.toIntOrNull() ?: 3,
                    description
                )
            }) {
                Text("Lưu Luật", color = NeonGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = NeonGray)
            }
        }
    )
}
@Composable
fun CleanupConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeonSurface,
        title = { Text("Xác nhận dọn dẹp", color = NeonRed) },
        text = { Text("Dọn dẹp tất cả thông báo cũ hơn 30 ngày?", color = NeonGray) },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Xác nhận", color = NeonRed) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy", color = NeonGreen) } }
    )
}

@Composable
fun EditNotificationDialog(notification: NotificationItem, onDismiss: () -> Unit, onConfirm: (String, String, String, Int) -> Unit) {
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
        confirmButton = { TextButton(onClick = { onConfirm(title, content, notification.type, 1) }) { Text("Lưu") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun NotificationItemCard(item: NotificationItem, onDelete: () -> Unit, onEdit: (NotificationItem) -> Unit) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(item.content, maxLines = 2, fontSize = 14.sp)
            }

            if (item.source == "manual") {
                IconButton(onClick = { onEdit(item) }) { Icon(Icons.Default.Edit, "Sửa") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Xóa") }
            } else if (item.source == "system" && item.ref_entity_type == "tournament_rules") {
                IconButton(onClick = { onEdit(item) }) { Icon(Icons.Default.Edit, "Sửa luật") }
            }
        }
    }
}
