package com.example.qlbongda.utils

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {
    // Chỉ cần gọi 1 lần ở đây, dùng cho cả dự án
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    private val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    fun formatTime(isoDate: String): String = try {
        ZonedDateTime.parse(isoDate).format(timeFormatter)
    } catch (e: Exception) {
        "..."
    }

    fun formatDate(isoDate: String?): String {
        if (isoDate.isNullOrEmpty() || isoDate == "null") return "Chưa cập nhật"

        return try {
            // Nhánh 1: Nếu Server trả về "1996-01-27" (Dạng YYYY-MM-DD)
            if (isoDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = java.time.LocalDate.parse(isoDate, formatter)
                return date.format(dateFormatter) // dateFormatter của bạn là dd/MM/yyyy
            }

            // Nhánh 2: Nếu là ISO chuẩn (có chữ T)
            if (isoDate.contains("T")) {
                ZonedDateTime.parse(isoDate).format(dateFormatter)
            } else {
                // Nhánh 3: Định dạng Database cũ (nếu còn sót)
                val cleanDate = if (isoDate.length >= 23) isoDate.substring(0, 23) else isoDate
                val dateTime = java.time.LocalDateTime.parse(
                    cleanDate.replace(" ", "T"),
                    java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
                dateTime.format(dateFormatter)
            }
        } catch (e: Exception) {
            android.util.Log.e("DateUtils", "Lỗi format ngày: $isoDate", e)
            "Sai định dạng"
        }
    }
    fun calculateMinutes(actualStartTime: String?): String {
        if (actualStartTime == null) return ""

        return try {
            // 1. Parse thời gian bắt đầu thực tế (Backend trả về ISO_DATE_TIME)
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            // Nếu chuỗi có chứa ký tự lạ hoặc format khác, hãy dùng ZonedDateTime.parse nếu cần
            val start = LocalDateTime.parse(actualStartTime, formatter)
            val now = LocalDateTime.now()

            val minutes = ChronoUnit.MINUTES.between(start, now)

            // 2. Logic tính toán phút hiển thị
            when {
                minutes < 0 -> "0"
                minutes in 45..59 -> "HT" // Nghỉ giải lao
                minutes > 59 -> (minutes - 15).toString() // Trừ 15p nghỉ để ra phút thực
                else -> minutes.toString()
            }
        } catch (e: Exception) {
            "LIVE"
        }
    }
}