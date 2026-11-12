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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

    LaunchedEffect(Unit) { viewModel.fetchNotifications() }

    val displayList = remember(selectedTab, generalList, teamList, personalList) {
        when (selectedTab) { 0 -> generalList; 1 -> teamList; else -> personalList }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRuleDialog(
    seasons: List<SeasonWithPhases>,
    initialSeasonId: Int? = null,
    initialRule: NotificationItem? = null, // 🌟 THÊM DÒNG NÀY: Nhận thông tin luật hiện tại khi sửa
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, Int, Int, Int, Int, String) -> Unit
) {
    var selectedSeason by remember {
        mutableStateOf(
            if (initialSeasonId != null) seasons.find { it.id == initialSeasonId } else seasons.firstOrNull()
        )
    }
    var expanded by remember { mutableStateOf(false) }

    // Tạo các state với giá trị mặc định ban đầu
    var minPlayers by remember { mutableStateOf("7") }
    var maxPlayers by remember { mutableStateOf("11") }
    var ptsWin by remember { mutableStateOf("3") }
    var ptsDraw by remember { mutableStateOf("1") }
    var ptsLoss by remember { mutableStateOf("0") }
    var forfeitScore by remember { mutableStateOf("3") }
    var description by remember { mutableStateOf("") }

    // 🌟 THÊM ĐOẠN NÀY: Tự động đổ dữ liệu từ API/Thông báo cũ vào khi mở chế độ Sửa luật
    // 🌟 Đổi key truyền vào thành selectedSeason để kích hoạt lại mỗi khi chọn giải khác
    LaunchedEffect(selectedSeason) {
        // 🌟 TẠO BIẾN CỤC BỘ: Gán vào một biến thường để Kotlin có thể Smart cast mượt mà
        val season = selectedSeason

        if (season != null) {
            // Sử dụng biến 'season' (đã được smart cast thành SeasonWithPhases) thay vì 'selectedSeason'
            if (initialRule != null && season.id == initialSeasonId) {
                val text = initialRule.content
                description = text

                // Regex trích xuất số cầu thủ
                val playerRegex = Regex("""Đội hình\s+(\d+)-(\d+)\s+cầu thủ""")
                val playerMatch = playerRegex.find(text)
                if (playerMatch != null) {
                    minPlayers = playerMatch.groupValues[1]
                    maxPlayers = playerMatch.groupValues[2]
                }

                // Regex trích xuất điểm số
                val pointsRegex = Regex("""Thắng\s*\((\d+)\)\s*-\s*Hòa\s*\((\d+)\)\s*-\s*Thua\s*\((\d+)\)""")
                val pointsMatch = pointsRegex.find(text)
                if (pointsMatch != null) {
                    ptsWin = pointsMatch.groupValues[1]
                    ptsDraw = pointsMatch.groupValues[2]
                    ptsLoss = pointsMatch.groupValues[3]
                }
            }
            // Nếu chuyển sang mùa giải khác thì reset về mặc định
            else {
                minPlayers = "7"
                maxPlayers = "11"
                ptsWin = "3"
                ptsDraw = "1"
                ptsLoss = "0"
                forfeitScore = "3"
                description = ""
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeonSurface,
        title = { Text(if (initialRule != null) "Sửa Luật Giải Đấu" else "Thêm Luật Giải Đấu", color = NeonWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                // 🌟 DROPDOWN MENU CHỌN MÙA GIẢI
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = selectedSeason?.name ?: "Đang tải mùa giải...",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Mùa giải áp dụng luật", color = NeonGray) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeonWhite,
                            unfocusedTextColor = NeonWhite,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = NeonGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(NeonSurface)
                    ) {
                        if (seasons.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Không có dữ liệu", color = NeonGray) },
                                onClick = { expanded = false }
                            )
                        } else {
                            seasons.forEach { season ->
                                DropdownMenuItem(
                                    text = { Text(season.name, color = NeonWhite) },
                                    onClick = {
                                        selectedSeason = season
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }

                // CÁC Ô NHẬP LIỆU CÒN LẠI
                val fields = listOf(
                    "Tối thiểu cầu thủ" to minPlayers, "Tối đa cầu thủ" to maxPlayers,
                    "Điểm thắng" to ptsWin, "Điểm hòa" to ptsDraw,
                    "Điểm thua" to ptsLoss, "Điểm xử thua" to forfeitScore,
                    "Mô tả" to description
                )

                fields.forEach { (label, value) ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = { newValue ->
                            val filteredValue = if (label != "Mô tả") newValue.filter { it.isDigit() } else newValue

                            when(label) {
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
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeonWhite,
                            unfocusedTextColor = NeonWhite,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = NeonGray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = if (label == "Mô tả") KeyboardType.Text else KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedSeason?.let { season ->
                        onConfirm(
                            season.id,
                            minPlayers.toIntOrNull() ?: 7,
                            maxPlayers.toIntOrNull() ?: 11,
                            ptsWin.toIntOrNull() ?: 3,
                            ptsDraw.toIntOrNull() ?: 1,
                            ptsLoss.toIntOrNull() ?: 0,
                            forfeitScore.toIntOrNull() ?: 3,
                            description
                        )
                    }
                },
                enabled = selectedSeason != null
            ) {
                Text("Lưu Luật", color = if (selectedSeason != null) NeonGreen else NeonGray)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = NeonGray) }
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
