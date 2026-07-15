package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// MATCH MODELS
// =================================================================

enum class MatchStatus {
    PENDING, ONGOING, FINISHED
}

data class MatchDto(
    val id: Int,
    val teamA: String,
    val teamB: String,
    @SerializedName("home_final_score") val home_score: Int?,
    @SerializedName("away_final_score") val away_score: Int?,
    val scheduled_at: String?,
    @SerializedName("status") val status: String,
    @SerializedName("is_featured") val is_featured: Int? = 0
) {
    val matchStatus: MatchStatus
        get() = when (status.lowercase()) {
            "ongoing" -> MatchStatus.ONGOING
            "finished" -> MatchStatus.FINISHED
            else -> MatchStatus.PENDING
        }
    val isHot: Boolean get() = is_featured == 1
    val isFinished: Boolean get() = matchStatus == MatchStatus.FINISHED
    val isLive: Boolean get() = matchStatus == MatchStatus.ONGOING
    val isUpcoming: Boolean get() = matchStatus == MatchStatus.PENDING
    val isStarted: Boolean get() = isFinished || isLive
}

data class MatchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<MatchDto>
)

data class FullMatchDetail(
    val id: Int,
    @SerializedName("teamA", alternate = ["home_team_name"]) val teamA: String,
    @SerializedName("teamB", alternate = ["away_team_name"]) val teamB: String,
    @SerializedName("home_team_id") val teamAId: Int = 0,
    @SerializedName("away_team_id") val teamBId: Int = 0,
    val status: String,
    val isStarted: Boolean,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val time: String,
    val date: String,
    @SerializedName("venue_name", alternate = ["stadium"]) val stadium: String?,
    val events: List<MatchEvent> = emptyList(),
    val lineupA: List<PlayerInfo> = emptyList(),
    val lineupB: List<PlayerInfo> = emptyList(),
    val subsA: List<PlayerInfo> = emptyList(),
    val subsB: List<PlayerInfo> = emptyList(),
    val PossessionA: String = "0%",
    val PossessionB: String = "0%",
    val ShotsA: String = "0",
    val ShotsB: String = "0",
    val mvp: String = "Chưa xác định",
    val isHot: Boolean = false
)

data class MatchEvent(
    val minute: String,
    val team: String,
    val type: String,
    val playerName: String
)

data class MatchDetailResponse(
    val status: String? = null,
    val success: Boolean? = null,
    val data: FullMatchDetail
)
