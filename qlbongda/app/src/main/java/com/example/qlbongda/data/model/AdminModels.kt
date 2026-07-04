package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

data class AdminPlayerItem(
    @SerializedName("player_id") val playerId: Int? = null,
    @SerializedName("date_of_birth") val dateOfBirth: String? = null,
    val position: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val nationality: String? = null,
    @SerializedName("player_active") val playerActive: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @SerializedName("user_active") val userActive: Int? = null
)

data class AdminPlayerResponse(
    val success: Boolean? = null,
    val status: String? = null,
    val data: List<AdminPlayerItem>? = null,
    val message: String? = null
)

data class RoleRequest(
    @SerializedName("roleId") val roleId: Int
)

data class GenericAdminResponse(
    val success: Boolean,
    val message: String
)

data class AdminTeamItem(
    val id: Int? = null,
    val name: String? = null,
    @SerializedName("coach_name") val coachName: String? = null,
    val logo: String? = null,
    val description: String? = null,
    @SerializedName("is_active") val isActive: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

data class AdminTeamResponse(
    val success: Boolean? = null,
    val status: String? = null,
    val data: List<AdminTeamItem>? = null,
    val message: String? = null
)

data class AdminMatchItem(
    val id: Int,
    @SerializedName("phase_id") val phaseId: Int?,
    @SerializedName("group_id") val groupId: Int?,
    @SerializedName("home_team_id") val homeTeamId: Int,
    @SerializedName("home_team_name") val homeTeamName: String,
    @SerializedName("home_team_logo") val homeTeamLogo: String?,
    @SerializedName("away_team_id") val awayTeamId: Int,
    @SerializedName("away_team_name") val awayTeamName: String,
    @SerializedName("away_team_logo") val awayTeamLogo: String?,
    @SerializedName("scheduled_at") val scheduledAt: String,
    @SerializedName("played_at") val playedAt: String?,
    @SerializedName("home_score") val homeScore: Int?,
    @SerializedName("away_score") val awayScore: Int?,
    val status: String,
    val round: Int?,
    val leg: Int?,
    @SerializedName("venue_id") val venueId: Int?,
    val referee: String?,
    @SerializedName("season_id") val seasonId: Int?,
    @SerializedName("is_published") val isPublished: Int,
    @SerializedName("is_featured") val isFeatured: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class FeaturedMatchRequest(
    @SerializedName("is_featured") val isFeatured: Boolean
)

data class FeaturedMatchResponse(
    val success: Boolean,
    val message: String,
    val data: AdminMatchItem? = null
)

data class FeaturedMatchesListResponse(
    val success: Boolean? = null,
    val status: String? = null,
    val message: String? = null,
    val data: List<AdminMatchItem>? = null,
    val pagination: PaginationInfo? = null
)

data class PaginationInfo(
    val total: Int,
    val limit: Int,
    val offset: Int,
    val hasMore: Boolean
)

data class AdminMatchResponse(
    val success: Boolean? = null,
    val status: String? = null,
    val data: List<AdminMatchItem>? = null,
    val message: String? = null
)

data class UpdateMatchRequest(
    val status: String? = null,
    @SerializedName("home_score") val homeScore: Int? = null,
    @SerializedName("away_score") val awayScore: Int? = null,
    @SerializedName("scheduled_at") val scheduledAt: String? = null
)

// --- MATCH EVENTS MODELS ---

data class UpdateScoreRequest(
    @SerializedName("home_score") val homeScore: Int,
    @SerializedName("away_score") val awayScore: Int,
    val status: String? = "ongoing"
)

data class SubstitutionRequest(
    @SerializedName("team_id") val teamId: Int,
    @SerializedName("player_in_id") val playerInId: Int,
    @SerializedName("player_out_id") val playerOutId: Int,
    val minute: Int,
    val period: String
)

data class CardRequest(
    @SerializedName("team_id") val teamId: Int,
    @SerializedName("player_id") val playerId: Int,
    val minute: Int,
    val period: String,
    val note: String? = null
)

data class MatchEventDetailed(
    val id: Int,
    @SerializedName("match_id") val matchId: Int,
    @SerializedName("player_id") val playerId: Int?,
    @SerializedName("team_id") val teamId: Int?,
    val type: String,
    val minute: Int,
    val period: String,
    val note: String?,
    @SerializedName("card_color") val cardColor: String?,
    @SerializedName("sub_out_player_id") val subOutPlayerId: Int?,
    @SerializedName("player_name") val playerName: String?,
    @SerializedName("team_name") val teamName: String?,
    @SerializedName("sub_out_player_name") val subOutPlayerName: String?,
    @SerializedName("created_at") val createdAt: String?
)

data class MatchEventResponse(
    val success: Boolean,
    val data: List<MatchEventDetailed>? = null,
    val message: String? = null
)

data class GenericEventResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)
