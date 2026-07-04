package com.example.qlbongda.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.qlbongda.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM_LOG", "Nhận tin nhắn từ: ${remoteMessage.from}")

        // Hiển thị thông báo khi nhận được
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }
    }
    override fun onNewToken(token: String) {
        // Gửi token này lên Backend của bạn để lưu vào database
        Log.d("FCM", "New Token: $token")
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "football_notifications_channel"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Thay icon app của bạn ở đây
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)// Quan trọng để không bị Silent

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Thông báo bóng đá",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(1, builder.build())
    }
}