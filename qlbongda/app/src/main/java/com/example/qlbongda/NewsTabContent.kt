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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import com.example.qlbongda.data.api.RetrofitClient
import com.example.qlbongda.data.model.Notification
import com.example.qlbongda.ui.theme.NeonGreen

@Composable
fun NewsTabContent() {
    var activeDetailNews by remember { mutableStateOf<Notification?>(null) }
    val context = LocalContext.current
    var newsList by remember { mutableStateOf<List<Notification>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.getClient(context).getNotifications()
            if (response.isSuccessful) {
                val body = response.body() // Lấy dữ liệu
                newsList = body?.data ?: emptyList()
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
        Column(modifier = Modifier.fillMaxSize().padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "THÔNG BÁO", color = NeonGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonGreen)
                }
            } else if (newsList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có thông báo mới.", color = Color.Gray, fontSize = 14.sp)
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
fun NewsItemRow(news: Notification, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = BorderStroke(1.dp, Color(0xFF222222))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).background(Color(0xFF1F1F1F), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Text("🔔", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = news.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                Text(text = news.time, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun NewsDetailScreen(news: Notification, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().clickable { onBack() }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NeonGreen)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Quay lại", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = news.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = news.time, color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFF222222))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = news.content, color = Color.White, fontSize = 15.sp, lineHeight = 24.sp)
    }
}