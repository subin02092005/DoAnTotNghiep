package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.PhaseBracket
import com.example.qlbongda.ui.theme.NeonGreen

@Composable
fun KnockoutBracketScreen(seasonId: Int, viewModel: AdminViewModel) {
    val bracketData by viewModel.bracketData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(seasonId) {
        viewModel.fetchKnockoutBracket(seasonId)
    }

    if (isLoading && bracketData.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NeonGreen)
        }
    } else if (bracketData.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Chưa có dữ liệu nhánh đấu knockout", color = Color.Gray)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            // Vẽ các cột Tứ Kết, Bán Kết, Chung Kết
            bracketData.forEach { phase ->
                BracketColumn(phase)
            }
            
            // Cột cuối cùng: Nhà vô địch
            val finalPhase = bracketData.find { it.phaseType == "final" }
            ChampionColumn(finalPhase)
        }
    }
}

@Composable
fun BracketColumn(phase: PhaseBracket) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = phase.phaseName.uppercase(),
            color = NeonGreen,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val round1Slots = phase.slots.filter { it.round == 1 }
            round1Slots.forEach { slot ->
                MatchCupCard(
                    teamA = slot.homeTeamName ?: "TBD",
                    teamB = slot.awayTeamName ?: "TBD",
                    scoreA = if (slot.isBye == 1) "BYE" else (slot.homeScore?.toString() ?: "-"),
                    scoreB = if (slot.isBye == 1) "-" else (slot.awayScore?.toString() ?: "-")
                )
            }
        }
    }
}

@Composable
fun ChampionColumn(finalPhase: PhaseBracket?) {
    val finalSlot = finalPhase?.slots?.find { it.round == 1 }
    val championName = if (finalSlot != null && finalSlot.matchStatus == "finished") {
        val hScore = finalSlot.homeScore ?: 0
        val aScore = finalSlot.awayScore ?: 0
        if (hScore > aScore) finalSlot.homeTeamName else if (aScore > hScore) finalSlot.awayTeamName else "Hòa"
    } else null

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "NHÀ VÔ ĐỊCH",
            color = Color.Yellow,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.width(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            border = BorderStroke(2.dp, Color.Yellow)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = championName?.uppercase() ?: "ĐANG CHỜ...",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MatchCupCard(teamA: String, teamB: String, scoreA: String, scoreB: String) {
    Card(
        modifier = Modifier.width(180.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        border = BorderStroke(1.dp, Color(0xFF333333))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = teamA, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), maxLines = 1)
                Text(text = scoreA, color = if (scoreA == "BYE") Color.Yellow else NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = teamB, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), maxLines = 1)
                Text(text = scoreB, color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            }
        }
    }
}
