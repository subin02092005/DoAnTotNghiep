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
    @SerializedName("field_type") val fieldType: Int = 7, // Thêm loại sân (mặc định 7)
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
    @SerializedName("role") val role: String? = null,
    @SerializedName("is_starter") val isStarter: Boolean = false, // 🌟 Thêm dòng này
    // 🌟 THÊM CÁC TRƯỜNG THẺ PHẠT VÀ TREO GIÒ MỚI DƯỚI ĐÂY:
    @SerializedName("accumulated_yellow_cards") val yellowCards: Int = 0, // Số thẻ vàng tích lũy
@SerializedName("red_cards") val redCards: Int = 0,                    // Số thẻ đỏ
@SerializedName("is_suspended") val isSuspended: Boolean = false      // Trạng thái bị treo giò (true/false)
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
    @SerializedName("field_type") val fieldType: Int = 7, // Thêm loại sân ở chi tiết team
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
    @SerializedName("field_type") val fieldType: Int = 7, // Thêm loại sân cho team của tôi
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
    val user_id: Int,
    @SerializedName("field_type") val fieldType: Int = 7 // Cho phép chọn loại sân ngay khi đăng ký đội
)

data class RegisterTeamResponse(
    val status: String,
    val teamId: Int?
)

data class UpdateFieldTypeRequest(
    @SerializedName("team_id") val teamId: Int,
    @SerializedName("field_type") val fieldType: Int
)

data class SimpleResponse(
    val status: String,
    val message: String
)
data class StarterResponse(
    @SerializedName("status") val status: String,
    @SerializedName("starter_player_ids") val starter_player_ids: List<Int>,
    @SerializedName("message") val message: String? = null
)

data class UnregisterRequest(
    val team_id: String,
    val season_id: String
)
data class UpdateStartersRequest(
    @SerializedName("team_id") val team_id: Int,
    @SerializedName("field_type") val field_type: Int,
    @SerializedName("starter_player_ids") val starter_player_ids: List<Int>
)
// =================================================================
// SCORER & ASSISTER MODEL (Thêm mới)
// =================================================================

data class ScorerAssisterInfo(
    @SerializedName("player_id") val playerId: Int,
    @SerializedName("player_name") val playerName: String,
    @SerializedName("jersey_number") val jerseyNumber: String,
    @SerializedName("position") val position: String,
    @SerializedName("is_still_in_team") val isStillInTeam: Int, // 1: Còn trong đội (chấm xanh neon), 0: Đã rời đi (đỏ/xám)
    @SerializedName("total_goals") val totalGoals: Int,
    @SerializedName("total_assists") val totalAssists: Int
)
data class CardStatusResponse(
    val status: String,
    val data: List<PlayerCardDto>
)

data class PlayerCardDto(
    @SerializedName("player_id") val playerId: Int,
    @SerializedName("accumulated_yellow_cards") val yellowCards: Int,
    @SerializedName("red_cards") val redCards: Int,
    @SerializedName("is_suspended") val isSuspended: Int // hoặc Boolean tùy database trả về (0/1)
)
data class ScorerAssisterResponse(
    val status: String,
    val data: List<ScorerAssisterInfo>,
    val message: String? = null
)
