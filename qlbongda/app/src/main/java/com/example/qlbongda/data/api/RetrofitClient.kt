package com.example.qlbongda.data.api

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.TournamentPhase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// =================================================================
// CHỨC NĂNG 1: RETROFIT CLIENT (ĐƯỜNG ỐNG KẾT NỐI SERVER)
// =================================================================
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private var retrofit: Retrofit? = null

    fun getClient(context: Context): ApiService {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}
