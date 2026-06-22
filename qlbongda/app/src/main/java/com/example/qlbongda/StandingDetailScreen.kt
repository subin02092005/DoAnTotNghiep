package com.example.qlbongda

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingDetailScreen(
    standings: List<GroupStanding>,
    onBack: () -> Unit,
    onTeamClick: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val sharedScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.Black)) {
                TopAppBar(
                    title = { Text("BẢNG XẾP HẠNG CHI TIẾT", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
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
        },
        containerColor = Color.Black
    ) { innerPadding ->
        val currentGroup = standings.getOrNull(selectedTabIndex)

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (currentGroup != null) {
                // 1. HEADER
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("STT", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                            Text("ĐỘI BÓNG", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.width(100.dp))

                            Row(modifier = Modifier.horizontalScroll(sharedScrollState)) {
                                HeaderCell("TRẬN"); HeaderCell("THẮNG"); HeaderCell("HÒA"); HeaderCell("THUA")
                                HeaderCell("BT"); HeaderCell("BB"); HeaderCell("HS")
                                HeaderCell("ĐIỂM", true); HeaderCell("5 TRẬN", true, 130.dp)
                            }
                        }
                    }
                }

                // 2. DỮ LIỆU
                items(currentGroup.standings) { row ->
                    // Logic màu sắc cho Top 3
                    val rankColor = when (row.rank) {
                        1, 2 -> NeonGreen
                        3 -> Color.White
                        else -> Color.Gray
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(row.rank.toString(), color = rankColor, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                            Text(
                                text = row.teamName,
                                color = if (row.rank <= 3) rankColor else Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(100.dp).clickable { onTeamClick(row.teamName) },
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )

                            Row(modifier = Modifier.horizontalScroll(sharedScrollState)) {
                                DataCell(row.played.toString()); DataCell(row.won.toString()); DataCell(row.drawn.toString()); DataCell(row.lost.toString())
                                DataCell(row.goalsFor.toString()); DataCell(row.goalsAgainst.toString()); DataCell(row.goalDifference)
                                DataCell(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold)

                                // Render phong độ 5 trận
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
            }
        }
    }
}

@Composable
fun HeaderCell(text: String, isHighlight: Boolean = false, width: Dp = 45.dp) {
    Text(text, color = if (isHighlight) NeonGreen else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(width), textAlign = TextAlign.Center)
}

@Composable
fun DataCell(text: String, width: Dp = 45.dp, color: Color = Color.White, fontWeight: FontWeight = FontWeight.Normal) {
    Text(text, color = color, fontSize = 13.sp, fontWeight = fontWeight, modifier = Modifier.width(width), textAlign = TextAlign.Center)
}