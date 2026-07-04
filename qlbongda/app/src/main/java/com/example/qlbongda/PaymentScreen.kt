package com.example.qlbongda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.SeasonInfo
import com.example.qlbongda.ui.theme.NeonGreen
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(season: SeasonInfo, onConfirm: (String) -> Unit, onBack: () -> Unit) {
    val randomRef = remember { "TXN" + java.util.UUID.randomUUID().toString().take(8).uppercase() }
    var transactionRef by remember { mutableStateOf(randomRef) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "THANH TOÁN",
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
    ) {padding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.Black)) {
            Text(
                "Thanh toán cho: ${season.name}",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Số tiền: ${season.registrationFee} VNĐ",
                color = Color.Yellow,
                fontSize = 18.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = transactionRef,
                onValueChange = { transactionRef = it },
                label = { Text("Mã giao dịch (Có thể dùng mã tự sinh)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Button(
                onClick = { onConfirm(transactionRef) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("XÁC NHẬN THANH TOÁN")
            }
        }
    }
}