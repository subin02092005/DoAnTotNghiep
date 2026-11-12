package com.example.qlbongda.admin

import android.app.DatePickerDialog
import android.widget.Toast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.PreviewMatch
import com.example.qlbongda.ui.theme.NeonGreen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SchedulePreviewDialog(
    initialMatches: List<PreviewMatch>,
    onDismiss: () -> Unit,
    onConfirm: (List<PreviewMatch>) -> Unit
) {
    val context = LocalContext.current
    var matchesList = remember { mutableStateListOf(*initialMatches.toTypedArray()) }
    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val todayStr = dateFormatter.format(Date())

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Xem trước & Chỉnh sửa lịch đấu", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                Text(
                    "Kiểm tra kỹ thời gian và cặp đấu. Bạn có thể bấm vào ngày để thay đổi:",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(matchesList) { index, match ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Vòng ${match.round_number}", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${match.home_team_name} vs ${match.away_team_name}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Nút chọn ngày có validate quá khứ
                                    OutlinedButton(
                                        onClick = {
                                            val calendar = Calendar.getInstance()
                                            try { dateFormatter.parse(match.match_date)?.let { calendar.time = it } } catch (_: Exception) {}

                                            DatePickerDialog(
                                                context,
                                                { _, year, month, dayOfMonth ->
                                                    val selectedCal = Calendar.getInstance().apply {
                                                        set(year, month, dayOfMonth, 0, 0, 0)
                                                        set(Calendar.MILLISECOND, 0)
                                                    }
                                                    val todayCal = Calendar.getInstance().apply {
                                                        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                                                        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                                                    }

                                                    // 🌟 VALIDATE NGÀY TRONG QUÁ KHỨ
                                                    if (selectedCal.before(todayCal)) {
                                                        Toast.makeText(context, "⚠ Lỗi: Ngày này trong quá khứ, không thể chọn!", Toast.LENGTH_LONG).show()
                                                    } else {
                                                        matchesList[index] = match.copy(match_date = dateFormatter.format(selectedCal.time))
                                                    }
                                                },
                                                calendar.get(Calendar.YEAR),
                                                calendar.get(Calendar.MONTH),
                                                calendar.get(Calendar.DAY_OF_MONTH)
                                            ).show()
                                        },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(14.dp), tint = NeonGreen)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(match.match_date, color = Color.White, fontSize = 12.sp)
                                    }

                                    Text("Giờ: ${match.match_time}", color = Color.LightGray, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(matchesList.toList()) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Xác nhận tạo trận", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = Color.Gray) }
        }
    )
}