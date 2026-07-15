package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// TOURNAMENT & PHASES MODELS
// =================================================================

data class SeasonResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: SeasonDataResponse?
)

data class SeasonDataResponse(
    @SerializedName("season_id") val seasonId: Int,
    @SerializedName("phases") val phases: List<TournamentPhase>
)

data class TournamentPhase(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("format") val format: String
)

data class TournamentRulesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: TournamentRules,
    val message: String,
)

data class TournamentRules(
    val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("tournament_id") val tournamentId: Int,
    @SerializedName("points_per_win") val pointsPerWin: Int,
    @SerializedName("points_per_draw") val pointsPerDraw: Int,
    @SerializedName("points_per_loss") val pointsPerLoss: Int,
    @SerializedName("forfeit_score") val forfeitScore: Int,
    @SerializedName("yellow_cards_suspension") val yellowCardsSuspension: Int,
    @SerializedName("max_players_per_team") val maxPlayersPerTeam: Int,
    @SerializedName("min_players_per_team") val minPlayersPerTeam: Int,
    val description: String,
    @SerializedName("tiebreaker_order") val tiebreaker_order: List<String>
)

data class SeasonInfo(
    val id: Int,
    val name: String,
    val description: String?,
    val status: String,
    val max_teams: Int,
    val start_date: String?,
    val end_date: String?,
    val registration_deadline: String?,
    @SerializedName("registration_fee") val registrationFee: Long,
    val is_registered: Int,
    val payment_status: String?,
    val season_team_id: Int? = null
)

data class SeasonWithPhases(
    val id: Int,
    val name: String,
    val phases: List<TournamentPhase>
)
