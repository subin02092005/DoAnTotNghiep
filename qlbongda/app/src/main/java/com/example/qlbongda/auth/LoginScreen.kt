package com.example.qlbongda.auth

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.qlbongda.R
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.LoginRequest
import com.example.qlbongda.ui.theme.NeonGreen
import kotlinx.coroutines.launch
import com.google.firebase.messaging.FirebaseMessaging
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onLoginAdminSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    // Khởi tạo SharedPreferences để đọc/ghi nhớ tài khoản
    val sharedPref = remember {
        context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE)
    }

    // 🌟 ĐỌC DỮ LIỆU ĐÃ LƯU TRƯỚC ĐÓ KHI MÀN HÌNH KHỞI CHẠY 🌟
    var email by remember { mutableStateOf(sharedPref.getString("REMEMBERED_EMAIL", "") ?: "") }
    var password by remember { mutableStateOf(sharedPref.getString("REMEMBERED_PASSWORD", "") ?: "") }
    var isRememberMeChecked by remember { mutableStateOf(sharedPref.getBoolean("IS_REMEMBERED", false)) }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // ---- QUẢN LÝ HIỆU ỨNG NÚT BẤM ----
    val loginInteractionSource = remember { MutableInteractionSource() }
    val isLoginPressed by loginInteractionSource.collectIsPressedAsState()
    val loginScale by animateFloatAsState(if (isLoginPressed) 0.95f else 1.0f)

    val registerInteractionSource = remember { MutableInteractionSource() }
    val isRegisterPressed by registerInteractionSource.collectIsPressedAsState()
    val registerScale by animateFloatAsState(if (isRegisterPressed) 0.95f else 1.0f)

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
    ) {Text(
        text = "NEONBALL PRO",
        color = NeonGreen,
        fontWeight = FontWeight.Black,
        fontSize = 35.sp
    )
        Spacer(modifier = Modifier.width(12.dp)) // Khoảng cách giữa logo và c
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            tint = NeonGreen,
            modifier = Modifier.size(150.dp) // Tăng size logo một chút cho đẹp
                .padding(top = 20.dp) //
        )

        Spacer(modifier = Modifier.height(40.dp))// Khoảng cách giữa logo và chữ


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
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            enabled = !isLoading,
            label = { Text("Email", color = Color.Gray) },
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
            enabled = !isLoading,
            label = { Text("Mật khẩu", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                        tint = NeonGreen
                    )
                }
            },
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

        Spacer(modifier = Modifier.height(12.dp))

        // 🌟 THANH ĐIỀU KHIỂN: GHI NHỚ MẬT KHẨU & QUÊN MẬT KHẨU 🌟
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Khối nút Checkbox Ghi nhớ tài khoản (Bên trái)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isRememberMeChecked,
                    onCheckedChange = { isRememberMeChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = NeonGreen,
                        uncheckedColor = NeonGreen,
                        checkmarkColor = Color.Black
                    )
                )
                Text(
                    text = "Ghi nhớ",
                    color =NeonGreen,
                    fontSize = 14.sp
                )
            }

            // Nút Quên mật khẩu (Bên phải)
            TextButton(
                onClick = {
                    if (!isLoading) {
                        onForgotPasswordClick()
                    }
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // NÚT ĐĂNG NHẬP (Bên trái)
            Button(
                onClick = {
                    val inputEmail = email.trim()
                    val inputPassword = password.trim()

                    if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                        errorMessage = "Vui lòng nhập đầy đủ tài khoản và mật khẩu!"
                    } else if (!isLoading) {
                        errorMessage = ""
                        isLoading = true
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            val token = if (task.isSuccessful) task.result else null
                        lifecycleScope.launch {
                            try {
                                val loginRequest = LoginRequest(
                                    email = inputEmail,
                                    password = inputPassword,
                                    fcm_token = token
                                )
                                val apiService = RetrofitClient.getClient(context)
                                val response = apiService.login(loginRequest)

                                // 🌟 KIỂM TRA TRẠNG THÁI TRƯỚC KHI TRUY CẬP BODY 🌟
                                if (response.isSuccessful) {
                                    val loginResult = response.body()

                                    if (loginResult != null) {

                                        val userId = loginResult.user?.id ?: -1
                                        val user = loginResult.user
                                        // Lưu dữ liệu vào SharedPreferences
                                        android.util.Log.d("DEBUG_API", "User role từ API: ${loginResult.user?.role}")
                                        sharedPref.edit().apply {
                                            putInt("USER_ID", userId)
                                            putInt("TEAM_ID", loginResult.user?.team_id ?: 0)
                                            putString("ACCESS_TOKEN", loginResult.token ?: "")
                                            putString("USER_ROLE", user?.role ?: "player")
                                            putString(
                                                "USER_NAME",
                                                loginResult.user?.name ?: "Người dùng"
                                            )
                                            if (isRememberMeChecked) {
                                                putString("REMEMBERED_EMAIL", inputEmail)
                                                putString("REMEMBERED_PASSWORD", inputPassword)
                                                putBoolean("IS_REMEMBERED", true)
                                            } else {
                                                remove("REMEMBERED_EMAIL")
                                                remove("REMEMBERED_PASSWORD")
                                                putBoolean("IS_REMEMBERED", false)
                                            }
                                            apply()
                                        }

                                        val isAdmin = loginResult.user?.isAdmin ?: false
                                        Toast.makeText(
                                            context,
                                            "Đăng nhập thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val userRole = loginResult.user?.role ?: "player"
                                        if (isAdmin) onLoginAdminSuccess() else onLoginSuccess(userRole)
                                    } else {
                                        errorMessage = "Dữ liệu trả về từ máy chủ trống!"
                                    }
                                } else {
                                    // Xử lý khi API trả về lỗi (ví dụ 401 Unauthorized)
                                    when (response.code()) {
                                        401 -> errorMessage = "Sai tài khoản hoặc mật khẩu!"
                                        404 -> errorMessage = "Không tìm thấy người dùng!"
                                        else -> errorMessage = "Lỗi máy chủ (${response.code()})"
                                    }
                                }
                            } catch (t: Throwable) {
                                // Lỗi kết nối mạng
                                errorMessage =
                                    "Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng!"
                            } finally {
                                isLoading = false
                            }
                        }
                        }
                    }
                },

                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .scale(loginScale),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeonGreen),
                interactionSource = loginInteractionSource,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isLoginPressed) NeonGreen.copy(alpha = 0.15f) else Color.Transparent
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = NeonGreen,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("ĐĂNG NHẬP", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // NÚT ĐĂNG KÝ TÀI KHOẢN (Bên phải)
            OutlinedButton(
                onClick = {
                    if (!isLoading) {
                        onNavigateToRegister()
                    }
                },
                modifier = Modifier
                    .weight(1f)
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