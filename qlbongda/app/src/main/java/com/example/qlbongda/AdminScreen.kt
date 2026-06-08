package com.example.qlbongda

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Tái sử dụng màu sắc chủ đạo của ứng dụng
// val NeonGreen = Color(0xFF00FF00) // Đã được định nghĩa ở file khác trong cùng package

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onLogout: () -> Unit) {
    var currentSection by remember { mutableStateOf(AdminSection.DASHBOARD) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "HỆ THỐNG QUẢN TRỊ (ADMIN)",
                        color = NeonGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    if (currentSection != AdminSection.DASHBOARD) {
                        IconButton(onClick = { currentSection = AdminSection.DASHBOARD }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSection) {
                AdminSection.DASHBOARD -> AdminDashboard(onSectionSelect = { currentSection = it })
                AdminSection.TEAMS -> TeamManagementScreen()
                AdminSection.LEAGUES -> LeagueManagementScreen()
                AdminSection.PLAYERS -> PlayerManagementAdminScreen()
                AdminSection.SCHEDULE -> MatchScheduleManagementScreen()
                AdminSection.STATS -> StatisticsScreen()
                AdminSection.PAYMENTS -> PaymentConfirmationScreen()
            }
        }
    }
}

enum class AdminSection {
    DASHBOARD, TEAMS, LEAGUES, PLAYERS, SCHEDULE, STATS, PAYMENTS
}

data class AdminMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val section: AdminSection,
    val color: Color = NeonGreen
)

@Composable
fun AdminDashboard(onSectionSelect: (AdminSection) -> Unit) {
    val menuItems = listOf(
        AdminMenuItem("Quản lý Đội bóng", "Duyệt và chỉnh sửa các đội tham gia", Icons.Default.Groups, AdminSection.TEAMS),
        AdminMenuItem("Quản lý Giải đấu", "Thiết lập mùa giải và vòng đấu", Icons.Default.EmojiEvents, AdminSection.LEAGUES),
        AdminMenuItem("Quản lý Cầu thủ", "Cơ sở dữ liệu cầu thủ toàn hệ thống", Icons.Default.Person, AdminSection.PLAYERS),
        AdminMenuItem("Lịch thi đấu", "Sắp xếp và cập nhật kết quả trận đấu", Icons.Default.CalendarMonth, AdminSection.SCHEDULE),
        AdminMenuItem("Thống kê & Báo cáo", "Xem hiệu suất và dữ liệu giải đấu", Icons.Default.Assessment, AdminSection.STATS),
        AdminMenuItem("Xác nhận Thanh toán", "Phê duyệt lệ phí tham gia của các đội", Icons.Default.Payments, AdminSection.PAYMENTS, Color.Yellow)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(menuItems) { item ->
            AdminMenuCard(item) { onSectionSelect(item.section) }
        }
    }
}

@Composable
fun AdminMenuCard(item: AdminMenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, item.color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

// Các màn hình con (Placeholders)

@Composable
fun TeamManagementScreen() {
    ManagementHeader("QUẢN LÝ ĐỘI BÓNG", "Danh sách 24 đội bóng đã đăng ký")
    // Ở đây sẽ có LazyColumn hiển thị danh sách đội bóng với các nút Duyệt/Xóa/Sửa
}

@Composable
fun LeagueManagementScreen() {
    ManagementHeader("QUẢN LÝ GIẢI ĐẤU", "Thiết lập thông số giải đấu Mùa Xuân 2026")
}

@Composable
fun PlayerManagementAdminScreen() {
    ManagementHeader("QUẢN LÝ CẦU THỦ", "Tìm kiếm và quản lý thông tin cầu thủ")
}

@Composable
fun MatchScheduleManagementScreen() {
    ManagementHeader("LỊCH THI ĐẤU", "Cập nhật thời gian và tỉ số trận đấu")
}

@Composable
fun StatisticsScreen() {
    ManagementHeader("THỐNG KÊ & BÁO CÁO", "Tổng quan doanh thu và dữ liệu chuyên môn")
}

@Composable
fun PaymentConfirmationScreen() {
    ManagementHeader("XÁC NHẬN THANH TOÁN", "Danh sách các giao dịch chờ phê duyệt")

    val pendingPayments = remember {
        listOf(
            "Đội Arsenal - Lệ phí: 5.000.000đ",
            "Đội Chelsea - Lệ phí: 5.000.000đ",
            "Đội MU - Lệ phí: 5.000.000đ"
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(pendingPayments) { payment ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(payment, color = Color.White, fontSize = 14.sp)
                    Button(
                        onClick = { /* Logic phê duyệt */ },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Duyệt", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ManagementHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title, color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = subtitle, color = Color.LightGray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFF222222))
    }
}

// ---- MÀN HÌNH ĐĂNG NHẬP ADMIN (Nếu cần tách biệt) ----
@Composable
fun AdminLoginScreen(onLoginSuccess: () -> Unit) {
    var adminCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ADMIN ACCESS", color = NeonGreen, fontSize = 30.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = adminCode,
            onValueChange = { adminCode = it },
            label = { Text("Mã quản trị", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu bảo mật", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = NeonGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if(adminCode == "admin" && password == "123") onLoginSuccess()
                else { /* Toast error */ }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ĐĂNG NHẬP QUẢN TRỊ", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
