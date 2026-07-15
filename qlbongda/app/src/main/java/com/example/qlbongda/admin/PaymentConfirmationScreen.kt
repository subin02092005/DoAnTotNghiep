package com.example.qlbongda.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qlbongda.viewmodel.AdminViewModel
import com.example.qlbongda.data.model.*
import com.example.qlbongda.ui.theme.NeonGreen

@Composable
fun PaymentConfirmationScreen(viewModel: AdminViewModel) {
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchPayments()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ManagementHeader("XÁC NHẬN THANH TOÁN", "Danh sách các giao dịch chờ phê duyệt")

        if (isLoading && payments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else if (payments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không có giao dịch nào", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(payments) { payment ->
                    PaymentItemCard(
                        payment = payment,
                        onApprove = { viewModel.confirmPayment(payment.id) },
                        onReject = { viewModel.rejectPayment(payment.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentItemCard(payment: PaymentItem, onApprove: () -> Unit, onReject: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(1.dp, if (payment.status == "pending") Color.Yellow else Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = payment.teamName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mùa giải: ${payment.seasonName}",
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Số tiền: ${String.format("%,.0f", payment.amount)}đ",
                        color = NeonGreen,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (payment.status == "pending") {
                    Row {
                        IconButton(onClick = onApprove) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Approve", tint = NeonGreen)
                        }
                        IconButton(onClick = onReject) {
                            Icon(Icons.Default.Cancel, contentDescription = "Reject", tint = Color.Red)
                        }
                    }
                } else {
                    Text(
                        text = payment.status.uppercase(),
                        color = if (payment.status == "confirmed") NeonGreen else Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Mã GD: ${payment.transactionRef ?: "N/A"}",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Text(
                    text = payment.createdAt.split("T").getOrNull(0) ?: "",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}
