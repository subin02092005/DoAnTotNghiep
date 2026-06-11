package com.example.qlbongda

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabContent(onLogout: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val sharedPref = remember { context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE) }

    var userName by remember { mutableStateOf(sharedPref.getString("USER_NAME", "Quản trị viên") ?: "Quản trị viên") }
    var userEmail by remember { mutableStateOf(sharedPref.getString("REMEMBERED_EMAIL", "admin@tttqshop.com") ?: "admin@tttqshop.com") }
    var userPhone by remember { mutableStateOf(sharedPref.getString("USER_PHONE", "0123456789") ?: "0123456789") }
    var userPassword by remember { mutableStateOf(sharedPref.getString("REMEMBERED_PASSWORD", "123456") ?: "123456") }

    var showEditSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(90.dp).background(Color(0xFF121212), RoundedCornerShape(45.dp)).border(2.dp, NeonGreen, RoundedCornerShape(45.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if(userName.trim().length >= 2) userName.trim().take(2).uppercase() else "AD",
                color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Chào mừng, $userName!", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text(text = "$userEmail  |  $userPhone", color = Color.Gray, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = { showEditSheet = true },
            modifier = Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 32.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, NeonGreen),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
        ) {
            Text(text = "CHỈNH SỬA THÔNG TIN", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = Color(0xFF222222))
        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)), border = BorderStroke(0.5.dp, Color.DarkGray)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Đơn vị", color = Color.LightGray, fontSize = 13.sp)
                    Text("Trường CĐ KT Cao Thắng", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Khoa", color = Color.LightGray, fontSize = 13.sp)
                    Text("Công nghệ Thông tin", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Vai trò", color = Color.LightGray, fontSize = 13.sp)
                    Text("Ban Tổ Chức", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFF222222))
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Phiên bản ứng dụng", color = Color.LightGray, fontSize = 13.sp)
                    Text("v2.6.0 (Mùa giải 2026)", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Trạng thái máy chủ", color = Color.LightGray, fontSize = 13.sp)
                    Text("Ổn định 🟢", color = Color.White, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { onLogout() }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Red), shape = RoundedCornerShape(8.dp)) {
            Text(text = "ĐĂNG XUẤT TÀI KHOẢN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }

    if (showEditSheet) {
        var editName by remember { mutableStateOf(userName) }
        var editEmail by remember { mutableStateOf(userEmail) }
        var editPhone by remember { mutableStateOf(userPhone) }
        var showChangePasswordDialog by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            containerColor = Color(0xFF0F0F0F),
            scrimColor = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp).verticalScroll(rememberScrollState()).navigationBarsPadding()) {
                Text(text = "Thay đổi thông tin cá nhân", color = NeonGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 20.dp))

                OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Họ và Tên", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = editEmail, onValueChange = { editEmail = it }, label = { Text("Địa chỉ Email", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text("Số điện thoại", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(onClick = { showChangePasswordDialog = true }, modifier = Modifier.align(Alignment.End)) {
                    Text(text = "Đổi mật khẩu tài khoản?", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = { showEditSheet = false }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Gray), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) {
                        Text("HỦY BỎ", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (editName.trim().isEmpty() || editEmail.trim().isEmpty() || editPhone.trim().isEmpty()) {
                                Toast.makeText(context, "Vui lòng không để trống thông tin!", Toast.LENGTH_SHORT).show()
                            } else {
                                userName = editName
                                userEmail = editEmail
                                userPhone = editPhone
                                sharedPref.edit().apply {
                                    putString("USER_NAME", editName)
                                    putString("REMEMBERED_EMAIL", editEmail)
                                    putString("USER_PHONE", editPhone)
                                    apply()
                                }
                                Toast.makeText(context, "Đã lưu thay đổi thông tin!", Toast.LENGTH_SHORT).show()
                                showEditSheet = false
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                    ) {
                        Text("LƯU LẠI", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showChangePasswordDialog) {
            var oldPasswordInput by remember { mutableStateOf("") }
            var newPasswordInput by remember { mutableStateOf("") }
            var confirmPasswordInput by remember { mutableStateOf("") }
            var isOldVisible by remember { mutableStateOf(false) }
            var isNewVisible by remember { mutableStateOf(false) }
            var isConfirmVisible by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { showChangePasswordDialog = false },
                containerColor = Color(0xFF121212), shape = RoundedCornerShape(16.dp),
                title = { Text("Đổi mật khẩu mới", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Nhập đầy đủ thông tin bảo mật để thiết lập mật khẩu mới.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 16.dp))
                        OutlinedTextField(value = oldPasswordInput, onValueChange = { oldPasswordInput = it }, label = { Text("Mật khẩu hiện tại", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = if (isOldVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), trailingIcon = { IconButton(onClick = { isOldVisible = !isOldVisible }) { Icon(imageVector = if (isOldVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen) } }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = newPasswordInput, onValueChange = { newPasswordInput = it }, label = { Text("Mật khẩu mới", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = if (isNewVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), trailingIcon = { IconButton(onClick = { isNewVisible = !isNewVisible }) { Icon(imageVector = if (isNewVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen) } }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = confirmPasswordInput, onValueChange = { confirmPasswordInput = it }, label = { Text("Xác nhận mật khẩu mới", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), trailingIcon = { IconButton(onClick = { isConfirmVisible = !isConfirmVisible }) { Icon(imageVector = if (isConfirmVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen) } }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            when {
                                oldPasswordInput.isEmpty() || newPasswordInput.isEmpty() || confirmPasswordInput.isEmpty() -> Toast.makeText(context, "Vui lòng nhập đầy đủ 3 ô!", Toast.LENGTH_SHORT).show()
                                oldPasswordInput != userPassword -> Toast.makeText(context, "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show()
                                newPasswordInput == oldPasswordInput -> Toast.makeText(context, "Mật khẩu mới không được trùng mật khẩu cũ!", Toast.LENGTH_SHORT).show()
                                newPasswordInput != confirmPasswordInput -> Toast.makeText(context, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show()
                                else -> {
                                    userPassword = newPasswordInput
                                    sharedPref.edit().putString("REMEMBERED_PASSWORD", newPasswordInput).apply()
                                    Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                                    showChangePasswordDialog = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                    ) { Text("XÁC NHẬN", fontWeight = FontWeight.Bold) }
                },
                dismissButton = { TextButton(onClick = { showChangePasswordDialog = false }) { Text("HỦY BỎ", color = Color.Gray) } }
            )
        }
    }
}