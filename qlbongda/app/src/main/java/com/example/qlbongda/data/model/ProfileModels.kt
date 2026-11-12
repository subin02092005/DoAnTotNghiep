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
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    val height: Double?,
    val weight: Double?,
    val position: String?
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
    val date_of_birth: String?,
    val height: Double?,
    val weight: Double?,
    val position: String?
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
data class TeamPlayerItem(
    val team_name: String?,
    val jersey_number: Int?,
    val position: String?,
    val role: String?,
    val status: String?
)

data class PlayerStatItem(
    val season_name: String?,
    val matches_played: Int?,
    val goals_scored: Int?,
    val assists: Int?,
    val yellow_cards: Int?,
    val red_cards: Int?,
    val minutes_played: Int?
)
data class ProfileOverviewResponse(
    val success: Boolean,
    val teams: List<TeamPlayerItem>?,
    val statistics: List<PlayerStatItem>?
)
