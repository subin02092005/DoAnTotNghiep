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
                if (response.isSuccessful && response.body()?.success == true) {
                    _players.value = response.body()?.data ?: emptyList()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi tải danh sách cầu thủ"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
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
                if (response.isSuccessful && response.body()?.success == true) {
                    _teams.value = response.body()?.data ?: emptyList()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi tải danh sách đội bóng"
                }
            } catch (e: Exception) {
                _message.value = "Lỗi kết nối: ${e.message}"
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
                if (response.isSuccessful && response.body()?.success == true) {
                    _matches.value = response.body()?.data ?: emptyList()
                } else {
                    _message.value = response.body()?.message ?: "Lỗi tải lịch thi đấu"
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
}
