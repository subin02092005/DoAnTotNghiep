package com.example.qlbongda.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.viewmodel.AdminViewModel
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchEventManagementScreen(matchId: Int, viewModel: AdminViewModel, onBack: () -> Unit) {
    val matchDetail by viewModel.matchDetail.collectAsStateWithLifecycle()
    val events by viewModel.matchEvents.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var showScoreDialog by remember { mutableStateOf(false) }
    var showGoalDialog by remember { mutableStateOf(false) }
    var showSubDialog by remember { mutableStateOf(false) }
    var showCardDialog by remember { mutableStateOf(false) }
    var showPlayerListDialog by remember { mutableStateOf(false) }
    var initialIsTeamAForGoal by remember { mutableStateOf(true) }

    LaunchedEffect(matchId) { viewModel.fetchMatchDetail(matchId); viewModel.fetchMatchEvents(matchId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Quản lý sự kiện", color = NeonGreen) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)) },
        containerColor = Color.Black
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = NeonGreen) }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                matchDetail?.let { match ->
                    val isMatchStarted = match.status == "ongoing" || match.status == "live" || match.status == "finished"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        border = BorderStroke(1.dp, Color(0xFF333333))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Đội A
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(match.teamA, color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text("${match.scoreA}", color = NeonGreen, fontSize = 48.sp, fontWeight = FontWeight.Black)
                                    Button(
                                        onClick = { 
                                            initialIsTeamAForGoal = true
                                            showGoalDialog = true 
                                        },
                                        enabled = isMatchStarted && match.status != "finished",
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text("GHI BÀN +1", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Text("VS", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))

                                // Đội B
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(match.teamB, color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text("${match.scoreB}", color = NeonGreen, fontSize = 48.sp, fontWeight = FontWeight.Black)
                                    Button(
                                        onClick = { 
                                            initialIsTeamAForGoal = false
                                            showGoalDialog = true 
                                        },
                                        enabled = isMatchStarted && match.status != "finished",
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text("GHI BÀN +1", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextButton(
                                    onClick = { showScoreDialog = true }, 
                                    modifier = Modifier.weight(1f),
                                    enabled = isMatchStarted
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (isMatchStarted) Color.Gray else Color.DarkGray)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Sửa tỉ số", color = if (isMatchStarted) Color.Gray else Color.DarkGray, fontSize = 12.sp)
                                }
                                TextButton(onClick = { showPlayerListDialog = true }, modifier = Modifier.weight(1f)) {
                                    Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(16.dp), tint = NeonGreen)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Cầu thủ", color = NeonGreen, fontSize = 12.sp)
                                }
                            }

                            if (!isMatchStarted) {
                                Text(
                                    "Vui lòng nhấn 'BẮT ĐẦU' trận đấu để quản lý sự kiện",
                                    color = Color.Yellow,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { showSubDialog = true }, 
                            modifier = Modifier.weight(1f), 
                            enabled = isMatchStarted && match.status != "finished",
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                            border = BorderStroke(1.dp, if (isMatchStarted) Color.Gray else Color.DarkGray)
                        ) { 
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Thay người") 
                        }
                        Button(
                            onClick = { showCardDialog = true }, 
                            modifier = Modifier.weight(1f), 
                            enabled = isMatchStarted && match.status != "finished",
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                            border = BorderStroke(1.dp, if (isMatchStarted) Color.Gray else Color.DarkGray)
                        ) { 
                            Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Thẻ phạt") 
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(events) { event -> EventAdminItem(event, onDelete = { viewModel.deleteEvent(matchId, event.id) }) }
                    }
                }
            }
        }
    }
    if (showScoreDialog && matchDetail != null) UpdateScoreDialog(currentScoreA = matchDetail!!.scoreA, currentScoreB = matchDetail!!.scoreB, onDismiss = { showScoreDialog = false }, onConfirm = { home, away -> viewModel.updateScore(matchId, home, away); showScoreDialog = false })
    if (showGoalDialog && matchDetail != null) GoalDialog(
        matchDetail = matchDetail!!, 
        initialIsTeamA = initialIsTeamAForGoal,
        onDismiss = { showGoalDialog = false }, 
        onConfirm = { teamId, jersey, min, period -> viewModel.addGoal(matchId, teamId, jersey, min, period); showGoalDialog = false }
    )
    if (showSubDialog && matchDetail != null) SubstitutionDialog(matchDetail = matchDetail!!, onDismiss = { showSubDialog = false }, onConfirm = { teamId, pIn, pOut, min, period -> viewModel.addSubstitution(matchId, teamId, pIn, pOut, min, period); showSubDialog = false })
    if (showCardDialog && matchDetail != null) CardDialog(matchDetail = matchDetail!!, onDismiss = { showCardDialog = false }, onConfirm = { teamId, playerId, min, period, isRed -> viewModel.addCard(matchId, teamId, playerId, min, period, isRed); showCardDialog = false })
    if (showPlayerListDialog && matchDetail != null) PlayerListDialog(matchDetail = matchDetail!!, onDismiss = { showPlayerListDialog = false })
}

@Composable
fun PlayerListDialog(matchDetail: FullMatchDetail, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF161616),
        title = { 
            Text("Danh sách thi đấu", color = NeonGreen, fontWeight = FontWeight.Bold) 
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Đội A
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(matchDetail.teamA, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                        HorizontalDivider(color = NeonGreen, modifier = Modifier.padding(vertical = 4.dp))
                        matchDetail.lineupA.forEach { player ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                Text("#${player.number}", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
                                Text(player.name, color = Color.White, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }

                    // Đội B
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(matchDetail.teamB, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                        HorizontalDivider(color = NeonGreen, modifier = Modifier.padding(vertical = 4.dp))
                        matchDetail.lineupB.forEach { player ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                Text("#${player.number}", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
                                Text(player.name, color = Color.White, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Đóng", color = Color.White) }
        }
    )
}

@Composable
fun EventAdminItem(event: MatchEventDetailed, onDelete: () -> Unit) {
    val eventColor = when (event.type) {
        "goal" -> Color(0xFFFFD700) // Vàng Gold cho bàn thắng
        "yellow_card" -> Color.Yellow
        "red_card", "second_yellow" -> Color.Red
        "substitution_in", "substitution_out" -> NeonGreen
        else -> Color.White
    }

    val eventTitle = when (event.type) {
        "goal" -> "BÀN THẮNG ⚽"
        "yellow_card" -> "THẺ VÀNG"
        "red_card" -> "THẺ ĐỎ"
        "second_yellow" -> "THẺ ĐỎ (2 THẺ VÀNG)"
        "substitution_in" -> "THAY NGƯỜI (VÀO)"
        "substitution_out" -> "THAY NGƯỜI (RA)"
        else -> event.type.uppercase()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(0.5.dp, Color(0xFF333333))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(eventColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .border(1.dp, eventColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${event.minute}'",
                    color = eventColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when {
                            event.type == "goal" -> Icons.Default.SportsFootball
                            event.type.contains("card") -> Icons.Default.Style
                            event.type.contains("sub") -> Icons.Default.Sync
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = eventColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = eventTitle,
                        color = eventColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${event.teamName ?: ""}",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val detailText = when (event.type) {
                    "substitution_in" -> {
                        if (event.subOutPlayerName != null) {
                            "Vào: ${event.playerName} \nRa: ${event.subOutPlayerName}"
                        } else {
                            "Vào sân: ${event.playerName}"
                        }
                    }
                    "substitution_out" -> "Rời sân: ${event.playerName}"
                    else -> "Cầu thủ: ${event.playerName ?: "N/A"}"
                }

                Text(
                    text = detailText,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp
                )
                
                if (!event.note.isNullOrBlank()) {
                    Text(
                        text = "Ghi chú: ${event.note}",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        style = TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GoalDialog(
    matchDetail: FullMatchDetail, 
    initialIsTeamA: Boolean,
    onDismiss: () -> Unit, 
    onConfirm: (Int, Int, Int, String) -> Unit
) {
    var isTeamASelected by remember(initialIsTeamA) { mutableStateOf(initialIsTeamA) }
    var jerseyNumber by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("1") }
    var period by remember { mutableStateOf("first_half") }
    var expandedPeriod by remember { mutableStateOf(false) }

    val periods = mapOf(
        "first_half" to "Hiệp 1",
        "second_half" to "Hiệp 2",
        "extra_time_first" to "Hiệp phụ 1",
        "extra_time_second" to "Hiệp phụ 2",
        "penalty_shootout" to "Loạt sút luân lưu"
    )

    val selectedTeamId = if (isTeamASelected) matchDetail.teamAId else matchDetail.teamBId

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ghi nhận bàn thắng ⚽", color = Color.White, fontWeight = FontWeight.Bold) },
        containerColor = Color(0xFF161616),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isTeamASelected = true }
                    ) {
                        RadioButton(selected = isTeamASelected, onClick = { isTeamASelected = true }, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen))
                        Text(matchDetail.teamA, color = Color.White, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isTeamASelected = false }
                    ) {
                        RadioButton(selected = !isTeamASelected, onClick = { isTeamASelected = false }, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen))
                        Text(matchDetail.teamB, color = Color.White, fontSize = 14.sp)
                    }
                }

                Box {
                    OutlinedButton(
                        onClick = { expandedPeriod = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(periods[period] ?: "Chọn hiệp", color = Color.White)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = expandedPeriod,
                        onDismissRequest = { expandedPeriod = false },
                        modifier = Modifier.background(Color(0xFF161616))
                    ) {
                        periods.forEach { (key, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = Color.White) },
                                onClick = {
                                    period = key
                                    expandedPeriod = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = jerseyNumber,
                    onValueChange = { jerseyNumber = it.filter { it.isDigit() } },
                    label = { Text("Số áo cầu thủ ghi bàn") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = minute,
                    onValueChange = { minute = it.filter { it.isDigit() } },
                    label = { Text("Phút ghi bàn") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (jerseyNumber.isNotBlank()) {
                        onConfirm(selectedTeamId, jerseyNumber.toIntOrNull() ?: 0, minute.toIntOrNull() ?: 0, period) 
                    }
                },
                enabled = jerseyNumber.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Xác nhận", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
        }
    )
}

@Composable
fun UpdateScoreDialog(currentScoreA: Int, currentScoreB: Int, onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit) {
    var scoreA by remember { mutableStateOf(currentScoreA.toString()) }
    var scoreB by remember { mutableStateOf(currentScoreB.toString()) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Cập nhật tỉ số") }, text = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = scoreA, 
                onValueChange = { scoreA = it.filter { char -> char.isDigit() } }, 
                modifier = Modifier.width(80.dp),
                label = { Text("Đội nhà") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(" - ", modifier = Modifier.padding(horizontal = 8.dp))
            OutlinedTextField(
                value = scoreB, 
                onValueChange = { scoreB = it.filter { char -> char.isDigit() } }, 
                modifier = Modifier.width(80.dp),
                label = { Text("Đội khách") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }, confirmButton = { 
        Button(
            onClick = { onConfirm(scoreA.toIntOrNull() ?: 0, scoreB.toIntOrNull() ?: 0) },
            enabled = scoreA.isNotBlank() && scoreB.isNotBlank()
        ) { Text("Cập nhật") } 
    }, dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } })
}

@Composable
fun SubstitutionDialog(matchDetail: FullMatchDetail, onDismiss: () -> Unit, onConfirm: (Int, Int, Int, Int, String) -> Unit) {
    var isTeamASelected by remember { mutableStateOf(true) }
    var jerseyIn by remember { mutableStateOf("") }
    var jerseyOut by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("45") }
    var period by remember { mutableStateOf("first_half") }
    var expandedPeriod by remember { mutableStateOf(false) }

    val periods = mapOf(
        "first_half" to "Hiệp 1",
        "second_half" to "Hiệp 2",
        "extra_time_first" to "Hiệp phụ 1",
        "extra_time_second" to "Hiệp phụ 2",
        "penalty_shootout" to "Loạt sút luân lưu"
    )

    val onFieldPlayers = if (isTeamASelected) matchDetail.lineupA else matchDetail.lineupB
    val benchPlayers = if (isTeamASelected) matchDetail.subsA else matchDetail.subsB
    val selectedTeamId = if (isTeamASelected) matchDetail.teamAId else matchDetail.teamBId

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thay người 🔄", color = Color.White, fontWeight = FontWeight.Bold) },
        containerColor = Color(0xFF161616),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    RadioButton(selected = isTeamASelected, onClick = { isTeamASelected = true; jerseyIn = ""; jerseyOut = "" })
                    Text(matchDetail.teamA, color = Color.White, modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.width(20.dp))
                    RadioButton(selected = !isTeamASelected, onClick = { isTeamASelected = false; jerseyIn = ""; jerseyOut = "" })
                    Text(matchDetail.teamB, color = Color.White, modifier = Modifier.align(Alignment.CenterVertically))
                }

                Box {
                    OutlinedButton(onClick = { expandedPeriod = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(periods[period] ?: "Chọn hiệp", color = Color.White)
                    }
                    DropdownMenu(expanded = expandedPeriod, onDismissRequest = { expandedPeriod = false }, modifier = Modifier.background(Color(0xFF161616))) {
                        periods.forEach { (k, v) -> DropdownMenuItem(text = { Text(v, color = Color.White) }, onClick = { period = k; expandedPeriod = false }) }
                    }
                }

                OutlinedTextField(value = minute, onValueChange = { minute = it.filter { it.isDigit() } }, label = { Text("Phút") })

                Text("Cầu thủ RA (Đang đá):", color = NeonGreen, fontSize = 12.sp)
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(onFieldPlayers) { p ->
                        FilterChip(
                            selected = jerseyOut == p.number,
                            onClick = { jerseyOut = p.number },
                            label = { Text("#${p.number} ${p.name.take(10)}") },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                Text("Cầu thủ VÀO (Dự bị):", color = Color.Yellow, fontSize = 12.sp)
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(benchPlayers) { p ->
                        FilterChip(
                            selected = jerseyIn == p.number,
                            onClick = { jerseyIn = p.number },
                            label = { Text("#${p.number} ${p.name.take(10)}") },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    val pIn = jerseyIn.toIntOrNull()
                    val pOut = jerseyOut.toIntOrNull()
                    if (pIn != null && pOut != null) {
                        onConfirm(selectedTeamId, pIn, pOut, minute.toIntOrNull() ?: 0, period) 
                    }
                },
                enabled = jerseyIn.isNotBlank() && jerseyOut.isNotBlank()
            ) { Text("Xác nhận") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun CardDialog(matchDetail: FullMatchDetail, onDismiss: () -> Unit, onConfirm: (Int, Int, Int, String, Boolean) -> Unit) {
    var isTeamASelected by remember { mutableStateOf(true) }
    var jerseyNumber by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("45") }
    var period by remember { mutableStateOf("first_half") }
    var expandedPeriod by remember { mutableStateOf(false) }
    var isRed by remember { mutableStateOf(false) }

    val periods = mapOf(
        "first_half" to "Hiệp 1",
        "second_half" to "Hiệp 2",
        "extra_time_first" to "Hiệp phụ 1",
        "extra_time_second" to "Hiệp phụ 2",
        "penalty_shootout" to "Loạt sút luân lưu"
    )

    val selectedTeamId = if (isTeamASelected) matchDetail.teamAId else matchDetail.teamBId

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thẻ phạt", color = Color.White, fontWeight = FontWeight.Bold) },
        containerColor = Color(0xFF161616),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isTeamASelected = true }
                    ) {
                        RadioButton(
                            selected = isTeamASelected,
                            onClick = { isTeamASelected = true },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonGreen)
                        )
                        Text(matchDetail.teamA, color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isTeamASelected = false }
                    ) {
                        RadioButton(
                            selected = !isTeamASelected,
                            onClick = { isTeamASelected = false },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonGreen)
                        )
                        Text(matchDetail.teamB, color = Color.White, fontSize = 14.sp)
                    }
                }

                Box {
                    OutlinedButton(
                        onClick = { expandedPeriod = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text(periods[period] ?: "Chọn hiệp", color = Color.White)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = expandedPeriod,
                        onDismissRequest = { expandedPeriod = false },
                        modifier = Modifier.background(Color(0xFF161616))
                    ) {
                        periods.forEach { (key, label) ->
                            DropdownMenuItem(
                                text = { Text(label, color = Color.White) },
                                onClick = {
                                    period = key
                                    expandedPeriod = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = jerseyNumber,
                    onValueChange = { jerseyNumber = it.filter { char -> char.isDigit() } },
                    label = { Text("Số áo cầu thủ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = minute,
                    onValueChange = { minute = it.filter { char -> char.isDigit() } },
                    label = { Text("Phút thi đấu") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isRed = !isRed }
                ) {
                    Checkbox(
                        checked = isRed,
                        onCheckedChange = { isRed = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                    )
                    Text("Thẻ đỏ trực tiếp", color = Color.White)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (jerseyNumber.isNotBlank()) {
                        onConfirm(selectedTeamId, jerseyNumber.toIntOrNull() ?: 0, minute.toIntOrNull() ?: 0, period, isRed)
                    }
                },
                enabled = jerseyNumber.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Thêm", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
        }
    )
}
