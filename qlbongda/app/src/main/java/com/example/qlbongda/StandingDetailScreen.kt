package com.example.qlbongda

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.data.model.StandingItem
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.api.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingDetailScreen(
    standings: List<GroupStanding>,
    teamName: String,
    coachName: String,
    leaderName: String,
    playerList: List<PlayerInfo>,
    onBack: () -> Unit,
    onTeamClick: (Int) -> Unit
) {
    val sharedScrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State lưu tab đang chọn
    var selectedTabIndex by remember { mutableStateOf(0) }
    var activeTeamDetail by remember { mutableStateOf<StandingItem?>(null) }
    var isDetailLoading by remember { mutableStateOf(false) }

    if (activeTeamDetail != null) {
        TeamDetailScreen(team = activeTeamDetail!!) { activeTeamDetail = null }
    } else {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(Color.Black)) {
                    TopAppBar(
                        title = { Text("BẢNG XẾP HẠNG CHI TIẾT", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Black) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                    )
                    // Thanh Tab chuyển đổi
                    if (standings.isNotEmpty()) {
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = Color.Black,
                            contentColor = NeonGreen,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]), color = NeonGreen)
                            }
                        ) {
                            standings.forEachIndexed { index, group ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(group.groupName, fontWeight = FontWeight.Black) }
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().background(Color.Black).padding(innerPadding)) {
                if (isDetailLoading) {
                    CircularProgressIndicator(color = NeonGreen, modifier = Modifier.align(Alignment.Center))
                } else {
                    val currentGroup = standings.getOrNull(selectedTabIndex)

                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                        if (currentGroup != null) {
                            // Header
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("STT", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                                        Text("ĐỘI BÓNG", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
                                        Row(modifier = Modifier.weight(1f).horizontalScroll(sharedScrollState)) {
                                            HeaderCell("TRẬN"); HeaderCell("THẮNG"); HeaderCell("HÒA"); HeaderCell("THUA"); HeaderCell("BT"); HeaderCell("BB"); HeaderCell("HS"); HeaderCell("ĐIỂM", true); HeaderCell("5 TRẬN", true,130.dp)
                                        }
                                    }
                                }
                            }

                            // Danh sách đội
                            items(currentGroup.standings) { row ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                    shape = RoundedCornerShape(0.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text(row.rank.toString(), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                                        Text(row.teamName, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp).clickable {
                                            isDetailLoading = true
                                            coroutineScope.launch {
                                                try {
                                                    val response = RetrofitClient.getClient(context).getTeamDetail(row.rank)
                                                    if (response.isSuccessful) activeTeamDetail = response.body()?.data
                                                } finally { isDetailLoading = false }
                                                onTeamClick(row.rank)
                                            }
                                        }, maxLines = 1, overflow = TextOverflow.Ellipsis)

                                        Row(modifier = Modifier.weight(1f).horizontalScroll(sharedScrollState)) {
                                            DataCell(row.played.toString()); DataCell(row.won.toString()); DataCell(row.drawn.toString()); DataCell(row.lost.toString()); DataCell(row.goalsFor.toString()); DataCell(row.goalsAgainst.toString())
                                            DataCell(row.goalDifference, color = if(row.goalDifference.startsWith("+")) NeonGreen else Color.Red)
                                            DataCell(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold)

                                            // Phong độ 5 trận
                                            Row(modifier = Modifier.width(130.dp).padding(start = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                row.form.forEach { outcome ->
                                                    val (bg, txt) = when (outcome) { "W" -> Color(0xFF00C853) to Color.Black; "D" -> Color.DarkGray to Color.White; else -> Color.Red to Color.White }
                                                    Box(modifier = Modifier.size(18.dp).background(bg, RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
                                                        Text(outcome, color = txt, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Chân bảng
                            item {
                                Card(modifier = Modifier.fillMaxWidth().height(8.dp), shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderCell(text: String, isHighlight: Boolean = false, width: Dp = 55.dp) {
    Text(text, color = if (isHighlight) NeonGreen else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(width), textAlign = TextAlign.Center)
}

@Composable
fun DataCell(text: String, width: Dp = 55.dp, color: Color = Color.White, fontWeight: FontWeight = FontWeight.Normal) {
    Text(text, color = color, fontSize = 13.sp, fontWeight = fontWeight, modifier = Modifier.width(width), textAlign = TextAlign.Center)
}