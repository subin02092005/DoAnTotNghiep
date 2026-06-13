package com.example.qlbongda.data.api

import com.example.qlbongda.data.model.LoginRequest
import com.example.qlbongda.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.qlbongda.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): retrofit2.Response<RegisterResponse>
//quenmk
   @POST("api/verify-email")
   suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    // 🌟 SỬA LẠI: Bọc thêm Response<> vào bên ngoài
    @POST("api/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<GenericResponse>
//profile
    @GET("api/profile/get-info")
        suspend fun getProfileInfo(@Query("email") email: String): Response<ProfileResponse>

        @POST("api/profile/verify-otp")
        suspend fun verifyOtpProfile(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

}