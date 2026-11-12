package com.example.qlbongda.home

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.*
import com.example.qlbongda.details.FormationBoard
import com.example.qlbongda.details.TeamScorersAssistersContent
import com.example.qlbongda.payment.PaymentScreen
import com.example.qlbongda.ui.theme.NeonGreen
import com.example.qlbongda.utils.DateUtils
import com.example.qlbongda.utils.DateUtils.isDeadlinePassed
import com.example.qlbongda.viewmodel.HomeViewModel
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
    var teamFieldType by remember { mutableIntStateOf(7) }

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

    val fetchSeasons = {
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
                    teamFieldType = teamData?.fieldType ?: 7

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
                CircularProgressIndicator(color = NeonGreen)
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
        } else {
            if (!hasTeam) {
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
                        fieldType = teamFieldType,
                        playerList = playerList,
                        teamName = teamName,
                        leaderName = leaderName,
                        coachName = coachName,
                        onNavigateToRegister = { showRegistrationScreen = true }
                    )
                } else {
                    PlayerReadOnlyScreen(
                        teamName = teamName,
                        leaderName = leaderName,
                        coachName = coachName,
                        playerList = playerList,
                        fieldType = teamFieldType // Truyền loại sân vào đây
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoachCaptainScreen(
    teamId: Int,
    fieldType: Int,
    playerList: SnapshotStateList<PlayerInfo>,
    teamName: String,
    leaderName: String,
    coachName: String,
    onNavigateToRegister: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var currentFieldType by remember(fieldType) { mutableIntStateOf(fieldType) }

    var selectedSubTab by remember { mutableIntStateOf(0) }

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


    // Khởi tạo ViewModel nếu cần hoặc dùng chung ViewModel từ trên xuống
    val apiService = remember { RetrofitClient.getClient(context) }
    val viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(apiService) as T
            }
        }
    )

    // 🌟 GỌI HÀM LẤY TRẠNG THÁI THẺ PHẠT Ở ĐÂY KHI MÀN HÌNH ĐƯỢC MỞ
    LaunchedEffect(teamId) {
        if (teamId != -1) {
            // Giả sử bạn lấy seasonId hiện tại của đội (ví dụ truyền từ ngoài vào hoặc lấy mùa giải đang chọn)
            val currentSeasonId = 1 // Thay bằng seasonId thực tế trong app của bạn

            // Gọi hàm fetch tích hợp thẻ phạt vào danh sách playerList
            viewModel.fetchTeamDataAndCardStatus(teamId, currentSeasonId)
        }
    }
    val formationCache = remember { mutableStateMapOf<Int, List<Int>>() }

    // Tự động tải danh sách đá chính độc lập cho từng loại sân (Sân 5, 7, 11)
    LaunchedEffect(currentFieldType, playerList.size) {
        if (teamId != -1 && playerList.isNotEmpty()) {
            try {
                val starterIds = formationCache[currentFieldType] ?: run {
                    val response = RetrofitClient.getClient(context).getStarters(teamId, currentFieldType)
                    val ids = if (response.isSuccessful && response.body() != null) {
                        response.body()!!.starter_player_ids
                    } else emptyList()
                    formationCache[currentFieldType] = ids
                    ids
                }

                val updatedList = playerList.map { p ->
                    p.copy(isStarter = starterIds.contains(p.id))
                }
                playerList.clear()
                playerList.addAll(updatedList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                "QUẢN LÝ ĐỘI BÓNG",
                color = NeonGreen,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(horizontal = 60.dp)
                    .background(Color.White)
            )
        }

        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color(0xFF161616),
            contentColor = NeonGreen
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        "TỔNG QUAN & CẦU THỦ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (selectedSubTab == 0) NeonGreen else Color.Gray
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        "ĐỘI HÌNH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (selectedSubTab == 1) NeonGreen else Color.Gray
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = {
                    Text(
                        "BÀN THẮNG & ASSIST",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (selectedSubTab == 2) NeonGreen else Color.Gray
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedSubTab) {
            1 -> {
                // Tab 1: ĐỘI HÌNH
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    FormationBoard(
                        currentFieldType = currentFieldType,
                        players = playerList,
                        isEditable = true,
                        canChangeFieldType = true,
                        onFieldTypeChanged = { newType ->
                            currentFieldType = newType
                            scope.launch {
                                try {
                                    val request = UpdateFieldTypeRequest(
                                        teamId = teamId,
                                        fieldType = newType
                                    )
                                    RetrofitClient.getClient(context).updateFieldType(request)
                                    Toast.makeText(context, "Đã chuyển sang sân $newType người!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onPlayerSwap = { oldPlayer, newPlayer ->
                            scope.launch {
                                try {
                                    if (oldPlayer != null) {
                                        val oldIdx = playerList.indexOfFirst { it.id == oldPlayer.id }
                                        if (oldIdx != -1) {
                                            playerList[oldIdx] = playerList[oldIdx].copy(isStarter = false)
                                        }
                                    }
                                    val newIdx = playerList.indexOfFirst { it.id == newPlayer.id }
                                    if (newIdx != -1) {
                                        playerList[newIdx] = playerList[newIdx].copy(isStarter = true)
                                    }

                                    val starterIds = playerList.filter { it.isStarter }.map { it.id }
                                    formationCache[currentFieldType] = starterIds

                                    val request = UpdateStartersRequest(
                                        team_id = teamId,
                                        field_type = currentFieldType,
                                        starter_player_ids = starterIds
                                    )
                                    val response = RetrofitClient.getClient(context).updateStarters(request)
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Đã cập nhật đội hình thành công!", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(context, "Lỗi kết nối khi lưu đội hình!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
            2 -> {
                // Tab 2: BÀN THẮNG & ASSIST
                TeamScorersAssistersContent(teamId = teamId)
            }
            else -> {
                // Tab 0: TỔNG QUAN & CẦU THỦ
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Surface(
                            onClick = { onNavigateToRegister() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF161616),
                            border = BorderStroke(1.dp, NeonGreen)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .background(NeonGreen.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = null,
                                            tint = NeonGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "ĐĂNG KÝ GIẢI ĐẤU",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Xem lịch và tham gia mùa giải mới",
                                            color = Color.Gray,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = NeonGreen
                                )
                            }
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
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                            border = BorderStroke(1.dp, NeonGreen)
                        ) {
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
                                        modifier = Modifier
                                            .width(95.dp)
                                            .height(56.dp),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            focusedBorderColor = NeonGreen,
                                            unfocusedBorderColor = Color.DarkGray,
                                            focusedLabelColor = NeonGreen
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    val positions = listOf("GK", "DF", "MF", "FW")
                                    val selectedIndex = positions.indexOf(pos)

                                    BoxWithConstraints(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                            .background(Color(0xFF222222), RoundedCornerShape(8.dp))
                                            .padding(4.dp)
                                    ) {
                                        val itemWidth = maxWidth / 4
                                        val indicatorOffset by animateDpAsState(
                                            targetValue = itemWidth * selectedIndex,
                                            animationSpec = tween(durationMillis = 250),
                                            label = "PositionSlider"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .width(itemWidth)
                                                .fillMaxHeight()
                                                .offset(x = indicatorOffset)
                                                .background(NeonGreen, RoundedCornerShape(6.dp))
                                        )

                                        Row(modifier = Modifier.fillMaxSize()) {
                                            positions.forEachIndexed { index, p ->
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxHeight()
                                                        .clickable { pos = p },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = p,
                                                        color = if (selectedIndex == index) Color.Black else Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp
                                                    )
                                                }
                                            }
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
                                                Toast.makeText(context, "Số áo $nbr đã có người dùng!", Toast.LENGTH_SHORT).show()
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
                                                        Toast.makeText(context, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isUpdating
                                ) {
                                    Text(
                                        if (isEditing) "CẬP NHẬT THÔNG TIN" else "THÊM VÀO ĐỘI",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    items(playerList) { p ->
                        val i = playerList.indexOf(p)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                            border = BorderStroke(1.dp, if (editIdx == i) NeonGreen else Color.DarkGray)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(NeonGreen, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(p.number, color = Color.Black, fontWeight = FontWeight.Bold)
                                }

                                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                    Text(p.name, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text(text = p.position, color = getPositionColor(p.position), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                IconButton(onClick = { isEditing = true; editIdx = i; nbr = p.number; email = p.name; pos = mapFullToShort(p.position) }) {
                                    Icon(Icons.Default.Edit, "Edit", tint = if (editIdx == i) NeonGreen else Color.LightGray)
                                }
                                if (p.userId != currentUserId) {
                                    IconButton(onClick = {
                                        scope.launch {
                                            try {
                                                val requestBody = RemovePlayerRequest(team_id = teamId, player_id = p.id, currentUserId = currentUserId)
                                                val response = RetrofitClient.getClient(context).removePlayer(requestBody)

                                                if (response.isSuccessful) {
                                                    playerList.remove(p)
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerReadOnlyScreen(
    teamName: String,
    leaderName: String,
    coachName: String,
    playerList: List<PlayerInfo>,
    fieldType: Int = 7 // Loại sân mặc định của đội
) {
    // Biến lưu loại sân đang được xem trên sơ đồ (có thể bấm đổi qua lại 5, 7, 11)
    var viewFieldType by remember(fieldType) { mutableIntStateOf(fieldType) }

    // Biến điều khiển tab: 0 -> Thông tin & Cầu thủ, 1 -> Sơ đồ đội hình
    var selectedSubTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Tiêu đề đầu trang
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ĐỘI BÓNG CỦA TÔI",
                color = NeonGreen,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(horizontal = 60.dp)
                    .background(Color.White)
            )
        }

        // TabRow chuyển đổi giữa Thông tin và Sơ đồ đội hình
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color(0xFF161616),
            contentColor = NeonGreen
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        "THÔNG TIN & CẦU THỦ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (selectedSubTab == 0) NeonGreen else Color.Gray
                    )
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        "ĐỘI HÌNH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (selectedSubTab == 1) NeonGreen else Color.Gray
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nội dung hiển thị tương ứng theo Tab
        if (selectedSubTab == 1) {
            // TAB 1: SƠ ĐỒ CHIẾN THUẬT (Có thể cuộn và đổi loại sân 5, 7, 11)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                FormationBoard(
                    currentFieldType = viewFieldType,
                    players = playerList,
                    isEditable = false,           // Chế độ chỉ xem, không kéo thả đổi vị trí
                    canChangeFieldType = true,    // Cho phép bấm đổi qua lại các loại sân (5, 7, 11)
                    onFieldTypeChanged = { newType ->
                        viewFieldType = newType   // Cập nhật lại loại sân khi người dùng bấm chọn[cite: 8]
                    }
                )
            }
        } else {
            // TAB 0: THÔNG TIN TỔNG QUAN & DANH SÁCH CẦU THỦ
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(NeonGreen.copy(alpha = 0.1f), CircleShape)
                                    .border(BorderStroke(2.dp, NeonGreen), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = teamName.take(2).uppercase(),
                                    color = NeonGreen,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = teamName.uppercase(),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "MÙA GIẢI 2026",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                            border = BorderStroke(1.dp, Color(0xFF333333))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Person, contentDescription = "", tint = Color(0xFFFFD740), modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("HUẤN LUYỆN VIÊN", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = coachName.ifEmpty { "Chưa cập nhật" },
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                            border = BorderStroke(1.dp, Color(0xFF333333))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Star, contentDescription = "", tint = Color(0xFFFF5252), modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("ĐỘI TRƯỞNG (C)", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = leaderName.ifEmpty { "Chưa cập nhật" },
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "DANH SÁCH CẦU THỦ (${playerList.size})",
                        color = NeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(playerList) { player ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                        border = BorderStroke(1.dp, Color.DarkGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonGreen, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(player.number, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .weight(1f)
                            ) {
                                Text(player.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Vị trí: ${player.position}", color = Color.Gray, fontSize = 12.sp)
                            }
                            Text(
                                text = when (player.position.lowercase()) {
                                    "defender" -> "DF"
                                    "midfielder" -> "MF"
                                    "forward" -> "FW"
                                    else -> "GK"
                                },
                                color = when (player.position.lowercase()) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "ĐĂNG KÝ ĐỘI BÓNG",
            color = NeonGreen,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(2.dp)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = teamName,
            onValueChange = { teamName = it },
            label = { Text("Nhập tên đội bóng") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = Color.DarkGray,
                focusedLabelColor = NeonGreen,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = NeonGreen
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = currentUserName,
            onValueChange = {},
            label = { Text("Đội trưởng") },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xFF222222),
                disabledLabelColor = Color.Gray,
                disabledTextColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
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
                            val message = try {
                                org.json.JSONObject(errorBody ?: "").getString("message")
                            } catch (e: Exception) {
                                "Lỗi: ${response.code()}"
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "HOÀN TẤT ĐĂNG KÝ",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonRegistrationScreen(
    teamId: Int,
    onNavigateToPayment: (SeasonInfo) -> Unit,
    onBack: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val seasons = remember { mutableStateListOf<SeasonInfo>() }
    var isLoading by remember { mutableStateOf(true) }
    var showRulesDialog by remember { mutableStateOf(false) }
    val rules by viewModel.tournamentRules.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedSeason by remember { mutableStateOf<SeasonInfo?>(null) }

    val fetchSeasons = {
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
                title = {
                    Text(
                        "ĐĂNG KÝ GIẢI ĐẤU",
                        color = NeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = NeonGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
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
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = season.name,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )

                            if (!season.description.isNullOrEmpty()) {
                                Text(
                                    text = season.description,
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
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
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("👥 Tối đa: ${season.max_teams} đội", color = Color.LightGray)
                                Text(
                                    "💰 ${season.registrationFee} VNĐ",
                                    color = Color.Yellow,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                "📅 ${DateUtils.formatDate(season.start_date)} - ${
                                    DateUtils.formatDate(season.end_date)
                                }",
                                color = Color.Gray, modifier = Modifier.padding(top = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (season.status == "finished") {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (season.is_registered == 1) "🏆 ĐÃ THAM GIA" else "GIẢI ĐÃ KẾT THÚC",
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                } else if (season.is_registered == 1) {
                                    if (season.status == "registration_open") {
                                        Button(
                                            onClick = { selectedSeason = season; showDialog = true },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                            modifier = Modifier.weight(1f).height(48.dp)
                                        ) {
                                            Text("HỦY GIẢI", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.weight(1f).height(48.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Đang thi đấu", color = Color.Gray, fontSize = 12.sp)
                                        }
                                    }

                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when (season.payment_status?.trim()?.lowercase()) {
                                            "confirmed" -> {
                                                Text("✅ Đã thanh toán", color = Color.Green, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            }
                                            "pending_approval" -> {
                                                Box(
                                                    modifier = Modifier.fillMaxWidth().height(48.dp).background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("⏳ Chờ xác nhận", color = Color.Yellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                }
                                            }
                                            "pending" -> {
                                                Button(
                                                    onClick = { onNavigateToPayment(season) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                                    shape = RoundedCornerShape(12.dp)
                                                ) {
                                                    Text("THANH TOÁN", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                }
                                            }
                                            else -> {
                                                Text("Chưa thanh toán", color = Color.Gray, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                try {
                                                    val response = RetrofitClient.getClient(context)
                                                        .registerToSeason(
                                                            mapOf(
                                                                "team_id" to teamId,
                                                                "season_id" to season.id
                                                            )
                                                        )
                                                    if (response.isSuccessful) {
                                                        Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                                                        fetchSeasons()
                                                    } else {
                                                        Toast.makeText(context, "Có lỗi xảy ra!", Toast.LENGTH_LONG).show()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f).height(48.dp),
                                    ) {
                                        Text("ĐĂNG KÝ NGAY", color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog && selectedSeason != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Xác nhận hủy", color = Color.White) },
                    text = { Text("Bạn có chắc chắn muốn hủy tham gia giải '${selectedSeason?.name}' không?", color = Color.LightGray) },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                val request = UnregisterRequest(team_id = teamId.toString(), season_id = selectedSeason!!.id.toString())
                                val response = RetrofitClient.getClient(context).unregisterFromSeason(request)
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Đã hủy giải thành công!", Toast.LENGTH_SHORT).show()
                                    fetchSeasons()
                                } else {
                                    Toast.makeText(context, "Không thể hủy do đã phân bảng!", Toast.LENGTH_LONG).show()
                                }
                            }
                            showDialog = false
                        }) {
                            Text("Đồng ý", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Hủy", color = Color.White)
                        }
                    },
                    containerColor = Color(0xFF161616)
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(status: String, registrationDeadline: String?, onStatusClick: () -> Unit) {
    val isExpired = registrationDeadline != null && isDeadlinePassed(registrationDeadline) && status == "registration_open"
    val (color, text) = when {
        isExpired -> Color.Red to "Đã hết hạn"
        status == "upcoming" -> Color.Blue to "Sắp tới"
        status == "registration_open" -> Color.Green to "Đang mở đăng ký"
        status == "ongoing" -> Color.Yellow to "Đang diễn ra"
        status == "finished" -> Color.Gray to "Đã kết thúc"
        else -> Color.Gray to status
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


// ==========================================
// 1. PREVIEW MÀN HÌNH QUẢN LÝ CỦA HLV / ĐỘI TRƯỞNG
// ==========================================
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun CoachCaptainScreenPreview() {
    val samplePlayerList = remember {
        mutableStateListOf(
            PlayerInfo(id = 1, name = "Nguyễn Văn A", number = "10", position = "Forward", userId = 100, isStarter = true),
            PlayerInfo(id = 2, name = "Trần Văn B", number = "1", position = "Goalkeeper", userId = 101, isStarter = true),
            PlayerInfo(id = 3, name = "Lê Văn C", number = "4", position = "Defender", userId = 102, isStarter = true),
            PlayerInfo(id = 4, name = "Phạm Văn D", number = "8", position = "Midfielder", userId = 103, isStarter = true),
            PlayerInfo(id = 5, name = "Hoàng Văn E", number = "6", position = "Midfielder", userId = 104, isStarter = true),
            PlayerInfo(id = 6, name = "Vũ Văn F", number = "2", position = "Defender", userId = 105, isStarter = true),
            PlayerInfo(id = 7, name = "Đặng Văn G", number = "5", position = "Defender", userId = 106, isStarter = true),
            PlayerInfo(id = 8, name = "Bùi Tiến H", number = "12", position = "Goalkeeper", userId = 107, isStarter = false),
            PlayerInfo(id = 9, name = "Đỗ Hùng I", number = "16", position = "Midfielder", userId = 108, isStarter = false),
            PlayerInfo(id = 10, name = "Nguyễn Quang K", number = "19", position = "Forward", userId = 109, isStarter = false),
            PlayerInfo(id = 11, name = "Phan Văn L", number = "20", position = "Defender", userId = 110, isStarter = false)
        )
    }

    CoachCaptainScreen(
        teamId = 3,
        fieldType = 7,
        playerList = samplePlayerList,
        teamName = "FC Phoenix",
        leaderName = "Nguyễn Văn A",
        coachName = "Coach Park",
        onNavigateToRegister = {}
    )
}

// ==========================================
// 2. PREVIEW TAB BÀN THẮNG & KIẾN TẠO KÈM SẮP XẾP
// ==========================================
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TeamScorersAssistersPreview() {
    // Dữ liệu mẫu giả lập số liệu bàn thắng, assist và trạng thái hoạt động của cầu thủ
    val sampleScorerList = remember {
        mutableStateListOf(
            ScorerAssisterInfo(
                playerId = 1,
                playerName = "Nguyễn Văn A",
                jerseyNumber = "10",
                position = "forward",
                isStillInTeam = 1, // Vẫn ở đội -> Hiện chấm Xanh neon
                totalGoals = 12,
                totalAssists = 5
            ),
            ScorerAssisterInfo(
                playerId = 2,
                playerName = "Trần Văn B",
                jerseyNumber = "7",
                position = "midfielder",
                isStillInTeam = 1, // Vẫn ở đội -> Hiện chấm Xanh neon
                totalGoals = 8,
                totalAssists = 10
            ),
            ScorerAssisterInfo(
                playerId = 3,
                playerName = "Lê Văn C",
                jerseyNumber = "15",
                position = "defender",
                isStillInTeam = 0, // Đã rời đội -> Hiện chấm Đỏ
                totalGoals = 3,
                totalAssists = 1
            )
        )
    }

    // Gọi thẳng component truyền danh sách mẫu vào để render trực tiếp không cần gọi mạng
    TeamScorersAssistersContent(
        teamId = 3,
        previewList = sampleScorerList
    )
}