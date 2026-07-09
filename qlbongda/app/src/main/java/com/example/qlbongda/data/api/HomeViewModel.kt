package com.example.qlbongda.data.api

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.model.ChangePasswordRequest
import com.example.qlbongda.data.model.FullMatchDetail
import com.example.qlbongda.data.model.GroupStanding
import com.example.qlbongda.data.model.MyTeamData
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.data.model.SeasonWithPhases
import com.example.qlbongda.data.model.StandingItem
import com.example.qlbongda.data.model.TeamDetailData
import com.example.qlbongda.data.model.TournamentPhase
import com.example.qlbongda.data.model.UpdateProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class HomeViewModel(private val apiService: ApiService) : ViewModel() {

    // --- CÁC STATEFLOW DỮ LIỆU ---
    private val _phases = MutableStateFlow<List<TournamentPhase>>(emptyList())
    val phases: StateFlow<List<TournamentPhase>> = _phases

    val matchList = MutableStateFlow<List<FullMatchDetail>>(emptyList())
    val hotMatchList = MutableStateFlow<List<FullMatchDetail>>(emptyList())
    val standingList = MutableStateFlow<List<GroupStanding>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Tự động load dữ liệu khi khởi tạo ViewModel
        loadTournamentPhases(seasonId = 1)
        loadMatches()
        loadFeaturedMatches()
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
                    matchList.value = listDto.map { dto ->android.util.Log.d("DEBUG_HOT", "ID: ${dto.id}, isFeatured: ${dto.isHot}")
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
                            isHot = dto.isHot
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadFeaturedMatches() {
        viewModelScope.launch {
            try {
                val response = apiService.getFeaturedMatches(limit = 5)

                if (response.isSuccessful) {
                    val listDto = response.body()?.data

                    if (listDto != null) {
                        hotMatchList.value = listDto.map { dto ->
                            FullMatchDetail(
                                id = dto.id,
                                teamA = dto.homeTeamName ?: "TBD", // Tránh lỗi null nếu tên đội trống
                                teamB = dto.awayTeamName ?: "TBD",
                                status = dto.status ?: "scheduled",
                                scoreA = dto.homeScore ?: 0,
                                scoreB = dto.awayScore ?: 0,
                                time = dto.scheduledAt ?: "",
                                date = dto.scheduledAt ?: "",
                                stadium = "Chưa cập nhật",
                                isStarted = dto.status != "scheduled",
                                events = emptyList(),
                                lineupA = emptyList(),
                                lineupB = emptyList(),
                                subsA = emptyList(),
                                subsB = emptyList(),
                                PossessionA = "0%",
                                PossessionB = "0%",
                                ShotsA = "0",
                                ShotsB = "0",
                                mvp = "",
                                isHot = true
                            )
                        }
                        Log.d("API_SUCCESS", "Đã tải ${listDto.size} trận đấu hot")
                    } else {
                        Log.e("API_ERROR", "Body data bị null")
                    }
                } else {
                    Log.e("API_ERROR", "Response không thành công: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_CRASH", "Lỗi mapping dữ liệu: ${e.message}")
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
    private val _teamDetail = MutableStateFlow<TeamDetailData?>(null)
    val teamDetail = _teamDetail.asStateFlow()
    fun fetchTeamDetail(teamId: Int) {
        Log.d("TeamViewModel", "Đang gọi API chi tiết với teamId: $teamId")

        // Kiểm tra nhanh: Nếu ID bằng 0, không gọi API nữa để tránh lỗi server
        if (teamId <= 0) {
            Log.e("TeamViewModel", "teamId không hợp lệ: $teamId")
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTeamDetail(teamId)
                if (response.isSuccessful && response.body() != null) {
                    _teamDetail.value = response.body()?.data
                    Log.d("API_DEBUG", "Dữ liệu body: ${response.body()}")
                    Log.d("TeamViewModel", "Lấy dữ liệu thành công cho teamId: $teamId")
                } else {
                    Log.e("TeamViewModel", "API trả về lỗi: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_DEBUG", "Lỗi API: ${e.message}")
                Log.e("TeamViewModel", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
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
    val teamPlayers = MutableStateFlow<List<PlayerInfo>>(emptyList()) // Danh sách cầu thủ của đội
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
            TournamentPhase(1, "Vòng Bảng", "round_robin",""),
            TournamentPhase(2, "Tứ Kết", "knockout",""),
            TournamentPhase(3, "Bán Kết", "knockout",""),
            TournamentPhase(4, "Chung Kết", "knockout","")
        )
    }

    val allStandings = MutableStateFlow<List<GroupStanding>>(emptyList())

    // 2. ID của phase đang được chọn (mặc định null hoặc chọn cái đầu tiên)
    private val _selectedPhaseId = MutableStateFlow<Int?>(null)
    val selectedPhaseId = _selectedPhaseId.asStateFlow()

    // 3. Logic lọc tự động (Reactive)
    val filteredStandings = derivedStateOf {
        val currentId = _selectedPhaseId.value
        if (currentId == null) {
            allStandings.value
        } else {
            allStandings.value.filter { it.phaseId == currentId }
        }
    }

    fun selectPhase(phaseId: Int) {
        _selectedPhaseId.value = phaseId
    }
    private val _seasonsWithPhases = MutableStateFlow<List<SeasonWithPhases>>(emptyList())
    val seasonsWithPhases = _seasonsWithPhases.asStateFlow()

    // 2. Hàm gọi dữ liệu từ Backend
    fun loadSeasons() {
        viewModelScope.launch {
            try {
                // Giả sử bạn có repository để gọi API
                val response = apiService.getAllData()
                if (response.body()?.status == "success") {
                    val myData = response.body()?.data

                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Lỗi load dữ liệu: ${e.message}")
            }
        }
    }
}

