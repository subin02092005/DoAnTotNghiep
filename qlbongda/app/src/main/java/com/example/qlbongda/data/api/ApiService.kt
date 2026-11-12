package com.example.qlbongda.data.api

import com.example.qlbongda.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // =========================================================================
    // 1. AUTHENTICATION (Đăng nhập, Đăng ký, Quên mật khẩu)
    // =========================================================================
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("check-admin-email")
    suspend fun checkAdminEmail(@Body request: CheckAdminRequest): Response<CheckAdminResponse>

    @POST("verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<GenericResponse>


    // =========================================================================
    // 2. USER PROFILE (Màn hình Trang cá nhân & Cài đặt)
    // =========================================================================
    @GET("profile/get-info")
    suspend fun getProfileInfo(@Query("email") email: String): Response<ProfileResponse>

    @POST("profile/verify-otp")
    suspend fun verifyOtpProfile(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("profile/update-info")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<GeneralResponse>

    @POST("profile/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<GeneralResponse>

    @GET("overview")
    suspend fun getProfileOverview(@Query("email") email: String): Response<ProfileOverviewResponse>


    // =========================================================================
    // 3. HOME & MATCHES (Màn hình Trang chủ, Lịch thi đấu & Chi tiết trận đấu)
    // =========================================================================
    @GET("matches")
    suspend fun getMatches(): Response<MatchResponse>

    @GET("match/detail")
    suspend fun getMatchDetail(@Query("id") matchId: Int): Response<MatchDetailResponse>

    @GET("my_notifications")
    suspend fun getNotifications(
        @Query("userId") userId: Int,
        @Query("teamId") teamId: Int
    ): Response<NotificationResponse>


    // =========================================================================
    // 4. TEAM MANAGEMENT (Màn hình Quản lý đội bóng, Đội hình, Sơ đồ chiến thuật)
    // =========================================================================
    @GET("my_team")
    suspend fun getMyTeam(@Query("userId") userId: Int): Response<MyTeamResponse>

    @POST("register_team")
    suspend fun registerTeam(@Body request: RegisterTeamRequest): Response<RegisterTeamResponse>

    @POST("add_player_by_email")
    suspend fun addPlayerByEmail(@Body request: AddPlayerRequest): Response<AddPlayerResponse>

    @POST("add_coach_by_email")
    suspend fun addCoachByEmail(@Body request: AddCoachRequest): Response<Void>

    @POST("update_player")
    suspend fun updatePlayer(@Body request: UpdatePlayerRequest): Response<ApiResponse>

    @POST("remove_player")
    suspend fun removePlayer(@Body request: RemovePlayerRequest): Response<Void>

    @POST("update_field_type")
    suspend fun updateFieldType(@Body request: UpdateFieldTypeRequest): Response<SimpleResponse>

    @POST("update_starters")
    suspend fun updateStarters(@Body request: UpdateStartersRequest): Response<ApiResponse>

    @GET("get_starters")
    suspend fun getStarters(
        @Query("team_id") teamId: Int,
        @Query("field_type") fieldType: Int
    ): Response<StarterResponse>

    @GET("team_scorers_assisters")
    suspend fun getTeamScorersAndAssisters(@Query("team_id") teamId: Int): Response<ScorerAssisterResponse>

    @GET("player_card_status")
    suspend fun getPlayerCardStatus(
        @Query("team_id") teamId: Int,
        @Query("season_id") seasonId: Int
    ): Response<CardStatusResponse>


    // =========================================================================
    // 5. TOURNAMENTS & STANDINGS (Màn hình Giải đấu, Bảng xếp hạng & Đăng ký giải)
    // =========================================================================
    @GET("team/{teamId}/detail")
    suspend fun getTeamDetail(@Path("teamId") teamId: Int): Response<TeamDetailResponse>

    @GET("standings")
    suspend fun getDetailedStandings(@Query("seasonId") seasonId: Int? = null): Response<DetailedStandingResponse>

    @GET("all_seasons")
    suspend fun getAllData(): Response<ApiResponses<List<SeasonWithPhases>>>

    @GET("seasons/{seasonId}/phases")
    suspend fun getSeasonPhases(@Path("seasonId") seasonId: Int): Response<SeasonResponse>

    @GET("open_seasons")
    suspend fun getOpenSeasons(@Query("teamId") teamId: Int): Response<List<SeasonInfo>>

    @GET("rules/global")
    suspend fun getGlobalRules(): Response<TournamentRulesResponse>

    @POST("register_to_season")
    suspend fun registerToSeason(@Body request: Map<String, Int>): Response<Void>

    @POST("unregister_season")
    suspend fun unregisterFromSeason(@Body request: UnregisterRequest): Response<ApiResponse>

    @POST("confirm_payment")
    suspend fun confirmPayment(@Body request: PaymentRequest): Response<ResponseBody>


    // =========================================================================
    // 6. ADMIN SECTION (Khu vực Quản trị - Phân quyền, Duyệt đội, Sắp lịch, Sự kiện)
    // =========================================================================

    // --- Admin Players ---
    @GET("players")
    suspend fun getPlayersAdmin(@Query("name") name: String? = null): Response<AdminPlayerResponse>

    @POST("players")
    suspend fun createPlayerAdmin(@Body request: AdminPlayerRequest): Response<GenericAdminResponse>

    @PUT("players/{playerId}")
    suspend fun updatePlayerAdmin(
        @Path("playerId") playerId: Int,
        @Body request: AdminPlayerRequest
    ): Response<GenericAdminResponse>

    @PATCH("players/{userId}/lock")
    suspend fun lockAccount(@Path("userId") userId: Int): Response<GenericAdminResponse>

    @PATCH("players/{userId}/unlock")
    suspend fun unlockAccount(@Path("userId") userId: Int): Response<GenericAdminResponse>

    @POST("users/{userId}/roles")
    suspend fun assignRole(@Path("userId") userId: Int, @Body request: RoleRequest): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/approve")
    suspend fun approvePlayer(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/reject")
    suspend fun rejectPlayer(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/status/injured")
    suspend fun markInjured(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("team-players/{id}/starter")
    suspend fun updatePlayerStarterStatus(@Path("id") id: Int, @Body request: Map<String, Boolean>): Response<GenericAdminResponse>

    @DELETE("team-players/{id}")
    suspend fun removePlayerFromTeamAdmin(@Path("id") id: Int): Response<GenericAdminResponse>

    @PUT("team-players/{id}")
    suspend fun updateTeamPlayerInfo(@Path("id") id: Int, @Body request: Map<String, Any>): Response<GenericAdminResponse>


    // --- Admin Teams ---
    @GET("teams")
    suspend fun getTeamsAdmin(
        @Query("name") name: String? = null,
        @Query("coach_name") coachName: String? = null,
        @Query("is_active") isActive: Int? = null
    ): Response<AdminTeamResponse>

    @GET("teams/{id}")
    suspend fun getAdminTeamDetail(@Path("id") id: Int): Response<AdminTeamDetailResponse>

    @POST("teams")
    suspend fun createTeamAdmin(@Body request: CreateTeamRequestAdmin): Response<GenericAdminResponse>

    @PUT("teams/{id}")
    suspend fun updateTeamAdmin(@Path("id") id: Int, @Body request: CreateTeamRequestAdmin): Response<GenericAdminResponse>

    @PATCH("teams/{id}/approve")
    suspend fun approveTeam(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("teams/{id}/reject")
    suspend fun rejectTeam(@Path("id") id: Int): Response<GenericAdminResponse>

    @POST("teams/{teamId}/players/add")
    suspend fun addPlayerToTeamAdmin(@Path("teamId") teamId: Int, @Body request: AddPlayerByEmailRequestAdmin): Response<GenericAdminResponse>


    // --- Admin Matches & Events ---
    @GET("matchesadmin")
    suspend fun getMatchesAdmin(
        @Query("status") status: String? = null,
        @Query("season_id") seasonId: Int? = null,
        @Query("phase_id") phaseId: Int? = null,
        @Query("teamId") teamId: Int? = null
    ): Response<AdminMatchResponse>

    @GET("matches/{id}")
    suspend fun getMatchRawDetail(@Path("id") matchId: Int): Response<MatchDetailResponse>

    @POST("matchesadd")
    suspend fun createMatch(@Body request: CreateMatchRequest): Response<GenericAdminResponse>

    @PUT("matches/{id}")
    suspend fun updateMatch(@Path("id") id: Int, @Body request: UpdateMatchRequest): Response<GenericAdminResponse>

    @PATCH("matches/{id}/cancel")
    suspend fun cancelMatch(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("matches/{id}/start")
    suspend fun startMatch(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("matches/{id}/finish")
    suspend fun finishMatch(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("matches/{id}/reschedule")
    suspend fun rescheduleMatch(@Path("id") id: Int, @Body request: RescheduleMatchRequest): Response<GenericAdminResponse>

    @PATCH("admin/matches/{id}/featured")
    suspend fun updateFeaturedMatch(@Path("id") id: Int, @Body request: FeaturedMatchRequest): Response<FeaturedMatchResponse>

    @GET("admin/matches/featured")
    suspend fun getFeaturedMatches(@Query("limit") limit: Int? = 10, @Query("offset") offset: Int? = 0): Response<FeaturedMatchesListResponse>

    // Match Events (Goals, Cards, Subs, Score)
    @PUT("match-events/{matchId}/score")
    suspend fun updateMatchScore(@Path("matchId") matchId: Int, @Body request: UpdateScoreRequest): Response<GenericEventResponse>

    @POST("match-events/{matchId}/substitution")
    suspend fun addSubstitution(@Path("matchId") matchId: Int, @Body request: SubstitutionRequest): Response<GenericEventResponse>

    @POST("match-events/{matchId}/yellow-card")
    suspend fun addYellowCard(@Path("matchId") matchId: Int, @Body request: CardRequest): Response<GenericEventResponse>

    @POST("match-events/{matchId}/red-card")
    suspend fun addRedCard(@Path("matchId") matchId: Int, @Body request: CardRequest): Response<GenericEventResponse>

    @POST("match-events/{matchId}/goal")
    suspend fun addGoal(@Path("matchId") matchId: Int, @Body request: GoalRequest): Response<GenericEventResponse>

    @GET("match-events/{matchId}")
    suspend fun getMatchEvents(@Path("matchId") matchId: Int): Response<MatchEventResponse>

    @DELETE("match-events/{eventId}")
    suspend fun deleteMatchEvent(@Path("eventId") eventId: Int): Response<GenericEventResponse>


    // --- Admin Tournaments & Seasons ---
    @GET("tournaments")
    suspend fun getTournamentsAdmin(@Query("name") name: String? = null, @Query("is_active") isActive: Int? = null): Response<TournamentResponse>

    @GET("tournaments/{id}")
    suspend fun getTournamentDetailAdmin(@Path("id") id: Int): Response<TournamentDetailResponse>

    @POST("tournaments")
    suspend fun createTournament(@Body request: CreateTournamentRequest): Response<GenericAdminResponse>

    @PUT("tournaments/{id}")
    suspend fun updateTournament(@Path("id") id: Int, @Body request: TournamentUpdateRequest): Response<GenericAdminResponse>

    @POST("seasons")
    suspend fun createSeasonDirect(@Body request: CreateSeasonRequest): Response<GenericAdminResponse>

    @GET("seasons/{id}")
    suspend fun getSeasonDetails(@Path("id") seasonId: Int): Response<GenericResponseAdmin<SeasonDetailResponse>>

    @POST("seasons/{seasonId}/phases")
    suspend fun createSeasonPhase(@Path("seasonId") seasonId: Int, @Body request: CreatePhaseRequest): Response<GenericAdminResponse>

    @DELETE("phases/{id}")
    suspend fun deletePhase(@Path("id") phaseId: Int): Response<GenericAdminResponse>

    @POST("phases/{phaseId}/add-team")
    suspend fun addTeamToPhase(@Path("phaseId") phaseId: Int, @Body request: AddTeamRequestadmin): Response<AddTeamResponse>

    @DELETE("phases/{phaseId}/teams/{teamId}")
    suspend fun removeTeamFromPhase(@Path("phaseId") phaseId: Int, @Path("teamId") teamId: Int, @Query("season_id") seasonId: Int): Response<GenericAdminResponse>

    @PUT("tournaments/{tournamentId}/seasons/{seasonId}/rules")
    suspend fun updateSeasonRules(@Path("tournamentId") tournamentId: Int, @Path("seasonId") seasonId: Int, @Body request: SeasonRulesRequest): Response<GenericAdminResponse>

    @PATCH("tournaments/{tournamentId}/seasons/{seasonId}/open-registration")
    suspend fun openRegistration(@Path("tournamentId") tournamentId: Int, @Path("seasonId") seasonId: Int): Response<GenericAdminResponse>

    @PATCH("tournaments/{tournamentId}/seasons/{seasonId}/close-registration")
    suspend fun closeRegistration(@Path("tournamentId") tournamentId: Int, @Path("seasonId") seasonId: Int): Response<GenericAdminResponse>

    @POST("phases/{phaseId}/generate-schedule")
    suspend fun autoGenerateSchedule(@Path("phaseId") phaseId: Int, @Body options: ScheduleOptionsRequest = ScheduleOptionsRequest()): Response<BaseResponse>

    @POST("seasons/{seasonId}/auto-import-teams-and-schedule")
    suspend fun autoImportTeamsAndSchedule(@Path("seasonId") seasonId: Int, @Body options: ScheduleOptionsRequest): Response<BaseResponse>

    @POST("create_rules")
    suspend fun createRules(@Body request: CreateRuleRequest): Response<Void>

    @GET("seasons/{seasonId}/knockout-bracket")
    suspend fun getKnockoutBracket(@Path("seasonId") seasonId: Int): Response<BracketResponse>

    @PATCH("seasons/{seasonId}/teams/{teamId}/approve")
    suspend fun approveSeasonTeam(@Path("seasonId") seasonId: Int, @Path("teamId") teamId: Int): Response<GenericAdminResponse>

    @PATCH("seasons/{seasonId}/teams/{teamId}/reject")
    suspend fun rejectSeasonTeam(@Path("seasonId") seasonId: Int, @Path("teamId") teamId: Int): Response<GenericAdminResponse>

    @GET("venues")
    suspend fun getVenues(): Response<VenueResponse>
    @POST("api/phases/{phaseId}/preview-schedule")
    suspend fun previewSchedule(
        @Path("phaseId") phaseId: Int,
        @Body options: ScheduleOptionsRequest
    ): Response<PreviewMatchResponse> // 👈 Dùng class riêng thay vì ApiResponse<...>

    @POST("api/phases/{phaseId}/confirm-schedule")
    suspend fun confirmSchedule(
        @Path("phaseId") phaseId: Int,
        @Body request: ConfirmScheduleRequest
    ): Response<ApiResponse> // Không có <...>
    // --- Admin Notifications & Payments ---
    @GET("all_notifications")
    suspend fun getAllNotifications(): Response<NotificationResponseadmin>

    @POST("create_notification")
    suspend fun createNotification(@Body request: CreateNotificationRequest): Response<Unit>

    @PUT("update_notification/{id}")
    suspend fun updateNotification(@Path("id") id: Int, @Body request: UpdateNotificationRequest): Response<ResponseBody>

    @DELETE("delete_notification/{id}")
    suspend fun deleteNotification(@Path("id") id: Int): Response<Unit>

    @POST("notifications/cleanup_notifications")
    suspend fun cleanupNotifications(): Response<Unit>

    @GET("payments")
    suspend fun getPayments(@Query("status") status: String? = null): Response<PaymentResponse>

    @PATCH("payments/{id}/confirm")
    suspend fun confirmPaymentAdmin(@Path("id") id: Int): Response<GenericAdminResponse>

    @PATCH("payments/{id}/reject")
    suspend fun rejectPaymentAdmin(@Path("id") id: Int): Response<GenericAdminResponse>
}
