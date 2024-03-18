package com.hal.convenientfile.util
import android.content.res.Configuration
import android.os.PowerManager
import com.hal.convenientfile.base.BaseApp

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/15
 */
object ScreenUtil {
    private val powerManager by lazy { BaseApp.app.getSystemService(PowerManager::class.java) }
    fun isScreenPortrait() = BaseApp.app.resources
        .configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    /**
     * 屏幕当前是否属于可交互状态
     */
    fun isInteractive() = powerManager.isInteractive
}