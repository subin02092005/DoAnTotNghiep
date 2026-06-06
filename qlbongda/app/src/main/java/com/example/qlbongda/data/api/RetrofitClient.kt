package com.example.qlbongda.data.api

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:7001/" // 10.0.2.2 là localhost của máy tính khi dùng giả lập Android

    private var retrofit: Retrofit? = null

    fun getClient(context: Context): AuthApiService {
        if (retrofit == null) {
            // Tự động lưu và gửi Cookie (Hứng refresh_token từ Server C#)
            val cookieJar = object : CookieJar {
                private val cookieStore = HashMap<String, List<Cookie>>()

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore[url.host] = cookies
                    // Bạn có thể lưu thêm vào SharedPreferences nếu muốn tắt app đi bật lại không mất login
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookieStore[url.host] ?: ArrayList()
                }
            }

            val okHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(AuthApiService::class.java)
    }
}