package com.example.qlbongda.services

import com.example.qlbongda.data.model.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationRepository {
    // Dùng List<NotificationItem> để khớp với UI của bạn
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Hàm cập nhật
    fun updateNotifications(newList: List<NotificationItem>) {
        _notifications.value = newList
    }
}