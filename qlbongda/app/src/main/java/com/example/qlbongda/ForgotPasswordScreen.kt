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
import com.example.qlbongda.data.model.ResetPasswordRequest
import com.example.qlbongda.data.model.VerifyEmailRequest
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(onResetSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    // ---- TRẠNG THÁI DỮ LIỆU ----
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Quản lý các giai đoạn: false = Nhập Email, true = Nhập mật khẩu mới
    var isEmailVerified by remember { mutableStateOf(false) }
    // Lưu lại tên người dùng trả về từ API
    var userNameByEmail by remember { mutableStateOf("") }

    // Ẩn/hiện mật khẩu
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // ---- HIỆU ỨNG NÚT BẤM ----
    val actionInteractionSource = remember { MutableInteractionSource() }
    val isActionPressed by actionInteractionSource.collectIsPressedAsState()
    val actionScale by animateFloatAsState(if (isActionPressed) 0.95f else 1.0f)

    val backInteractionSource = remember { MutableInteractionSource() }
    val isBackPressed by backInteractionSource.collectIsPressedAsState()
    val backScale by animateFloatAsState(if (isBackPressed) 0.95f else 1.0f)

    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tiêu đề màn hình
        Text(
            text = if (!isEmailVerified) "Quên mật khẩu" else "Đặt lại mật khẩu",
            color = NeonGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = if (!isEmailVerified) "Nhập email tài khoản để xác minh" else "Nhập mật khẩu mới cho tài khoản của bạn",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // ==========================================
        // GIAI ĐOẠN 1: NHẬP EMAIL XÁC NHẬN
        // ==========================================
        if (!isEmailVerified) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
                },
                enabled = !isLoading,
                label = { Text("Email xác nhận", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = NeonGreen,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }
        // ==========================================
        // GIAI ĐOẠN 2: EMAIL ĐÚNG -> HIỆN TÊN + KHUNG MK MỚI
        // ==========================================
        else {
            Text(
                text = "Tài khoản: $userNameByEmail",
                color = NeonGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Ô nhập Mật khẩu mới
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                enabled = !isLoading,
                label = { Text("Mật khẩu mới", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
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

            Spacer(modifier = Modifier.height(16.dp))

            // Ô nhập Xác nhận mật khẩu mới
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = ""
                },
                enabled = !isLoading,
                label = { Text("Xác nhận mật khẩu mới", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
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
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ---- THANH NÚT ĐIỀU HƯỚNG ----
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // NÚT HÀNH ĐỘNG CHÍNH (XÁC NHẬN / CẬP NHẬT)
            Button(
                onClick = {
                    if (!isEmailVerified) {
                        // --- 🌟 LOGIC GỌI API XÁC MINH EMAIL (BƯỚC 1) ---
                        val currentEmail = email.trim()
                        when {
                            currentEmail.isEmpty() -> {
                                errorMessage = "Vui lòng nhập Email cần xác nhận!"
                            }
                            !currentEmail.matches(emailPattern.toRegex()) -> {
                                errorMessage = "Định dạng Email không đúng chuẩn!"
                            }
                            !isLoading -> {
                                errorMessage = ""
                                isLoading = true
                                lifecycleScope.launch {
                                    try {
                                        val apiService = RetrofitClient.getClient(context)
                                        val response = apiService.verifyEmail(VerifyEmailRequest(currentEmail))

                                        if (response.isSuccessful && response.body() != null) {
                                            val result = response.body()!!
                                            if (result.success) {
                                                userNameByEmail = result.name ?: "Thành viên"
                                                isEmailVerified = true // Chuyển sang Giai đoạn 2
                                            } else {
                                                errorMessage = result.message
                                            }
                                        } else {
                                            errorMessage = "Email này không tồn tại trên hệ thống!"
                                        }
                                    } catch (t: Throwable) {
                                        errorMessage = "Lỗi kết nối Server: ${t.localizedMessage}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    } else {
                        // --- 🌟 LOGIC GỌI API CẬP NHẬT MẬT KHẨU MỚI (BƯỚC 2) ---
                        val newPassword = password.trim()
                        val confirmPass = confirmPassword.trim()

                        when {
                            newPassword.isEmpty() || confirmPass.isEmpty() -> {
                                errorMessage = "Vui lòng nhập đầy đủ mật khẩu mới!"
                            }
                            newPassword.length < 6 -> {
                                errorMessage = "Mật khẩu phải chứa ít nhất 6 ký tự!"
                            }
                            newPassword != confirmPass -> {
                                errorMessage = "Mật khẩu xác nhận không trùng khớp!"
                            }
                            !isLoading -> {
                                errorMessage = ""
                                isLoading = true
                                lifecycleScope.launch {
                                    try {
                                        val apiService = RetrofitClient.getClient(context)
                                        val response = apiService.resetPassword(
                                            ResetPasswordRequest(email.trim(), newPassword)
                                        )

                                        if (response.isSuccessful && response.body() != null) {
                                            val result = response.body()!!
                                            if (result.success) {
                                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                                onResetSuccess() // Callback chuyển hướng người dùng về màn Login
                                            } else {
                                                errorMessage = result.message
                                            }
                                        } else {
                                            errorMessage = "Đặt lại mật khẩu thất bại, vui lòng thử lại!"
                                        }
                                    } catch (t: Throwable) {
                                        errorMessage = "Lỗi kết nối Server: ${t.localizedMessage}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .scale(actionScale),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeonGreen),
                interactionSource = actionInteractionSource,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isActionPressed) NeonGreen.copy(alpha = 0.15f) else Color.Transparent
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = NeonGreen, strokeWidth = 2.dp)
                } else {
                    Text(
                        text = if (!isEmailVerified) "XÁC NHẬN" else "CẬP NHẬT",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // NÚT QUAY LẠI
            OutlinedButton(
                onClick = {
                    if (!isLoading) {
                        if (isEmailVerified) {
                            isEmailVerified = false
                            password = ""
                            confirmPassword = ""
                        } else {
                            onBackToLogin()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .scale(backScale),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeonGreen),
                interactionSource = backInteractionSource,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isBackPressed) NeonGreen.copy(alpha = 0.15f) else Color.Transparent
                )
            ) {
                Text("QUAY LẠI", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}