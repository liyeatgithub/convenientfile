package com.hal.convenientfile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Description:应用安装卸载监听
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/17
 */
class AppConReceiver(
    private val appInstallEvent: ((packageName: String?) -> Unit)? = null,
    private val appUninstallEvent: ((packageName: String?) -> Unit)? = null,
) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED, Intent.ACTION_PACKAGE_REPLACED -> {
                appInstallEvent?.invoke(intent.dataString)
            }

            Intent.ACTION_PACKAGE_REMOVED -> {
                appUninstallEvent?.invoke(intent.dataString)
            }
        }
    }
}