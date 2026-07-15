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
fun PlayerManagementAdminScreen(viewModel: AdminViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val players by viewModel.players.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var selectedPlayer by remember { mutableStateOf<AdminPlayerItem?>(null) }
    
    var showAddEditDialog by remember { mutableStateOf(false) }
    var playerToEdit by remember { mutableStateOf<AdminPlayerItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchPlayers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "QUẢN LÝ CẦU THỦ", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "Tìm kiếm và quản lý thông tin cầu thủ", color = Color.LightGray, fontSize = 13.sp)
            }
            IconButton(
                onClick = { 
                    playerToEdit = null
                    showAddEditDialog = true 
                },
                modifier = Modifier.background(NeonGreen, RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Player", tint = Color.Black)
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                viewModel.fetchPlayers(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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

        if (isLoading && players.isEmpty()) {
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
                        onUnlockClick = { player.userId?.let { viewModel.unlockAccount(it) } },
                        onEditClick = {
                            playerToEdit = player
                            showAddEditDialog = true
                        },
                        onClick = { selectedPlayer = player }
                    )
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditPlayerDialog(
            player = playerToEdit,
            onDismiss = { showAddEditDialog = false },
            onConfirm = { request ->
                if (playerToEdit == null) {
                    viewModel.createPlayer(request)
                } else {
                    playerToEdit!!.playerId?.let { viewModel.updatePlayer(it, request) }
                }
                showAddEditDialog = false
            }
        )
    }

    selectedPlayer?.let { player ->
        PlayerDetailDialog(player = player, onDismiss = { selectedPlayer = null })
    }
}

@Composable
fun PlayerAdminCard(
    player: AdminPlayerItem, 
    onLockClick: () -> Unit, 
    onUnlockClick: () -> Unit,
    onEditClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                }
                if (player.userActive == 0) {
                    IconButton(onClick = onUnlockClick) {
                        Icon(imageVector = Icons.Default.LockOpen, contentDescription = "Unlock", tint = NeonGreen)
                    }
                } else {
                    IconButton(onClick = onLockClick) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditPlayerDialog(
    player: AdminPlayerItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (AdminPlayerRequest) -> Unit
) {
    var name by remember { mutableStateOf(player?.name ?: "") }
    var email by remember { mutableStateOf(player?.email ?: "") }
    var phone by remember { mutableStateOf(player?.phone ?: "") }
    var pos by remember { mutableStateOf(player?.position ?: "") }
    var dob by remember { mutableStateOf(player?.dateOfBirth?.split("T")?.get(0) ?: "2000-01-01") }
    var nationality by remember { mutableStateOf(player?.nationality ?: "Việt Nam") }
    var height by remember { mutableStateOf(player?.height ?: "") }
    var weight by remember { mutableStateOf(player?.weight ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text(if (player == null) "Thêm Cầu thủ mới" else "Sửa thông tin Cầu thủ", color = NeonGreen) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Họ và tên") }, textStyle = TextStyle(color = Color.White))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, textStyle = TextStyle(color = Color.White))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, textStyle = TextStyle(color = Color.White))
                OutlinedTextField(value = pos, onValueChange = { pos = it }, label = { Text("Vị trí") }, textStyle = TextStyle(color = Color.White))
                OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Ngày sinh (YYYY-MM-DD)") }, textStyle = TextStyle(color = Color.White))
                OutlinedTextField(value = nationality, onValueChange = { nationality = it }, label = { Text("Quốc tịch") }, textStyle = TextStyle(color = Color.White))
                OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Chiều cao (cm)") }, textStyle = TextStyle(color = Color.White), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Cân nặng (kg)") }, textStyle = TextStyle(color = Color.White), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(AdminPlayerRequest(
                        name = name,
                        email = email,
                        phone = phone.ifBlank { null },
                        position = pos.ifBlank { null },
                        dateOfBirth = dob.ifBlank { null },
                        nationality = nationality.ifBlank { null },
                        height = height.ifBlank { null },
                        weight = weight.ifBlank { null },
                        userId = player?.userId
                    ))
                },
                enabled = name.isNotBlank() && email.isNotBlank()
            ) {
                Text(if (player == null) "Tạo" else "Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun PlayerDetailDialog(player: AdminPlayerItem, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { 
            Text(text = "Chi tiết Cầu thủ", color = NeonGreen, fontWeight = FontWeight.Bold) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DetailRow("Họ và tên", player.name)
                DetailRow("Email", player.email)
                DetailRow("Số điện thoại", player.phone)
                DetailRow("Ngày sinh", player.dateOfBirth?.split("T")?.get(0))
                DetailRow("Quốc tịch", player.nationality)
                DetailRow("Vị trí", player.position)
                DetailRow("Chiều cao", "${player.height} cm")
                DetailRow("Cân nặng", "${player.weight} kg")
                DetailRow("Trạng thái tài khoản", if (player.userActive == 1) "Đang hoạt động" else "Đang bị khóa")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = NeonGreen)
            }
        }
    )
}

