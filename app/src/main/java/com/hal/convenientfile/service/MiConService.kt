package com.hal.convenientfile.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.hal.convenientfile.R
import com.hal.convenientfile.util.AppUtil
import com.hal.convenientfile.util.Cloak
import com.hal.convenientfile.util.log

class MiConService : Service() {
    private val notificationId = 344
    private val notificationManager by lazy { getSystemService(NotificationManager::class.java) }
    private val channelId = "sureok"
    private val channelName = "More"

    override fun onCreate() {
        super.onCreate()
        "MiConService onCreate...".log()
        AppUtil.isServiceStartSuccess.value = true
        Cloak.onServiceDestroy.postValue(false)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "MiConService onStartCommand...".log()
        super.onStartCommand(intent, flags, startId)
        createForegroundNotification()
        return START_STICKY
    }

    private fun createForegroundNotification() {
        // 创建一个 NotificationChannel
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        // 自定义设置通知声音、震动等
        channel.enableVibration(false)
        channel.setSound(null, null)
        channel.setShowBadge(false)

        notificationManager.createNotificationChannel(channel)
        // 构建一个 Notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setCustomContentView(RemoteViews(this.packageName, R.layout.touding_remote_view))
            //兼容老版本
            .setContent(RemoteViews(this.packageName, R.layout.touding_remote_view))
            //展开
            .setCustomBigContentView(
                RemoteViews(
                    this.packageName,
                    R.layout.touding_remote_view
                )
            )
            .setContentTitle("")
            .setContentText("")
            .setColor(Color.WHITE)
            .setSmallIcon(R.drawable.dididi_shape)
//            .setSmallIcon(R.drawable.ac_icon)
//            .setSmallIcon(R.drawable.trans_shape)
            .build()
        "startForeground notification...".log()
        startForeground(notificationId, notification)
    }

    override fun onBind(intent: Intent) = ConInBinder()

    override fun onDestroy() {
        super.onDestroy()
        "MiConService service onDestory...".log()
        Cloak.onServiceDestroy.postValue(true)
    }

    inner class ConInBinder : Binder()
}