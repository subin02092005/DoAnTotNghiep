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
import androidx.compose.ui.platform.LocalContext // 🌟 ĐÃ THÊM: Để lấy context trong Jetpack Compose
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qlbongda.data.model.FootballNews
import androidx.compose.foundation.border
import com.example.qlbongda.data.api.RetrofitClient

@Composable
fun NewsTabContent() {
    var activeDetailNews by remember { mutableStateOf<FootballNews?>(null) }

    // 🌟 ĐÃ THÊM: Lấy Context hiện tại của màn hình ứng dụng để truyền vào Retrofit
    val context = LocalContext.current

    var newsList by remember { mutableStateOf<List<FootballNews>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // TỰ ĐỘNG GỌI API KHI CHUYỂN QUA TAB TIN TỨC
    LaunchedEffect(Unit) {
        try {
            // 🌟 ĐÃ SỬA: Truyền 'context' vào hàm getClient() để sửa triệt để lỗi biên dịch
            val response = RetrofitClient.getClient(context).getNews()

            if (response.isSuccessful && response.body()?.status == "success") {
                newsList = response.body()?.data ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    if (activeDetailNews != null) {
        NewsDetailScreen(news = activeDetailNews!!) { activeDetailNews = null }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "TIN TỨC HOT", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            } else if (newsList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có tin tức nào mới.", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(newsList) { item ->
                        NewsItemRow(news = item, onClick = { activeDetailNews = item })
                    }
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