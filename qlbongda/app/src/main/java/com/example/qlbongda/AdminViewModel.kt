package com.example.qlbongda

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.api.ApiService
import com.example.qlbongda.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val apiService: ApiService) : ViewModel() {

    private val _players = MutableStateFlow<List<AdminPlayerItem>>(emptyList())
    val players: StateFlow<List<AdminPlayerItem>> = _players

    private val _teams = MutableStateFlow<List<AdminTeamItem>>(emptyList())
    val teams: StateFlow<List<AdminTeamItem>> = _teams

    private val _matches = MutableStateFlow<List<AdminMatchItem>>(emptyList())
    val matches: StateFlow<List<AdminMatchItem>> = _matches

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // Trong AdminViewModel
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications = _notifications.asStateFlow()
    // Cách dùng StateFlow để luôn phản ánh dữ liệu mới nhất
    val generalNotifications = notifications.map { list ->
        list.filter { it.target_team_id == null && it.recipient_user_id == null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val teamNotifications = notifications.map { list ->
        list.filter { it.target_team_id != null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 3. Thông báo cá nhân
    val personalNotifications = notifications.map { list ->
        list.filter { it.recipient_user_id != null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllNotifications()
                if (response.isSuccessful) {
                    val result = response.body()
                    // Lấy list từ biến "data" của result
                    val notificationList = result?.data ?: emptyList()
                    _notifications.value = notificationList
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
                // Tạo đối tượng request với đầy đủ dữ liệu
                val request = CreateNotificationRequest(
                    title = title,
                    content = content,
                    type = type,
                    target_team_id = target_team_id,
                    recipient_user_id = recipient_user_id,
                    source = "admin_panel" // Giá trị mặc định
                )
                Log.d("API_DEBUG", "Request gửi đi: $request")
                val response = apiService.createNotification(request)
                if (response.isSuccessful) {
                    _message.value = "Đã gửi thông báo!"
                    fetchNotifications() // Tải lại danh sách sau khi thêm thành công
                } else {
                    val errorMsg = response.errorBody()?.string()
                    Log.e("API_DEBUG", "Server trả về lỗi: $errorMsg")
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
                    _message.value="Xóa thành công"
                } else {
                    // IN RA LỖI TỪ SERVER
                    val errorMsg = response.errorBody()?.string()
                    Log.e("DELETE_DEBUG", "Server trả về lỗi code ${response.code()}: $errorMsg")
                    _message.value = "Lỗi ${response.code()}: Không thể xóa"
                }
            } catch (e: Exception) {
                Log.e("DELETE_DEBUG", "Lỗi kết nối: ${e.message}")
                _message.value = "Lỗi kết nối server"
            }
        }
    }
    fun updateNotification(id: Int, title: String, content: String, type: String, isActive: Int) {
        viewModelScope.launch {
            try {
                // Chuẩn bị dữ liệu theo yêu cầu API PUT
                val body = mapOf(
                    "title" to title,
                    "content" to content,
                    "type" to type,
                    "is_active" to isActive
                )

                val requestBody = UpdateNotificationRequest(title, content, type, isActive)
                val response = apiService.updateNotification(id, requestBody)
                if (response.isSuccessful) {
                    _message.value = "Cập nhật thành công!"
                    fetchNotifications() // Tải lại danh sách để cập nhật UI
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
                // Giả định bạn đã khai báo @POST("notifications/cleanup_notifications") trong ApiService
                val response = apiService.cleanupNotifications()
                if (response.isSuccessful) {
                    _message.value = "Đã dọn dẹp các thông báo cũ!"
                    fetchNotifications() // Tải lại để thấy danh sách đã ẩn bớt
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
                if (response.isSuccessful && body != null && (body.success == true || body.status == "success")) {
                    _players.value = body.data ?: emptyList()
                } else {
                    _message.value = body?.message ?: "Lỗi tải: ${response.code()}"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi hệ thống: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun lockAccount(userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.lockAccount(userId)
                val body = response.body()
                if (response.isSuccessful) {
                    _message.value = body?.message ?: "Đã khóa tài khoản thành công"
                    fetchPlayers() // Làm mới danh sách
                } else {
                    val errorBody = response.errorBody()?.string()
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
                    _message.value = "Đã mở khóa tài khoản $userId thành công"
                    fetchPlayers() 
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi ${response.code()}: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối mở khóa: ${e.localizedMessage}"
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

    // ---- TEAM MANAGEMENT ----
    fun fetchTeams(name: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTeamsAdmin(name = name)
                val body = response.body()
                
                // Kiểm tra linh hoạt cả success (boolean) hoặc status (string)
                val isSuccessful = response.isSuccessful && body != null && 
                    (body.success == true || body.status == "success" || body.status == "ok")

                if (isSuccessful && body != null) {
                    _teams.value = body.data ?: emptyList()
                } else {
                    val errorMsg = body?.message ?: "Mã lỗi: ${response.code()}"
                    _message.value = "Không thể tải danh sách: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
                e.printStackTrace()
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
                    fetchTeams() // Refresh
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
                    fetchTeams() // Refresh
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi từ chối"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    // ---- MATCH MANAGEMENT ----
    fun fetchMatches() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getMatchesAdmin()
                val body = response.body()
                if (response.isSuccessful && body != null && (body.success == true || body.status == "success")) {
                    _matches.value = body.data ?: emptyList()
                } else {
                    _message.value = body?.message ?: "Lỗi tải lịch thi đấu"
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
                    _message.value = "Cập nhật trạng thái trận đấu thành công"
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
            try {
                val response = apiService.cancelMatch(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã hủy trận đấu"
                    fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi hủy"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun startMatch(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.startMatch(id)
                if (response.isSuccessful) {
                    _message.value = "Trận đấu đã bắt đầu!"
                    fetchMatches()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi ${response.code()}: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun finishMatch(id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.finishMatch(id)
                if (response.isSuccessful) {
                    _message.value = "Trận đấu đã kết thúc!"
                    fetchMatches()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi ${response.code()}: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun rescheduleMatch(id: Int, scheduledAt: String) {
        viewModelScope.launch {
            try {
                val response = apiService.rescheduleMatch(id, RescheduleMatchRequest(scheduledAt = scheduledAt))
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã dời lịch thi đấu"
                    fetchMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi dời lịch"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    // ---- FEATURED MATCHES ----
    private val _featuredMatches = MutableStateFlow<List<AdminMatchItem>>(emptyList())
    val featuredMatches: StateFlow<List<AdminMatchItem>> = _featuredMatches

    fun fetchFeaturedMatches(limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getFeaturedMatches(limit, offset)
                val body = response.body()
                if (response.isSuccessful && body != null && (body.success == true || body.status == "success")) {
                    _featuredMatches.value = body.data ?: emptyList()
                } else {
                    _message.value = body?.message ?: "Lỗi tải danh sách nổi bật"
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
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getMatchDetail(matchId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    // Chấp nhận cả status=="success" hoặc success==true
                    if (body.status == "success" || body.success == true) {
                        _matchDetail.value = body.data
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
            try {
                val response = apiService.updateFeaturedMatch(id, FeaturedMatchRequest(isFeatured))
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = response.body()?.message
                    // Cập nhật lại danh sách matches và featuredMatches
                    fetchMatches()
                    fetchFeaturedMatches()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi cập nhật"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    // ---- MATCH EVENTS ACTIONS ----

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
            try {
                val response = apiService.updateMatchScore(matchId, UpdateScoreRequest(homeScore, awayScore, status))
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    _message.value = "Cập nhật tỉ số thành công"
                    fetchMatchDetail(matchId) // Refresh UI chi tiết để thấy tỉ số mới
                    fetchMatches() // Refresh list ngoài
                } else {
                    _message.value = body?.message ?: "Lỗi cập nhật tỉ số"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun addSubstitution(matchId: Int, teamId: Int, playerInId: Int, playerOutId: Int, minute: Int, period: String) {
        viewModelScope.launch {
            try {
                val response = apiService.addSubstitution(matchId, SubstitutionRequest(teamId, playerInId, playerOutId, minute, period))
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    _message.value = "Thay người thành công"
                    fetchMatchEvents(matchId)
                    fetchMatchDetail(matchId) // Refresh để cập nhật lineup/subs nếu cần
                } else {
                    _message.value = body?.message ?: "Lỗi thay người"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun addCard(matchId: Int, teamId: Int, playerId: Int, minute: Int, period: String, isRed: Boolean, note: String? = null) {
        viewModelScope.launch {
            try {
                val request = CardRequest(teamId, playerId, minute, period, note)
                val response = if (isRed) {
                    apiService.addRedCard(matchId, request)
                } else {
                    apiService.addYellowCard(matchId, request)
                }
                
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    _message.value = "Đã thêm thẻ ${if (isRed) "đỏ" else "vàng"}"
                    fetchMatchEvents(matchId)
                } else {
                    _message.value = body?.message ?: "Lỗi thêm thẻ"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun deleteEvent(matchId: Int, eventId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteMatchEvent(eventId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Xóa sự kiện thành công"
                    fetchMatchEvents(matchId)
                } else {
                    _message.value = response.body()?.message ?: "Lỗi xóa sự kiện"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    // ---- TOURNAMENT MANAGEMENT ACTIONS ----

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
                _message.value = "Lỗi tải chi tiết giải đấu: ${e.message}"
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
    fun createTournament(
        name: String,
        description: String?,
        maxPlayers: Int, // Thêm tham số
        minPlayers: Int ,
       // userId: Int? // Thêm tham số
    ) {val userId = _currentUserId.value
        viewModelScope.launch {
            try {
                // Truyền giá trị vào request
                val request = CreateTournamentRequest(
                    name = name,
                    description = description,
                    logo = "",
                    maxPlayers = maxPlayers,
                    minPlayers = minPlayers,
                    userId = userId
                )

                val response = apiService.createTournament(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Tạo giải đấu thành công"
                    fetchTournaments()
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
                // Đảm bảo tournamentId được gán vào body
                val finalRequest = request.copy(tournamentId = tournamentId)
                val response = apiService.createSeasonDirect(finalRequest)
                
                if (response.isSuccessful) {
                    _message.value = "Tạo mùa giải thành công"
                    fetchTournamentDetail(tournamentId) // Cập nhật lại danh sách
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi từ Server: ${response.code()} $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }

    fun toggleRegistration(tournamentId: Int, seasonId: Int, open: Boolean) {
        viewModelScope.launch {
            try {
                val response = if (open) {
                    apiService.openRegistration(tournamentId, seasonId)
                } else {
                    apiService.closeRegistration(tournamentId, seasonId)
                }
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = if (open) "Đã mở cổng đăng ký" else "Đã đóng cổng đăng ký"
                    fetchTournamentDetail(tournamentId)
                } else {
                    _message.value = response.body()?.message ?: "Lỗi cập nhật trạng thái đăng ký"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun updateTournamentStatus(id: Int, isActive: Boolean) {
        viewModelScope.launch {
            try {
                // Sử dụng PATCH/PUT để cập nhật trạng thái
                val response = apiService.updateTournament(id, mapOf("is_active" to if (isActive) 1 else 0))
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã cập nhật trạng thái giải đấu"
                    fetchTournaments()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
    }

    fun updateTournament(id: Int, name: String?, description: String?, maxTeams: Int?) {
        viewModelScope.launch {
            try {
                val body = mutableMapOf<String, Any?>()
                if (name != null) body["name"] = name
                if (description != null) body["description"] = description
                if (maxTeams != null) body["max_teams"] = maxTeams

                val response = apiService.updateTournament(id, body)
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Cập nhật giải đấu thành công"
                    fetchTournaments()
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
            }
        }
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
                if (response.isSuccessful) {
                    _message.value = "Tạo vòng đấu thành công"
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi tạo vòng đấu: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            }
        }
    }
    fun autoGenerateSchedule(phaseId: Int, options: ScheduleOptionsRequest = ScheduleOptionsRequest()) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.autoGenerateSchedule(phaseId, options)
                if (response.isSuccessful) {
                    _message.value = "Tự động import đội và xếp lịch thành công!"
                    fetchMatches()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi: $errorMsg"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun autoImportTeamsAndSchedule(seasonId: Int, options: ScheduleOptionsRequest = ScheduleOptionsRequest()) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.autoImportTeamsAndSchedule(seasonId, options)
                if (response.isSuccessful) {
                    _message.value = "Đã tự động thêm đội và xếp lịch mùa giải thành công!"
                    fetchMatches()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: ""
                    _message.value = "Lỗi xử lý: $errorMsg"
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

}
