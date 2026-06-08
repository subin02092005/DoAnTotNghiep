package com.example.qlbongda.data.api

import com.example.qlbongda.data.model.LoginRequest
import com.example.qlbongda.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.qlbongda.data.model.*
interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): retrofit2.Response<RegisterResponse>

    @POST("api/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    // 🌟 SỬA LẠI: Bọc thêm Response<> vào bên ngoài
    @POST("api/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<GenericResponse>

}