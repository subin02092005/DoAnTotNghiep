package com.example.qlbongda.data.model

// Dữ liệu gửi lên API
data class LoginRequest(
    val email: String,
    val password: String
)

// Dữ liệu User nhận về
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String?,
    val email_verified: Int
)

// Response tổng từ API
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val user: User?
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)
data class RegisterResponse(
    val success: Boolean,
    val message: String
)
data class ProfileData(
    val name: String,
    val email: String,
    val phone: String,
    val email_verified: Int, // Nhận về 0 (chưa kích hoạt) hoặc 1 (đã kích hoạt)
    val role: String
)

// Khối phản hồi tổng khi lấy dữ liệu Profile
data class ProfileResponse(
    val success: Boolean,
    val data: ProfileData?
)

// Khối dữ liệu truyền lên API khi thực hiện Xác thực OTP tại Profile
data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

// Khối phản hồi dùng chung khi xác thực OTP thành công/thất bại
data class VerifyOtpResponse(
    val success: Boolean,
    val message: String
)
// Khối dữ liệu gửi đi ở Bước 1
data class VerifyEmailRequest(
    val email: String
)

// Khối dữ liệu nhận về ở Bước 1
data class VerifyEmailResponse(
    val success: Boolean,
    val name: String?,
    val message: String
)

// Khối dữ liệu gửi đi ở Bước 2
data class ResetPasswordRequest(
    val email: String,
    val password: String
)

// Khối dữ liệu nhận về chung cho Bước 2
data class GenericResponse(
    val success: Boolean,
    val message: String
)
/////////////////////////////////////////////////////////////////////////////////////////////////
// Dữ liệu gửi lên API login


// 2. Tạo Class hứng dữ liệu C# trả về
data class StandingItem(
    val rank: Int,
    val teamName: String,
    val played: Int,
    val goalDifference: String,
    val points: Int,
    val coachName: String = "Chưa cập nhật",   // 🌟 Thêm HLV
    val captainName: String = "Chưa cập nhật", // 🌟 Thêm Đội trưởng
    val players: List<PlayerInfo> = emptyList() // 🌟 Thêm danh sách cầu thủ
)
data class StandingRow(
    val rank: Int,
    val teamName: String,
    val played: Int,
    val goalDifference: String,
    val points: Int
)
data class FootballNews(
    val id: Int,
    val title: String,
    val summary: String,
    val time: String,
    val source: String,
    val imageUrl: String = "",
    val content: String = "",
    val category: String = "Tin Tức",
    val author: String = "Ban Biên Tập"// Bạn có thể tích hợp thư viện Coil để tải ảnh sau
)
data class PlayerInfo(
    val number: String,
    val name: String,
    val position: String
)

data class MatchEvent(
    val minute: String,
    val team: String,
    val type: String,
    val playerName: String
)

data class FullMatchDetail(
    val id: Int,
    val teamA: String,
    val teamB: String,
    val isStarted: Boolean,
    val scoreA: String,
    val scoreB: String,
    val time: String,
    val date: String,
    val stadium: String,
    val events: List<MatchEvent>,
    val lineupA: List<PlayerInfo>,
    val lineupB: List<PlayerInfo>,
    val subsA: List<PlayerInfo>,
    val subsB: List<PlayerInfo>,
    val PossessionA: String,
    val PossessionB: String,
    val ShotsA: String,
    val ShotsB: String,
    val mvp: String,
    val isHot: Boolean = false
)
data class DetailedStanding(
    val rank: Int,
    val teamName: String,
    val logoUrl: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: String,
    val points: Int,
    val form: List<String>
)
