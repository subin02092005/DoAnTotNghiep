package com.example.qlbongda

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.ui.theme.NeonGreen

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
    onLeagueRegisteredChange: (Boolean) -> Unit,
    currentUserRole: String
) {
    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE) }
    val userId = sharedPref.getInt("USER_ID", -1)

    // Load dữ liệu ban đầu
    LaunchedEffect(userId) {
        if (userId != -1) {
            try {
                val response = RetrofitClient.getClient(context).getMyTeam(userId)
                if (response.isSuccessful && response.body()?.hasTeam == true) {
                    val teamData = response.body()?.data
                    onTeamRegisteredChange(true)
                    onTeamNameChange(teamData?.teamName ?: "")
                    onLeaderNameChange(teamData?.captainName ?: "")
                    onCoachNameChange(teamData?.coachName ?: "")
                    playerList.clear()
                    teamData?.players?.let { playerList.addAll(it) }
                }
            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "Lỗi: ${e.message}")
            }
        }
    }
    android.util.Log.d("ROLE_DEBUG", "Role hiện tại là: '$currentUserRole'")
    // PHÂN TÁCH GIAO DIỆN TẠI ĐÂY
    val role = currentUserRole.lowercase().trim() // CHUYỂN VỀ THƯỜNG VÀ XÓA KHOẢNG TRẮNG DƯ

    if (role == "coach" || role == "captain") {
        CoachCaptainScreen(
            playerList,
            isTeamRegistered,
            onTeamRegisteredChange,
            teamName,
            onTeamNameChange,
            leaderName,
            onLeaderNameChange,
            coachName,
            onCoachNameChange,
            isLeagueRegistered,
            onLeagueRegisteredChange
        )
    } else {
        PlayerReadOnlyScreen(teamName, leaderName, coachName, playerList)
    }
}

// Màn hình QUẢN LÝ (Coach/Captain)
@Composable
fun CoachCaptainScreen(
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
    var isEditing by remember { mutableStateOf(false) }
    var editIdx by remember { mutableStateOf(-1) }
    var nbr by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pos by remember { mutableStateOf("FW") } // Biến lưu vị trí
    var newCoachName by remember { mutableStateOf(coachName) }
    LaunchedEffect(coachName) {
        newCoachName = coachName
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 🌟 Dùng stickyHeader để tiêu đề ghim lại khi cuộn
        stickyHeader {
            Surface(
                color = Color.Black, // Phải set màu nền để không bị trong suốt
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text(
                        "QUẢN LÝ ĐỘI BÓNG",
                        color = NeonGreen,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .padding(horizontal = 60.dp)
                            .background(Color.White))
                }
            }
        }

        // KHUNG THÔNG TIN ĐỘI BÓNG
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = BorderStroke(1.dp, NeonGreen)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("ĐỘI: ${teamName.uppercase()}", color = NeonGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("CAPTAIN: $leaderName", color = Color.White, fontSize = 14.sp)
                    Text("HLV: ${coachName.ifEmpty { "Chưa cập nhật" }}", color = Color.Yellow, fontSize = 14.sp)
                }
            }
        }

        // KHUNG ĐỔI HLV (Màu vàng)
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)), border = BorderStroke(1.dp, Color.Yellow)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = newCoachName,
                        onValueChange = {
                            // 🌟 Chỉ cho phép nếu thỏa mãn validator
                            if (isCoachNameValid(it) || it.isEmpty()) {
                                newCoachName = it
                            }
                        },
                        label = { Text("Cập nhật HLV", color = Color.Yellow) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Yellow, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onCoachNameChange(newCoachName) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow), modifier = Modifier.fillMaxWidth()) {
                        Text("LƯU HLV", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // FORM CẦU THỦ
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)), border = BorderStroke(1.dp, NeonGreen)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(if (isEditing) "CẬP NHẬT CẦU THỦ" else "THÊM CẦU THỦ", color = NeonGreen, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value =email,
                        onValueChange = { if (isEmailValid(it)) email = it },
                        label = { Text("Email thành viên") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isEditing, // 🌟 KHÔNG CHO SỬA EMAIL KHI ĐANG EDIT
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = if (isEditing) Color.Gray else Color.White,
                            disabledTextColor = Color.Gray
                        )
                    )

                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = nbr,
                            onValueChange = { if (isNumberValid(it)) nbr = it },
                            label = { Text("Số", fontSize = 12.sp) }, // Giảm font label cho gọn
                            modifier = Modifier
                                .width(70.dp) // 🌟 Thay vì weight, hãy dùng độ rộng cố định nhỏ
                                .height(56.dp), // Chiều cao chuẩn
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Cụm Vị trí (vẫn giữ weight để nó chiếm phần còn lại)
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ){
                            listOf("GK", "DF", "MF", "FW").forEach { p ->
                                FilterChip(
                                    selected = pos == p,
                                    onClick = { pos = p },
                                    label = { Text(p, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NeonGreen)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (isEditing && editIdx != -1) {
                                // CHỈ CẬP NHẬT SỐ ÁO & VỊ TRÍ
                                val oldPlayer = playerList[editIdx]
                                playerList[editIdx] = PlayerInfo(nbr, oldPlayer.name, mapPositionToFull(pos))
                            } else {
                                // THÊM MỚI BẰNG EMAIL
                                playerList.add(PlayerInfo(nbr, email, mapPositionToFull(pos)))
                            }
                            // Reset form
                            email = ""; nbr = ""; pos = "FW"; isEditing = false; editIdx = -1
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isEditing) "CẬP NHẬT THÔNG TIN" else "THÊM VÀO ĐỘI", color = Color.Black)
                    }

                    if(isEditing) {
                        TextButton(onClick = { isEditing = false; editIdx = -1; email = ""; nbr = "" }) {
                            Text("Hủy chỉnh sửa", color = Color.Red)
                        }
                    }
                }
            }
        }

        // DANH SÁCH CẦU THỦ VỚI MÀU VỊ TRÍ
        items(playerList.size) { i ->
            val p = playerList[i]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                border = BorderStroke(1.dp, if (editIdx == i) NeonGreen else Color.DarkGray)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(NeonGreen, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text(p.number, color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(p.name, color = Color.White, fontWeight = FontWeight.Bold)
                        // Hiển thị vị trí với màu tương ứng
                        Text(text = p.position, color = getPositionColor(p.position), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(onClick = { isEditing = true; editIdx = i; nbr = p.number; email = p.name; pos = mapFullToShort(p.position)}) {
                        Icon(Icons.Default.Edit, "Edit", tint = if (editIdx == i) NeonGreen else Color.LightGray)
                    }
                    IconButton(onClick = { playerList.removeAt(i) }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFFF5252))
                    }
                }
            }
        }
    }
}

// Hàm hỗ trợ chọn màu theo vị trí
fun getPositionColor(pos: String): Color {
    return when (pos) {
        "forward" -> Color(0xFFFF5252)
        "midfielder" -> Color(0xFF69F0AE)
        "defender" -> Color(0xFF40C4FF)
        else -> Color(0xFFFFD740)
    }
}
fun isCoachNameValid(input: String) = input.matches(Regex("^[\\p{L}\\s]*$"))
fun isEmailValid(input: String) = input.matches(Regex("^[A-Za-z0-9+_.-]*$"))
// Validator cho số: Chỉ cho số
fun isNumberValid(input: String) = input.matches(Regex("^[0-9]*$"))
fun mapPositionToFull(shortPos: String): String {
    return when (shortPos) {
        "GK" -> "goalkeeper"
        "DF" -> "defender"
        "MF" -> "midfielder"
        "FW" -> "forward"
        else -> shortPos
    }
}
fun mapFullToShort(fullPos: String): String {
    return when (fullPos.lowercase()) {
        "goalkeeper" -> "GK"
        "defender" -> "DF"
        "midfielder" -> "MF"
        "forward" -> "FW"
        else -> "FW"
    }
}

// Màn hình CHỈ XEM (Player)
@OptIn(ExperimentalFoundationApi::class) // 🌟 Cần thêm annotation này để dùng stickyHeader
@Composable
fun PlayerReadOnlyScreen(
    teamName: String,
    leaderName: String,
    coachName: String,
    playerList: List<PlayerInfo>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Phần Tiêu đề CỐ ĐỊNH (Sticky Header)
        stickyHeader {
            // Cần bọc trong Box hoặc Surface để nó không bị trong suốt khi kéo
            Surface(color = Color.Black, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "ĐỘI BÓNG CỦA TÔI",
                    color = NeonGreen,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .padding(horizontal = 60.dp)
                        .background(Color.White))

            }
        }

        // 2. Các thành phần cuộn bên dưới
        item {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(NeonGreen.copy(alpha = 0.1f), CircleShape)
                    .border(BorderStroke(2.dp, NeonGreen), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = teamName.take(2).uppercase(),
                    color = NeonGreen,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Text(teamName.uppercase(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text("MÙA GIẢI 2026", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), // Thêm padding ngang để không bị dính sát mép
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                // Thẻ HLV
                Card(
                    modifier = Modifier.weight(1f).height(100.dp), // Thêm height cố định để 2 thẻ bằng nhau
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa nội dung bên trong Card
                    ) {
                        Icon(Icons.Default.Person, "", tint = Color(0xFFFFD740))
                        Text("HLV", color = Color.Gray, fontSize = 10.sp)
                        Text(coachName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                // Thẻ CAPTAIN
                Card(
                    modifier = Modifier.weight(1f).height(100.dp), // Đảm bảo height bằng nhau
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa nội dung bên trong Card
                    ) {
                        Icon(Icons.Default.Star, "", tint =  Color(0xFFFF5252))
                        Text("CAPTAIN", color = Color.Gray, fontSize = 10.sp)
                        Text(leaderName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }
            }
        }

        item {
            Text("DANH SÁCH CẦU THỦ (${playerList.size})", color = NeonGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp).fillMaxWidth())
        }

        items(playerList) { player ->
            Card(
                modifier = Modifier.padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = BorderStroke(1.dp, Color.DarkGray)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(NeonGreen, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text(player.number, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(player.name, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Vị trí: ${player.position}", color = Color.Gray, fontSize = 12.sp)
                    }
                    Text(
                        text = when(player.position) {
                            "defender" -> "DF"
                            "midfielder" -> "MF"
                            "forward" -> "FW"
                            else -> "GK"
                        },
                        color = when(player.position) {
                            "forward" -> Color(0xFFFF5252)
                            "midfielder" -> Color(0xFF69F0AE)
                            "defender" -> Color(0xFF40C4FF)
                            else -> Color(0xFFFFD740)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
