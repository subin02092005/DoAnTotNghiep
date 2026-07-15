package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// STANDING MODELS
// =================================================================

data class DetailedStanding(
    @SerializedName("id") val id: Int,
    val rank: Int,
    val teamName: String,
    val logoUrl: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: String,
    val points: Int,
    val form: List<String>?
)

data class DetailedStandingResponse(
    val status: String,
    val message: String? = null,
    val data: List<GroupStanding>
)

data class GroupStanding(
    val phaseId: Int,
    val groupId: Int,
    val groupName: String,
    val standings: List<DetailedStanding>
)
