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
import com.example.qlbongda.data.model.RegisterRequest
import kotlinx.coroutines.launch




@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    val context = LocalContext.current
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // 🌟 1. TRẠNG THÁI ẨN / HIỆN MẬT KHẨU
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Trạng thái Loading khi đang gọi API Đăng ký
    var isLoading by remember { mutableStateOf(false) }

    // ---- QUẢN LÝ HIỆU ỨNG NÚT ĐĂNG KÝ ----
    val registerInteractionSource = remember { MutableInteractionSource() }
    val isRegisterPressed by registerInteractionSource.collectIsPressedAsState()
    val registerScale by animateFloatAsState(if (isRegisterPressed) 0.95f else 1.0f)

    // ---- QUẢN LÝ HIỆU ỨNG NÚT QUAY LẠI ĐĂNG NHẬP ----
    val backInteractionSource = remember { MutableInteractionSource() }
    val isBackPressed by backInteractionSource.collectIsPressedAsState()
    val backScale by animateFloatAsState(if (isBackPressed) 0.95f else 1.0f)

    // Biểu thức chính quy (Regex) để kiểm tra định dạng email chuẩn bao gồm đuôi chữ (ví dụ @gmail.com)
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"

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
            text = "Đăng ký tài khoản",
            color = NeonGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 1. Ô nhập Họ và Tên
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            enabled = !isLoading,
            label = { Text("Họ và tên", color = Color.Gray) },
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

        // 2. Ô nhập Email
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = NeonGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Ô nhập Số điện thoại (🌟 CHỈ CHO NHẬP TỐI ĐA 10 SỐ VÀ CHỈ ĐƯỢC NHẬP SỐ)
        OutlinedTextField(
            value = phone,
            onValueChange = { input ->
                // Chỉ xử lý nếu chuỗi nhập vào toàn là số và không quá 10 ký tự
                if (input.length <= 10 && input.all { it.isDigit() }) {
                    phone = input

                    // 🌟 KIỂM TRA BÁO LỖI NGAY KHI ĐANG GÕ 🌟
                    if (input.isNotEmpty() && !input.startsWith("0")) {
                        errorMessage = "Số đầu tiên phải là số 0!"
                    } else if (input.length < 10) {
                        // Nếu đã bắt đầu bằng số 0 nhưng chưa gõ đủ số, bạn có thể xóa lỗi cũ hoặc nhắc nhở
                        errorMessage = ""
                    } else {
                        errorMessage = ""
                    }
                }
            },
            enabled = !isLoading,
            label = { Text("Số điện thoại", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = NeonGreen,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Ô nhập Mật khẩu (🌟 TÍCH HỢP TÍNH NĂNG ẨN/HIỆN CON MẮT)
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
            // Thay đổi cấu hình hiển thị dựa trên biến trạng thái con mắt
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                // Nút bấm con mắt nằm ở cuối ô nhập liệu
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

        // Hiển thị thông báo lỗi bằng chữ Đỏ nếu có
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ---- HÀNG ĐIỀU HƯỚNG / HÀNH ĐỘNG ----
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // NÚT ĐĂNG KÝ (Bên trái)
            Button(
                onClick = {
                    val currentName = name.trim()
                    val currentEmail = email.trim()
                    val currentPhone = phone.trim()
                    val currentPassword = password.trim()

                    // 🌟 2. KIỂM TRA ĐIỀU KIỆN LOGIC ĐẦU VÀO GIAO DIỆN CHẶN TRƯỚC KHI GỬI API
                    when {
                        currentName.isEmpty() || currentEmail.isEmpty() ||
                                currentPhone.isEmpty() || currentPassword.isEmpty() -> {
                            errorMessage = "Vui lòng nhập đầy đủ tất cả thông tin!"
                        }
                        !currentEmail.matches(emailPattern.toRegex()) -> {
                            errorMessage = "Định dạng Email không chính xác (Ví dụ: abc@gmail.com)!"
                        }
                        currentPhone.length != 10 || !currentPhone.startsWith("0") -> {
                            errorMessage = "Số điện thoại phải nhập đủ chính xác 10 chữ số và bắt đầu bằng số 0!"
                        }
                        currentPassword.length < 6 -> {
                            errorMessage = "Mật khẩu phải chứa ít nhất 6 ký tự!"
                        }
                        !isLoading -> {
                            errorMessage = ""
                            isLoading = true

                            lifecycleScope.launch {
                                try {
                                    val registerRequest = RegisterRequest(
                                        name = currentName,
                                        email = currentEmail,
                                        phone = currentPhone,
                                        password = currentPassword
                                    )
                                    val apiService = RetrofitClient.getClient(context)
                                    val response = apiService.register(registerRequest)

                                    if (response.isSuccessful && response.body() != null) {
                                        val result = response.body()!!
                                        if (result.success) {
                                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                            onRegisterSuccess()
                                        } else {
                                            errorMessage = result.message
                                        }
                                    } else {
                                        errorMessage = "Email đã tồn tại hoặc dữ liệu không hợp lệ!"
                                    }
                                } catch (t: Throwable) {
                                    errorMessage = "Lỗi kết nối Server: ${t.localizedMessage}"
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
                    .scale(registerScale),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, NeonGreen),
                interactionSource = registerInteractionSource,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isRegisterPressed) NeonGreen.copy(alpha = 0.15f) else Color.Transparent
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = NeonGreen,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("ĐĂNG KÝ", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // NÚT QUAY LẠI ĐĂNG NHẬP (Bên phải)
            OutlinedButton(
                onClick = {
                    if (!isLoading) {
                        onBackToLogin()
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