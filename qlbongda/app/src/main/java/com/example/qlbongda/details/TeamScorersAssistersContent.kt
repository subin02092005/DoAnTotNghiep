package com.example.qlbongda.details

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.ScorerAssisterInfo
import com.example.qlbongda.ui.theme.NeonGreen

enum class SortField {
    GOALS, ASSISTS, STATUS
}

@Composable
fun TeamScorersAssistersContent(
    teamId: Int,
    previewList: List<ScorerAssisterInfo>? = null
) {
    val context = LocalContext.current
    val originalList = remember { mutableStateListOf<ScorerAssisterInfo>() }
    var isLoading by remember { mutableStateOf(previewList == null) }

    // Trạng thái sắp xếp: Mặc định sắp xếp theo Bàn thắng từ cao xuống thấp
    var sortField by remember { mutableStateOf(SortField.GOALS) }
    var isDescending by remember { mutableStateOf(true) } // true: Mũi tên xuống (Nhiều -> Ít), false: Mũi tên lên (Ít -> Nhiều)

    // Tải dữ liệu từ API hoặc Preview
    LaunchedEffect(teamId) {
        if (previewList != null) {
            originalList.clear()
            originalList.addAll(previewList)
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        try {
            val response = RetrofitClient.getClient(context).getTeamScorersAndAssisters(teamId)
            if (response.isSuccessful && response.body() != null) {
                val resBody = response.body()!!
                if (resBody.status == "success") {
                    originalList.clear()
                    originalList.addAll(resBody.data)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Lỗi kết nối tải thống kê!", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    // Danh sách sau khi sắp xếp
    val sortedList = remember(originalList, sortField, isDescending) {
        when (sortField) {
            SortField.GOALS -> {
                if (isDescending) originalList.sortedByDescending { it.totalGoals }
                else originalList.sortedBy { it.totalGoals }
            }
            SortField.ASSISTS -> {
                if (isDescending) originalList.sortedByDescending { it.totalAssists }
                else originalList.sortedBy { it.totalAssists }
            }
            SortField.STATUS -> {
                // Sắp xếp theo trạng thái hoạt động (1: Trong đội, 0: Rời đi)
                if (isDescending) originalList.sortedByDescending { it.isStillInTeam }
                else originalList.sortedBy { it.isStillInTeam }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = NeonGreen)
        }
    } else if (originalList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chưa có dữ liệu bàn thắng hoặc kiến tạo.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text(
                text = "BẢNG THỐNG KÊ CẦU THỦ",
                color = Color.LightGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // ==========================================
            // THANH TIÊU ĐỀ DẠNG BẢNG (BXH STYLE) DÍNH LIỀN
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                border = BorderStroke(1.dp, Color.DarkGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cột Tên Cầu Thủ (Bấm để sắp xếp theo Trạng thái)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (sortField == SortField.STATUS) {
                                    isDescending = !isDescending
                                } else {
                                    sortField = SortField.STATUS
                                    isDescending = true
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CẦU THỦ / TRẠNG THÁI",
                            color = if (sortField == SortField.STATUS) NeonGreen else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (sortField == SortField.STATUS) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Cột Bàn Thắng (Bấm để sắp xếp)
                    Row(
                        modifier = Modifier
                            .width(75.dp)
                            .clickable {
                                if (sortField == SortField.GOALS) {
                                    isDescending = !isDescending
                                } else {
                                    sortField = SortField.GOALS
                                    isDescending = true
                                }
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚽ BÀN",
                            color = if (sortField == SortField.GOALS) NeonGreen else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (sortField == SortField.GOALS) {
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = if (isDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Cột Kiến Tạo (Bấm để sắp xếp)
                    Row(
                        modifier = Modifier
                            .width(75.dp)
                            .clickable {
                                if (sortField == SortField.ASSISTS) {
                                    isDescending = !isDescending
                                } else {
                                    sortField = SortField.ASSISTS
                                    isDescending = true
                                }
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👟 ASSIST",
                            color = if (sortField == SortField.ASSISTS) NeonGreen else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (sortField == SortField.ASSISTS) {
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = if (isDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // DANH SÁCH CẦU THỦ BÊN DƯỚI
            // ==========================================
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(sortedList) { item ->
                    val isStillActive = item.isStillInTeam == 1
                    val statusColor = if (isStillActive) NeonGreen else Color.Red
                    val statusText = if (isStillActive) "Đang trong đội" else "Đã rời đội"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(0.5.dp, Color.DarkGray)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Chấm trạng thái nhỏ
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(statusColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            // Số áo
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(Color(0xFF222222), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.jerseyNumber,
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Tên và Trạng thái chữ ("Đang trong đội" / "Đã rời đội")
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.playerName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = statusText,
                                    color = statusColor,
                                    fontSize = 10.sp
                                )
                            }

                            // Số lượng Bàn thắng
                            Box(
                                modifier = Modifier.width(75.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.totalGoals}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            // Số lượng Kiến tạo
                            Box(
                                modifier = Modifier.width(75.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.totalAssists}",
                                    color = Color.Yellow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}