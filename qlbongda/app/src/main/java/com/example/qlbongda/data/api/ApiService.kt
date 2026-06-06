package com.example.qlbongda.data.api
import com.example.qlbongda.data.model.AuthLoginRequest
import com.example.qlbongda.data.model.AuthLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/v1/auth/login")
    fun login(@Body request: AuthLoginRequest): Call<AuthLoginResponse>
}