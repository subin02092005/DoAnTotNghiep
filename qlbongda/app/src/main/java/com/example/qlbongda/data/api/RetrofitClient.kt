package com.example.qlbongda.data.api

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 10.0.2.2 là IP trỏ về localhost của máy tính khi dùng máy ảo Android Studio
    // Nếu chạy bằng máy thật, hãy đổi thành IP mạng Wifi cục bộ của bạn (Ví dụ: "http://192.168.1.5:3000/")
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private var retrofit: Retrofit? = null


    fun getClient(context: Context): ApiService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}