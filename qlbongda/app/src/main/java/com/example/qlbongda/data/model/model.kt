package com.example.qlbongda.data.model



// Dữ liệu gửi lên API login
data class AuthLoginRequest(
    val email: String,
    val password: String // API C# viết chữ thường nên ở đây ta map cho đúng
)

// 2. Tạo Class hứng dữ liệu C# trả về
data class AuthLoginResponse(
    val accessToken: String,
    val csrfToken: String,
    val expiresAt: String
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
    val imageUrl: String = "" // Bạn có thể tích hợp thư viện Coil để tải ảnh sau
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
    val mvp: String
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
