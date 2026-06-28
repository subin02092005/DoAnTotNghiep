package com.example.qlbongda.data.api

import com.example.qlbongda.data.model.* 
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // ---- AUTHENTICATION ----
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("check-admin-email")
    suspend fun checkAdminEmail(@Body request: CheckAdminRequest): Response<CheckAdminResponse>

    // ---- QUÊN MẬT KHẨU ----
    @POST("verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<GenericResponse>

    // ---- PROFILE ----
    @GET("profile/get-info")
    suspend fun getProfileInfo(@Query("email") email: String): Response<ProfileResponse>

    @POST("profile/verify-otp")
    suspend fun verifyOtpProfile(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    // ---- CHI TIẾT ĐỘI BÓNG & BXH ----
    @GET("teams/{teamId}") // Khớp với router.get('/teams/:id') trong teamController.js
    suspend fun getTeamDetail(
        @Path("teamId") teamId: Int
    ): Response<TeamDetailResponse>

    @GET("standings")
    suspend fun getDetailedStandings(): Response<DetailedStandingResponse>

    @GET("seasons/{seasonId}/phases") // Đã sửa theo server.js: /api/seasons/:seasonId/phases
    suspend fun getSeasonPhases(@Path("seasonId") seasonId: Int): Response<SeasonResponse>

    @GET("notifications")
    suspend fun getNotifications(): Response<NotificationResponse>

    @GET("matches")
    suspend fun getMatches(): Response<MatchResponse>

    @GET("match/detail")
    suspend fun getMatchDetail(@Query("id") matchId: Int): Response<MatchDetailResponse>

    // ---- ADMIN SECTION ----
    @GET("players")
    suspend fun getPlayersAdmin(@Query("name") name: String? = null): Response<AdminPlayerResponse>

    @PATCH("players/{userId}/lock")
    suspend fun lockAccount(@Path("userId") userId: Int): Response<GenericAdminResponse>

    @POST("users/{userId}/roles")
    suspend fun assignRole(@Path("userId") userId: Int, @Body request: RoleRequest): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/approve")
    suspend fun approvePlayer(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/reject")
    suspend fun rejectPlayer(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/status/injured")
    suspend fun markInjured(@Path("id") id: Int): Response<GenericAdminResponse>

    // ---- ADMIN TEAMS ----
    @GET("teams")
    suspend fun getTeamsAdmin(
        @Query("name") name: String? = null,
        @Query("coach_name") coachName: String? = null,
        @Query("is_active") isActive: Int? = null
    ): Response<AdminTeamResponse>

    @PATCH("teams/{id}/approve")
    suspend fun approveTeam(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("teams/{id}/reject")
    suspend fun rejectTeam(@Path("id") id: Int): Response<GenericAdminResponse>

    // ---- ADMIN MATCHES ----
    @GET("matches") // Bạn nên cân nhắc đặt path khác nếu bị trùng với public matches
    suspend fun getMatchesAdmin(
        @Query("status") status: String? = null,
        @Query("season_id") seasonId: Int? = null
    ): Response<AdminMatchResponse>

    @PUT("matches/{id}")
    suspend fun updateMatch(
        @Path("id") id: Int,
        @Body request: UpdateMatchRequest
    ): Response<GenericAdminResponse>

    @PATCH("matches/{id}/cancel")
    suspend fun cancelMatch(@Path("id") id: Int): Response<GenericAdminResponse>

    // ---- ADMIN FEATURED MATCHES ----
    @PATCH("matches/{id}/featured")
    suspend fun updateFeaturedMatch(
        @Path("id") id: Int,
        @Body request: FeaturedMatchRequest
    ): Response<FeaturedMatchResponse>

    @GET("featured")
    suspend fun getFeaturedMatches(
        @Query("limit") limit: Int? = 10,
        @Query("offset") offset: Int? = 0
    ): Response<FeaturedMatchesListResponse>
}
