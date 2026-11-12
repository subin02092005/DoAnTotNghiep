package com.example.qlbongda.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.PlayerInfo
import com.example.qlbongda.ui.theme.NeonGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormationBoard(
    currentFieldType: Int,
    players: List<PlayerInfo>,
    isEditable: Boolean = false,
    canChangeFieldType: Boolean = false,
    onFieldTypeChanged: (Int) -> Unit = {},
    onPlayerSwap: (oldPlayer: PlayerInfo?, newPlayer: PlayerInfo) -> Unit = { _, _ -> }
) {
    android.util.Log.d("FORMATION_CHECK", "Tổng số player: ${players.size}, Số cầu thủ đá chính: ${players.count { it.isStarter == true }}")
    var selectedFieldType by remember(currentFieldType) { mutableIntStateOf(currentFieldType) }

    var showPlayerSelectDialog by remember { mutableStateOf(false) }
    var selectedSlotPosition by remember { mutableStateOf("") }
    var currentPlayerAtSlot by remember { mutableStateOf<PlayerInfo?>(null) }

    // State dùng để hiện Dialog cảnh báo khi HLV chọn cầu thủ bị treo giò / thẻ đỏ
    var warningMessage by remember { mutableStateOf<String?>(null) }

    val maxStarters = selectedFieldType
    val activeStarters = players.filter { it.isStarter == true }.take(maxStarters)
    val substitutes = players.filter { !activeStarters.contains(it) }

    val fwCount = when (selectedFieldType) { 5 -> 1; 7 -> 1; else -> 2 }
    val mfCount = when (selectedFieldType) { 5 -> 2; 7 -> 2; else -> 4 }
    val dfCount = when (selectedFieldType) { 5 -> 1; 7 -> 3; else -> 4 }
    val gkCount = 1

    var currentIndex = 0
    val fwList = activeStarters.drop(currentIndex).take(fwCount).also { currentIndex += it.size }
    val mfList = activeStarters.drop(currentIndex).take(mfCount).also { currentIndex += it.size }
    val dfList = activeStarters.drop(currentIndex).take(dfCount).also { currentIndex += it.size }
    val gkList = activeStarters.drop(currentIndex).take(gkCount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF161616), RoundedCornerShape(16.dp))
            .border(1.dp, NeonGreen, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ĐỘI HÌNH (SÂN $selectedFieldType NGƯỜI)",
                color = NeonGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            if (isEditable) {
                Text(
                    text = "Chạm vị trí để đổi người",
                    color = Color.Yellow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (canChangeFieldType) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                listOf(5, 7, 11).forEach { type ->
                    FilterChip(
                        selected = (selectedFieldType == type),
                        onClick = {
                            selectedFieldType = type
                            onFieldTypeChanged(type)
                        },
                        label = {
                            Text(
                                "Sân $type",
                                color = if (selectedFieldType == type) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonGreen,
                            containerColor = Color(0xFF222222)
                        )
                    )
                }
            }
        }

        // KHUNG SÂN BÓNG
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(Color(0xFF1B4332), RoundedCornerShape(12.dp))
                .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.4f))
                )
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FormationRow(count = fwCount, playerPool = fwList, defaultPos = "FW", isEditable = isEditable) { player, pos ->
                    selectedSlotPosition = pos; currentPlayerAtSlot = player; showPlayerSelectDialog = true
                }
                FormationRow(count = mfCount, playerPool = mfList, defaultPos = "MF", isEditable = isEditable) { player, pos ->
                    selectedSlotPosition = pos; currentPlayerAtSlot = player; showPlayerSelectDialog = true
                }
                FormationRow(count = dfCount, playerPool = dfList, defaultPos = "DF", isEditable = isEditable) { player, pos ->
                    selectedSlotPosition = pos; currentPlayerAtSlot = player; showPlayerSelectDialog = true
                }
                FormationRow(count = gkCount, playerPool = gkList, defaultPos = "GK", isEditable = isEditable) { player, pos ->
                    selectedSlotPosition = pos; currentPlayerAtSlot = player; showPlayerSelectDialog = true
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // DANH SÁCH DỰ BỊ
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "CẦU THỦ DỰ BỊ (${substitutes.size})",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            if (substitutes.isEmpty()) {
                Text(
                    text = "Không có cầu thủ dự bị nào ngoài sân.",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(substitutes) { sub ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF222222)),
                            border = BorderStroke(1.dp, Color.DarkGray),
                            modifier = Modifier.width(115.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(6.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "#${sub.number}",
                                        color = NeonGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        sub.name,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(sub.position, color = Color.Gray, fontSize = 9.sp)
                                }
                                // Hiển thị thẻ phạt nhỏ trên thẻ dự bị nếu có
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                ) {
                                    PlayerCardBadge(
                                        yellowCards = sub.yellowCards ?: 0,
                                        isSuspended = sub.isSuspended == true,
                                        hasRedCard = sub.redCards ?: 0
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG CHỌN CẦU THỦ THAY THẾ
    if (showPlayerSelectDialog && isEditable) {
        var dialogTab by remember { mutableIntStateOf(0) }
        val dialogStarters = players.filter { it.isStarter == true }
        val dialogSubs = players.filter { it.isStarter != true }

        AlertDialog(
            onDismissRequest = { showPlayerSelectDialog = false },
            title = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Đổi người cho vị trí $selectedSlotPosition", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { dialogTab = 0 },
                            modifier = Modifier.weight(1f).height(36.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (dialogTab == 0) NeonGreen else Color(0xFF222222)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Đá chính (${dialogStarters.size})", color = if (dialogTab == 0) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Button(
                            onClick = { dialogTab = 1 },
                            modifier = Modifier.weight(1f).height(36.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (dialogTab == 1) Color.Yellow else Color(0xFF222222)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Dự bị (${dialogSubs.size})", color = if (dialogTab == 1) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            },
            text = {
                Box(modifier = Modifier.heightIn(max = 300.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        val currentList = if (dialogTab == 0) dialogStarters else dialogSubs
                        if (currentList.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                    Text("Không có cầu thủ nào", color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                        } else {
                            items(currentList) { p ->
                                PlayerSelectItemRow(
                                    player = p,
                                    tagText = if (dialogTab == 0) "Đá chính" else "Dự bị",
                                    tagColor = if (dialogTab == 0) NeonGreen else Color.Yellow,
                                    onClick = {
                                        // 🌟 LOGIC KIỂM TRA TREO GIÒ / THẺ ĐỎ KHI CHỌN
                                        if (p.isSuspended == true || (p.redCards ?: 0) > 0) {
                                            warningMessage = "Cầu thủ ${p.name} đang bị treo giò hoặc dính thẻ đỏ, không thể xếp đá chính!"
                                        } else {
                                            onPlayerSwap(currentPlayerAtSlot, p)
                                            showPlayerSelectDialog = false
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlayerSelectDialog = false }) {
                    Text("Đóng", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF161616)
        )
    }

    // 🌟 DIALOG CẢNH BÁO KHI CỐ TÌNH CHỌN CẦU THỦ BỊ TREO GIÒ
    warningMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { warningMessage = null },
            title = { Text("⚠️ Cảnh báo kỷ luật", color = Color.Red, fontWeight = FontWeight.Bold) },
            text = { Text(msg, color = Color.White, fontSize = 14.sp) },
            confirmButton = {
                Button(
                    onClick = { warningMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Đã hiểu", color = Color.White)
                }
            },
            containerColor = Color(0xFF161616)
        )
    }
}

@Composable
fun FormationRow(
    count: Int,
    playerPool: List<PlayerInfo>,
    defaultPos: String,
    isEditable: Boolean,
    onSlotClick: (PlayerInfo?, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            constrainPlayerSlot(player = playerPool.getOrNull(index), defaultPos = defaultPos, isEditable = isEditable, onSlotClick = onSlotClick)
        }
    }
}

@Composable
fun constrainPlayerSlot(
    player: PlayerInfo?,
    defaultPos: String,
    isEditable: Boolean,
    onSlotClick: (PlayerInfo?, String) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .graphicsLayer(translationX = offsetX, translationY = offsetY)
            .then(
                if (isEditable) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                } else Modifier
            )
            .clickable(enabled = isEditable) {
                onSlotClick(player, defaultPos)
            }
    ) {
        // 🌟 Bọc Box chứa avatar cầu thủ và huy hiệu thẻ phạt ở góc trên bên phải
        Box(modifier = Modifier.size(42.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        if (player != null) NeonGreen else Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = player?.number ?: defaultPos,
                    color = if (player != null) Color.Black else Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (player != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    PlayerCardBadge(
                        yellowCards = player.yellowCards ?: 0,
                        isSuspended = player.isSuspended == true,
                        hasRedCard = player.redCards ?: 0
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = player?.name ?: "Trống",
            color = Color.White,
            fontSize = 10.sp,
            maxLines = 1,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 60.dp)
        )
    }
}

@Composable
fun PlayerSelectItemRow(
    player: PlayerInfo,
    tagText: String,
    tagColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF222222), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(tagColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("#${player.number}", color = tagColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(player.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tagText,
                        color = Color.Black,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(tagColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
                Text("Vị trí: ${player.position}", color = Color.Gray, fontSize = 11.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // 🌟 Hiển thị thẻ phạt trực tiếp trong list chọn cầu thủ
            PlayerCardBadge(
                yellowCards = player.yellowCards ?: 0,
                isSuspended = player.isSuspended == true,
                hasRedCard = player.redCards ?: 0
            )
            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = tagColor)
        }
    }
}

@Composable
fun PlayerCardBadge(yellowCards: Int, isSuspended: Boolean, hasRedCard: Int) {
    when {
        hasRedCard > 0 || isSuspended -> {
            CardBadgeIcon(color = Color.Red, text = if (hasRedCard > 1) "2V" else "")
        }
        yellowCards > 0 -> {
            CardBadgeIcon(color = Color.Yellow, text = if (yellowCards > 1) "$yellowCards" else "")
        }
    }
}

@Composable
fun CardBadgeIcon(color: Color, text: String) {
    Box(
        modifier = Modifier
            .size(14.dp, 18.dp)
            .background(color, RoundedCornerShape(2.dp))
            .border(0.5.dp, Color.Black, RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (text.isNotEmpty()) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFF161616)
@Composable
fun FormationBoardWithCardsPreview() {
    val samplePlayersWithCards = listOf(
        // Cầu thủ bình thường
        PlayerInfo(id = 1, number = "1", name = "Thủ Môn", position = "GK", isStarter = true, yellowCards = 0, redCards = 0, isSuspended = false),

        // Cầu thủ dính 1 thẻ vàng
        PlayerInfo(id = 2, number = "2", name = "Hậu Vệ 1", position = "DF", isStarter = true, yellowCards = 1, redCards = 0, isSuspended = false),

        // Cầu thủ dính 2 thẻ vàng (Hiển thị 2V)
        PlayerInfo(id = 3, number = "4", name = "Hậu Vệ 2", position = "DF", isStarter = true, yellowCards = 2, redCards = 0, isSuspended = false),

        // Cầu thủ bị thẻ đỏ / treo giò (Hiển thị thẻ đỏ)
        PlayerInfo(id = 4, number = "6", name = "Tiền Vệ", position = "MF", isStarter = true, yellowCards = 0, redCards = 1, isSuspended = true),

        PlayerInfo(id = 5, number = "10", name = "Tiền Đạo", position = "FW", isStarter = true, yellowCards = 0, redCards = 0, isSuspended = false),

        // Cầu thủ dự bị có dính thẻ vàng
        PlayerInfo(id = 6, number = "12", name = "Dự Bị 1", position = "GK", isStarter = false, yellowCards = 1, redCards = 0, isSuspended = false)
    )

    Box(modifier = Modifier.padding(16.dp)) {
        FormationBoard(
            currentFieldType = 5,
            players = samplePlayersWithCards,
            isEditable = true,
            canChangeFieldType = true
        )
    }
}