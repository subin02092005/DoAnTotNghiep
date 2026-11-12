package com.example.qlbongda.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

val DarkBackground = Color(0xFF0A0A0A)
val CardBackground = Color(0xFF121212)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    team: StandingItem,
    onBackClick: () -> Unit
) {
    var selectedPlayer by remember { mutableStateOf<PlayerInfo?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("CHI TIẾT ĐỘI BÓNG", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        TeamDetailContentLoaded(
            team = team,
            innerPadding = innerPadding,
            onPlayerClick = { player -> selectedPlayer = player }
        )

        if (selectedPlayer != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedPlayer = null },
                sheetState = sheetState,
                containerColor = Color(0xFF121212),
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(NeonGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedPlayer!!.number,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = selectedPlayer!!.name,
                        color = NeonGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vị trí: ${selectedPlayer!!.position}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { selectedPlayer = null },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("ĐÓNG", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailContainer(
    teamId: Int,
    viewModel: com.example.qlbongda.viewmodel.HomeViewModel,
    onBackClick: () -> Unit
) {
    val teamDetail by viewModel.teamDetail.collectAsState(initial = null)
    var selectedPlayer by remember { mutableStateOf<PlayerInfo?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(teamId) {
        viewModel.fetchTeamDetail(teamId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("CHI TIẾT ĐỘI BÓNG", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        val data = teamDetail
        if (data != null) {
            val mappedTeam = StandingItem(
                id = data.id,
                teamName = data.teamName ?: "Không có tên",
                coachName = data.coachName ?: "Chưa cập nhật",
                captainName = data.captainName ?: "Chưa cập nhật",
                fieldType = data.fieldType,
                players = data.players
            )
            TeamDetailContentLoaded(
                team = mappedTeam,
                innerPadding = innerPadding,
                onPlayerClick = { player -> selectedPlayer = player }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NeonGreen)
            }
        }
    }
}

@Composable
fun TeamDetailContentLoaded(
    team: StandingItem,
    innerPadding: PaddingValues,
    onPlayerClick: (PlayerInfo) -> Unit
) {
    var viewFieldType by remember(team.fieldType) { mutableIntStateOf(team.fieldType) }
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var currentPlayers by remember(team.players) { mutableStateOf(team.players) }
    val context = LocalContext.current

    LaunchedEffect(viewFieldType, team.id, team.players) {
        if (team.id != -1) {
            try {
                val response = RetrofitClient.getClient(context).getStarters(team.id, viewFieldType)
                if (response.isSuccessful && response.body() != null) {
                    val starterIds = response.body()!!.starter_player_ids
                    currentPlayers = team.players.map { p ->
                        p.copy(isStarter = starterIds.contains(p.id))
                    }
                } else {
                    currentPlayers = team.players
                }
            } catch (e: Exception) {
                e.printStackTrace()
                currentPlayers = team.players
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(NeonGreen.copy(alpha = 0.1f), CircleShape)
                    .border(BorderStroke(2.dp, NeonGreen), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = team.teamName.take(2).uppercase(),
                    color = NeonGreen,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = team.teamName.uppercase(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = CardBackground,
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

        // 🌟 NẾU LÀ TAB SƠ ĐỒ CHIẾN THUẬT, BỌC TRONG verticalScroll ĐỂ CÓ THỂ KÉO LÊN KÉO XUỐNG THOẢI MÁI
        // Trong file TeamDetailScreen.kt, đoạn hiển thị tab Đội hình (Tab 1):
        if (selectedSubTab == 1) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                FormationBoard(
                    currentFieldType = viewFieldType,
                    players = currentPlayers,
                    isEditable = false, // Không phải đội trưởng nên không được sửa/kéo thả đổi người
                    canChangeFieldType = true, // ✅ Cho phép thành viên tự do bấm xem các loại sân (5, 7, 11)
                    onFieldTypeChanged = { newType ->
                        viewFieldType = newType // Cập nhật loại sân đang xem khi họ bấm chuyển đổi
                    }
                )
            }

        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                            border = BorderStroke(1.dp, Color(0xFF333333))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "HLV",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("HUẤN LUYỆN VIÊN", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = team.coachName.ifEmpty { "Chưa cập nhật" },
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                            border = BorderStroke(1.dp, Color(0xFF333333))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Đội trưởng",
                                    tint = NeonGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("ĐỘI TRƯỞNG (C)", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = team.captainName.ifEmpty { "Chưa cập nhật" },
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "DANH SÁCH CẦU THỦ (${team.players.size})",
                        color = NeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(team.players) { player ->
                    val isCaptain = player.name == team.captainName
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardBackground, RoundedCornerShape(8.dp))
                            .border(
                                border = if (isCaptain) BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)) else BorderStroke(1.dp, Color(0xFF1F1F1F)),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onPlayerClick(player) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(NeonGreen, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = player.number, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = player.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                if (isCaptain) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "C",
                                        color = Color.Black,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .background(NeonGreen, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(text = "Vị trí: ${player.position}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun TeamDetailScreenPreview() {
    val sampleTeam = StandingItem(
        id = 1,
        teamName = "FC Phoenix",
        coachName = "Park Hang Seo",
        captainName = "Nguyễn Văn A",
        fieldType = 7,
        players = listOf(
            PlayerInfo(id = 1, number = "1", name = "Trần Văn B", position = "Goalkeeper", isStarter = true),
            PlayerInfo(id = 2, number = "4", name = "Lê Văn C", position = "Defender", isStarter = true),
            PlayerInfo(id = 3, number = "2", name = "Vũ Văn F", position = "Defender", isStarter = true),
            PlayerInfo(id = 4, number = "5", name = "Đặng Văn G", position = "Defender", isStarter = true),
            PlayerInfo(id = 5, number = "8", name = "Phạm Văn D", position = "Midfielder", isStarter = true),
            PlayerInfo(id = 6, number = "6", name = "Hoàng Văn E", position = "Midfielder", isStarter = true),
            PlayerInfo(id = 7, number = "10", name = "Nguyễn Văn A", position = "Forward", isStarter = true),
            PlayerInfo(id = 8, number = "12", name = "Bùi Tiến H", position = "Goalkeeper", isStarter = false),
            PlayerInfo(id = 9, number = "16", name = "Đỗ Hùng I", position = "Midfielder", isStarter = false)
        )
    )

    TeamDetailScreen(
        team = sampleTeam,
        onBackClick = {}
    )
}