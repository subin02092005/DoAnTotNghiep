package com.example.qlbongda.data.api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.data.model.MyTeamData
import com.example.qlbongda.data.model.PlayerInfo
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
                            status = dto.status, // <--- THÊM DÒNG NÀY VÀO ĐỂ TRUYỀN DỮ LIỆU
                            scoreA = dto.home_score ?: 0,
                            scoreB = dto.away_score ?: 0,
                            time = dto.scheduled_at ?: "",
                            date = dto.scheduled_at ?: "",
                            stadium = "Chưa cập nhật",
                            isStarted = dto.isStarted,
                            // Các trường mặc định để hiển thị UI

                            events = emptyList(), lineupA = emptyList(), lineupB = emptyList(),
                            subsA = emptyList(), subsB = emptyList(), PossessionA = "0%",
                            PossessionB = "0%", ShotsA = "0", ShotsB = "0", mvp = "",
                            isHot = true
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private val _selectedMatchDetail = MutableStateFlow<FullMatchDetail?>(null)
    val selectedMatchDetail: StateFlow<FullMatchDetail?> = _selectedMatchDetail
    fun fetchMatchDetail(matchId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getMatchDetail(matchId)
                Log.d("API_CHECK_FULL", "Raw Body: ${response.body()}")
                if (response.isSuccessful) {
                    _selectedMatchDetail.value = response.body()?.data
                }
                Log.d("API_CHECK", "Data nhận được: ${response.body()?.data}")
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
///////////////////////

    val teamPlayers = MutableStateFlow<List<PlayerInfo>>(emptyList()) // Danh sách cầu thủ của đội



    // Sửa lại hàm fetchMyTeam
    // Khai báo state để UI biết có đội hay chưa


    private val _myTeam = MutableStateFlow<MyTeamData?>(null)
    val myTeam: StateFlow<MyTeamData?> = _myTeam
    private val _hasTeam = MutableStateFlow<Boolean?>(null)
    val hasTeam: StateFlow<Boolean?> = _hasTeam


    fun fetchMyTeam(userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getMyTeam(userId) // Gọi API bạn đã gửi
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.hasTeam == true) {
                        _myTeam.value = body.data
                        _hasTeam.value = true // Đã có đội -> UI sẽ tự chuyển sang màn hình quản lý
                        body.data?.teamId?.let { loadTeamDetails(it) }
                    } else {
                        _hasTeam.value = false // Chưa có đội
                    }
                }
            } catch (e: Exception) {
                _hasTeam.value = false
            }
        }
    }


    // Gọi API lấy chi tiết đội (đã có từ trước)
    fun loadTeamDetails(teamId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getTeamDetail(teamId)
                if (response.isSuccessful) {
                    teamPlayers.value = response.body()?.data?.players ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun addPlayer(teamId: Int, player: PlayerInfo) {
        viewModelScope.launch {
            try {
                val response = apiService.addPlayerToTeam(teamId, player)
                if (response.isSuccessful) {
                    loadTeamDetails(teamId) // Cập nhật lại list sau khi thêm
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Hàm xóa cầu thủ
    fun removePlayer(teamId: Int, playerId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.removePlayerFromTeam(teamId, playerId)
                if (response.isSuccessful) {
                    loadTeamDetails(teamId) // Cập nhật lại list sau khi xóa
                }
            } catch (e: Exception) {
                e.printStackTrace()
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