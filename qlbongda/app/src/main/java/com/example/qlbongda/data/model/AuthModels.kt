package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// AUTHENTICATION & USER MODELS
// =================================================================

data class LoginRequest(
    val email: String,
    val password: String,
    val fcm_token: String? = null
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val role: String?,
    val email_verified: Int,
    val team_id: Int?,
    @SerializedName("is_admin") val isAdmin: Boolean? = false,
    @SerializedName("fcm_token") val fcmToken: String?
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val user: User?,
    val team_id: Int?
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
)

data class CheckAdminRequest(
    val email: String
)

data class CheckAdminResponse(
    val success: Boolean,
    @SerializedName("is_admin") val isAdmin: Boolean,
    val message: String? = null
)

// Khối dữ liệu gửi đi ở Bước 1 (Gửi email yêu cầu OTP)
data class VerifyEmailRequest(
    val email: String
)

// Khối dữ liệu nhận về ở Bước 1
data class VerifyEmailResponse(
    val success: Boolean,
    val name: String?,
    val message: String
)

// Bước 2: Đặt lại mật khẩu mới
data class ResetPasswordRequest(
    val email: String,
    val password: String
)
