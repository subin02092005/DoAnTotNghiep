package com.example.qlbongda.data.model

import com.google.gson.annotations.SerializedName

// =================================================================
// 1. AUTHENTICATION & USER MODELS
// =================================================================

// Dữ liệu gửi lên API login
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

// Response tổng từ API login
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val user: User?
)

// Dữ liệu gửi lên API đăng ký
data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)

// Response từ API đăng ký
data class RegisterResponse(
    val success: Boolean,
    val message: String
)


// =================================================================
// 2. PROFILE MODELS
// =================================================================

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


// =================================================================
// 3. PASSWORD RESET MODELS (QUÊN MẬT KHẨU)
// =================================================================

// Khối dữ liệu gửi đi ở Bước 1 (Gửi email yêu cầu OTP)
data class VerifyEmailRequest(
    val email: String
)

// Khối dữ liệu nhận về ở Bước 1
data class VerifyEmailResponse(
    val success: Boolean,
    val name: String?,
    val message: String
)

// Khối dữ liệu gửi đi ở Bước 2 (Đặt lại mật khẩu)
data class ResetPasswordRequest(
    val email: String,
    val password: String
)

// Khối dữ liệu nhận về chung cho Bước 2
data class GenericResponse(
    val success: Boolean,
    val message: String
)


// =================================================================
// 4. 🌟 TOURNAMENT & PHASES MODELS (ĐỒNG BỘ VÒNG ĐẤU ĐỘNG TỪ ADMIN)
// =================================================================

// Khối phản hồi tổng thể của mùa giải từ API
data class SeasonResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: SeasonDataResponse?
)

// 🌟 THÊM CHO ĐẦY ĐỦ: Khối dữ liệu chứa ID mùa giải và mảng các vòng đấu
data class SeasonDataResponse(
    @SerializedName("season_id") val seasonId: Int,
    @SerializedName("phases") val phases: List<TournamentPhase>
)

// Model chi tiết của từng vòng đấu (Vòng Bảng, Tứ Kết, Bán Kết, Chung Kết...)
data class TournamentPhase(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,         // Tên hiển thị (Ví dụ: "Vòng Bảng")
    @SerializedName("format") val format: String        // Thể thức: "round_robin" hoặc "knockout"
)


// =================================================================
// 5. MATCH & STANDINGS MODELS (BẢNG XẾP HẠNG & TRẬN ĐẤU)
// =================================================================

// Tạo Class hứng dữ liệu C# trả về
data class StandingItem(
    val rank: Int,
    val teamName: String,
    val played: Int,
    val goalDifference: String,
    val points: Int,
    val coachName: String = "Chưa cập nhật",   // Thêm HLV
    val captainName: String = "Chưa cập nhật", // Thêm Đội trưởng
    val players: List<PlayerInfo> = emptyList() // Thêm danh sách cầu thủ
)
data class TeamDetailResponse(
    val status: String,
    val message: String,
    val data: StandingItem // Trả về object StandingItem chứa list cầu thủ, tên HLV, Đội trưởng
)
// Model dòng bảng xếp hạng rút gọn hiển thị ở Home
data class StandingRow(
    val rank: Int,
    val teamName: String,
    val played: Int,
    val goalDifference: String,
    val points: Int
)

// Model chi tiết bảng xếp hạng đầy đủ
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
data class DetailedStandingResponse(
    val status: String,
    val message: String,
    val data: List<GroupStanding> // Thay vì List<DetailedStanding>, giờ là danh sách các Bảng
)
data class GroupStanding(
    val groupName: String,          // "Bảng A", "Bảng B", "Bảng C"...
    val standings: List<DetailedStanding> // Danh sách các đội thuộc bảng này
)
// Cầu thủ
data class PlayerInfo(
    val number: String,
    val name: String,
    val position: String
)

// Sự kiện trong trận đấu
data class MatchEvent(
    val minute: String,
    val team: String,
    val type: String,
    val playerName: String
)

// Chi tiết toàn bộ trận đấu (Gồm cả thông số sút bóng, kiểm soát bóng,...)
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


// =================================================================
// 6. NEWS MODELS (TIN TỨC BÓNG ĐÁ)
// =================================================================

data class FootballNews(
    val id: Int,
    val title: String,
    val summary: String,
    val time: String,
    val source: String,
    val imageUrl: String = "",
    val content: String = "",
    val category: String = "Tin Tức",
    val author: String = "Ban Biên Tập"
)
data class NewsResponse(
    val status: String,
    val message: String,
    val data: List<FootballNews>
)