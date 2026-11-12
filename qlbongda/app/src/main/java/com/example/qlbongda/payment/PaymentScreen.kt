package com.example.qlbongda.payment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.SeasonInfo
import com.example.qlbongda.ui.theme.NeonGreen
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(season: SeasonInfo, onConfirm: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current

    // Hàm tạo mã giao dịch ngẫu nhiên
    fun generateRef() = "NEON" + UUID.randomUUID().toString().take(6).uppercase()

    var transactionRef by remember { mutableStateOf(generateRef()) }
    val scrollState = rememberScrollState()

    // Hàm tiện ích sao chép văn bản nhanh
    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Đã sao chép $label!", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "THANH TOÁN GIẢI ĐẤU",
                        color = NeonGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. PHẦN HÓA ĐƠN ĐĂNG KÝ (TICKET/INVOICE CARD) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = BorderStroke(1.dp, Color(0xFF222222))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "THÔNG TIN HÓA ĐƠN",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = season.name.uppercase(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Lệ phí tham gia:", color = Color.LightGray, fontSize = 14.sp)
                        Text(
                            text = "${season.registrationFee} VNĐ",
                            color = Color.Yellow,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Nội dung chuyển khoản gợi ý
                    val transferContent = "CK ${transactionRef}"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Nội dung chuyển khoản bắt buộc:", color = Color.Gray, fontSize = 12.sp)
                            Text(
                                text = transferContent,
                                color = NeonGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = { copyToClipboard("Nội dung chuyển khoản", transferContent) }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy content", tint = NeonGreen)
                        }
                    }
                }
            }

            // --- 2. THÔNG TIN CHUYỂN KHOẢN (BANKING DETAILS) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = BorderStroke(1.dp, Color(0xFF222222))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HƯỚNG DẪN CHUYỂN KHOẢN",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
                    )

                    // Giả lập vùng quét mã QR thanh toán nhanh
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(2.dp, NeonGreen, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = "QR Code Placeholder",
                                tint = Color.Black,
                                modifier = Modifier.size(60.dp)
                            )
                            Text(
                                "VIETQR",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            Text(
                                "QUÉT MÃ ĐỂ TỰ ĐIỀN",
                                color = Color.Gray,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bảng chi tiết ngân hàng
                    val bankName = "Ngân hàng Thương mại Cổ phần Quân đội (MB)"
                    val bankAccount = "190356789999"
                    val accountHolder = "NEONBALL PRO SPORTS JOINT STOCK COMPANY"

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BankingRow("Ngân hàng", bankName, onClickCopy = { copyToClipboard("Ngân hàng", bankName) })
                        BankingRow("Số tài khoản", bankAccount, onClickCopy = { copyToClipboard("Số tài khoản", bankAccount) })
                        BankingRow("Chủ tài khoản", accountHolder, isBold = true)
                    }
                }
            }

            // --- 3. ĐIỀN MÃ THAM CHIẾU GIAO DỊCH (TRANSACTION REFERENCE INPUT) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "XÁC NHẬN GIAO DỊCH",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Sau khi đã thực hiện chuyển tiền thành công, hãy nhập mã giao dịch tại ngân hàng vào bên dưới để Ban Tổ Chức đối soát nhanh nhất.",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = transactionRef,
                        onValueChange = { transactionRef = it },
                        label = { Text("Mã tham chiếu / Mã giao dịch") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Row {
                                IconButton(onClick = { transactionRef = generateRef() }) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Tạo mã mới",
                                        tint = NeonGreen
                                    )
                                }
                                IconButton(onClick = { copyToClipboard("Mã giao dịch", transactionRef) }) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = "Sao chép mã",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = NeonGreen,
                            unfocusedLabelColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
            }

            // --- 4. NÚT XÁC NHẬN CHUNG ---
            Button(
                onClick = {
                    if (transactionRef.isNotBlank()) {
                        onConfirm(transactionRef)
                    } else {
                        Toast.makeText(context, "Vui lòng không để trống mã giao dịch!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "TÔI ĐÃ CHUYỂN KHOẢN",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
            }
        }
    }
}

// Composable phụ để vẽ từng hàng thông tin ngân hàng kèm nút copy nhanh tiện lợi
@Composable
fun BankingRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    onClickCopy: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.Gray, fontSize = 11.sp)
            Text(
                text = value,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            )
        }
        if (onClickCopy != null) {
            IconButton(
                onClick = onClickCopy,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = NeonGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}