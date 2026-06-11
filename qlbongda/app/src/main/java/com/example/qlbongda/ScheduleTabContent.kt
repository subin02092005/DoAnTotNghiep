package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.FullMatchDetail

@Composable
fun ScheduleTabContent(
    matchList: List<FullMatchDetail>,
    onMatchClick: (FullMatchDetail) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "LỊCH THI ĐẤU", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(matchList) { match ->
                val isFeatured = match.isHot

                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onMatchClick(match) },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = if (isFeatured) 2.dp else 1.dp,
                        color = if (isFeatured) Color.Red else NeonGreen
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = match.teamA,
                                color = if (isFeatured) Color(0xFFFFD700) else Color.White,
                                fontSize = 15.sp,
                                fontWeight = if (isFeatured) FontWeight.Black else FontWeight.Bold
                            )

                            Text(
                                text = "VS",
                                color = NeonGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )

                            Text(
                                text = match.teamB,
                                color = if (isFeatured) Color(0xFFFFD700) else Color.White,
                                fontSize = 15.sp,
                                fontWeight = if (isFeatured) FontWeight.Black else FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFF222222))
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = match.time,
                                color = if (isFeatured) Color.Red else NeonGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )

                            Text(text = match.date, color = Color.LightGray, fontSize = 11.sp)
                        }

                        if (isFeatured) {
                            Text(
                                text = "⭐",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.TopEnd).padding(top = 10.dp, end = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}