package com.hal.convenientfile.util
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.io.File

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/1/24
 */
object EnvCheckUtil {
    /**
     * 检查是否存在sim卡
     */
    fun isExistSim(context: Context): Boolean {
        val manager = context.getSystemService(TelephonyManager::class.java)
        return manager.simState != TelephonyManager.SIM_STATE_ABSENT
    }

    /**
     * 检查设备是否root
     */
    fun isDeviceRoot() =
        (Build.TAGS != null && Build.TAGS.contains("test-keys", true)
                || getRootDirs().any { File(it).exists() })


    private fun getRootDirs() = arrayOf(
        "/su",
        "/su/bin/su",
        "/sbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/data/local/su",
        "/system/xbin/su",
        "/system/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/system/bin/cufsdosck",
        "/system/xbin/cufsdosck",
        "/system/bin/cufsmgr",
        "/system/xbin/cufsmgr",
        "/system/bin/cufaevdd",
        "/system/xbin/cufaevdd",
        "/system/bin/conbb",
        "/system/xbin/conbb",
        "/data/adb/magisk",
        "/data/adb/modules",
        "/data/app/com.topjohnwu.magisk",
        "/data/user_de/0/com.topjohnwu.magisk",
        "/config/sdcardfs/com.topjohnwu.magisk",
        "/data/data/com.topjohnwu.magisk",
        "/config/sdcardfs/com.topjohnwu.magisk",
        "/data/media/0/Android/data/com.topjohnwu.magisk",
        "/mnt/runtime/default/emulated/0/Android/data/com.topjohnwu.magisk"
    )


    /**
     * 设备是否开启自动化测试
     */
    fun isOpenAutoTest(): Boolean {
        return ActivityManager.isUserAMonkey()
    }
}