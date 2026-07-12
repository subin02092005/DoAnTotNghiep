package com.example.qlbongda

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.api.ApiService
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.*
import com.example.qlbongda.services.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.String

class AdminViewModel(private val apiService: ApiService) : ViewModel() {

    private val _players = MutableStateFlow<List<AdminPlayerItem>>(emptyList())
    val players: StateFlow<List<AdminPlayerItem>> = _players

    private val _teams = MutableStateFlow<List<AdminTeamItem>>(emptyList())
    val teams: StateFlow<List<AdminTeamItem>> = _teams

    private val _matches = MutableStateFlow<List<AdminMatchItem>>(emptyList())
    val matches: StateFlow<List<AdminMatchItem>> = _matches

    private val _publicMatches = MutableStateFlow<List<MatchDto>>(emptyList())
    val publicMatches: StateFlow<List<MatchDto>> = _publicMatches

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications = _notifications.asStateFlow()

    val generalNotifications = notifications.map { list ->
        list.filter { it.target_team_id == null && it.recipient_user_id == null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val teamNotifications = notifications.map { list ->
        list.filter { it.target_team_id != null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val personalNotifications = notifications.map { list ->
        list.filter { it.recipient_user_id != null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllNotifications()

                if (response.isSuccessful) {
                    // 1. Lấy dữ liệu thô từ API (List<Notification>)
                    val apiList = response.body()?.data ?: emptyList()

                    // 2. Chuyển đổi sang kiểu dữ liệu mà UI cần (List<NotificationItem>)
                    // Đây chính là chỗ tạo ra biến uiData mà bạn đang thiếu
                    val uiData = apiList.map { item ->
                        NotificationItem(
                            id = item.id,
                            title = item.title,
                            content = item.content,
                            type = "general",            // Gán giá trị trực tiếp
                            source = "manual",           // Gán giá trị trực tiếp
                            target_team_id = item.target_team_id ?.toString()?.toIntOrNull(), // Dùng toán tử ?: để tránh null
                            recipient_user_id = item.recipient_user_id?.toString()?.toIntOrNull(), // Nếu null thì gán 0

                            is_active = if (item.is_active == 1) 1 else 0,// Nếu null thì gán true
                            created_at = item.created_at ?: "" // Nếu null thì gán chuỗi rỗng
                        )
                    }


                    // 3. Cập nhật vào Repository
                    NotificationRepository.updateNotifications(uiData)

                    // Lưu ý: Nếu bạn dùng Repo thì không cần dùng _notifications.value nữa
                } else {
                    _message.value = "Lỗi: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Chi tiết lỗi: ", e)
                _message.value = "Lỗi: ${e.localizedMessage}"
            }
        }
    }
    fun createNotification(
        title: String,
        content: String,
        type: String,
        target_team_id: Int?,
        recipient_user_id: Int?
    ) {
        viewModelScope.launch {
            try {
                val request = CreateNotificationRequest(
                    title = title,
                    content = content,
                    type = type,
                    target_team_id = target_team_id,
                    recipient_user_id = recipient_user_id,
                    source = "admin_panel"
                )
                val response = apiService.createNotification(request)
                if (response.isSuccessful) {
                    _message.value = "Đã gửi thông báo!"
                    fetchNotifications()
                } else {
                    _message.value = "Lỗi: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối server"
            }
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteNotification(id)
                if (response.isSuccessful) {
                    fetchNotifications()
                    _message.value = "Xóa thành công"
                } else {
                    _message.value = "Lỗi ${response.code()}: Không thể xóa"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối server"
            }
        }
    }

    fun updateNotification(id: Int, title: String, content: String, type: String, isActive: Int) {
        viewModelScope.launch {
            try {
                val requestBody = UpdateNotificationRequest(title, content, type, isActive)
                val response = apiService.updateNotification(id, requestBody)
                if (response.isSuccessful) {
                    _message.value = "Cập nhật thành công!"
                    fetchNotifications()
                } else {
                    _message.value = "Cập nhật thất bại"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối server"
            }
        }
    }

    fun triggerCleanupNotifications() {
        viewModelScope.launch {
            try {
                val response = apiService.cleanupNotifications()
                if (response.isSuccessful) {
                    _message.value = "Đã dọn dẹp các thông báo cũ!"
                    fetchNotifications()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi dọn dẹp hệ thống"
            }
        }
    }

    private val _selectedNotification = MutableStateFlow<NotificationItem?>(null)
    val selectedNotification = _selectedNotification.asStateFlow()

    fun setSelectedNotification(notification: NotificationItem?) {
        _selectedNotification.value = notification
    }

    private val _showCleanupDialog = MutableStateFlow(false)
    val showCleanupDialog = _showCleanupDialog.asStateFlow()
    fun setShowCleanupDialog(show: Boolean) {
        _showCleanupDialog.value = show
    }


    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog = _showAddDialog.asStateFlow()
    fun setShowAddDialog(show: Boolean) {
        _showAddDialog.value = show
    }

    fun fetchPlayers(name: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getPlayersAdmin(name)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _players.value = body.data ?: emptyList()
                } else {
                    _message.value = body?.message ?: "Lỗi tải: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi hệ thống: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun lockAccount(userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.lockAccount(userId)
                if (response.isSuccessful) {
                    _message.value = response.body()?.message ?: "Đã khóa tài khoản thành công"
                    fetchPlayers()
                } else {
                    _message.value = "Lỗi: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun unlockAccount(userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.unlockAccount(userId)
                if (response.isSuccessful) {
                    _message.value = "Đã mở khóa tài khoản thành công"
                    fetchPlayers()
                } else {
                    _message.value = "Lỗi ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun approvePlayer(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.approvePlayer(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã duyệt cầu thủ vào đội"
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi duyệt"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun rejectPlayer(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.rejectPlayer(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã từ chối yêu cầu"
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi từ chối"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun markInjured(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.markInjured(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã cập nhật trạng thái chấn thương"
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi cập nhật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun fetchTeams(name: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTeamsAdmin(name = name)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _teams.value = body.data ?: emptyList()
                } else {
                    _message.value = body?.message ?: "Mã lỗi: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun approveTeam(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.approveTeam(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã duyệt đội bóng thành công"
                    fetchTeams()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi duyệt"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun rejectTeam(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.rejectTeam(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã từ chối đăng ký đội bóng"
                    fetchTeams()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi từ chối"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun fetchMatches() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getMatchesAdmin()
                if (response.isSuccessful && response.body() != null) {
                    _matches.value = response.body()?.data ?: emptyList()
                }
                val publicResponse = apiService.getMatches()
                if (publicResponse.isSuccessful) {
                    _publicMatches.value = publicResponse.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMatchStatus(id: Int, status: String) {
        viewModelScope.launch {
            try {
                val response = apiService.updateMatch(id, UpdateMatchRequest(status = status))
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Cập nhật trạng thái thành công"
                    fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi cập nhật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun createMatch(request: CreateMatchRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.createMatch(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Tạo trận đấu thành công"
                    fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi tạo trận đấu"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelMatch(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.cancelMatch(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã hủy trận đấu"
                    fetchMatchDetail(id)
                    fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi hủy"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startMatch(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.startMatch(id)
                if (response.isSuccessful) {
                    _message.value = "Trận đấu đã bắt đầu!"
                    fetchMatchDetail(id)
                    fetchMatches()
                } else {
                    _message.value = "Lỗi ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun finishMatch(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Tìm thông tin trận đấu hiện tại để lấy tỉ số và ID 2 đội
                val match = _matches.value.find { it.id == id }
                if (match == null) {
                    _message.value = "Không tìm thấy thông tin trận đấu"
                    return@launch
                }

                val hScore = match.homeScore ?: 0
                val aScore = match.awayScore ?: 0

                // 2. Xác định Winner ID (null nếu hòa)
                val winnerId = when {
                    hScore > aScore -> match.homeTeamId
                    aScore > hScore -> match.awayTeamId
                    else -> null // Hòa
                }

                // 3. Gọi API cập nhật trạng thái kết thúc kèm theo Winner ID
                // Chúng ta sử dụng hàm updateMatch chung để đảm bảo winner_team_id được gửi đi
                val updateReq = UpdateMatchRequest(
                    status = "finished",
                    homeScore = hScore,
                    awayScore = aScore,
                    winnerTeamId = winnerId
                )

                val response = apiService.updateMatch(id, updateReq)

                if (response.isSuccessful) {
                    _message.value = "Đã kết thúc trận đấu và xác nhận đội thắng!"
                    fetchMatches()
                    fetchMatchDetail(id)

                    // Làm mới BXH
                    val seasonId = match.seasonId ?: currentSeasonId
                    if (seasonId != -1) {
                        fetchStandings(seasonId)
                    }
                } else {
                    _message.value = "Lỗi cập nhật: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun rescheduleMatch(id: Int, scheduledAt: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.rescheduleMatch(
                    id,
                    RescheduleMatchRequest(scheduledAt = scheduledAt)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã dời lịch thi đấu"
                    fetchMatchDetail(id)
                    fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi dời lịch"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _featuredMatches = MutableStateFlow<List<AdminMatchItem>>(emptyList())
    val featuredMatches: StateFlow<List<AdminMatchItem>> = _featuredMatches

    fun fetchFeaturedMatches(limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getFeaturedMatches(limit, offset)
                if (response.isSuccessful && response.body() != null) {
                    _featuredMatches.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _matchDetail = MutableStateFlow<FullMatchDetail?>(null)
    val matchDetail: StateFlow<FullMatchDetail?> = _matchDetail

    fun fetchMatchDetail(matchId: Int) {
        // 1. XÓA DỮ LIỆU CŨ NGAY LẬP TỨC để tránh hiện tỉ số cũ (0-3)
        _matchDetail.value = null
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rawResponse = apiService.getMatchRawDetail(matchId)
                val rawMatch = rawResponse.body()?.data
                if (rawMatch != null) {
                    // Nếu đang đá, ưu tiên lấy từ raw match
                    if (rawMatch.status == "ongoing" || rawMatch.status == "live") {
                        _matchDetail.value = rawMatch
                    } else {
                        // Nếu chưa đá hoặc đã xong, lấy chi tiết đầy đủ
                        val detailResponse = apiService.getMatchDetail(matchId)
                        val body = detailResponse.body()
                        if (detailResponse.isSuccessful && body != null) {
                            // Chấp nhận cả success == true hoặc status == "success"
                            if (body.success == true || body.status == "success") {
                                _matchDetail.value = body.data
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _message.value = "Lỗi tải chi tiết: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFeatured(id: Int, isFeatured: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.updateFeaturedMatch(id, FeaturedMatchRequest(isFeatured))
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = response.body()?.message
                    fetchMatches()
                    fetchFeaturedMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi cập nhật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _matchEvents = MutableStateFlow<List<MatchEventDetailed>>(emptyList())
    val matchEvents: StateFlow<List<MatchEventDetailed>> = _matchEvents

    fun fetchMatchEvents(matchId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getMatchEvents(matchId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _matchEvents.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi tải sự kiện: ${e.message}"
            }
        }
    }

    fun updateScore(matchId: Int, homeScore: Int, awayScore: Int, status: String = "ongoing") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Xác định winnerId nếu kết thúc trận đấu
                var winnerId: Int? = null
                if (status == "finished") {
                    val match = _matches.value.find { it.id == matchId }
                    if (match != null) {
                        winnerId = when {
                            homeScore > awayScore -> match.homeTeamId
                            awayScore > homeScore -> match.awayTeamId
                            else -> null
                        }
                    }
                }

                // Gửi yêu cầu cập nhật bao gồm winner_team_id
                val response = apiService.updateMatch(
                    matchId, UpdateMatchRequest(
                        status = status,
                        homeScore = homeScore,
                        awayScore = awayScore,
                        winnerTeamId = winnerId
                    )
                )

                if (response.isSuccessful) {
                    _message.value = "Cập nhật tỉ số và đội thắng thành công"
                    fetchMatchEvents(matchId); fetchMatchDetail(matchId); fetchMatches()

                    if (status == "finished") {
                        val currentMatch = _matches.value.find { it.id == matchId }
                        val seasonId = currentMatch?.seasonId ?: currentSeasonId
                        if (seasonId != -1) {
                            fetchStandings(seasonId)
                        }
                    }
                } else {
                    _message.value = "Lỗi cập nhật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addSubstitution(
        matchId: Int,
        teamId: Int,
        playerInId: Int,
        playerOutId: Int,
        minute: Int,
        period: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.addSubstitution(
                    matchId,
                    SubstitutionRequest(teamId, playerInId, playerOutId, minute, period)
                )
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Thay người thành công"
                    fetchMatchEvents(matchId); fetchMatchDetail(matchId); fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi thay người"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCard(
        matchId: Int,
        teamId: Int,
        playerId: Int,
        minute: Int,
        period: String,
        isRed: Boolean,
        note: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = CardRequest(teamId, playerId, minute, period, note)
                val response = if (isRed) apiService.addRedCard(
                    matchId,
                    request
                ) else apiService.addYellowCard(matchId, request)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã thêm thẻ"; fetchMatchEvents(matchId); fetchMatchDetail(
                        matchId
                    ); fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi thêm thẻ"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvent(matchId: Int, eventId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.deleteMatchEvent(eventId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Xóa sự kiện thành công"
                    fetchMatchEvents(matchId); fetchMatchDetail(matchId); fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi xóa sự kiện"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _tournaments = MutableStateFlow<List<TournamentItem>>(emptyList())
    val tournaments: StateFlow<List<TournamentItem>> = _tournaments

    private val _tournamentDetail = MutableStateFlow<TournamentDetailData?>(null)
    val tournamentDetail: StateFlow<TournamentDetailData?> = _tournamentDetail

    fun fetchTournamentDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTournamentDetailAdmin(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _tournamentDetail.value = response.body()?.data
                }
            } catch (e: Exception) {
                _message.value = "Lỗi tải chi tiết: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchTournaments(name: String? = null, isActive: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTournamentsAdmin(name, isActive)
                if (response.isSuccessful && response.body()?.success == true) {
                    _tournaments.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi tải giải đấu: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _currentUserId = MutableStateFlow<Int?>(null)
    fun createTournament(name: String, description: String?, maxPlayers: Int, minPlayers: Int) {
        viewModelScope.launch {
            try {
                val request = CreateTournamentRequest(
                    name = name,
                    description = description,
                    logo = "",
                    maxPlayers = maxPlayers,
                    minPlayers = minPlayers,
                    userId = _currentUserId.value
                )
                val response = apiService.createTournament(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Tạo giải đấu thành công"; fetchTournaments()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi tạo giải đấu"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun createSeason(tournamentId: Int, request: CreateSeasonRequest) {
        viewModelScope.launch {
            try {
                val response =
                    apiService.createSeasonDirect(request.copy(tournamentId = tournamentId))
                if (response.isSuccessful) {
                    _message.value = "Tạo mùa giải thành công"; fetchTournamentDetail(tournamentId)
                } else {
                    _message.value = "Lỗi từ Server: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun toggleRegistration(tournamentId: Int, seasonId: Int, open: Boolean) {
        viewModelScope.launch {
            try {
                val response = if (open) apiService.openRegistration(
                    tournamentId,
                    seasonId
                ) else apiService.closeRegistration(tournamentId, seasonId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = if (open) "Đã mở cổng đăng ký" else "Đã đóng cổng đăng ký"
                    fetchTournamentDetail(tournamentId)
                } else {
                    _message.value = response.body()?.message ?: "Lỗi cập nhật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun updateTournamentStatus(id: Int, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                val response = apiService.updateTournament(
                    id,
                    TournamentUpdateRequest(is_active = if (isChecked) 1 else 0)
                )
                if (response.isSuccessful) {
                    _message.value = "Đã cập nhật trạng thái"; fetchTournaments()
                } else {
                    _message.value = "Cập nhật thất bại"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun updateMessage(msg: String) {
        _message.value = msg
    }

    fun updateSeasonRules(tournamentId: Int, seasonId: Int, rules: SeasonRulesRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.updateSeasonRules(tournamentId, seasonId, rules)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Cập nhật luật thi đấu thành công"
                } else {
                    _message.value = response.body()?.message ?: "Lỗi cập nhật luật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun createPhase(seasonId: Int, request: CreatePhaseRequest) {
        viewModelScope.launch {
            try {
                val response = apiService.createSeasonPhase(seasonId, request)
                val body = response.body()
                
                if (response.isSuccessful && body != null) {
                    if (body.success) {
                        _message.value = "Tạo vòng đấu thành công"
                        fetchSeasonDetails(seasonId)
                    } else {
                        _message.value = "Lỗi: ${body.message}"
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Mã lỗi: ${response.code()}"
                    _message.value = "Lỗi tạo vòng đấu: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun deletePhase(seasonId: Int, phaseId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.deletePhase(phaseId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã xóa vòng đấu thành công"
                    fetchSeasonDetails(seasonId)
                } else {
                    val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Lỗi server"
                    _message.value = "Xóa thất bại: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun autoGenerateSchedule(
        phaseId: Int,
        options: ScheduleOptionsRequest = ScheduleOptionsRequest()
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.autoGenerateSchedule(phaseId, options)
                if (response.isSuccessful && (response.body()?.success == true || response.body()?.status == "success")) {
                    _message.value = "Đã xếp lịch tự động thành công!"
                    _seasonDetail.value?.season?.id?.let { fetchSeasonDetails(it) }
                    fetchMatches() // Cập nhật lại danh sách trận đấu tổng thể
                } else {
                    _message.value = response.body()?.message ?: "Lỗi xếp lịch tự động"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _phasesOfSeason = MutableStateFlow<List<TournamentPhase>>(emptyList())
    val phasesOfSeason: StateFlow<List<TournamentPhase>> = _phasesOfSeason

    fun fetchSeasonPhasesForAdmin(seasonId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getSeasonPhases(seasonId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _phasesOfSeason.value = response.body()?.data?.phases ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _seasonDetail = MutableStateFlow<SeasonDetailResponse?>(null)
    val seasonDetail = _seasonDetail.asStateFlow()

    private val _standingList = MutableStateFlow<List<GroupStanding>>(emptyList())
    val standingList: StateFlow<List<GroupStanding>> = _standingList.asStateFlow()

    private var currentSeasonId: Int = -1

    fun fetchStandings(seasonId: Int) {
        _standingList.value = emptyList() // Xóa dữ liệu cũ trước khi tải mới
        viewModelScope.launch {
            try {
                val response = apiService.getDetailedStandings(seasonId)
                if (response.isSuccessful) {
                    _standingList.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("AdminViewModel", "Error fetching standings", e)
            }
        }
    }

    fun advanceTopTeamsToPhase(sourcePhaseId: Int, targetPhaseId: Int, seasonId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Lấy BXH của vòng đấu nguồn
                val standingResponse = apiService.getDetailedStandings(seasonId)
                if (!standingResponse.isSuccessful) {
                    _message.value = "Không thể lấy bảng xếp hạng"
                    return@launch
                }

                val allStandings = standingResponse.body()?.data ?: emptyList()
                val phaseStandings = allStandings.filter { it.phaseId == sourcePhaseId }

                if (phaseStandings.isEmpty()) {
                    _message.value = "Không tìm thấy dữ liệu BXH cho vòng này"
                    return@launch
                }

                // 2. Lấy top 2 mỗi bảng và thêm vào vòng tiếp theo
                var successCount = 0
                var totalAdvancing = 0

                phaseStandings.forEach { group ->
                    val topTwo = group.standings.take(2)
                    totalAdvancing += topTwo.size
                    topTwo.forEach { team ->
                        // Gọi API add team vào phase Knockout
                        val addResponse = apiService.addTeamToPhase(
                            targetPhaseId,
                            AddTeamRequestadmin(teamId = team.id)
                        )
                        if (addResponse.isSuccessful && addResponse.body()?.success == true) {
                            successCount++
                        }
                    }
                }

                val targetPhaseName = if (totalAdvancing == 8) "Tứ Kết" else if (totalAdvancing == 4) "Bán Kết" else "vòng tiếp theo"
                _message.value = "Đã chuyển thành công $successCount đội vào $targetPhaseName!"

                // 3. Sau khi chuyển xong, Admin có thể nhấn "Auto Xếp Lịch" ở vòng Knockout 
                // để hệ thống tự động random cặp đấu (bao gồm cả logic 3 đội).
                fetchSeasonDetails(seasonId)
            } catch (e: Exception) {
                _message.value = "Lỗi: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchSeasonDetails(seasonId: Int) {
        this.currentSeasonId = seasonId
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getSeasonDetails(seasonId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _seasonDetail.value = response.body()?.data
                } else {
                    _message.value = "Lỗi: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTeamGroup(phaseId: Int, teamId: Int, newGroupId: Int?) {
        viewModelScope.launch {
            try {
                val response = apiService.addTeamToPhase(
                    phaseId,
                    AddTeamRequestadmin(teamId = teamId, groupId = newGroupId)
                )
                if (response.isSuccessful) {
                    _message.value = "Cập nhật bảng đấu thành công!"; fetchSeasonDetails(
                        currentSeasonId
                    )
                } else {
                    _message.value = "Lỗi ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Kết nối thất bại: ${e.message}"
            }
        }
    }

    fun addTeamToPhase(phaseId: Int, teamItem: TeamItem, groupId: Int?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Gửi teamItem.team_id (Global ID) để khớp với logic Backend mới
                val request = AddTeamRequestadmin(
                    teamId = teamItem.team_id,
                    groupId = groupId
                )
                val response = apiService.addTeamToPhase(phaseId, request)
                val body = response.body()

                if (response.isSuccessful && body?.success == true) {
                    _message.value = "Thêm đội thành công!"
                } else {
                    val errorMsg = body?.message ?: response.errorBody()?.string() ?: "Lỗi server"
                    _message.value = "Thất bại: $errorMsg"
                }
                fetchSeasonDetails(currentSeasonId)
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeTeamFromPhase(phaseId: Int, teamId: Int, seasonId: Int) {
        // Cập nhật local state tức thì
        val currentDetail = _seasonDetail.value
        if (currentDetail != null) {
            val updatedTeams = currentDetail.teams.map { 
                if (it.team_id == teamId) it.copy(groupId = null) else it 
            }
            _seasonDetail.value = currentDetail.copy(teams = updatedTeams)
        }

        viewModelScope.launch {
            try {
                val response = apiService.removeTeamFromPhase(phaseId, teamId, seasonId)
                val body = response.body()

                if (response.isSuccessful && body?.success == true) {
                    _message.value = "Đã xóa đội khỏi vòng đấu"
                    fetchSeasonDetails(currentSeasonId)
                } else {
                    val errorMsg = body?.message ?: response.errorBody()?.string() ?: "Lỗi không xác định"
                    _message.value = "Thất bại: $errorMsg"
                    fetchSeasonDetails(currentSeasonId)
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
                fetchSeasonDetails(currentSeasonId)
            }
        }
    }

    fun assignTeamToGroup(phaseId: Int, globalTeamId: Int, groupId: Int?) {
        // Cập nhật local state tức thì để giao diện mượt mà (Optimistic Update)
        val currentDetail = _seasonDetail.value
        if (currentDetail != null) {
            val updatedTeams = currentDetail.teams.map { 
                if (it.team_id == globalTeamId) it.copy(groupId = groupId) else it 
            }
            _seasonDetail.value = currentDetail.copy(teams = updatedTeams)
        }

        viewModelScope.launch {
            try {
                val request = AddTeamRequestadmin(teamId = globalTeamId, groupId = groupId)
                val response = apiService.addTeamToPhase(phaseId, request)
                val body = response.body()

                if (response.isSuccessful && body?.success == true) {
                    _message.value = "Di chuyển đội thành công!"
                    fetchSeasonDetails(currentSeasonId) // Đồng bộ lại với Server
                } else {
                    val errorMsg = body?.message ?: response.errorBody()?.string() ?: "Lỗi server"
                    _message.value = "Thất bại: $errorMsg"
                    fetchSeasonDetails(currentSeasonId) // Rollback nếu lỗi
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
                fetchSeasonDetails(currentSeasonId) // Rollback
            }
        }
    }

    private val _showAddRuleDialog = MutableStateFlow(false)
    val showAddRuleDialog = _showAddRuleDialog.asStateFlow()

    fun setShowAddRuleDialog(show: Boolean) {
        _showAddRuleDialog.value = show
    }

    fun createRules(request: CreateRuleRequest) {
        Log.d("DEBUG_REQUEST", "Đang gửi: $request")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Gọi trực tiếp apiService, không cần context
                val response = apiService.createRules(request)

                if (response.isSuccessful) {
                    _message.value = "Đã áp dụng luật mới cho giải đấu!"
                } else {
                    _message.value = "Lỗi từ server: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }

    }

    private val _bracketData = MutableStateFlow<List<PhaseBracket>>(emptyList())
    val bracketData: StateFlow<List<PhaseBracket>> = _bracketData.asStateFlow()

    fun fetchKnockoutBracket(seasonId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getKnockoutBracket(seasonId)
                if (response.isSuccessful) {
                    _bracketData.value = response.body()?.data ?: emptyList()
                } else {
                    _message.value = "Lỗi tải nhánh đấu: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

