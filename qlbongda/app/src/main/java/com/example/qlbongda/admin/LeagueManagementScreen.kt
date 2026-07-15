package com.example.qlbongda.admin

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.qlbongda.utils.DateUtils
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.input.pointer.pointerInput
import android.content.ClipData
import com.example.qlbongda.details.KnockoutBracketScreen

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
                                    viewModel.updateMessage("Giải đấu đang tạm dừng, vui lòng kích hoạt trước.")
                                } else {
                                    selectedTournamentId = tournament.id
                                    viewModel.fetchTournamentDetail(tournament.id)
                                }
                            }
                        )
                    }
                }
            }
        } else {
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

    if (showAddTournamentDialog) {
        AddTournamentDialog(
            onDismiss = { showAddTournamentDialog = false },
            onConfirm = { name, desc, max, min ->
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
            onBack = { selectedSeasonId = -1 }
        )
    }
}

@Composable
fun SeasonDetailScreen(seasonId: Int, viewModel: AdminViewModel, onBack: () -> Unit)  {
    LaunchedEffect(seasonId) {
        viewModel.fetchSeasonDetails(seasonId)
        viewModel.fetchStandings(seasonId)
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Danh sách đội", "Bảng đấu & Vòng đấu", "Bảng xếp hạng", "Cây nhánh đấu")

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
            }
            Text("Chi tiết Mùa giải", color = Color.White, fontWeight = FontWeight.Bold)
        }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Black,
            contentColor = NeonGreen
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> SeasonTeamListContent(seasonId, viewModel)
            1 -> SeasonStageContent(seasonId, viewModel)
            2 -> AdminStandingContent(seasonId, viewModel)
            3 -> KnockoutBracketScreen(seasonId, viewModel)
        }
    }
}

@Composable
fun AdminStandingContent(seasonId: Int, viewModel: AdminViewModel) {
    val standings by viewModel.standingList.collectAsState()
    val seasonDetail by viewModel.seasonDetail.collectAsState()
    val phases = seasonDetail?.phases ?: emptyList()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(seasonId) {
        viewModel.fetchStandings(seasonId)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("BẢNG XẾP HẠNG MÙA GIẢI", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                if (standings.isNotEmpty()) {
                    Text("Số bảng: ${standings.size}", color = Color.Gray, fontSize = 10.sp)
                }
            }

            // Nút tạo Vòng Knockout tự động cho toàn bộ giải đấu
            if (standings.isNotEmpty() && phases.any { it.format == "knockout" }) {
                var showGlobalPhaseMenu by remember { mutableStateOf(false) }
                val currentPhaseId = standings.firstOrNull()?.phaseId ?: -1
                val targetPhases = phases.filter { it.format == "knockout" }

                // Kiểm tra hoàn thành tất cả trận đấu của vòng bảng hiện tại
                val currentPhase = phases.find { it.id == currentPhaseId }
                val phaseMatches = currentPhase?.matches ?: emptyList()
                val isPhaseFinished = phaseMatches.isNotEmpty() && phaseMatches.all { it.status == "finished" }

                if (currentPhaseId != -1 && targetPhases.isNotEmpty()) {
                    Box {
                        Button(
                            onClick = { 
                                if (isPhaseFinished) {
                                    showGlobalPhaseMenu = true 
                                } else {
                                    viewModel.updateMessage("Cần hoàn thành tất cả trận đấu của ${currentPhase?.name} trước khi tạo Knockout!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPhaseFinished) Color.Yellow else Color.Gray
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            modifier = Modifier.height(32.dp).padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Tạo Knockout", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        DropdownMenu(
                            expanded = showGlobalPhaseMenu,
                            onDismissRequest = { showGlobalPhaseMenu = false },
                            modifier = Modifier.background(Color(0xFF252525))
                        ) {
                            Text("Đưa Nhất/Nhì các bảng vào:", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(8.dp))
                            targetPhases.forEach { target ->
                                DropdownMenuItem(
                                    text = { Text(target.name, color = Color.White) },
                                    onClick = {
                                        viewModel.advanceTopTeamsToPhase(currentPhaseId, target.id, seasonId)
                                        showGlobalPhaseMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            IconButton(onClick = { viewModel.fetchStandings(seasonId) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = NeonGreen)
            }
        }

        if (isLoading && standings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else if (standings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có dữ liệu bảng xếp hạng", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                items(standings) { groupStanding ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = groupStanding.groupName,
                                        color = NeonGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    val currentPhase = phases.find { it.id == groupStanding.phaseId }
                                    if (currentPhase != null) {
                                        Text(
                                            text = currentPhase.name,
                                            color = Color.Gray,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                
                                IconButton(onClick = { viewModel.fetchStandings(seasonId) }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Header Bảng - Thêm cột ST
                            Row(modifier = Modifier.fillMaxWidth().background(Color.Black).padding(4.dp)) {
                                Text("Đội", color = Color.Gray, modifier = Modifier.weight(3f), fontSize = 10.sp)
                                Text("ST", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp)
                                Text("T", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp)
                                Text("H", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp)
                                Text("B", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp)
                                Text("Đ", color = Color.Gray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            
                            groupStanding.standings.forEachIndexed { index, team ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${index + 1}. ${team.teamName}", color = if (index < 2) NeonGreen else Color.White, modifier = Modifier.weight(3f), fontSize = 12.sp, maxLines = 1)
                                    Text("${team.played}", color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp)
                                    Text("${team.won}", color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp)
                                    Text("${team.drawn}", color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp)
                                    Text("${team.lost}", color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp)
                                    Text("${team.points}", color = NeonGreen, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeasonTeamListContent(seasonId: Int, viewModel: AdminViewModel) {
    val seasonDetail by viewModel.seasonDetail.collectAsState()
    val teamList = seasonDetail?.teams ?: emptyList()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(teamList) { team ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = team.name, color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = when(team.status) {
                                "pending" -> "CHỜ THANH TOÁN"
                                "approved" -> "CHỜ DUYỆT THAM GIA"
                                "active" -> "ĐÃ THAM GIA"
                                else -> team.status.uppercase()
                            }, 
                            color = when(team.status) {
                                "pending" -> Color.Yellow
                                "approved" -> Color.Cyan
                                "active" -> NeonGreen
                                else -> Color.Red
                            }, 
                            fontSize = 11.sp, 
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Row {
                        if (team.status == "pending" || team.status == "approved") {
                            // CHỈ CHO PHÉP BẤM DUYỆT KHI ĐÃ ĐÓNG TIỀN (APPROVED)
                            if (team.status == "approved") {
                                IconButton(onClick = { viewModel.approveSeasonTeam(seasonId, team.team_id) }) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Approve", tint = NeonGreen)
                                }
                            } else {
                                // Nếu chưa đóng tiền, hiện icon cảnh báo thay vì nút duyệt
                                Icon(
                                    Icons.Default.Payments, 
                                    contentDescription = "Waiting Payment", 
                                    tint = Color.Gray,
                                    modifier = Modifier.padding(12.dp).size(20.dp)
                                )
                            }

                            IconButton(onClick = { viewModel.rejectSeasonTeam(seasonId, team.team_id) }) {
                                Icon(Icons.Default.Cancel, contentDescription = "Reject", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeasonStageContent(seasonId: Int, viewModel: AdminViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val seasonDetail by viewModel.seasonDetail.collectAsState()
    val phases = seasonDetail?.phases ?: emptyList()
    val allTeams = seasonDetail?.teams ?: emptyList()

    var selectedTeamToMove by remember { mutableStateOf<TeamItem?>(null) }
    var sourcePhaseIdForMove by remember { mutableIntStateOf(-1) }

    var showAddTeamDialog by remember { mutableStateOf(false) }
    var selectedPhaseForTeam by remember { mutableStateOf<PhaseItem?>(null) }
    var showAutoScheduleDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchVenues()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // CHỈ LỌC NHỮNG ĐỘI ĐÃ DUYỆT (active/approved) ĐỂ XẾP BẢNG
        val unassignedTeams = allTeams.filter { 
            it.groupId == null && (it.status == "active" || it.status == "approved") 
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (selectedTeamToMove == null) "ĐỘI CHƯA XẾP BẢNG (${unassignedTeams.size})" else "ĐANG CHỌN: ${selectedTeamToMove?.name?.uppercase()}",
                color = if (selectedTeamToMove == null) NeonGreen else Color.Yellow,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            if (selectedTeamToMove != null && sourcePhaseIdForMove != -1) {
                TextButton(onClick = {
                    viewModel.removeTeamFromPhase(sourcePhaseIdForMove, selectedTeamToMove!!.team_id, seasonId)
                    selectedTeamToMove = null
                }) {
                    Text("GỠ KHỎI BẢNG", color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
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
            items(unassignedTeams, key = { it.team_id }) { team ->
                val isSelected = (team == selectedTeamToMove)

                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(vertical = 2.dp)
                        .background(if (isSelected) Color.DarkGray else Color.Transparent, RoundedCornerShape(8.dp))
                        .pointerInput(team, selectedTeamToMove) { 
                            detectTapGestures(
                                onTap = {
                                    selectedTeamToMove = if (isSelected) null else team
                                    sourcePhaseIdForMove = -1
                                }
                            )
                        }
                        .dragAndDropSource(
                            block = {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = { _ ->
                                        startTransfer(
                                            DragAndDropTransferData(
                                                clipData = ClipData.newPlainText("team_id", team.team_id.toString())
                                            )
                                        )
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                    }
                                )
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(team.name, color = if (isSelected) NeonGreen else Color.White, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { showDialog = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)) {
                Text("Thêm vòng đấu", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { 
                    if (unassignedTeams.isNotEmpty()) {
                        viewModel.updateMessage("Cảnh báo: Còn ${unassignedTeams.size} đội chưa được xếp bảng. Vui lòng xếp tất cả đội vào bảng trước khi xếp lịch!")
                    } else if (allTeams.isEmpty()) {
                        viewModel.updateMessage("Mùa giải chưa có đội bóng nào tham gia.")
                    } else {
                        showAutoScheduleDialog = true 
                    }
                },
                modifier = Modifier.weight(1f), 
                border = BorderStroke(1.dp, NeonGreen)
            ) {
                Text("Auto Xếp Lịch", color = NeonGreen, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(phases, key = { it.id }) { phase ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = phase.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                                Text(text = "Định dạng: ${if(phase.format == "round_robin") "Vòng tròn" else "Loại trực tiếp"}",
                                    style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Row {
                                IconButton(onClick = { 
                                    // KIỂM TRA LỊCH ĐẤU TRƯỚC KHI CHO XÓA
                                    val hasActiveMatches = phase.matches?.any { it.status != "cancelled" } == true
                                    if (hasActiveMatches) {
                                        viewModel.updateMessage("Không thể xóa vòng đấu đã xếp lịch. Vui lòng Hủy (Cancel) tất cả trận đấu trước!")
                                    } else {
                                        viewModel.deletePhase(seasonId, phase.id)
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Xóa vòng đấu", tint = Color.Red.copy(alpha = 0.7f))
                                }
                                IconButton(onClick = { selectedPhaseForTeam = phase; showAddTeamDialog = true }) {
                                    Icon(Icons.Default.Add, contentDescription = "Thêm đội", tint = NeonGreen)
                                }
                            }
                        }
                        val groups = phase.groups ?: emptyList()
                        // Lấy danh sách đội riêng của Phase này từ Backend
                        val phaseTeams = phase.teams ?: emptyList()

                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 8.dp)
                        ) {
                            groups.forEach { group ->
                                GroupCard(
                                    group = group,
                                    // LUÔN ưu tiên sử dụng danh sách đội đã được lọc theo Phase từ Backend
                                    allTeams = if (!phase.teams.isNullOrEmpty()) phase.teams!! else allTeams,
                                    isMoveActive = selectedTeamToMove != null,
                                    onRemoveTeam = { teamId ->
                                        viewModel.removeTeamFromPhase(phase.id, teamId, seasonId)
                                    },
                                    onSelectTeam = { team ->
                                        selectedTeamToMove = team
                                        sourcePhaseIdForMove = phase.id
                                    },
                                    onMoveHere = {
                                        selectedTeamToMove?.let { team ->
                                            viewModel.assignTeamToGroup(phase.id, team.team_id, group.id)
                                            selectedTeamToMove = null
                                        }
                                    },
                                    modifier = Modifier.dragAndDropTarget(
                                        shouldStartDragAndDrop = { event ->
                                            event.toAndroidDragEvent().clipDescription?.hasMimeType("text/plain") == true
                                        },
                                        target = object : DragAndDropTarget {
                                            override fun onDrop(event: DragAndDropEvent): Boolean {
                                                val clipData = event.toAndroidDragEvent().clipData
                                                val teamIdString = clipData?.getItemAt(0)?.text?.toString()
                                                val teamId = teamIdString?.toIntOrNull()

                                                if (teamId != null) {
                                                    viewModel.assignTeamToGroup(phase.id, teamId, group.id)
                                                    return true
                                                }
                                                return false
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddTeamDialog && selectedPhaseForTeam != null) {
        AddTeamToPhaseDialog(
            // CHỈ CHO PHÉP CHỌN ĐỘI ĐÃ DUYỆT
            teams = allTeams.filter { it.status == "active" || it.status == "approved" },
            groups = selectedPhaseForTeam!!.groups ?: emptyList(),
            onDismiss = { showAddTeamDialog = false },
            onTeamSelected = { team, group ->
                viewModel.addTeamToPhase(selectedPhaseForTeam!!.id, team, group?.id)
                showAddTeamDialog = false
            }
        )
    }

    if (showDialog) {
        AddPhaseDialog(
            onDismiss = { showDialog = false },
            onConfirm = { request ->
                viewModel.createPhase(seasonId, request)
                showDialog = false
            }
        )
    }

    if (showAutoScheduleDialog) {
        val venues by viewModel.venues.collectAsState()
        AutoSchedulePhaseSelectorDialog(
            phases = phases,
            venues = venues,
            seasonStartDate = seasonDetail?.season?.startDate,
            onDismiss = { showAutoScheduleDialog = false },
            onConfirm = { phaseId, options ->
                viewModel.autoGenerateSchedule(phaseId, options)
                showAutoScheduleDialog = false
            }
        )
    }
}

@Composable
fun GroupCard(
    group: GroupItem,
    allTeams: List<TeamItem>,
    isMoveActive: Boolean,
    onRemoveTeam: (Int) -> Unit,
    onSelectTeam: (TeamItem) -> Unit,
    onMoveHere: () -> Unit,
    modifier: Modifier = Modifier
) {
    // SỬA LỖI: Chỉ lọc đúng đội thuộc về bảng này (group.id)
    val teamsInGroup = allTeams.filter { it.groupId == group.id }

    Column(
        modifier = modifier
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
            Text(
                text = "${group.name.uppercase()} (${teamsInGroup.size})", 
                color = NeonGreen, 
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).clickable { onSelectTeam(team) },
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
                        Icon(Icons.Default.Close, contentDescription = "Xóa đội", tint = Color.Red, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddTeamToPhaseDialog(
    teams: List<TeamItem>,
    groups: List<GroupItem>,
    onDismiss: () -> Unit,
    onTeamSelected: (TeamItem, GroupItem?) -> Unit
) {
    var selectedTeam by remember { mutableStateOf<TeamItem?>(null) }
    var selectedGroup by remember { mutableStateOf<GroupItem?>(null) }
    val isConfirmEnabled = selectedTeam != null && (groups.isEmpty() || selectedGroup != null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm đội vào vòng đấu", color = Color.White) },
        text = {
            Column {
                Text("Chọn đội:", color = Color.Gray)
                LazyColumn(modifier = Modifier.height(150.dp)) {
                    items(teams) { team ->
                        TextButton(onClick = { selectedTeam = team }) {
                            Text(team.name, color = if (selectedTeam == team) NeonGreen else Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (groups.isNotEmpty()) {
                    Text("Chọn bảng đấu:", color = Color.Gray)
                    LazyRow {
                        items(groups) { group ->
                            TextButton(
                                onClick = { selectedGroup = group },
                                colors = ButtonDefaults.textButtonColors(containerColor = if (selectedGroup == group) Color.DarkGray else Color.Transparent)
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
                enabled = isConfirmEnabled
            ) {
                Text("Xác nhận", color = if (isConfirmEnabled) NeonGreen else Color.Gray)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.White) }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}

@Composable
fun AddSeasonDialog(onDismiss: () -> Unit, onConfirm: (CreateSeasonRequest) -> Unit) {
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
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên mùa giải") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Mô tả ngắn") })
                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Ngày bắt đầu") })
                OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("Ngày kết thúc") })
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Hạn đăng ký") })
                OutlinedTextField(value = maxTeams, onValueChange = { maxTeams = it }, label = { Text("Số đội tối đa") })
                OutlinedTextField(value = fee, onValueChange = { fee = it }, label = { Text("Lệ phí đăng ký") })
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
                        onConfirm(CreateSeasonRequest(
                            name = name,
                            description = desc.ifBlank { null },
                            startDate = startDate,
                            endDate = endDate,
                            registrationDeadline = deadline,
                            isRegistrationOpen = isRegOpen,
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
fun SeasonAdminCard(season: SeasonAdminItem, onToggleRegistration: (Boolean) -> Unit, onClick: () -> Unit) {
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
                    Switch(checked = season.isRegistrationOpen == 1, onCheckedChange = onToggleRegistration, colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen))
                }
            }
            Text(text = "Hạn đăng ký: ${DateUtils.formatTime(season.registrationDeadline ?: "")}", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhaseDialog(onDismiss: () -> Unit, onConfirm: (CreatePhaseRequest) -> Unit) {
    val phaseMapping = mapOf(
        "Vòng Bảng" to "group_stage",
        "Vòng 1/16" to "round_of_16",
        "Tứ Kết" to "quarter_final",
        "Bán Kết" to "semi_final",
        "Chung Kết" to "final"
    )
    val formatMap = mapOf("round_robin" to "Vòng tròn tính điểm", "knockout" to "Loại trực tiếp")
    
    var selectedName by remember { mutableStateOf("Vòng Bảng") }
    var selectedFormat by remember { mutableStateOf("round_robin") }
    var customType by remember { mutableStateOf("group_stage") }
    var order by remember { mutableStateOf("1") }
    var groupCount by remember { mutableStateOf("1") }
    var expandedName by remember { mutableStateOf(false) }
    var expandedFormat by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Vòng đấu") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                // Tên vòng đấu
                ExposedDropdownMenuBox(
                    expanded = expandedName,
                    onExpandedChange = { expandedName = !expandedName }
                ) {
                    OutlinedTextField(
                        value = selectedName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tên vòng đấu") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedName) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedName,
                        onDismissRequest = { expandedName = false }
                    ) {
                        phaseMapping.keys.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    selectedName = name
                                    // Tự động chuyển định dạng dựa trên tên vòng đấu
                                    selectedFormat = if (name == "Vòng Bảng") "round_robin" else "knockout"
                                    customType = phaseMapping[name] ?: "group_stage"
                                    // Tự động gán order gợi ý
                                    order = when(name) {
                                        "Vòng Bảng" -> "1"
                                        "Vòng 1/16" -> "2"
                                        "Tứ Kết" -> "3"
                                        "Bán Kết" -> "4"
                                        "Chung Kết" -> "5"
                                        else -> "1"
                                    }
                                    expandedName = false
                                }
                            )
                        }
                    }
                }

                // Mã loại (Type) - Cho phép Admin thấy hoặc chỉnh nếu cần
                OutlinedTextField(
                    value = customType,
                    onValueChange = { customType = it },
                    label = { Text("Mã loại (Type)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Định dạng
                ExposedDropdownMenuBox(
                    expanded = expandedFormat,
                    onExpandedChange = { expandedFormat = !expandedFormat }
                ) {
                    OutlinedTextField(
                        value = formatMap[selectedFormat] ?: selectedFormat,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Định dạng") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFormat) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFormat,
                        onDismissRequest = { expandedFormat = false }
                    ) {
                        formatMap.forEach { (key, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedFormat = key
                                    expandedFormat = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = order,
                    onValueChange = { order = it },
                    label = { Text("Thứ tự (Order)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedFormat == "round_robin") {
                    OutlinedTextField(
                        value = groupCount,
                        onValueChange = { groupCount = it },
                        label = { Text("Số lượng bảng") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(CreatePhaseRequest(
                    name = selectedName,
                    type = customType, // Sử dụng giá trị từ TextField để Admin có thể chỉnh sửa
                    format = selectedFormat,
                    order = order.toIntOrNull() ?: 1,
                    // Đối với knockout, thử gửi 1 thay vì 0 hoặc null nếu 0 bị lỗi
                    groupCount = if (selectedFormat == "round_robin") groupCount.toIntOrNull() else 1
                ))
            }) { Text("Tạo") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
fun AutoSchedulePhaseSelectorDialog(
    phases: List<PhaseItem>, 
    venues: List<VenueItem>,
    seasonStartDate: String?, 
    onDismiss: () -> Unit, 
    onConfirm: (Int, ScheduleOptionsRequest) -> Unit
) {
    var selectedPhaseId by remember { mutableIntStateOf(-1) }
    var isAutoDate by remember { mutableStateOf(true) }
    var isAutoTime by remember { mutableStateOf(true) }
    var startDate by remember(seasonStartDate) { mutableStateOf(seasonStartDate?.split("T")?.get(0) ?: "2025-06-01") }
    var startTime by remember { mutableStateOf("18:00") }
    var intervalHours by remember { mutableIntStateOf(2) }
    
    val selectedVenueIds = remember { mutableStateListOf<Int>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Cấu hình xếp lịch tự động", color = Color.White) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("1. Chọn Vòng đấu:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGreen)
                phases.forEach { phase ->
                    val isScheduled = !phase.matches.isNullOrEmpty()
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable(enabled = !isScheduled) { selectedPhaseId = phase.id }.padding(vertical = 4.dp)) {
                        RadioButton(selected = selectedPhaseId == phase.id, onClick = { if (!isScheduled) selectedPhaseId = phase.id }, enabled = !isScheduled, colors = RadioButtonDefaults.colors(selectedColor = NeonGreen))
                        Column {
                            Text(phase.name, fontSize = 14.sp, color = if (isScheduled) Color.Gray else Color.White)
                            if (isScheduled) Text("(Đã có lịch đấu)", fontSize = 10.sp, color = Color.Red)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("2. Chọn danh sách sân đấu:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGreen)
                if (venues.isEmpty()) {
                    Text("Không có sân đấu nào được tìm thấy", color = Color.Gray, fontSize = 12.sp)
                }
                venues.forEach { venue ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier.fillMaxWidth().clickable { 
                            if (selectedVenueIds.contains(venue.id)) selectedVenueIds.remove(venue.id)
                            else selectedVenueIds.add(venue.id)
                        }.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedVenueIds.contains(venue.id), 
                            onCheckedChange = { 
                                if (it) selectedVenueIds.add(venue.id)
                                else selectedVenueIds.remove(venue.id)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = NeonGreen)
                        )
                        Text(venue.name, color = Color.White, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("3. Thời gian:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonGreen)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAutoDate, onCheckedChange = { isAutoDate = it }, colors = CheckboxDefaults.colors(checkedColor = NeonGreen))
                    Text("Tự động chọn ngày", color = Color.White, fontSize = 12.sp)
                }
                if (!isAutoDate) OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Ngày bắt đầu") }, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(color = Color.White))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isAutoTime, onCheckedChange = { isAutoTime = it }, colors = CheckboxDefaults.colors(checkedColor = NeonGreen))
                    Text("Tự động chọn giờ", color = Color.White, fontSize = 12.sp)
                }
                if (!isAutoTime) OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Giờ bắt đầu") }, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(color = Color.White))
                Spacer(modifier = Modifier.height(16.dp))
                Text("4. Khoảng cách (giờ):", color = NeonGreen)
                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..10).forEach { hour ->
                        FilterChip(selected = intervalHours == hour, onClick = { intervalHours = hour }, label = { Text(hour.toString()) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NeonGreen, labelColor = Color.White, selectedLabelColor = Color.Black))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    onConfirm(
                        selectedPhaseId, 
                        ScheduleOptionsRequest(
                            startDate = if (isAutoDate) null else startDate, 
                            startTime = if (isAutoTime) null else startTime, 
                            intervalHours = intervalHours,
                            venueIds = if (selectedVenueIds.isEmpty()) null else selectedVenueIds.toList()
                        )
                    ) 
                }, 
                enabled = selectedPhaseId != -1, 
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Chạy tự động", color = Color.Black)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) } }
    )
}

@Composable
fun TournamentAdminCard(tournament: TournamentItem, onToggleStatus: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, if (tournament.isActive == 1) NeonGreen.copy(alpha = 0.5f) else Color.Gray)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tournament.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                tournament.description?.let { Text(it, color = Color.Gray, fontSize = 13.sp, maxLines = 1) }
                Text("Cầu thủ: ${tournament.minPlayers ?: 0} - ${tournament.maxPlayers ?: 0}", color = Color.LightGray, fontSize = 12.sp)
            }
            Switch(checked = tournament.isActive == 1, onCheckedChange = { onToggleStatus() }, colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen, checkedTrackColor = NeonGreen.copy(alpha = 0.5f)))
        }
    }
}

@Composable
fun AddTournamentDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableStateOf("20") }
    var minPlayers by remember { mutableStateOf("7") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo giải đấu mới") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên giải đấu") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = maxPlayers, onValueChange = { maxPlayers = it }, label = { Text("Cầu thủ tối đa") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = minPlayers, onValueChange = { minPlayers = it }, label = { Text("Cầu thủ tối thiểu") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, desc, maxPlayers.toIntOrNull() ?: 20, minPlayers.toIntOrNull() ?: 7) }) { Text("Tạo") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}
