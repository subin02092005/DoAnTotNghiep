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
        super.onMessageReceived(remoteMessage)

        // message.notification?.title và message.notification?.body chính là tiêu đề/nội dung bạn gửi từ Node.js
        val title = remoteMessage.notification?.title
        val body =remoteMessage.notification?.body

        // Bạn có thể hiển thị nó lên Notification Bar thủ công tại đây nếu cần
        showNotification(title, body)
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Gửi token này lên API /update_fcm_token của bạn để lưu vào DB
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