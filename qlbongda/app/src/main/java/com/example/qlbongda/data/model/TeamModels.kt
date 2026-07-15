package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// TEAM & PLAYER MODELS
// =================================================================

data class StandingItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val teamName: String,
    @SerializedName("coach_name") val coachName: String = "Chưa cập nhật",
    @SerializedName("captainName") val captainName: String,
    @SerializedName("players") val players: List<PlayerInfo>
)

data class PlayerInfo(
    val id: Int = 0,
    @SerializedName("jersey_number") val number: String,
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: String,
    @SerializedName("date_of_birth") val dateOfBirth: String? = null,
    val isCaptain: Boolean = false,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("role") val role: String? = null
)

data class TeamDetailResponse(
    val status: String,
    val message: String,
    val data: TeamDetailData
)

data class TeamDetailData(
    val id: Int,
    val teamName: String?,
    val coachName: String?,
    val captainName: String?,
    val players: List<PlayerInfo>
)

data class MyTeamResponse(
    val status: String,
    val hasTeam: Boolean,
    val data: MyTeamData?,
    val message: String?
)

data class MyTeamData(
    val teamId: Int,
    val teamName: String,
    val coachName: String?,
    val captainName: String?,
    val currentUserRole: String,
    val players: List<PlayerInfo>
)

data class AddPlayerRequest(
    val team_id: Int,
    val email: String,
    val jersey_number: String,
    val position: String
)

data class AddPlayerResponse(
    val status: String,
    val message: String,
    val data: PlayerInfo
)

data class UpdatePlayerRequest(
    val team_id: Int,
    val id: Int,
    val jersey_number: String,
    val position: String
)

data class RemovePlayerRequest(
    val team_id: Int,
    val player_id: Int,
    val currentUserId: Int
)

data class AddCoachRequest(
    val team_id: Int, 
    val email: String
)

data class RegisterTeamRequest(
    @SerializedName("team_name") val teamName: String,
    val captain_name: String,
    val user_id: Int
)

data class RegisterTeamResponse(
    val status: String,
    val teamId: Int?
)
