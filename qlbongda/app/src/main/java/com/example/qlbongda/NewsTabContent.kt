package com.example.qlbongda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.FootballNews
import androidx.compose.foundation.border
@Composable
fun NewsTabContent() {
    var activeDetailNews by remember { mutableStateOf<FootballNews?>(null) }

    val newsList = remember {
        listOf(
            FootballNews(id = 1, title = "Arsenal giành chiến thắng kịch tính phút bù giờ cuối cùng", summary = "Trận đấu nghẹt thở kết thúc với tỷ số 3-2 nghiêng về Pháo thủ.", time = "10 phút trước", source = "Sky Sports", category = "Ngoại Hạng Anh", content = "Trận đấu muộn vòng 34 đã diễn ra vô cùng kịch tính tại thánh địa Emirates. Dù bị dẫn trước từ sớm, các cầu thủ Arsenal vẫn kiên trì lội ngược dòng thành công. Bàn thắng quyết định được ghi do công của đội trưởng ở phút bù giờ thứ 5, đem về 3 điểm quý giá giữ vững ngôi đầu bảng cho Pháo Thủ."),
            FootballNews(id = 2, title = "Siêu máy tính dự đoán nhà vô địch Ngoại Hạng Anh 2026", summary = "Tỷ lệ vô địch của Man City giảm mạnh sau chuỗi trận hòa.", time = "1 giờ trước", source = "BBC Sport", category = "Phân Tích", content = "Theo mô phỏng mới nhất từ siêu máy tính Opta, tỷ lệ bảo vệ ngôi vương của Manchester City đã sụt giảm nghiêm trọng xuống còn 42.5%. Trong khi đó, phong độ hủy diệt của các câu lạc bộ bám đuổi đã đẩy cục diện cuộc đua vô địch năm nay trở nên căng thẳng và khó lường hơn bao giờ hết."),
            FootballNews(id = 3, title = "Chấn thương của ngôi sao MU nghiêm trọng hơn dự kiến", summary = "Tiền đạo chủ lực phải nghỉ thi đấu ít nhất 3 tuần.", time = "3 giờ trước", source = "ESPN", category = "Chấn Thương", content = "Tin không vui dành cho người hâm mộ Quỷ Đỏ khi bộ phận y tế xác nhận tiền đạo chủ lực đã gặp phải một chấn thương gân khoeo cấp độ 2. Anh chắc chắn sẽ vắng mặt trong chuỗi 4 trận đấu quan trọng sắp tới, bao gồm cả trận derby rực lửa vào tuần sau."),
            FootballNews(id = 4, title = "Thị trường chuyển nhượng: Chelsea nhắm bom tấn 100 triệu Euro", summary = "Đội bóng quyết tâm bổ sung một trung phong đẳng cấp.", time = "5 giờ trước", source = "Romano", category = "Chuyển Nhượng", content = "Chuyên gia săn tin chuyển nhượng Fabrizio Romano vừa độc quyền tiết lộ ban lãnh đạo Chelsea đang rục rịch đàm phán kích hoạt điều khoản giải phóng hợp đồng của tiền đạo hot nhất châu Âu hiện tại. Dự kiến thương vụ bom tấn này có mức phí không dưới 100 triệu Euro nhằm sửa chữa hàng công cho câu lạc bộ.")
        )
    }

    if (activeDetailNews != null) {
        NewsDetailScreen(news = activeDetailNews!!) { activeDetailNews = null }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "TIN TỨC HOT", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(newsList) { item ->
                    NewsItemRow(news = item, onClick = { activeDetailNews = item })
                }
            }
        }
    }
}

@Composable
fun NewsItemRow(news: FootballNews, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, Color(0xFF222222))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(70.dp).background(Color(0xFF1F1F1F), RoundedCornerShape(8.dp)).border(1.dp, Color(0xFF333333), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Text("📰", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = news.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = news.source, color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = " • ${news.time}", color = Color.LightGray, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun NewsDetailScreen(news: FootballNews, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().clickable { onBack() }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Quay lại tin tức", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Surface(color = NeonGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp), border = BorderStroke(1.dp, NeonGreen)) {
                    Text(text = news.category.uppercase(), color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            item { Text(text = news.title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 28.sp) }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Tác giả: ${news.author}", color = Color.LightGray, fontSize = 12.sp)
                    Text(text = news.time, color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFF222222))
            }
            item { Text(text = news.content, color = Color.White, fontSize = 15.sp, lineHeight = 24.sp, textAlign = TextAlign.Justify) }
        }
    }
}