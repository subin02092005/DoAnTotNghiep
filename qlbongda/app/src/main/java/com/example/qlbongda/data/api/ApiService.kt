    package com.example.qlbongda.data.api

    import com.example.qlbongda.data.model.*
    import okhttp3.ResponseBody
    import retrofit2.Response
    import retrofit2.http.Body
    import retrofit2.http.POST
    import retrofit2.http.GET
    import retrofit2.http.PATCH
    import retrofit2.http.PUT
    import retrofit2.http.Path
    import retrofit2.http.Query
    import retrofit2.http.DELETE

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
        @POST("profile/update-info")
        suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<GeneralResponse>

        // API Đổi mật khẩu
        @POST("profile/change-password")
        suspend fun changePassword(@Body request: ChangePasswordRequest): Response<GeneralResponse>

        @GET("my_notifications")
        suspend fun getNotifications(
            @Query("userId") userId: Int,
            @Query("teamId") teamId: Int
        ): Response<NotificationResponse>
        @POST("mark_as_read/{id}")
        suspend fun markAsRead(@Path("id") id: Int): Response<Unit>
        // ---- ĐỘI BÓNG CỦA TÔI (Cá nhân) ----

        @GET("my_team")
        suspend fun getMyTeam(@Query("userId") userId: Int): Response<MyTeamResponse>
        @POST("add_player_by_email")
        suspend fun addPlayerByEmail(
            @Body request: AddPlayerRequest // Thay Map bằng Data Class này
        ): Response<AddPlayerResponse>
        @POST("update_player")
        suspend fun updatePlayer(@Body request: UpdatePlayerRequest): Response<ApiResponse>

        @POST("remove_player") // Tùy thuộc vào baseURL của bạn
        suspend fun removePlayer(@Body request: RemovePlayerRequest): Response<Void>
        @POST("register_team")
        suspend fun registerTeam(@Body request: RegisterTeamRequest): Response< RegisterTeamResponse>
        @POST("add_coach_by_email")
        suspend fun addCoachByEmail(@Body request: AddCoachRequest): Response<Void>
         // Đảm bảo khớp với route trong server
         @GET("open_seasons")
         suspend fun getOpenSeasons(@Query("teamId") teamId: Int): Response<List<SeasonInfo>>
        @POST("unregister_season") // Đảm bảo URL này khớp với route bên Node.js
        suspend fun unregisterFromSeason(
            @Body request: Map<String, String>
        ): Response<ApiResponse>
        @POST("register_to_season")
        suspend fun registerToSeason(@Body request: Map<String, Int>): Response<Void>
        // Giả sử ApiResponse của bạn là:
        @POST("confirm_payment")
        suspend fun confirmPayment(@Body request: PaymentRequest): Response<ResponseBody>

        // ---- QUẢN LÝ THÀNH VIÊN TRONG ĐỘI ----
        // Dùng để thêm cầu thủ vào đội
        @POST("teams/{teamId}/players")
        suspend fun addPlayerToTeam(
            @Path("teamId") teamId: Int,
            @Body player: PlayerInfo
        ): Response<GenericResponse>

        // Dùng để xóa cầu thủ khỏi đội
        @PATCH("teams/{teamId}/players/{playerId}/remove")
        suspend fun removePlayerFromTeam(
            @Path("teamId") teamId: Int,
            @Path("playerId") playerId: Int
        ): Response<GenericResponse>

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

        // ---- MATCH EVENTS ----
        @PUT("match-events/{matchId}/score")
        suspend fun updateMatchScore(
            @Path("matchId") matchId: Int,
            @Body request: UpdateScoreRequest
        ): Response<GenericEventResponse>

        @POST("match-events/{matchId}/substitution")
        suspend fun addSubstitution(
            @Path("matchId") matchId: Int,
            @Body request: SubstitutionRequest
        ): Response<GenericEventResponse>

        @POST("match-events/{matchId}/yellow-card")
        suspend fun addYellowCard(
            @Path("matchId") matchId: Int,
            @Body request: CardRequest
        ): Response<GenericEventResponse>

        @POST("match-events/{matchId}/red-card")
        suspend fun addRedCard(
            @Path("matchId") matchId: Int,
            @Body request: CardRequest
        ): Response<GenericEventResponse>

        @GET("match-events/{matchId}")
        suspend fun getMatchEvents(
            @Path("matchId") matchId: Int
        ): Response<MatchEventResponse>

        @DELETE("match-events/{eventId}")
        suspend fun deleteMatchEvent(
            @Path("eventId") eventId: Int
        ): Response<GenericEventResponse>
    }
