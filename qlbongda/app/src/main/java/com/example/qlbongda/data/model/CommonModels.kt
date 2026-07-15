package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// COMMON MODELS
// =================================================================

data class GenericResponse(
    val success: Boolean,
    val message: String
)

data class ApiResponse(
    val status: String,
    val message: String
)

data class ApiResponses<T>(
    val status: String,
    val data: T
)

data class BaseResponse(
    val status: String? = null,
    val message: String? = null,
    val success: Boolean? = null
)

data class Notification(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val time: String,
    @SerializedName("is_read") var is_read: Int
)

data class NotificationResponse(
    val status: String, 
    val data: List<Notification>
)

data class PaymentRequest(
    val transaction_ref: String,
    val season_team_id: Int
)
