package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName
data class NotificationItem(
    val id: Int,
    val title: String,
    val content: String,
    val type: String,
    val source: String,
    val target_team_id: Int?,
    val recipient_user_id: Int?,
    val is_active: Int,
    val created_at: String
)
data class UpdateNotificationRequest(
    val title: String,
    val content: String,
    val type: String,
    @SerializedName("is_active") val is_active: Int
)
data class NotificationResponseadmin(
    val status: String,
    val data: List<NotificationItem> // Chứa danh sách thông báo bên trong
)
data class CreateNotificationRequest(
    val title: String,
    val content: String,
    val type: String="general",
    val source: String,
    @SerializedName("target_team_id") val target_team_id: Int?,
    @SerializedName("recipient_user_id") val recipient_user_id: Int?,
)
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
    @SerializedName("created_at") val createdAt: String? = null,
    val groupId: Int? = null
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
    @SerializedName("home_team_name", alternate = ["teamA"]) val homeTeamName: String,
    @SerializedName("home_team_logo") val homeTeamLogo: String?,
    @SerializedName("away_team_id") val awayTeamId: Int,
    @SerializedName("away_team_name", alternate = ["teamB"]) val awayTeamName: String,
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
    @SerializedName("winner_team_id") val winnerTeamId: Int? = null,
    @SerializedName("scheduled_at") val scheduledAt: String? = null,
    @SerializedName("phase_id") val phaseId: Int? = null,
    @SerializedName("group_id") val groupId: Int? = null,
    @SerializedName("home_team_id") val homeTeamId: Int? = null,
    @SerializedName("away_team_id") val awayTeamId: Int? = null,
    @SerializedName("round") val round: Int? = null,
    @SerializedName("leg") val leg: Int? = null,
    @SerializedName("venue_id") val venueId: Int? = null,
    @SerializedName("referee") val referee: String? = null,
    @SerializedName("season_id") val seasonId: Int? = null,
    @SerializedName("is_published") val isPublished: Int? = null
)

data class CreateMatchRequest(
    @SerializedName("phase_id") val phaseId: Int? = null,
    @SerializedName("group_id") val groupId: Int? = null,
    @SerializedName("home_team_id") val homeTeamId: Int,
    @SerializedName("away_team_id") val awayTeamId: Int,
    @SerializedName("scheduled_at") val scheduledAt: String,
    @SerializedName("venue_id") val venueId: Int? = null,
    @SerializedName("round") val round: Int? = null,
    @SerializedName("leg") val leg: Int? = null,
    @SerializedName("referee") val referee: String? = null,
    @SerializedName("season_id") val seasonId: Int? = null,
    @SerializedName("is_published") val isPublished: Int = 1
)

data class RescheduleMatchRequest(
    @SerializedName("scheduled_at") val scheduledAt: String? = null,
    @SerializedName("venue_id") val venueId: Int? = null
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

// --- TOURNAMENT MODELS ---

data class TournamentItem(
    val id: Int,
    val name: String,
    val description: String?,
    val logo: String?,
    @SerializedName("max_players") val maxPlayers: Int?, 
    @SerializedName("min_players") val minPlayers: Int?,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
data class TournamentUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val logo: String? = null,
    val is_active: Int? = null
)
data class TournamentResponse(
    val success: Boolean,
    val data: List<TournamentItem>? = null,
    val message: String? = null
)
data class SeasonDetailResponse(
    val season: SeasonAdminItem,
    val phases: List<PhaseItem>,
    val teams: List<TeamItem>// Mỗi mùa giải có danh sách các giai đoạn
)
data class TeamItem(
    val id: Int,
    @SerializedName("team_id") val team_id: Int,
    val name: String,
    val status: String,
    @SerializedName("group_id") val groupId: Int? = null
)
data class AddTeamRequestadmin(
    @SerializedName("teamId", alternate = ["team_id"]) val teamId: Int, // Chấp nhận cả teamId và team_id
    @SerializedName("groupId", alternate = ["group_id"]) val groupId: Int? = null
)

data class AddTeamResponse(
    val success: Boolean,
    val message: String
)

// 2. Giai đoạn (Phase)
data class PhaseItem(
    val id: Int,
    val name: String,
    val type: String,
    val format: String,
    val teams: List<TeamItem>? ,// THÊM DÒNG NÀY: D
    val matches: List<MatchItem>? = null,
    val groups: List<GroupItem>? = null // Phải có trường này
)
data class GroupItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)

// 3. Trận đấu (Match)
data class MatchItem(
    val id: Int,
    val home_team_id: Int,
    val away_team_id: Int,
    val scheduled_at: String,
    val status: String
)
// Phải có <T> ngay sau tên class
data class GenericResponseAdmin<T>(
    val success: Boolean,
    val message: String?,
    val data: T? // T là biến đại diện cho kiểu dữ liệu thực tế
)

data class SeasonAdminItem(
    val id: Int,
    val name: String,
    val description: String?,
    val status: String,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("end_date") val endDate: String?,
    @SerializedName("registration_deadline") val registrationDeadline: String?,
    @SerializedName("is_registration_open") val isRegistrationOpen: Int,
    @SerializedName("is_active") val isActive: Int
)

data class SeasonRulesRequest(
    @SerializedName("points_per_win") val pointsPerWin: Int? = 3,
    @SerializedName("points_per_draw") val pointsPerDraw: Int? = 1,
    @SerializedName("points_per_loss") val pointsPerLoss: Int? = 0,
    @SerializedName("yellow_cards_suspension") val yellowCardsSuspension: Int? = 3,
    @SerializedName("max_players_per_team") val maxPlayersPerTeam: Int? = 25,
    @SerializedName("min_players_per_team") val minPlayersPerTeam: Int? = 11,
    @SerializedName("registration_fee") val registrationFee: Double? = 0.0,
    @SerializedName("forfeit_score") val forfeitScore: Int? = 3,
    @SerializedName("teams_advance_per_group") val teamsAdvancePerGroup: Int? = 2,
    @SerializedName("tiebreaker_order") val tiebreakerOrder: List<String>? = listOf("goal_difference", "goals_scored", "head_to_head"),
    @SerializedName("user_id") val userId: Int? = null
)

data class TournamentDetailData(
    val tournament: TournamentItem,
    val seasons: List<SeasonAdminItem>
)

data class TournamentDetailResponse(
    val success: Boolean,
    val data: TournamentDetailData? = null,
    val message: String? = null
)

data class CreateTournamentRequest(
    val name: String,
    val description: String?,
    val logo: String?,
    @SerializedName("max_players") val maxPlayers: Int,
    @SerializedName("min_players") val minPlayers: Int,
    @SerializedName("user_id") val userId: Int?
)

data class CreateSeasonRequest(
    val name: String,
    val description: String?,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("registration_deadline") val registrationDeadline: String,
    @SerializedName("is_registration_open") val isRegistrationOpen: Boolean,
    @SerializedName("tournament_id") val tournamentId: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("max_teams") val maxTeams: Int? = 16,
    @SerializedName("registration_fee") val registrationFee: Double? = 0.0
)

data class CreatePhaseRequest(
    val name: String,
    val type: String, // group_stage, quarter_final, semi_final, final...
    val format: String, // round_robin, knockout
    val order: Int,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    @SerializedName("group_count") val groupCount: Int? = null,
    @SerializedName("group_names") val groupNames: List<String>? = null,
    @SerializedName("team_ids") val teamIds: List<Int>? = null,
    @SerializedName("autoSchedule") val autoSchedule: Boolean? = true
)


data class ScheduleOptionsRequest(
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("start_time") val startTime: String? = null,
    @SerializedName("interval_hours") val intervalHours: Int? = 2,
    @SerializedName("interval_minutes") val intervalMinutes: Int? = 0,
    @SerializedName("teamIds") val teamIds: List<Int>? = null,
    @SerializedName("phaseId") val phaseId: Int? = null // For season-level API
)

data class AutoImportAndScheduleRequest(
    val phaseId: Int? = null,
    val teamIds: List<Int>? = emptyList(),
    val start_date: String? = null,
    val start_time: String? = null,
    val interval_hours: Int? = null,
    val interval_minutes: Int? = null
)

// Class hứng phản hồi từ API (nếu bạn muốn lấy data từ result của server)
data class AutoImportAndScheduleResponse(
    val status: String,
    val message: String,

)