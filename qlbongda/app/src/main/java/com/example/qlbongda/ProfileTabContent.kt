    package com.example.qlbongda

    import android.util.Log
    import android.widget.Toast
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.clickable
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
    import androidx.lifecycle.compose.LocalLifecycleOwner
    import androidx.lifecycle.lifecycleScope
    import com.example.qlbongda.data.api.RetrofitClient
    import com.example.qlbongda.data.model.ChangePasswordRequest
    import com.example.qlbongda.data.model.UpdateProfileRequest
    import com.example.qlbongda.data.model.VerifyOtpRequest
    import com.example.qlbongda.ui.theme.NeonGreen
    import com.example.qlbongda.utils.DateUtils
    import kotlinx.coroutines.launch
    import java.time.Instant
    import java.time.ZoneId

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileTabContent(onLogout: () -> Unit) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val scrollState = rememberScrollState()
        val sharedPref = remember { context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE) }

        // Các state cơ bản lưu thông tin hiển thị
        var userName by remember { mutableStateOf(sharedPref.getString("USER_NAME", "Người dùng") ?: "Người dùng") }
        val userEmail = remember { sharedPref.getString("REMEMBERED_EMAIL", "") ?: "" }
        var userPhone by remember { mutableStateOf(sharedPref.getString("USER_PHONE", "") ?: "") }
        // var userPassword by remember { mutableStateOf(sharedPref.getString("REMEMBERED_PASSWORD", "123456") ?: "123456") }
        var userRole by remember { mutableStateOf("user") }
        var userDob by remember { mutableStateOf(sharedPref.getString("USER_DOB", "") ?: "") }
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        // Các state quản lý OTP và Dialog
        var isEmailVerified by remember { mutableStateOf(false) }
        var showOtpDialog by remember { mutableStateOf(false) }
        var otpInput by remember { mutableStateOf("") }
        var isLoadingProfile by remember { mutableStateOf(true) }
        var editDob by remember { mutableStateOf(userDob) }
        var showEditSheet by remember { mutableStateOf(false) }
        var showDatePicker by remember { mutableStateOf(false) }
        var dateOfBirthInput by remember { mutableStateOf(userDob) }

        // 🌟 1. TỰ ĐỘNG GỌI API LẤY THÔNG TIN REALTIME TỪ DB KHI MỞ TAB PROFILE
        LaunchedEffect(userEmail) {
            if (userEmail.isNotEmpty()) {
                try {
                    // Sửa thành getProfileInfo để nhận về ProfileResponse (có chứa trường .success và .data)
                    val response = RetrofitClient.getClient(context).getProfileInfo(userEmail)
                    if (response.isSuccessful && response.body()?.success == true) {
                        val profileData = response.body()?.data
                        if (profileData != null) {
                            // Thêm các toán tử safe call hoặc elvis operator
                            userName = profileData.name ?: "Người dùng"
                            userPhone = profileData.phone ?: ""
                            isEmailVerified = (profileData.email_verified == 1)
                            userRole = profileData.role ?: "user"
                            if (profileData != null) {
                                Log.d("DEBUG_DOB", "Dữ liệu ngày sinh từ API: ${profileData.dateOfBirth}")
                                userDob = profileData.dateOfBirth ?: ""
                                dateOfBirthInput = userDob
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoadingProfile = false
                }
            } else {
                isLoadingProfile = false
            }
        }

        if (isLoadingProfile) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else {
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
                        text = if(userName.trim().length >= 2) userName.trim().take(2).uppercase() else "US",
                        color = NeonGreen, fontSize = 24.sp, fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Chào mừng, $userName!", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(text = "$userEmail  |  $userPhone", color = Color.Gray, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))

                // 🌟 HIỂN THỊ TRẠNG THÁI XÁC THỰC EMAIL TỪ API
                Spacer(modifier = Modifier.height(8.dp))
                if (isEmailVerified) {
                    Text(text = "🟢 Tài khoản đã xác thực Email", color = Color(0xFF4CAF50), fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔴 Tài khoản chưa xác thực", color = Color.Red, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        TextButton(onClick = { showOtpDialog = true }, contentPadding = PaddingValues(0.dp), modifier = Modifier.height(30.dp)) {
                            Text("[Xác thực ngay]", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            Text(text = userRole.uppercase(), color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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

                Button(onClick = {
                    val sharedPref = context.getSharedPreferences("AUTH_PREF", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().apply {
                        remove("USER_ID")
                        apply()
                    }
                    onLogout()
                                 }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Red), shape = RoundedCornerShape(8.dp)) {
                    Text(text = "ĐĂNG XUẤT TÀI KHOẢN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        // 🌟 2. DIALOG XỬ LÝ NHẬP MÃ OTP KÍCH HOẠT VÀ CALL API XÁC THỰC
        if (showOtpDialog) {
            AlertDialog(
                onDismissRequest = { showOtpDialog = false },
                containerColor = Color(0xFF121212),
                shape = RoundedCornerShape(16.dp),
                title = { Text("Kích hoạt tài khoản bằng OTP", color = NeonGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Nhập mã OTP kích hoạt hệ thống gửi đến Email của bạn.", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(bottom = 16.dp))
                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { if (it.length <= 6) otpInput = it },
                            label = { Text("Mã OTP (Thử: 123456)", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (otpInput.isEmpty()) {
                                Toast.makeText(context, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Gọi API xử lý chạy ngầm Coroutine bằng lifecycleScope
                            lifecycleOwner.lifecycleScope.launch {
                                try {
                                    // Gọi đúng hàm verifyOtpProfile để nhận về VerifyOtpResponse
                                    val response = RetrofitClient.getClient(context).verifyOtpProfile(VerifyOtpRequest(userEmail, otpInput))

                                    if (response.isSuccessful && response.body()?.success == true) {
                                        isEmailVerified = true // Đổi giao diện thành chữ màu xanh lá
                                        showOtpDialog = false
                                        otpInput = ""
                                        Toast.makeText(context, "Xác thực tài khoản thành công!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, response.body()?.message ?: "Mã OTP không chính xác. Hãy thử lại!", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi kết nối Server: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                    ) { Text("XÁC NHẬN", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showOtpDialog = false; otpInput = "" }) { Text("HỦY BỎ", color = Color.Gray) }
                }
            )
        }

        // 🌟 PHẦN BOTTOMSHEET CHỈNH SỬA THÔNG TIN & ĐỔI MẬT KHẨU (GIỮ NGUYÊN CODE CŨ CỦA BẠN)
        if (showEditSheet) {
            var editName by remember { mutableStateOf(userName) }
            var editEmail by remember { mutableStateOf(userEmail) }
            var editPhone by remember { mutableStateOf(userPhone) }
            var showChangePasswordDialog by remember { mutableStateOf(false) }

            ModalBottomSheet(
                onDismissRequest = { showEditSheet = false },
                sheetState = sheetState, // Quan trọng: Đã thêm sheetState
                containerColor = Color(0xFF0F0F0F),
                scrimColor = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp).verticalScroll(rememberScrollState()).navigationBarsPadding()) {
                    Text(text = "Thay đổi thông tin cá nhân", color = NeonGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 20.dp))

                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Họ và Tên", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = editEmail, onValueChange = { editEmail = it }, label = { Text("Địa chỉ Email", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true, readOnly = true, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { newValue ->
                            // Chỉ cho phép nhập số
                            if (newValue.all { it.isDigit() }) {
                                // Giới hạn tối đa 11 ký tự
                                if (newValue.length < 11) {
                                    // Kiểm tra xem ký tự đầu tiên có phải là '0' không
                                    // Nếu người dùng xóa hết thì cho phép rỗng
                                    if (newValue.isEmpty() || newValue.startsWith("0")) {
                                        editPhone = newValue
                                    }
                                }
                            }
                        },
                        label = { Text("Số điện thoại", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        // Đảm bảo bàn phím chỉ hiện số
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = DateUtils.formatDate(dateOfBirthInput),
                        onValueChange = { editDob = it },
                        readOnly = true, // Khóa không cho nhập tay
                        label = { Text("Ngày sinh", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        enabled = false, // Vô hiệu hóa để click vẫn nhận sự kiện
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.White)
                    )
                    // Bảng chọn ngày (hiện ra khi showDatePicker = true)
                    if (showDatePicker) {
                        val datePickerState = rememberDatePickerState()
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        // 🌟 Cập nhật ngày đã chọn sang định dạng ISO cho server
                                        dateOfBirthInput = Instant.ofEpochMilli(millis)
                                            .atZone(ZoneId.of("UTC"))
                                            .toString()
                                    }
                                    showDatePicker = false
                                }) { Text("CHỌN") }
                            }
                        ) { DatePicker(state = datePickerState) }
                    }
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
                                val phoneRegex = Regex("^0[0-9]{9,10}$")

                                when {
                                    editName.trim().isEmpty() -> {
                                    Toast.makeText(context, "Họ và tên không được để trống!", Toast.LENGTH_SHORT).show()
                                }
                                    !editPhone.matches(phoneRegex) -> {
                                    Toast.makeText(context, "Số điện thoại phải bắt đầu bằng 0 và có 10-11 số!", Toast.LENGTH_SHORT).show()
                                }
                                    else -> {
                                    // Gọi API cập nhật lên server nếu mọi thông tin đều hợp lệ
                                        lifecycleOwner.lifecycleScope.launch {
                                            try {
                                                // 🌟 Gửi dateOfBirthInput (định dạng ISO) lên server
                                                val response = RetrofitClient.getClient(context).updateProfile(
                                                    UpdateProfileRequest(
                                                        email = userEmail,
                                                        name = editName,
                                                        phone = editPhone,
                                                        date_of_birth = dateOfBirthInput // Gửi qua API này
                                                    )
                                                )

                                            if (response.isSuccessful && response.body()?.success == true) {
                                                userDob = dateOfBirthInput
                                                sharedPref.edit().putString("USER_DOB", dateOfBirthInput).apply()
                                                userName = editName
                                                userPhone = editPhone
                                                sharedPref.edit()
                                                    .putString("USER_NAME", editName)
                                                    .putString("USER_PHONE", editPhone)
                                                    .apply()
                                                Toast.makeText(context, "Đã cập nhật thành công!", Toast.LENGTH_SHORT).show()
                                                showEditSheet = false
                                            } else {
                                                Toast.makeText(context, "Lỗi: " + (response.body()?.message ?: "Không thể cập nhật"), Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                }
                            },
                            modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                        ) { Text("LƯU LẠI", fontWeight = FontWeight.Bold) }
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
                            if (newPasswordInput.isNotEmpty() && newPasswordInput.length < 6) {
                                Text(
                                    text = "Mật khẩu quá ngắn (tối thiểu 6 ký tự)",
                                    color = Color.Red,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(value = confirmPasswordInput, onValueChange = { confirmPasswordInput = it }, label = { Text("Xác nhận mật khẩu mới", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), trailingIcon = { IconButton(onClick = { isConfirmVisible = !isConfirmVisible }) { Icon(imageVector = if (isConfirmVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = null, tint = NeonGreen) } }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonGreen, unfocusedBorderColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                when {
                                    // Kiểm tra các trường không để trống
                                    oldPasswordInput.isEmpty() || newPasswordInput.isEmpty() || confirmPasswordInput.isEmpty() ->
                                        Toast.makeText(context, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show()

                                    // KIỂM TRA ĐỘ DÀI MẬT KHẨU MỚI (Ít nhất 6 ký tự)
                                    newPasswordInput.length < 6 ->
                                        Toast.makeText(context, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show()

                                    // Kiểm tra xác nhận mật khẩu trùng khớp
                                    newPasswordInput != confirmPasswordInput ->
                                        Toast.makeText(context, "Xác nhận mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show()

                                    else -> {
                                        // Gọi API đổi mật khẩu...
                                        lifecycleOwner.lifecycleScope.launch {
                                            try {
                                                val response = RetrofitClient.getClient(context).changePassword(
                                                    ChangePasswordRequest(
                                                        userEmail,
                                                        oldPasswordInput,
                                                        newPasswordInput
                                                    )
                                                )
                                                if (response.isSuccessful && response.body()?.success == true) {
                                                    Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                                                    showChangePasswordDialog = false
                                                } else {
                                                    Toast.makeText(context, response.body()?.message ?: "Sai mật khẩu cũ!", Toast.LENGTH_SHORT).show()
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
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