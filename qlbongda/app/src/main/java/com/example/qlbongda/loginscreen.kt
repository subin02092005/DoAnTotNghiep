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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.LoginRequest
import kotlinx.coroutines.launch

val NeonGreen = Color(0xFF00FF00)

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit // 🌟 Nhận tham số điều hướng từ MainActivity
) {
    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    var isPasswordVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.height(8.dp))

        // Nút Quên mật khẩu lệch phải
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = {
                    if (!isLoading) {
                        // 🌟 ĐÃ SỬA: Thay thế Toast cũ bằng hàm chuyển hướng màn hình quên mật khẩu
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
                    if (email.trim().isEmpty() || password.trim().isEmpty()) {
                        errorMessage = "Vui lòng nhập đầy đủ tài khoản và mật khẩu!"
                    } else if (!isLoading) {
                        errorMessage = ""
                        isLoading = true

                        lifecycleScope.launch {
                            try {
                                val loginRequest = LoginRequest(
                                    email = email.trim(),
                                    password = password.trim()
                                )
                                val apiService = RetrofitClient.getClient(context)
                                val response = apiService.login(loginRequest)

                                if (response.isSuccessful && response.body() != null) {
                                    val loginResult = response.body()!!

                                    val sharedPref = context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE)
                                    sharedPref.edit().putString("ACCESS_TOKEN", loginResult.token).apply()

                                    Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                } else {
                                    errorMessage = "Tài khoản hoặc mật khẩu không chính xác."
                                }
                            } catch (t: Throwable) {
                                errorMessage = "Lỗi kết nối Server: ${t.localizedMessage}"
                            } finally {
                                isLoading = false
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