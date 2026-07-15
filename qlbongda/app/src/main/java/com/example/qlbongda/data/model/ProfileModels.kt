package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// PROFILE MODELS
// =================================================================

data class ProfileData(
    val name: String,
    val email: String,
    val phone: String,
    val email_verified: Int,
    val role: String,
    @SerializedName("date_of_birth") val dateOfBirth: String?
)

data class ProfileResponse(
    val success: Boolean,
    val data: ProfileData?
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class VerifyOtpResponse(
    val success: Boolean,
    val message: String
)

data class UpdateProfileRequest(
    val email: String,
    val name: String,
    val phone: String,
    val date_of_birth: String?
)

data class ChangePasswordRequest(
    val email: String, 
    val oldPassword: String, 
    val newPassword: String
)

data class GeneralResponse(
    val success: Boolean, 
    val message: String
)
