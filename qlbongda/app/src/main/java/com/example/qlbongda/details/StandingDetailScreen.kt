package com.example.qlbongda.details

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingDetailScreen(
    standings: List<GroupStanding>,
    initialTabIndex: Int,
    onBack: () -> Unit,
    onTeamClick: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }
    val sharedScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.Black)) {
                CenterAlignedTopAppBar(
                    title = { Text("BẢNG XẾP HẠNG CHI TIẾT", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
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
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("#", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                            Text("ĐỘI BÓNG", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.width(100.dp))

                            Row(modifier = Modifier.horizontalScroll(sharedScrollState)) {
                                HeaderCell("TRẬN")
                                HeaderCell("THẮNG")
                                HeaderCell("HÒA")
                                HeaderCell("THUA")
                                HeaderCell("BT")
                                HeaderCell("BB")
                                HeaderCell("HS")
                                HeaderCell("ĐIỂM", true)
                                HeaderCell("5 TRẬN", true, 130.dp)
                            }
                        }
                    }
                }

                items(currentGroup.standings) { row ->
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
                                DataCell(row.played.toString())
                                DataCell(row.won.toString())
                                DataCell(row.drawn.toString())
                                DataCell(row.lost.toString())
                                DataCell(row.goalsFor.toString())
                                DataCell(row.goalsAgainst.toString())
                                DataCell(row.goalDifference)
                                DataCell(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold)

                                Row(
                                    modifier = Modifier.width(130.dp).padding(start = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    (row.form ?: emptyList()).take(5).forEach { outcome ->
                                        val (bg, txt) = when (outcome) {
                                            "W" -> Color(0xFF00C853) to Color.Black
                                            "D" -> Color.DarkGray to Color.White
                                            "L" -> Color.Red to Color.White
                                            else -> Color.Transparent to Color.Transparent
                                        }

                                        Box(
                                            modifier = Modifier.size(18.dp).background(bg, RoundedCornerShape(4.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = outcome,
                                                color = txt,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
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
