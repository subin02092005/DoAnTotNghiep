package com.example.qlbongda

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.qlbongda.data.model.PlayerInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamTabContent(
    playerList: SnapshotStateList<PlayerInfo>,
    isTeamRegistered: Boolean,
    onTeamRegisteredChange: (Boolean) -> Unit,
    teamName: String,
    onTeamNameChange: (String) -> Unit,
    leaderName: String,
    onLeaderNameChange: (String) -> Unit,
    coachName: String,
    onCoachNameChange: (String) -> Unit,
    isLeagueRegistered: Boolean,
    onLeagueRegisteredChange: (Boolean) -> Unit
) {
    val context = LocalContext.current

    var isEditingPlayer by remember { mutableStateOf(false) }
    var editingPlayerIndex by remember { mutableStateOf(-1) }
    var inputPlayerNumber by remember { mutableStateOf("") }
    var inputPlayerName by remember { mutableStateOf("") }
    var inputPlayerPosition by remember { mutableStateOf("FW") }
    var inputCoachName by remember { mutableStateOf(coachName) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "QUẢN LÝ ĐỘI BÓNG", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text(text = "— MÙA GIẢI 2026 —", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 16.dp), textAlign = TextAlign.Center)

        if (!isTeamRegistered) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                border = BorderStroke(1.dp, NeonGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Đăng Ký Đội Bóng Thành Viên", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = teamName,
                        onValueChange = onTeamNameChange,
                        label = { Text("Tên đội bóng", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = leaderName,
                        onValueChange = onLeaderNameChange,
                        label = { Text("Tên Captain / Đại diện", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (teamName.trim().isEmpty() || leaderName.trim().isEmpty()) {
                                Toast.makeText(context, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show()
                            } else {
                                onTeamRegisteredChange(true)
                                Toast.makeText(context, "Đăng ký thành viên thành công!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("ĐĂNG KÝ THÀNH VIÊN GIẢI", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)), border = BorderStroke(1.dp, NeonGreen)) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(text = "Đội: ${teamName.uppercase()}", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Đại diện: $leaderName", color = Color.White, fontSize = 13.sp)
                                Text(
                                    text = "HLV: ${if(coachName.trim().isEmpty()) "Chưa cập nhật" else coachName}",
                                    color = if(coachName.trim().isEmpty()) Color.Gray else Color.Yellow,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Button(
                                onClick = {
                                    onLeagueRegisteredChange(!isLeagueRegistered)
                                    val msg = if(!isLeagueRegistered) "Đã nộp đơn đăng ký tham gia Giải đấu! 🏆" else "Đã hủy đơn đăng ký giải đấu!"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if(isLeagueRegistered) Color.DarkGray else Color.Red),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = if (isLeagueRegistered) "ĐÃ ĐĂNG KÝ GIẢI" else "ĐĂNG KÝ GIẢI", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F0F)), border = BorderStroke(1.dp, Color.Yellow)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "BAN HUẤN LUYỆN", color = Color.Yellow, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = inputCoachName,
                                    onValueChange = { inputCoachName = it },
                                    label = { Text("Họ tên Huấn Luyện Viên", color = Color.White, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Yellow, unfocusedBorderColor = Color.Yellow, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                                )
                                Button(
                                    onClick = {
                                        if (inputCoachName.trim().isEmpty()) {
                                            Toast.makeText(context, "Vui lòng nhập tên HLV!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            onCoachNameChange(inputCoachName.trim())
                                            Toast.makeText(context, "Cập nhật HLV thành công!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("LƯU", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F0F)), border = BorderStroke(1.dp, NeonGreen)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = if (isEditingPlayer) "CẬP NHẬT CẦU THỦ" else "THÊM CẦU THỦ MỚI", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(value = inputPlayerNumber, onValueChange = { inputPlayerNumber = it }, label = { Text("Số áo", color = Color.White, fontSize = 11.sp) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                                OutlinedTextField(value = inputPlayerName, onValueChange = { inputPlayerName = it }, label = { Text("Họ tên", color = Color.White, fontSize = 11.sp) }, modifier = Modifier.weight(2.5f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("GK", "DF", "MF", "FW").forEach { pos ->
                                        FilterChip(
                                            selected = inputPlayerPosition == pos,
                                            onClick = { inputPlayerPosition = pos },
                                            label = { Text(pos, fontSize = 10.sp, color = if(inputPlayerPosition == pos) Color.Black else Color.White) },
                                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NeonGreen, selectedLabelColor = Color.Black, containerColor = Color.Black)
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (inputPlayerNumber.trim().isEmpty() || inputPlayerName.trim().isEmpty()) {
                                            Toast.makeText(context, "Thiếu số áo hoặc họ tên!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val newPlayer = PlayerInfo(inputPlayerNumber.trim(), inputPlayerName.trim(), inputPlayerPosition)
                                            if (isEditingPlayer && editingPlayerIndex != -1) {
                                                playerList[editingPlayerIndex] = newPlayer
                                                isEditingPlayer = false
                                                editingPlayerIndex = -1
                                                Toast.makeText(context, "Đã sửa cầu thủ!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                playerList.add(newPlayer)
                                                Toast.makeText(context, "Đã thêm cầu thủ!", Toast.LENGTH_SHORT).show()
                                            }
                                            inputPlayerNumber = ""
                                            inputPlayerName = ""
                                            inputPlayerPosition = "FW"
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(if (isEditingPlayer) "CẬP NHẬT" else "THÊM", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Text("DANH SÁCH CẦU THỦ THÀNH VIÊN (${playerList.size})", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                if(playerList.isEmpty()) {
                    item {
                        Text("Chưa có cầu thủ nào. Vui lòng thêm ở form trên!", color = Color.White, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                }

                items(playerList.size) { index ->
                    val player = playerList[index]
                    Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF121212), RoundedCornerShape(8.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).background(NeonGreen, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                            Text(text = player.number, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = player.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Vị trí: ${player.position}", color = Color.LightGray, fontSize = 11.sp)
                        }
                        IconButton(onClick = {
                            isEditingPlayer = true
                            editingPlayerIndex = index
                            inputPlayerNumber = player.number
                            inputPlayerName = player.name
                            inputPlayerPosition = player.position
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color.LightGray)
                        }
                        IconButton(onClick = {
                            playerList.removeAt(index)
                            Toast.makeText(context, "Đã xóa cầu thủ!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}