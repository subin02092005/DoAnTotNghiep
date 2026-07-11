package com.example.qlbongda.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.qlbongda.MainActivity
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

        // Tạo Intent để mở App khi bấm vào thông báo
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // FLAG_IMMUTABLE là bắt buộc trên Android 12+
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Thông báo bóng đá", NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title ?: "Thông báo mới")
            .setContentText(body ?: "Nội dung thông báo")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Bắt buộc phải là HIGH hoặc MAX
            .setDefaults(NotificationCompat.DEFAULT_ALL)   // <--- THÊM DÒNG NÀY VÀO ĐÂY
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}