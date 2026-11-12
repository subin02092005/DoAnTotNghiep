package com.example.qlbongda.viewmodel

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.api.ApiService
import com.example.qlbongda.data.model.*
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

        val allMatchesList = MutableStateFlow<List<FullMatchDetail>>(emptyList())

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading
        private val _selectedSeason = MutableStateFlow<SeasonWithPhases?>(null)

        // 2. Biến công khai để MainActivity lắng nghe
        val selectedSeason = _selectedSeason.asStateFlow()
        init {
            // Tự động load dữ liệu khi khởi tạo ViewModel
            //loadTournamentPhases(seasonId = 1)
            loadMatches()
            loadFeaturedMatches()
         //  loadStandings(seasonId = 24)
            loadGlobalRules()
            loadSeasons()
        }

        fun selectSeason(season: SeasonWithPhases) {
            if (_selectedSeason.value?.id != season.id) {
                _selectedSeason.value = season
                season.id?.let { seasonId ->
                    // Tải bảng xếp hạng của giải này
                    loadStandings(seasonId)

                    // 🌟 Lọc danh sách trận đấu từ danh sách gốc đã lưu sẵn trong máy
                    // Giải nào không có trận thì matchList nhận danh sách rỗng để UI báo "Chưa có lịch",
                    // nhưng danh sách gốc `allMatchesList` vẫn còn nguyên để khi bấm sang giải khác vẫn hiện bình thường!
                    matchList.value = allMatchesList.value.filter { it.seasonId == seasonId }
                }
            }
        }
        private val _selectedPhase = MutableStateFlow<TournamentPhase?>(null)
        val selectedPhase: StateFlow<TournamentPhase?> = _selectedPhase.asStateFlow()

        // Hàm cập nhật khi người dùng bấm chọn vòng đấu mới
        fun selectPhase(phase: TournamentPhase?) {
            _selectedPhase.value = phase
            phase?.let {
                // Thực hiện logic load API standings hoặc xử lý logic theo phase id của bạn tại đây
                selectPhase(it.id)
            }
        }
        // --- CÁC HÀM GỌI API ---

        private var lastSuccessfullyLoadedPhasesId: Int? = null

        // 2. Cập nhật lại hàm loadTournamentPhases
        fun loadTournamentPhases(seasonId: Int) {
            // CHỐT CHẶN: Nếu ID này vừa được gọi thành công rồi thì KHÔNG GỌI API NỮA
            if (lastSuccessfullyLoadedPhasesId == seasonId) return

            // Đánh dấu là đang/đã xử lý ID này
            lastSuccessfullyLoadedPhasesId = seasonId

            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val response = apiService.getSeasonPhases(seasonId)
                    if (response.isSuccessful && response.body()?.status == "success") {
                        _phases.value = response.body()?.data?.phases ?: emptyList()
                    } else {
                        setMockData()
                        // Mở khóa nếu gọi API thất bại để cho phép thử lại sau
                        lastSuccessfullyLoadedPhasesId = null
                    }
                } catch (e: Exception) {
                    setMockData()
                    // Mở khóa nếu có lỗi mạng/văng app
                    lastSuccessfullyLoadedPhasesId = null
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

                        val mappedMatches = listDto.map { dto ->
                            FullMatchDetail(
                                id = dto.id,
                                seasonId = dto.seasonId ?: 0,
                                teamA = dto.teamA,
                                teamB = dto.teamB,
                                teamAId = dto.home_team_id ?: 0, // 🌟 Map đúng ID đội nhà ở đây
                                teamBId = dto.away_team_id ?: 0, // 🌟 Map đúng ID đội khách ở đây
                                status = dto.status,
                                scoreA = dto.home_score ?: 0,
                                scoreB = dto.away_score ?: 0,
                                time = dto.scheduled_at ?: "",
                                date = dto.scheduled_at ?: "",
                                stadium = "Chưa cập nhật",
                                isStarted = dto.isStarted,
                                events = emptyList(), lineupA = emptyList(), lineupB = emptyList(),
                                subsA = emptyList(), subsB = emptyList(), PossessionA = "0%",
                                PossessionB = "0%", ShotsA = "0", ShotsB = "0", mvp = "",
                                isHot = dto.isHot
                            )
                        }

                        // 🌟 1. Lưu vào danh sách gốc an toàn
                        allMatchesList.value = mappedMatches // Lưu danh sách gốc
                        matchList.value = mappedMatches

                        // 🌟 2. Lọc hiển thị cho mùa giải đang chọn (nếu có)
                        val currentSeasonId = _selectedSeason.value?.id
                        if (currentSeasonId != null) {
                            matchList.value = mappedMatches.filter { it.seasonId == currentSeasonId }
                        } else {
                            matchList.value = mappedMatches
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Trong HomeViewModel.kt
        private val _tournamentRules = MutableStateFlow<TournamentRules?>(null)
        val tournamentRules: StateFlow<TournamentRules?> = _tournamentRules.asStateFlow()

        // Hàm gọi API lấy luật chung
        fun loadGlobalRules() {
            viewModelScope.launch {
                try {
                    val response = apiService.getGlobalRules()
                    if (response.isSuccessful && response.body()?.status == "success") {
                        _tournamentRules.value = response.body()?.data
                    } else {
                        Log.e("HomeViewModel", "Không lấy được luật: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Lỗi Exception: ${e.message}")
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
                                    seasonId = dto.seasonId ?: 0,
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

        // 1. Thêm StateFlow quản lý trạng thái loading


        private val _selectedMatchDetail = MutableStateFlow<FullMatchDetail?>(null)
        val selectedMatchDetail: StateFlow<FullMatchDetail?> = _selectedMatchDetail

        fun setSelectedMatch(match: FullMatchDetail) {
            _selectedMatchDetail.value = match
        }

        fun fetchMatchDetail(matchId: Int) {
            viewModelScope.launch {
                _isLoading.value = true // Bật trạng thái loading trước khi gọi API
                try {
                    val response = apiService.getMatchDetail(matchId)
                    Log.d("API_CHECK_FULL", "Raw Body: ${response.body()}")
                    if (response.isSuccessful) {
                        _selectedMatchDetail.value = response.body()?.data
                    }
                    Log.d("API_CHECK", "Data nhận được: ${response.body()?.data}")
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false // LUÔN TẮT loading dù thành công hay thất bại
                }
            }
        }

        private val _teamDetail = MutableStateFlow<TeamDetailData?>(null)
        val teamDetail = _teamDetail.asStateFlow()
        fun fetchTeamDetail(teamId: Int) {
            Log.d("TeamViewModel", "Đang gọi API chi tiết với teamId: $teamId")

            // 🌟 1. THÊM DÒNG NÀY: Reset dữ liệu cũ về null ngay lập tức
            // Giúp màn hình chi tiết không bị hiện dính thông tin của đội vừa xem trước đó
            _teamDetail.value = null

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

        private var lastLoadedStandingSeasonId: Int? = null

        fun loadStandings(seasonId: Int?) {
            if (seasonId == null) return
            // 🌟 TỐI ƯU: Nếu đang load đúng mùa này rồi thì không load lại nữa
            if (lastLoadedStandingSeasonId == seasonId && allStandings.value.isNotEmpty()) return
            
            lastLoadedStandingSeasonId = seasonId
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val response = apiService.getDetailedStandings(seasonId)
                    if (response.isSuccessful) {
                        val data = response.body()?.data ?: emptyList()
                        allStandings.value = data
                        // Cập nhật hiển thị dựa trên phase hiện tại nếu có
                        standingList.value = if (_selectedPhaseId.value != null && _selectedPhaseId.value != -1) {
                            data.filter { it.phaseId == _selectedPhaseId.value }
                        } else {
                            data
                        }
                    } else {
                        // Nếu lỗi thì cho phép load lại lần sau
                        lastLoadedStandingSeasonId = null
                    }
                } catch (e: Exception) { 
                    e.printStackTrace() 
                    lastLoadedStandingSeasonId = null
                }
                finally { _isLoading.value = false }
            }
        }
        // Danh sách cầu thủ StateFlow để UI (Jetpack Compose) quan sát và hiển thị
        private val _players = MutableStateFlow<List<PlayerInfo>>(emptyList())
        val players: StateFlow<List<PlayerInfo>> = _players

        // Hàm gọi API đồng bộ dữ liệu cầu thủ và thẻ phạt
        // Hàm gọi API đồng bộ dữ liệu cầu thủ và thẻ phạt
        fun fetchTeamDataAndCardStatus(teamId: Int, seasonId: Int) {
            viewModelScope.launch {
                try {
                    // 1. Gọi API lấy chi tiết đội (bao gồm danh sách cầu thủ)
                    val teamResponse = apiService.getTeamDetail(teamId)

                    if (teamResponse.isSuccessful) {
                        val originalPlayers = teamResponse.body()?.data?.players ?: emptyList()

                        // 2. Gọi API lấy trạng thái thẻ phạt / treo giò
                        val cardStatusResponse = apiService.getPlayerCardStatus(teamId, seasonId)

                        if (cardStatusResponse.isSuccessful) {
                            val cardStatusList = cardStatusResponse.body()?.data ?: emptyList()

                            // 3. Gộp (merge) thông tin thẻ phạt vào PlayerInfo
                            val updatedPlayers = originalPlayers.map { player ->
                                val cardInfo = cardStatusList.find { it.playerId == player.id }

                                player.copy(
                                    yellowCards = cardInfo?.yellowCards ?: 0,
                                    redCards = cardInfo?.redCards ?: 0,
                                    isSuspended = cardInfo?.isSuspended == 1 // Chuyển từ Int (0/1) sang Boolean
                                )
                            }

                            // 4. Cập nhật lại danh sách mới vào StateFlow để UI hiển thị
                            _players.value = updatedPlayers
                        } else {
                            // Trường hợp API thẻ phạt lỗi -> Vẫn hiển thị danh sách cầu thủ ban đầu
                            _players.value = originalPlayers
                        }
                    } else {
                        Log.e("HomeViewModel", "Lỗi lấy chi tiết đội: ${teamResponse.code()}")
                    }

                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Lỗi fetchTeamDataAndCardStatus: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        // Hàm phụ trợ tính số thẻ phát sinh trong trận đấu dựa trên events của trận đó
        fun getPlayerCardsInThisMatch(player: PlayerInfo, matchEvents: List<MatchEvent>): Pair<Int, Int> {
            var yellowCount = 0
            var redCount = 0

            matchEvents.forEach { event ->
                // Kiểm tra xem tên cầu thủ trong sự kiện có khớp với cầu thủ hiện tại không
                if (event.playerName.equals(player.name, ignoreCase = true)) {
                    val typeLower = event.type.lowercase()
                    if (typeLower.contains("vàng") || typeLower.contains("yellow")) {
                        yellowCount++
                    } else if (typeLower.contains("đỏ") || typeLower.contains("red")) {
                        redCount++
                    }
                }
            }
            return Pair(yellowCount, redCount)
        }
//        val teamPlayers = MutableStateFlow<List<PlayerInfo>>(emptyList()) // Danh sách cầu thủ của đội
//        // Gọi API lấy chi tiết đội (đã có từ trước)
//        fun loadTeamDetails(teamId: Int) {
//            viewModelScope.launch {
//                try {
//                    val response = apiService.getTeamDetail(teamId)
//                    if (response.isSuccessful) {
//                        teamPlayers.value = response.body()?.data?.players ?: emptyList()
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        fun addPlayer(teamId: Int, player: PlayerInfo) {
//            viewModelScope.launch {
//                try {
//                    val response = apiService.addPlayerToTeam(teamId, player)
//                    if (response.isSuccessful) {
//                        loadTeamDetails(teamId) // Cập nhật lại list sau khi thêm
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }

//        // Hàm xóa cầu thủ
//        fun removePlayer(teamId: Int, playerId: Int) {
//            viewModelScope.launch {
//                try {
//                    val response = apiService.removePlayerFromTeam(teamId, playerId)
//                    if (response.isSuccessful) {
//                        loadTeamDetails(teamId) // Cập nhật lại list sau khi xóa
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }





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
            // Chỉ cập nhật hiển thị, không gọi API, không làm loạn dữ liệu gốc
            standingList.value = if (phaseId == -1) {
                allStandings.value
            } else {
                allStandings.value.filter { it.phaseId == phaseId }
            }
        }
        private val _seasonsWithPhases = MutableStateFlow<List<SeasonWithPhases>>(emptyList())
        val seasonsWithPhases = _seasonsWithPhases.asStateFlow()

        // 2. Hàm gọi dữ liệu từ Backend

        fun loadSeasons() {
            viewModelScope.launch {
                try {
                    val response = apiService.getAllData()
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val data = response.body()?.data ?: emptyList()
                        _seasonsWithPhases.value = data

                        // Chỉ set mùa giải mặc định nếu hiện tại đang là null (chưa chọn gì)
                        if (data.isNotEmpty() && _selectedSeason.value == null) {
                            _selectedSeason.value = data.first()
                            // XÓA DÒNG loadStandings(firstSeason.id) Ở ĐÂY
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Lỗi load dữ liệu: ${e.message}")
                }
            }
        }
    }

