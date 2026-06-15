package com.example.qlbongda.data.api

import com.example.qlbongda.data.model.* // Import toàn bộ các model đã tạo ở trên
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
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // ---- QUÊN MẬT KHẨU ----
    @POST("api/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("api/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<GenericResponse>

    // ---- PROFILE ----
    @GET("api/profile/get-info")
    suspend fun getProfileInfo(@Query("email") email: String): Response<ProfileResponse>

    @POST("api/profile/verify-otp")
    suspend fun verifyOtpProfile(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    // ---- 🌟 PHẦN SỬA ĐỔI / THÊM MỚI CHO HOME GIẢI ĐẤU 🌟 ----
    // Gọi API lấy danh sách các vòng đấu (Phases) theo id của mùa giải
    @GET("api/teams/{teamId}/detail")
    suspend fun getTeamDetail(
        @Path("teamId") teamId: Int
    ): Response<TeamDetailResponse>
    @GET("api/detailed")
    suspend fun getDetailedStandings(): Response<DetailedStandingResponse>
    @GET("api/season/get-phases")
    suspend fun getSeasonPhases(@Query("season_id") seasonId: Int): Response<SeasonResponse>
    @GET("api/news")
    suspend fun getNews(): Response<NewsResponse>

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
    @GET("matches")
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
}