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
        Log.d("FCM_DEBUG", "Data: ${remoteMessage.data}")
        Log.d("FCM_DEBUG", "Notification: ${remoteMessage.notification?.title}")

        // Xử lý cả 2 trường hợp: nếu nó gửi qua 'notification' hoặc gửi qua 'data'
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"]
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"]

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New Token: $token")
        // TODO: Gửi token này lên server của bạn ở đây
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "football_notifications_channel"

        // 1. Tạo NotificationManager
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 2. Tạo Channel (BẮT BUỘC cho Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Thông báo bóng đá",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        // 3. Tạo Builder với Icon đầy đủ
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Đảm bảo icon này tồn tại
            .setContentTitle(title ?: "Thông báo mới")
            .setContentText(body ?: "Nội dung thông báo")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // 4. Hiển thị
        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}