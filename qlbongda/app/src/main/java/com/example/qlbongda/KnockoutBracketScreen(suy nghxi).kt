package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KnockoutBracketScreen() {
    // Dùng Row ngoài cùng kết hợp với horizontalScroll để người dùng vuốt ngang xem các vòng đấu
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // CỘT 1: TỨ KẾT (4 Trận - 8 Đội)
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly, // Tự động giãn cách đều các trận đấu
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("TỨ KẾT", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            MatchCupCard("Arsenal", "Chelsea", "2", "1")
            MatchCupCard("Man City", "MU", "3", "4")
            MatchCupCard("Liverpool", "Tottenham", "1", "0")
            MatchCupCard("Aston Villa", "Newcastle", "2", "3")
        }

        // CỘT 2: BÁN KẾT (2 Trận - 4 Đội)
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly, // Giãn cách đều để rơi vào giữa các nhánh tứ kết
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("BÁN KẾT", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            MatchCupCard("Arsenal", "MU", "0", "2")
            MatchCupCard("Liverpool", "Newcastle", "3", "1")
        }

        // CỘT 3: CHUNG KẾT (1 Trận - 2 Đội)
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center, // Căn chính giữa màn hình dọc
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("CHUNG KẾT", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            MatchCupCard("MU", "Liverpool", "- -", "- -") // Trận đấu cuối cùng
        }
    }
}

@Composable
fun MatchCupCard(teamA: String, teamB: String, scoreA: String, scoreB: String) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, Color(0xFF222222))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(teamA, color = Color.White, fontSize = 14.sp)
                Text(scoreA, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(teamB, color = Color.White, fontSize = 14.sp)
                Text(scoreB, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}