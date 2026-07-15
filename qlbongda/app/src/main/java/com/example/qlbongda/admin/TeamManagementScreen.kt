package com.example.qlbongda.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.viewmodel.AdminViewModel
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

@Composable
fun TeamManagementScreen(viewModel: AdminViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val teams by viewModel.teams.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var selectedTeamId by remember { mutableIntStateOf(-1) }
    val teamDetail by viewModel.teamDetail.collectAsStateWithLifecycle()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var teamToEdit by remember { mutableStateOf<AdminTeamItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchTeams()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "QUẢN LÝ ĐỘI BÓNG", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Danh sách các đội bóng trên hệ thống", color = Color.LightGray, fontSize = 13.sp)
            }
            IconButton(
                onClick = { 
                    teamToEdit = null
                    showAddEditDialog = true 
                },
                modifier = Modifier.background(NeonGreen, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Team", tint = Color.Black)
            }
        }
        HorizontalDivider(color = Color(0xFF222222))

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

        if (isLoading && teams.isEmpty()) {
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
                        onReject = { team.id?.let { viewModel.rejectTeam(it) } },
                        onEdit = {
                            teamToEdit = team
                            showAddEditDialog = true
                        },
                        onClick = { team.id?.let { 
                            selectedTeamId = it 
                            viewModel.fetchAdminTeamDetail(it)
                        } }
                    )
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditTeamDialog(
            team = teamToEdit,
            onDismiss = { showAddEditDialog = false },
            onConfirm = { name, coach, desc ->
                val request = CreateTeamRequestAdmin(name, coach, desc)
                if (teamToEdit == null) {
                    viewModel.createTeam(request)
                } else {
                    viewModel.updateTeam(teamToEdit!!.id!!, request)
                }
                showAddEditDialog = false
            }
        )
    }

    if (selectedTeamId != -1 && teamDetail != null) {
        TeamDetailDialog(
            detail = teamDetail!!,
            viewModel = viewModel,
            onDismiss = { selectedTeamId = -1 }
        )
    }
}

@Composable
fun TeamAdminCard(team: AdminTeamItem, onApprove: () -> Unit, onReject: () -> Unit, onEdit: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
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

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }

                if (team.isActive == 0) {
                    IconButton(onClick = onApprove) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Approve", tint = NeonGreen)
                    }
                    IconButton(onClick = onReject) {
                        Icon(Icons.Default.Cancel, contentDescription = "Reject", tint = Color.Red)
                    }
                } else {
                    IconButton(onClick = onReject) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete/Deactivate", tint = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditTeamDialog(
    team: AdminTeamItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(team?.name ?: "") }
    var coach by remember { mutableStateOf(team?.coachName ?: "") }
    var desc by remember { mutableStateOf(team?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text(if (team == null) "Thêm Đội bóng mới" else "Sửa thông tin Đội bóng", color = NeonGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên đội") },
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = coach,
                    onValueChange = { coach = it },
                    label = { Text("Huấn luyện viên") },
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Mô tả") },
                    textStyle = TextStyle(color = Color.White)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, coach, desc) }, enabled = name.isNotBlank()) {
                Text(if (team == null) "Tạo" else "Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun TeamDetailDialog(detail: FullTeamDetail, viewModel: AdminViewModel, onDismiss: () -> Unit) {
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var playerToEdit by remember { mutableStateOf<TeamPlayerDetail?>(null) }

    val starters = detail.players.filter { it.isStarter == 1 }
    val reserves = detail.players.filter { it.isStarter == 0 }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { 
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Chi tiết Đội bóng", color = NeonGreen, fontWeight = FontWeight.Bold)
                IconButton(onClick = { showAddPlayerDialog = true }) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Add Player", tint = NeonGreen)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("THÔNG TIN CHUNG", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                DetailRow("Tên đội", detail.team.name)
                DetailRow("Huấn luyện viên", detail.team.coachName)
                DetailRow("Mô tả", detail.team.description)
                DetailRow("Ngày tạo", detail.team.createdAt?.split("T")?.get(0))

                Spacer(modifier = Modifier.height(8.dp))
                Text("NGƯỜI QUẢN LÝ (LEADERS)", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                if (detail.leaders.isEmpty()) {
                    Text("Chưa có người quản lý", color = Color.Gray, fontSize = 13.sp)
                } else {
                    detail.leaders.forEach { leader ->
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(leader.name, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Email: ${leader.email}", color = Color.LightGray, fontSize = 12.sp)
                            leader.phone?.let { Text("SĐT: $it", color = Color.LightGray, fontSize = 12.sp) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("ĐỘI HÌNH CHÍNH (${starters.size}/11)", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                if (starters.isEmpty()) {
                    Text("Chưa có cầu thủ đá chính", color = Color.Gray, fontSize = 13.sp)
                } else {
                    starters.forEach { player ->
                        PlayerItemRow(
                            player = player,
                            teamId = detail.team.id ?: -1,
                            viewModel = viewModel,
                            onEdit = { playerToEdit = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("DỰ BỊ (${reserves.size})", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                if (reserves.isEmpty()) {
                    Text("Không có cầu thủ dự bị", color = Color.Gray, fontSize = 13.sp)
                } else {
                    reserves.forEach { player ->
                        PlayerItemRow(
                            player = player,
                            teamId = detail.team.id ?: -1,
                            viewModel = viewModel,
                            onEdit = { playerToEdit = it }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = NeonGreen)
            }
        }
    )

    if (showAddPlayerDialog) {
        AddPlayerToTeamDialogAdmin(
            onDismiss = { showAddPlayerDialog = false },
            onConfirm = { email, jersey, pos ->
                detail.team.id?.let {
                    viewModel.addPlayerToTeamAdmin(it, AddPlayerByEmailRequestAdmin(email, jersey, pos))
                }
                showAddPlayerDialog = false
            }
        )
    }

    if (playerToEdit != null) {
        EditPlayerInTeamDialogAdmin(
            player = playerToEdit!!,
            onDismiss = { playerToEdit = null },
            onConfirm = { jersey, pos, role ->
                viewModel.updateTeamPlayerInfo(playerToEdit!!.id, detail.team.id ?: -1, jersey, pos, role)
                playerToEdit = null
            }
        )
    }
}

@Composable
fun PlayerItemRow(
    player: TeamPlayerDetail,
    teamId: Int,
    viewModel: AdminViewModel,
    onEdit: (TeamPlayerDetail) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(30.dp).background(NeonGreen, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("#${player.jerseyNumber}", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(player.playerName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                if (player.role == "captain") {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Star, contentDescription = "Captain", tint = Color.Yellow, modifier = Modifier.size(14.dp))
                }
            }
            Text("${player.position.uppercase()} - ${player.status}", color = Color.Gray, fontSize = 11.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onEdit(player) }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.LightGray, modifier = Modifier.size(18.dp))
            }
            
            IconButton(
                onClick = { viewModel.updatePlayerStarterStatus(player.id, player.isStarter == 0, teamId) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (player.isStarter == 1) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Toggle Starter",
                    tint = if (player.isStarter == 1) NeonGreen else Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = { viewModel.removePlayerFromTeamAdmin(player.id, teamId) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun EditPlayerInTeamDialogAdmin(
    player: TeamPlayerDetail,
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String) -> Unit
) {
    var jersey by remember { mutableStateOf(player.jerseyNumber.toString()) }
    var pos by remember { mutableStateOf(player.position) }
    var role by remember { mutableStateOf(player.role) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Sửa thông tin Cầu thủ trong đội", color = NeonGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = jersey,
                    onValueChange = { jersey = it.filter { c -> c.isDigit() } },
                    label = { Text("Số áo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = pos,
                    onValueChange = { pos = it },
                    label = { Text("Vị trí (gk, defender, midfielder, forward)") },
                    textStyle = TextStyle(color = Color.White)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = role == "captain",
                        onCheckedChange = { role = if (it) "captain" else "player" },
                        colors = CheckboxDefaults.colors(checkedColor = NeonGreen)
                    )
                    Text("Đội trưởng", color = Color.White)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(jersey.toIntOrNull() ?: 0, pos, role) },
                enabled = jersey.isNotBlank() && pos.isNotBlank()
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun AddPlayerToTeamDialogAdmin(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var jersey by remember { mutableStateOf("") }
    var pos by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Thêm Cầu thủ vào Đội", color = NeonGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Cầu thủ") },
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = jersey,
                    onValueChange = { jersey = it.filter { c -> c.isDigit() } },
                    label = { Text("Số áo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(color = Color.White)
                )
                OutlinedTextField(
                    value = pos,
                    onValueChange = { pos = it },
                    label = { Text("Vị trí") },
                    textStyle = TextStyle(color = Color.White)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(email, jersey.toIntOrNull() ?: 0, pos) },
                enabled = email.isNotBlank() && jersey.isNotBlank()
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

