package com.example.qlbongda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qlbongda.data.api.ApiService
import com.example.qlbongda.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã khóa tài khoản thành công"
                    fetchPlayers() // Refresh list
                } else {
                    _message.value = response.body()?.message ?: "Lỗi khi khóa tài khoản"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
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
                if (response.isSuccessful && response.body()?.status == "success") {
                    _matchDetail.value = response.body()?.data
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
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Cập nhật tỉ số thành công"
                    fetchMatches() // Refresh list để thấy tỉ số mới
                } else {
                    _message.value = response.body()?.message ?: "Lỗi cập nhật tỉ số"
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
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Thay người thành công"
                    fetchMatchEvents(matchId)
                } else {
                    _message.value = response.body()?.message ?: "Lỗi thay người"
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
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _message.value = "Đã thêm thẻ ${if (isRed) "đỏ" else "vàng"}"
                    fetchMatchEvents(matchId)
                } else {
                    _message.value = response.body()?.message ?: "Lỗi thêm thẻ"
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
}
