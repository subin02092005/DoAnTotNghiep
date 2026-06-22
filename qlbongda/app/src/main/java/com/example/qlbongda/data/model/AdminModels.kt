package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

data class AdminPlayerItem(
    @SerializedName("player_id") val playerId: Int,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    val position: String?,
    val height: Int?,
    val weight: Int?,
    val nationality: String?,
    @SerializedName("player_active") val playerActive: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String,
    val email: String,
    val phone: String?,
    @SerializedName("user_active") val userActive: Int
)

data class AdminPlayerResponse(
    val success: Boolean,
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
    val id: Int,
    val name: String,
    @SerializedName("coach_name") val coachName: String?,
    val logo: String?,
    val description: String?,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("created_at") val createdAt: String?
)

data class AdminTeamResponse(
    val success: Boolean,
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
    val success: Boolean,
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
    val success: Boolean,
    val data: List<AdminMatchItem>? = null,
    val message: String? = null
)

data class UpdateMatchRequest(
    val status: String? = null,
    @SerializedName("home_score") val homeScore: Int? = null,
    @SerializedName("away_score") val awayScore: Int? = null,
    @SerializedName("scheduled_at") val scheduledAt: String? = null
)
