package com.example.qlbongda.data.api

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.model.TournamentPhase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// =================================================================
// CHỨC NĂNG 1: RETROFIT CLIENT (ĐƯỜNG ỐNG KẾT NỐI SERVER)
// =================================================================
object RetrofitClient {
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

// =================================================================
// CHỨC NĂNG 2: HOME VIEWMODEL (BỘ NÃO QUẢN LÝ DỮ LIỆU MÀN HÌNH HOME)
// =================================================================
class HomeViewModel(private val apiService: ApiService) : ViewModel() {

    // Nơi lưu trữ danh sách vòng đấu đổ về từ API công khai ra cho Compose nhận diện
    private val _phases = MutableStateFlow<List<TournamentPhase>>(emptyList())
    val phases: StateFlow<List<TournamentPhase>> = _phases

    // Trạng thái hiển thị vòng xoay Loading trong lúc đợi API trả kết quả
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Mặc định khi mở màn hình Home lên sẽ tải dữ liệu vòng đấu của mùa giải số 1
        loadTournamentPhases(seasonId = 1)
    }

    fun loadTournamentPhases(seasonId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Gọi API lấy thông tin vòng đấu từ Node.js (BASE_URL ở trên)
                val response = apiService.getSeasonPhases(seasonId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _phases.value = response.body()?.data?.phases ?: emptyList()
                } else {
                    // Nếu lỗi API, nạp dữ liệu mẫu dự phòng tránh sập app
                    setMockData()
                }
            } catch (e: Exception) {
                // Đề phòng lỗi mất mạng, sai IP, nghẽn server
                setMockData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setMockData() {
        _phases.value = listOf(
            TournamentPhase(1, "Vòng Bảng", "round_robin"),
            TournamentPhase(2, "Tứ Kết", "knockout"),
            TournamentPhase(3, "Bán Kết", "knockout"),
            TournamentPhase(4, "Chung Kết", "knockout")
        )
    }
}