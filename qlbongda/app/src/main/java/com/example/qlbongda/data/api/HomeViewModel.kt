package com.example.qlbongda.data.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.data.model.TournamentPhase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class HomeViewModel(private val apiService: ApiService) : ViewModel() {

    // --- CÁC STATEFLOW DỮ LIỆU ---
    private val _phases = MutableStateFlow<List<TournamentPhase>>(emptyList())
    val phases: StateFlow<List<TournamentPhase>> = _phases

    val matchList = MutableStateFlow<List<FullMatchDetail>>(emptyList())
    val standingList = MutableStateFlow<List<GroupStanding>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Tự động load dữ liệu khi khởi tạo ViewModel
        loadTournamentPhases(seasonId = 1)
        loadMatches()
        loadStandings()
    }

    // --- CÁC HÀM GỌI API ---

    fun loadTournamentPhases(seasonId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getSeasonPhases(seasonId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _phases.value = response.body()?.data?.phases ?: emptyList()
                } else {
                    setMockData()
                }
            } catch (e: Exception) {
                setMockData()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadMatches() {
        viewModelScope.launch {
            try {
                val response = apiService.getMatches()
                if (response.isSuccessful) {
                    val listDto = response.body()?.data ?: emptyList()

                    // Map từ DTO sang Model hiển thị
                    matchList.value = listDto.map { dto ->
                        FullMatchDetail(
                            id = dto.id,
                            teamA = dto.teamA, // Lấy từ MatchDto
                            teamB = dto.teamB, // Lấy từ MatchDto
                            scoreA = dto.home_score?.toString() ?: "0",
                            scoreB = dto.away_score?.toString() ?: "0",
                            time = dto.match_date,
                            date = dto.match_date,
                            stadium = dto.stadium ?: "",
                            // Các trường mặc định để hiển thị UI
                            isStarted = false,
                            events = emptyList(), lineupA = emptyList(), lineupB = emptyList(),
                            subsA = emptyList(), subsB = emptyList(), PossessionA = "0%",
                            PossessionB = "0%", ShotsA = "0", ShotsB = "0", mvp = ""
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun loadStandings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Đảm bảo ApiService đã có hàm getDetailedStandings()
                val response = apiService.getDetailedStandings()
                if (response.isSuccessful) {
                    standingList.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
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