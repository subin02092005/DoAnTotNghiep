package com.example.qlbongda

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.AuthLoginRequest
import com.example.qlbongda.data.model.AuthLoginResponse

val NeonGreen = Color(0xFF00FF00)

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // ---- QUẢN LÝ HIỆU ỨNG NÚT ĐĂNG NHẬP ----
    val loginInteractionSource = remember { MutableInteractionSource() }
    val isLoginPressed by loginInteractionSource.collectIsPressedAsState()
    val loginScale by animateFloatAsState(if (isLoginPressed) 0.95f else 1.0f)

    // ---- QUẢN LÝ HIỆU ỨNG NÚT ĐĂNG KÝ ----
    val registerInteractionSource = remember { MutableInteractionSource() }
    val isRegisterPressed by registerInteractionSource.collectIsPressedAsState()
    val registerScale by animateFloatAsState(if (isRegisterPressed) 0.95f else 1.0f)

    // ---- QUẢN LÝ HIỆU ỨNG NÚT QUÊN MẬT KHẨU ----
    val forgotInteractionSource = remember { MutableInteractionSource() }
    val isForgotPressed by forgotInteractionSource.collectIsPressedAsState()
    val forgotScale by animateFloatAsState(if (isForgotPressed) 0.92f else 1.0f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tiêu đề
        Text(
            text = "Đăng nhập tài khoản",
            color = NeonGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Ô nhập tên đăng nhập
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = ""
            },
            label = { Text("Tên đăng nhập", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = NeonGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ô nhập mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = { Text("Mật khẩu", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = NeonGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nút Quên mật khẩu lệch phải
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = {
                    Toast.makeText(context, "Chức năng khôi phục mật khẩu", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.scale(forgotScale),
                interactionSource = forgotInteractionSource,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isForgotPressed) Color.Gray else NeonGreen
                )
            ) {
                Text(
                    text = "Quên mật khẩu?",
                    fontWeight = if (isForgotPressed) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🌟 ĐƯA HAI NÚT VÀO HÀNG NGANG (ROW) 🌟
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Đẩy hai nút xa nhau ra hai bên mép
            verticalAlignment = Alignment.CenterVertically
        ) {
            // NÚT ĐĂNG NHẬP (Bên trái)
            Button(
                onClick = {
                    if (username.trim().isEmpty() || password.trim().isEmpty()) {
                        errorMessage = "Vui lòng nhập đầy đủ tài khoản và mật khẩu!"
                    } else {
                        // 🚀 BẮT ĐẦU THÊM LOGIC GỌI API THẬT Ở ĐÂY 🚀
                        errorMessage = "" // Xóa thông báo lỗi cũ đi khi đang load

                        // Tạo request (Lấy dữ liệu người dùng nhập từ 2 biến 'username' và 'password' của bạn)
                        val loginRequest =
                            AuthLoginRequest(email = username.trim(), password = password.trim())

                        // Gọi cấu hình mạng RetrofitClient (Hãy dùng cấu hình ở câu trả lời trước nhé)
                        val apiService = RetrofitClient.getClient(context)

                        apiService.login(loginRequest).enqueue(object : retrofit2.Callback<AuthLoginResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<AuthLoginResponse>,
                                response: retrofit2.Response<AuthLoginResponse>
                            ) {
                                if (response.isSuccessful && response.body() != null) {
                                    val loginResult = response.body()!!

                                    // Đăng nhập thành công -> Lưu AccessToken vào máy
                                    val sharedPref = context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE)
                                    sharedPref.edit().putString("ACCESS_TOKEN", loginResult.accessToken).apply()
                                    sharedPref.edit().putString("CSRF_TOKEN", loginResult.csrfToken).apply()

                                    Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                                    // Chạy hàm callback của bạn để chuyển màn hình sang Dashboard
                                    onLoginSuccess()
                                } else {
                                    // Nếu Server C# báo lỗi (401 hoặc 400)
                                    errorMessage = "Tài khoản hoặc mật khẩu không chính xác."
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<AuthLoginResponse>, t: Throwable) {
                                // Nếu rớt mạng hoặc Server C# chưa bật
                                errorMessage = "Lỗi kết nối Server: ${t.localizedMessage}"
                            }
                        })
                        // 🚀 KẾT THÚC ĐOẠN THÊM CODE API 🚀
                    }
                },
                modifier = Modifier
                    .weight(1f) // Chia đều không gian hàng ngang
                    .height(48.dp)
                    .scale(loginScale),
                shape = RoundedCornerShape(12.dp),border = BorderStroke(1.dp, NeonGreen),
                interactionSource = loginInteractionSource,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isLoginPressed) NeonGreen.copy(alpha = 0.15f) else Color.Transparent
                )

            ) {
                Text("ĐĂNG NHẬP", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Khoảng cách nhỏ giữa 2 nút
            Spacer(modifier = Modifier.width(16.dp))

            // NÚT ĐĂNG KÝ TÀI KHOẢN (Bên phải)
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Chuyển sang màn hình Đăng ký tài khoản!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f) // Chia đều không gian hàng ngang giúp 2 nút bằng nhau
                    .height(48.dp)
                    .scale(registerScale),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeonGreen),
                interactionSource = registerInteractionSource,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isRegisterPressed) NeonGreen.copy(alpha = 0.15f) else Color.Transparent
                )
            ) {
                Text("ĐĂNG KÝ", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {})
}