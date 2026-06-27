package com.example.qlbongda

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

@Composable
fun HomeTabContent(
    phaseList: List<TournamentPhase>,
    standings: List<GroupStanding>,
    selectedTabIndex: Int,          // Nhận từ HomeScreen
    onTabSelected: (Int) -> Unit,   // Nhận từ HomeScreen
    onNavigateToStandingDetail: () -> Unit,
    onTeamClick: (String) -> Unit
) {
    // State chọn vòng đấu
    var selectedPhase by remember(phaseList) { mutableStateOf(phaseList.firstOrNull()) }

    // State chọn bảng đấu (index của standings)




    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // 1. THANH CHỌN VÒNG ĐẤU
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(phaseList) { phase ->
                val isSelected = selectedPhase?.id == phase.id
                Button(
                    onClick = { selectedPhase = phase },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonGreen else Color(0xFF2C2C2C)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = phase.name, color = if (isSelected) Color.Black else Color.White)
                }
            }
        }

        // 2. THANH CHỌN BẢNG
        if (standings.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(standings) { index, group ->
                    val isSelected = selectedTabIndex == index
                    FilterChip(
                        selected = isSelected,
                        onClick = { onTabSelected(index) },
                        label = { Text(group.groupName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonGreen,
                            containerColor = Color(0xFF2C2C2C),
                            labelColor = Color.White,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }

            // 3. DANH SÁCH BẢNG XẾP HẠNG
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                val currentGroup = standings.getOrNull(selectedTabIndex)

                if (currentGroup != null) {
                    // Header bảng
                    item {
                        Row(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                            Text("#", color = Color.Gray, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                            Text("Đội bóng", color = Color.Gray, modifier = Modifier.weight(1f))
                            Text("T", color = Color.Gray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                            Text("HS", color = Color.Gray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                            Text("Đ", color = Color.Gray, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                        }
                        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    }

                    // Danh sách đội bóng
                    items(currentGroup.standings) { row ->
                        val rankColor = when (row.rank) {
                            1, 2 -> NeonGreen
                            3 -> Color.White
                            else -> Color.Gray
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onNavigateToStandingDetail() },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(row.rank.toString(), color = rankColor, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                                Text(row.teamName, color = Color.White, modifier = Modifier.weight(1f).clickable { onTeamClick(row.teamName) })
                                Text(row.played.toString(), color = Color.White, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                                Text(row.goalDifference, color = Color.LightGray, modifier = Modifier.width(35.dp), textAlign = TextAlign.Center)
                                Text(row.points.toString(), color = NeonGreen, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        } else {
            // Trường hợp chưa có dữ liệu
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có dữ liệu bảng xếp hạng", color = Color.Gray)
            }
        }
    }
}