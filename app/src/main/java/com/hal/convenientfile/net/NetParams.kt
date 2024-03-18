package com.hal.convenientfile.net

import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.util.AdUtil
import com.hal.convenientfile.util.Device
import java.util.*

/**
 * Description:tba通用网络参数
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2023/12/27
 */
object NetParams {
    fun commonParamMap(): Map<String, Any?> =
        mapOf(
            "anther" to Device.osVersion,
            "embitter" to Device.androidId,
            "basilar" to UUID.randomUUID().toString(),
            "milt" to Device.packageName,
            "lengthy" to Device.netProvider,
            "dang" to Device.language,
            "bootes" to BaseApp.app.packageManager.getPackageInfo(
                Device.packageName,
                0
            ).versionName,
            "ransack" to TimeZone.getDefault().rawOffset / 3600_000,
            "freak" to Device.androidOS,
            "song" to System.currentTimeMillis(),
            "status" to Device.manufacturer,
            "moulton" to Device.model,
            "original" to Device.androidId,
            "himself" to AdUtil.gaid.value
        )
}