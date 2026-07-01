package com.example.qlbongda.data.model

import android.R
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
    val role:String?,
    val email_verified: Int,
    @SerializedName("is_admin") val isAdmin: Boolean? = false
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

// Khối dữ liệu check admin email
data class CheckAdminRequest(
    val email: String
)

data class CheckAdminResponse(
    val success: Boolean,
    @SerializedName("is_admin") val isAdmin: Boolean,
    val message: String? = null
)


// =================================================================
// 2. PROFILE MODELS
// =================================================================

data class ProfileData(
    val name: String,
    val email: String,
    val phone: String,
    val email_verified: Int, // Nhận về 0 (chưa kích hoạt) hoặc 1 (đã kích hoạt)
    val role: String,
    @SerializedName("date_of_birth") val dateOfBirth: String?
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

data class UpdateProfileRequest(val email: String, val name: String, val phone: String,val date_of_birth: String? )
data class ChangePasswordRequest(val email: String, val oldPassword: String, val newPassword: String)
data class GeneralResponse(val success: Boolean, val message: String)
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
data class PlayerInfo(
    val id: Int=0, // 🌟 BẮT BUỘC THÊM ĐỂ CẬP NHẬT/XÓA
    @SerializedName("jersey_number") val number: String, // Ánh xạ từ 'jersey_number' của SQL
    @SerializedName("name") val name: String,             // Ánh xạ từ 'name' của SQL
    @SerializedName("position") val position: String,
    @SerializedName("date_of_birth") val dateOfBirth: String? = null,
   val isCaptain: Boolean = false, // Thêm trường này
   val userId: Int = 0             // Thêm ID người dùng để so sánh

)

data class TeamDetailResponse(
    val status: String,
    val message: String,
    val data: StandingItem // Trả về object StandingItem chứa list cầu thủ, tên HLV, Đội trưởng
)

data class MyTeamResponse(
    val status: String,
    val hasTeam: Boolean,
    val data: MyTeamData?,
    val message: String?
)

data class MyTeamData(
    val teamId: Int,
    val teamName: String,
    val coachName: String?,
    val captainName: String?,
    val currentUserRole: String, // Dùng để ẩn/hiện nút Sửa
    val players: List<PlayerInfo>
)
data class AddPlayerRequest(
    val team_id: Int,
    val email: String,
    val jersey_number: String,
    val position: String
)
data class AddPlayerResponse(
    val status: String,
    val message: String,
    val data: PlayerInfo // PlayerInfo của bạn đã có các trường name, jersey_number, position
)
data class UpdatePlayerRequest(
    val team_id: Int,
    val id: Int,
    val jersey_number: String,
    val position: String
)
data class RemovePlayerRequest(
    val team_id: Int,
    val player_id: Int
)
// =================================================================
// 7. COMMON RESPONSE (Dùng chung cho các API chỉ trả về status)
// =================================================================

data class ApiResponse(
    val status: String,
    val message: String
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
    val form: List<String>?
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


// Sự kiện trong trận đấu


// Chi tiết toàn bộ trận đấu (Gồm cả thông số sút bóng, kiểm soát bóng,...)
data class FullMatchDetail(
    val id: Int,
    val teamA: String,
    val teamB: String,
    val status: String, // <--- THÊM DÒNG NÀY VÀO
    val isStarted: Boolean,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val time: String,
    val date: String,
    val stadium: String,
    val events: List<MatchEvent>,
    val lineupA: List<PlayerInfo>,
    val lineupB: List<PlayerInfo>,
    val subsA: List<PlayerInfo>,
    val subsB: List<PlayerInfo>,
    val PossessionA: String = "0%",
    val PossessionB: String = "0%",
    val ShotsA: String = "0",
    val ShotsB: String = "0",
    val mvp: String = "Chưa xác định",
    val isHot: Boolean = false
)
data class MatchEvent(
    val minute: String,
    val team: String,
    val type: String,
    val playerName: String
)
// Trong file model.kt
enum class MatchStatus {
    PENDING, ONGOING, FINISHED
}

data class MatchDto(
    val id: Int,
    val teamA: String,
    val teamB: String,
    @SerializedName("home_final_score") val home_score: Int?,
    @SerializedName("away_final_score") val away_score: Int?,
    val scheduled_at: String?,
    @SerializedName("status") val status: String // Nhận về "pending", "ongoing", "finished"
) {
    // 1. Chuyển đổi String sang Enum để dễ dùng
    val matchStatus: MatchStatus
        get() = when (status.lowercase()) {
            "ongoing" -> MatchStatus.ONGOING
            "finished" -> MatchStatus.FINISHED
            else -> MatchStatus.PENDING
        }

    // 2. Các flag tiện lợi cho UI
    val isFinished: Boolean get() = matchStatus == MatchStatus.FINISHED
    val isLive: Boolean get() = matchStatus == MatchStatus.ONGOING
    val isUpcoming: Boolean get() = matchStatus == MatchStatus.PENDING

    // Giữ lại nếu bạn vẫn muốn kiểm tra nhanh "đã bắt đầu hay chưa"
    val isStarted: Boolean get() = isFinished || isLive
}

// Wrapper này để hứng cấu trúc { status: "...", data: [...] }
data class MatchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<MatchDto> // 🌟 Dùng MatchDto ở đây
)
data class MatchDetailResponse(
    val status: String,
    val data: FullMatchDetail // Đây chính là class bạn dùng trong App
)


// =================================================================
// 6. NEWS MODELS (TIN TỨC BÓNG ĐÁ)
// =================================================================

data class Notification(
    val id: Int,
    val title: String,        // Map với tieu_de
    val content: String,      // Map với noi_dung
    val time: String          // Map với ngay_tao
)

data class NotificationResponse(
    val status: String,
    val message: String,
    val data: List<Notification>
)
