package com.hal.convenientfile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Description:电量监听
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/17
 */
class BatteryConReceiver(
    private val batteryLowEvent: (() -> Unit)? = null,
    private val powerConnected: (() -> Unit)? = null,
    private val powerDisConnected: (() -> Unit)? = null
) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BATTERY_LOW -> {
                batteryLowEvent?.invoke()
            }

            Intent.ACTION_POWER_CONNECTED -> {
                powerConnected?.invoke()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                powerDisConnected?.invoke()
            }
        }
    }
}