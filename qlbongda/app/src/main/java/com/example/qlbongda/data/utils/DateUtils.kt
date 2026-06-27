package com.example.qlbongda.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    // Chỉ cần gọi 1 lần ở đây, dùng cho cả dự án
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    fun formatTime(isoDate: String): String = try {
        ZonedDateTime.parse(isoDate).format(timeFormatter)
    } catch (e: Exception) { "..." }

    fun formatDate(isoDate: String): String = try {
        ZonedDateTime.parse(isoDate).format(dateFormatter)
    } catch (e: Exception) { "..." }
}