package com.hal.convenientfile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Description:屏幕监听
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/17
 */
class ScreenConReceiver(
    private val userPresentEvent: (() -> Unit)? = null,
    private val screenOnEvent: (() -> Unit)? = null,
    private val screenOffEvent: (() -> Unit)? = null
) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                screenOnEvent?.invoke()
            }

            Intent.ACTION_SCREEN_OFF -> {
                screenOffEvent?.invoke()
            }

            Intent.ACTION_USER_PRESENT -> {
                userPresentEvent?.invoke()
            }
        }
    }
}