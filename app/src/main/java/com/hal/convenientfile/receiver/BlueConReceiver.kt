package com.hal.convenientfile.receiver
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/20
 */
class BlueConReceiver(
    private val stateOnEvent: (() -> Unit)? = null,
    private val stateOffEvent: (() -> Unit)? = null
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_OFF -> {
                    stateOffEvent?.invoke()
                }

                BluetoothAdapter.STATE_ON -> {
                    stateOnEvent?.invoke()
                }
            }
        }
    }
}