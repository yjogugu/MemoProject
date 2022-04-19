package com.taijoo.potfolioproject

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


import android.app.NotificationManager

import android.app.NotificationChannel

import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import android.content.Intent
import com.taijoo.potfolioproject.presentation.view.MainActivity


class MyFireBaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFireBaseMessagingService"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        //token을 서버로 전송
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //수신한 메시지를 처리

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            val intent = Intent(this,MainActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


            val pendingIntent = PendingIntent.getActivity(
                this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]

            //값이 없을떄
            val notificationButlder: NotificationCompat.Builder =
                NotificationCompat.Builder(this,getString(R.string.channelId))
                    .setSmallIcon(R.drawable.ic_checkbox_star)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(1001, notificationButlder.build())
        }
//        remoteMessage.notification?.let {
//
//            Log.d(TAG, "Message Notification tag: ${it.tag}")
//            Log.d(TAG, "Message Notification Body: ${it.channelId}")
//        }

    }
}