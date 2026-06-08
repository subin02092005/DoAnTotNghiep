package com.example.qlbongda

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.qlbongda.data.model.DetailedStanding
import com.example.qlbongda.ui.theme.QlbongdaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QlbongdaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Quản lý trạng thái các màn hình: "login", "home", "standing_detail"
                    var currentScreen by remember { mutableStateOf("login") }

                    // DỮ LIỆU ĐẦY ĐỦ CHO MÀN HÌNH XẾP HẠNG CHI TIẾT
                    // Gồm các chỉ số: Hạng, Tên, Ảnh, Số trận, Thắng, Hòa, Thua, Bàn thắng, Bàn thua, Hiệu số, Điểm, Phong độ 5 trận
                    val detailedStandingsList = remember {
                        listOf(
                            DetailedStanding(
                                1,
                                "Arsenal",
                                "",
                                38,
                                28,
                                5,
                                5,
                                91,
                                29,
                                "+62",
                                89,
                                listOf("W", "W", "W", "W", "W")
                            ),
                            DetailedStanding(2, "Man City", "", 38, 27, 7, 4, 96, 34, "+62", 88, listOf("W", "W", "W", "D", "L")),
                            DetailedStanding(3, "MU", "", 38, 24, 10, 4, 86, 41, "+45", 82, listOf("W", "D", "W", "L", "D")),
                            DetailedStanding(4, "Chelsea", "", 38, 20, 11, 7, 77, 63, "+14", 71, listOf("W", "W", "W", "W", "W")),
                            DetailedStanding(5, "Liverpool", "", 38, 24, 10, 4, 86, 41, "+45", 82, listOf("W", "D", "W", "L", "D")),
                            DetailedStanding(6, "Tottenham", "", 38, 20, 6, 12, 74, 61, "+13", 66, listOf("W", "L", "W", "L", "L")),
                            DetailedStanding(7, "Aston Villa", "", 38, 20, 8, 10, 76, 61, "+15", 68, listOf("L", "D", "L", "W", "W")),
                            DetailedStanding(8, "Newcastle", "", 38, 18, 6, 14, 85, 62, "+23", 60, listOf("D", "W", "D", "L", "W"))
                        )
                    }


                    when (currentScreen) {
                        "admin"->{
                            AdminScreen({ Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                                // Chuyển ngược về màn đăng nhập
                                currentScreen = "login"})
                        }
                        "login" -> {
                            LoginScreen(
                                onLoginSuccess = {
                                    currentScreen = "home" // Đăng nhập xong chuyển sang trang chủ
                                },
                                onNavigateToRegister = {
                                    currentScreen = "register" // Bấm nút đăng ký chuyển trạng thái sang màn đăng ký
                                },
                                onForgotPasswordClick = {
                                    currentScreen="forgot_password"
                                }

                            )
                        }
                        "register" -> {
                            RegisterScreen(
                                onRegisterSuccess = { currentScreen = "login" }, // Đăng ký xong quay về đăng nhập
                                onBackToLogin = { currentScreen = "login" }      // Bấm nút Quay lại để về màn đăng nhập
                            )
                        }
                        "forgot_password" -> {
                            ForgotPasswordScreen(
                                onResetSuccess = {
                                    // 🌟 SỬA: Đổi mật khẩu xong thì quay về màn hình Đăng nhập bằng cách đổi trạng thái chuỗi
                                    currentScreen = "login"
                                },
                                onBackToLogin = {
                                    // 🌟 SỬA: Bấm quay lại thì chuyển về màn hình Đăng nhập
                                    currentScreen = "login"
                                }
                            )
                        }
                        "home" -> {
                            // Gọi màn hình chính và truyền thêm sự kiện onNavigateToStandingDetail
                            HomeScreen(
                                onLogout = {
                                    Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show()
                                    // Chuyển ngược về màn đăng nhập
                                    currentScreen = "login"
                                },
                                onNavigateToStandingDetail = {
                                    // Khi bấm vào bất kỳ dòng nào trên BXH ở trang chủ -> nhảy sang trang chi tiết
                                    currentScreen = "standing_detail"
                                }
                            )
                        }

                        "standing_detail" -> {
                            // Gọi màn hình hiển thị bảng xếp hạng chi tiết (vừa tạo ở bước trước)
                            StandingDetailScreen(
                                standings = detailedStandingsList,
                                onBack = {
                                    // Bấm nút quay lại (Arrow Back) trên thanh công cụ để về lại trang chủ
                                    currentScreen = "home"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}