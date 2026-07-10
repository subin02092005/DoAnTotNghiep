package com.example.qlbongda

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.AddCoachRequest
import com.example.qlbongda.data.model.AddPlayerRequest
import com.example.qlbongda.data.model.PaymentRequest
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.RegisterTeamRequest
import com.example.qlbongda.data.model.RemovePlayerRequest
import com.example.qlbongda.data.model.SeasonInfo
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlbongda.data.api.HomeViewModel
import com.example.qlbongda.data.model.UpdatePlayerRequest
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils
import com.example.qlbongda.utils.DateUtils.isDeadlinePassed
import kotlinx.coroutines.launch

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
    val sharedPref = remember { context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE) }
    val userId = sharedPref.getInt("USER_ID", -1)
    var myTeamId by remember { mutableIntStateOf(-1) }
    var hasTeam by remember { mutableStateOf(sharedPref.getInt("TEAM_ID", -1) != -1) }
    var currentRole by remember { mutableStateOf(currentUserRole) }
    var showRegistrationScreen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    var isPaymentMode by remember { mutableStateOf(false) }
    var selectedSeasonForPayment by remember { mutableStateOf<SeasonInfo?>(null) }
    val scope = rememberCoroutineScope()
    val seasons = remember { mutableStateListOf<SeasonInfo>() }
    val apiService = remember { RetrofitClient.getClient(context) }
    val viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(apiService) as T
            }
        }
    )

    val fetchSeasons = remember {
        {
            scope.launch {
                isLoading = true
                try {
                    val response = RetrofitClient.getClient(context).getOpenSeasons(myTeamId)
                    if (response.isSuccessful && response.body() != null) {
                        seasons.clear()
                        seasons.addAll(response.body()!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(userId) {
        isLoading = true
        hasTeam = false
        myTeamId = -1
        playerList.clear()
        if (userId != -1) {
            try {
                val response = RetrofitClient.getClient(context).getMyTeam(userId)
                val responseBody = response.body()

                if (response.isSuccessful && responseBody?.hasTeam == true) {
                    val teamData = responseBody.data
                    myTeamId = teamData?.teamId ?: -1
                    hasTeam = true
                    onTeamRegisteredChange(true)
                    onTeamNameChange(teamData?.teamName ?: "")
                    onLeaderNameChange(teamData?.captainName ?: "")
                    onCoachNameChange(teamData?.coachName ?: "")
                    currentRole = teamData?.currentUserRole ?: "player"
                    playerList.clear()
                    teamData?.players?.let {
                        playerList.addAll(it)
                    }
                } else {
                    hasTeam = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    val role = currentRole.lowercase().trim()
    
    if (isPaymentMode && selectedSeasonForPayment != null) {
        PaymentScreen(
            season = selectedSeasonForPayment!!,
            onConfirm = { ref ->
                selectedSeasonForPayment?.let { season ->
                    scope.launch {
                        try {
                            val request = PaymentRequest(
                                transaction_ref = ref,
                                season_team_id = season.season_team_id ?: 0
                            )
                            val response = RetrofitClient.getClient(context).confirmPayment(request)

                            if (response.isSuccessful) {
                                isPaymentMode = false
                                fetchSeasons()
                                Toast.makeText(context, "Thanh toán thành công!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            },
            onBack = { isPaymentMode = false }
        )
    } else {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (showRegistrationScreen) {
            SeasonRegistrationScreen(
                teamId = myTeamId,
                onNavigateToPayment = { season ->
                    selectedSeasonForPayment = season
                    isPaymentMode = true
                },
                onBack = { showRegistrationScreen = false },
                viewModel = viewModel
            )
        } else if (!hasTeam) {
            TeamRegistrationScreen(
                onTeamRegisteredChange = { success ->
                    if (success) {
                        hasTeam = true
                        currentRole = "captain"
                    }
                },
                sharedPref = sharedPref
            )
        } else {
            if (role == "coach" || role == "captain") {
                CoachCaptainScreen(
                    teamId = myTeamId,
                    playerList = playerList,
                    teamName = teamName,
                    leaderName = leaderName,
                    coachName = coachName,
                    onNavigateToRegister = { showRegistrationScreen = true }
                )
            } else {
                PlayerReadOnlyScreen(teamName, leaderName, coachName, playerList)
            }
        }
    }
}

@Composable
fun CoachCaptainScreen(
    teamId: Int,
    playerList: SnapshotStateList<PlayerInfo>,
    teamName: String,
    leaderName: String,
    coachName: String,
    onNavigateToRegister: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }
    var editIdx by remember { mutableIntStateOf(-1) }
    var nbr by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pos by remember { mutableStateOf("FW") }
    var coachEmail by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }

    val sharedPreferences = remember {
        context.getSharedPreferences("AUTH_PREF", Context.MODE_PRIVATE)
    }
    val currentUserId = remember {
        sharedPreferences.getInt("USER_ID", -1)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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

        item {
            Button(
                onClick = { onNavigateToRegister() },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("ĐĂNG KÝ GIẢI ĐẤU", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = BorderStroke(1.dp, NeonGreen)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "ĐỘI: ${teamName.uppercase()}",
                        color = NeonGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("CAPTAIN: $leaderName", color = Color.White, fontSize = 14.sp)
                    Text(
                        "HLV: ${coachName.ifEmpty { "Chưa cập nhật" }}",
                        color = Color.Yellow,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = coachEmail,
                onValueChange = { coachEmail = it },
                label = { Text("Email HLV", color = Color.Yellow) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Yellow,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val request = AddCoachRequest(team_id = teamId, email = coachEmail)
                            val response = RetrofitClient.getClient(context).addCoachByEmail(request)

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Đã gán HLV thành công!", Toast.LENGTH_SHORT).show()
                            } else {
                                val errorMsg = response.errorBody()?.string() ?: "Lỗi!"
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GÁN HLV QUA EMAIL", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)), border = BorderStroke(1.dp, NeonGreen)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        if (isEditing) "CẬP NHẬT CẦU THỦ" else "THÊM CẦU THỦ",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { if (isEmailValid(it)) email = it },
                        label = { Text("Email thành viên") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isEditing,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = if (isEditing) Color.Gray else Color.White,
                            disabledTextColor = Color.Gray
                        )
                    )

                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = nbr,
                            onValueChange = { if (isNumberValid(it)) nbr = it },
                            label = { Text("Số", fontSize = 12.sp) },
                            modifier = Modifier.width(70.dp).height(56.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("GK", "DF", "MF", "FW").forEach { p ->
                                FilterChip(
                                    selected = pos == p,
                                    onClick = { pos = p },
                                    label = { Text(p, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NeonGreen
                                    )
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (isEditing) {
                                val isDuplicateNumber = playerList.any {
                                    it.number == nbr && it.id != playerList[editIdx].id
                                }

                                if (isDuplicateNumber) {
                                    Toast.makeText(context, "Số áo $nbr đã có cầu thủ khác sử dụng!", Toast.LENGTH_SHORT).show()
                                } else {
                                    isUpdating = true
                                    scope.launch {
                                        try {
                                            val player = playerList[editIdx]
                                            val request = UpdatePlayerRequest(
                                                team_id = teamId,
                                                id = player.id,
                                                jersey_number = nbr,
                                                position = mapPositionToFull(pos)
                                            )

                                            val response = RetrofitClient.getClient(context).updatePlayer(request)

                                            if (response.isSuccessful) {
                                                playerList[editIdx] = player.copy(
                                                    number = nbr,
                                                    position = mapPositionToFull(pos)
                                                )
                                                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                                                isEditing = false; editIdx = -1; email = ""; nbr = ""; pos = "FW"
                                            } else {
                                                val errorMsg = response.errorBody()?.string() ?: "Không thể cập nhật!"
                                                Toast.makeText(context, "Lỗi: $errorMsg", Toast.LENGTH_LONG).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show()
                                        } finally {
                                            isUpdating = false
                                        }
                                    }
                                }
                            } else {
                                if (!isEmailFormatValid(email)) {
                                    Toast.makeText(context, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show()
                                } else if (email.isEmpty() || nbr.isEmpty()) {
                                    Toast.makeText(context, "Vui lòng nhập Email và Số áo", Toast.LENGTH_SHORT).show()
                                } else {
                                    scope.launch {
                                        try {
                                            val requestBody = AddPlayerRequest(
                                                team_id = teamId,
                                                email = email,
                                                jersey_number = nbr,
                                                position = mapPositionToFull(pos)
                                            )
                                            val response = RetrofitClient.getClient(context).addPlayerByEmail(requestBody)

                                            if (response.isSuccessful) {
                                                val newPlayer = response.body()?.data
                                                if (newPlayer != null) {
                                                    playerList.add(newPlayer)
                                                    email = ""; nbr = ""; pos = "FW"
                                                    Toast.makeText(context, "Đã thêm ${newPlayer.name} vào đội!", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                val errorString = response.errorBody()?.string()
                                                try {
                                                    val jsonObject = org.json.JSONObject(errorString ?: "{}")
                                                    val errorMessage = jsonObject.optString("message", "Có lỗi xảy ra!")
                                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Lỗi: Không thể thêm cầu thủ", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (isEditing) "CẬP NHẬT THÔNG TIN" else "THÊM VÀO ĐỘI",
                            color = Color.Black
                        )
                    }
                }
            }
        }

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
                        Text(text = p.position, color = getPositionColor(p.position), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(onClick = { isEditing = true; editIdx = i; nbr = p.number; email = p.name; pos = mapFullToShort(p.position)}) {
                        Icon(Icons.Default.Edit, "Edit", tint = if (editIdx == i) NeonGreen else Color.LightGray)
                    }
                    if (p.userId != currentUserId) {
                        IconButton(onClick = {
                            scope.launch {
                                try {
                                    val player = playerList[i]
                                    val requestBody = RemovePlayerRequest(team_id = teamId, player_id = player.id, currentUserId = currentUserId)
                                    val response = RetrofitClient.getClient(context).removePlayer(requestBody)

                                    if (response.isSuccessful) {
                                        playerList.removeAt(i)
                                        Toast.makeText(context, "Đã xóa cầu thủ", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Lỗi xóa cầu thủ", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFFF5252))
                        }
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                }
            }
        }
    }
}

fun getPositionColor(pos: String): Color {
    return when (pos.lowercase()) {
        "forward" -> Color(0xFFFF5252)
        "midfielder" -> Color(0xFF69F0AE)
        "defender" -> Color(0xFF40C4FF)
        else -> Color(0xFFFFD740)
    }
}

fun isEmailValid(input: String) = input.matches(Regex("^[A-Za-z0-9+_.-@]*$"))
fun isEmailFormatValid(email: String): Boolean {
    return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
}
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerReadOnlyScreen(
    teamName: String,
    leaderName: String,
    coachName: String,
    playerList: List<PlayerInfo>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
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
                    modifier = Modifier.fillMaxWidth().height(2.dp).padding(horizontal = 60.dp).background(Color.White))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Box(
                modifier = Modifier.size(80.dp).background(NeonGreen.copy(alpha = 0.1f), CircleShape).border(BorderStroke(2.dp, NeonGreen), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = teamName.take(2).uppercase(), color = NeonGreen, fontSize = 28.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Text(teamName.uppercase(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text("MÙA GIẢI 2026", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Card(modifier = Modifier.weight(1f).height(100.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Person, "", tint = Color(0xFFFFD740))
                        Text("HLV", color = Color.Gray, fontSize = 10.sp)
                        Text(coachName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Card(modifier = Modifier.weight(1f).height(100.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Star, "", tint = Color(0xFFFF5252))
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
            Card(modifier = Modifier.padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)), border = BorderStroke(1.dp, Color.DarkGray)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(NeonGreen, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        Text(player.number, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(player.name, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Vị trí: ${player.position}", color = Color.Gray, fontSize = 12.sp)
                    }
                    Text(
                        text = mapFullToShort(player.position),
                        color = getPositionColor(player.position),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TeamRegistrationScreen(
    onTeamRegisteredChange: (Boolean) -> Unit,
    sharedPref: SharedPreferences
) {
    var teamName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentUserName = sharedPref.getString("USER_NAME", "Người dùng") ?: "Người dùng"

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("ĐĂNG KÝ ĐỘI BÓNG", color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Nhập tên đội bóng") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = currentUserName, onValueChange = {}, label = { Text("Đội trưởng") }, enabled = false, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            if (teamName.isBlank()) {
                Toast.makeText(context, "Vui lòng nhập tên đội bóng!", Toast.LENGTH_SHORT).show()
                return@Button
            }
            scope.launch {
                try {
                    val userId = sharedPref.getInt("USER_ID", -1)
                    val request = RegisterTeamRequest(teamName, "Chưa cập nhật", userId)
                    val response = RetrofitClient.getClient(context).registerTeam(request)
                    if (response.isSuccessful) {
                        val teamId = response.body()?.teamId ?: -1
                        sharedPref.edit().putInt("TEAM_ID", teamId).apply()
                        Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_LONG).show()
                        onTeamRegisteredChange(true)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val message = try { org.json.JSONObject(errorBody ?: "").getString("message") } catch (e: Exception) { "Lỗi: ${response.code()}" }
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) { Toast.makeText(context, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show() }
            }
        }) { Text("HOÀN TẤT ĐĂNG KÝ") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonRegistrationScreen(
    teamId: Int,
    onNavigateToPayment: (SeasonInfo) -> Unit,
    onBack: () -> Unit,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val seasons = remember { mutableStateListOf<SeasonInfo>() }
    var isLoading by remember { mutableStateOf(true) }
    var showRulesDialog by remember { mutableStateOf(false) }
    val rules by viewModel.tournamentRules.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedSeason by remember { mutableStateOf<SeasonInfo?>(null) }

    fun fetchSeasons() {
        scope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.getClient(context).getOpenSeasons(teamId)
                if (response.isSuccessful && response.body() != null) {
                    seasons.clear()
                    seasons.addAll(response.body()!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(teamId) {
        fetchSeasons()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ĐĂNG KÝ GIẢI ĐẤU", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = NeonGreen)
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
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                items(seasons) { season ->
                    Card(
                        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                        border = BorderStroke(1.dp, NeonGreen)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = season.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                            if (!season.description.isNullOrEmpty()) {
                                Text(text = season.description, color = Color.Gray, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            StatusIndicator(
                                status = season.status,
                                registrationDeadline = season.registration_deadline,
                                onStatusClick = {
                                    viewModel.loadGlobalRules()
                                    showRulesDialog = true
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("👥 Tối đa: ${season.max_teams} đội", color = Color.LightGray)
                                Text("💰 ${season.registrationFee} VNĐ", color = Color.Yellow, fontWeight = FontWeight.Bold)
                            }
                            Text("📅 ${DateUtils.formatDate(season.start_date)} - ${DateUtils.formatDate(season.end_date)}", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (season.is_registered == 1) {
                                    if (season.status == "registration_open") {
                                        Button(
                                            onClick = { selectedSeason = season; showDialog = true },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                            modifier = Modifier.weight(1f).height(48.dp)
                                        ) { Text("HỦY", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                    } else {
                                        Box(modifier = Modifier.weight(1f).height(48.dp), contentAlignment = Alignment.Center) {
                                            Text("Đã bắt đầu", color = Color.Gray, fontSize = 10.sp)
                                        }
                                    }

                                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                        when (season.payment_status?.trim()?.lowercase()) {
                                            "confirmed" -> { Text("✅ Đã thanh toán", color = Color.Green, fontWeight = FontWeight.Bold) }
                                            "pending" -> {
                                                Button(
                                                    onClick = { onNavigateToPayment(season) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                                ) { Text("THANH TOÁN", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                            }
                                            else -> { Text("Chưa thanh toán", color = Color.Gray, fontSize = 12.sp) }
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            val alreadyRegistered = seasons.any { it.is_registered == 1 }
                                            if (alreadyRegistered) {
                                                Toast.makeText(context, "Bạn đã đăng ký một giải khác rồi!", Toast.LENGTH_LONG).show()
                                            } else {
                                                scope.launch {
                                                    try {
                                                        val response = RetrofitClient.getClient(context).registerToSeason(mapOf("team_id" to teamId, "season_id" to season.id))
                                                        if (response.isSuccessful) {
                                                            Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                                                            fetchSeasons()
                                                        } else {
                                                            val errorMsg = response.errorBody()?.string() ?: "Lỗi không xác định"
                                                            Toast.makeText(context, "Thất bại: $errorMsg", Toast.LENGTH_LONG).show()
                                                        }
                                                    } catch (e: Exception) {
                                                        Toast.makeText(context, "Không thể kết nối tới máy chủ!", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                        modifier = Modifier.weight(1f)
                                    ) { Text("ĐĂNG KÝ NGAY") }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog && selectedSeason != null) {
            val isPaid = selectedSeason?.payment_status == "confirmed"
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Xác nhận hủy", color = Color.White) },
                text = { Text(text = if (isPaid) "CẢNH BÁO: Bạn đã thanh toán cho giải '${selectedSeason?.name}'. Nếu hủy, bạn sẽ MẤT số tiền đã đóng. Bạn có thực sự muốn tiếp tục?" else "Bạn có chắc chắn muốn hủy tham gia giải '${selectedSeason?.name}' không?", color = if (isPaid) Color(0xFFFF5252) else Color.LightGray, fontWeight = if (isPaid) FontWeight.Bold else FontWeight.Normal) },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            val response = RetrofitClient.getClient(context).unregisterFromSeason(mapOf("team_id" to teamId.toString(), "season_id" to selectedSeason!!.id.toString()))
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Đã hủy giải thành công!", Toast.LENGTH_SHORT).show()
                                fetchSeasons()
                            } else {
                                Toast.makeText(context, "Lỗi khi hủy: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        showDialog = false
                    }) { Text("Đồng ý", color = Color.Red) }
                },
                dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Hủy", color = Color.White) } },
                containerColor = Color(0xFF161616)
            )
        }

        if (showRulesDialog) {
            AlertDialog(
                onDismissRequest = { showRulesDialog = false },
                title = { Text("Luật thi đấu", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Box(modifier = Modifier.heightIn(max = 300.dp).verticalScroll(rememberScrollState())) {
                        if (rules != null) {
                            Column {
                                Text("Điểm thắng: ${rules!!.pointsPerWin}", color = Color.LightGray)
                                Text("Điểm hòa: ${rules!!.pointsPerDraw}", color = Color.LightGray)
                                Text("Điểm thua: ${rules!!.pointsPerLoss}", color = Color.LightGray)
                                Text("Số thẻ vàng để ra sân: ${rules!!.yellowCardsSuspension}", color = Color.LightGray)
                                Text("Cách tính nếu bằng điểm: ${rules!!.tiebreaker_order} ", color = Color.LightGray)
                            }
                        } else { Text("Đang tải luật...", color = Color.Gray) }
                    }
                },
                confirmButton = { TextButton(onClick = { showRulesDialog = false }) { Text("Đóng", color = NeonGreen) } },
                containerColor = Color(0xFF161616)
            )
        }
    }
}

@Composable
fun StatusIndicator(
    status: String,
    registrationDeadline: String?,
    onStatusClick: () -> Unit
) {
    val isExpired = registrationDeadline != null && isDeadlinePassed(registrationDeadline) && status == "registration_open"
    val (color, text) = when {
        isExpired -> Color.Red to "Đã hết hạn"
        status == "upcoming" -> Color.Blue to "Sắp tới"
        status == "registration_open" -> Color.Green to "Đang mở đăng ký"
        status == "ongoing" -> Color.Yellow to "Đang diễn ra"
        status == "finished" -> Color.Gray to "Đã kết thúc"
        status == "cancelled" -> Color.Red to "Đã hủy"
        else -> Color.Gray to status.replaceFirstChar { it.uppercase() }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().clickable { onStatusClick() }.padding(4.dp)
    ) {
        Canvas(modifier = Modifier.size(8.dp)) { drawCircle(color = color) }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
