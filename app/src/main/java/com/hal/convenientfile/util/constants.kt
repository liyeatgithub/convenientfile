package com.hal.convenientfile.util

import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.config.EnvConfig
import java.util.*

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/10/27
 */

object SpKey {

    /**
     * 全局广告id的key
     */
    const val SP_KEY_GAID = "sp_key_sd"

    /**
     * 是否在白名单中
     */
    const val SP_KEY_IN_BLACK_LIST = "sp_key_in_bl"

    /**
     * 是否是第三方平台下载的app
     */
    const val SP_KEY_IN_THIRD_PLATFORM = "sp_key_in_tp"

    /**
     * 检查自动化测试的检查结果,1:通过 0:不通过
     */
    const val SP_KEY_CHECK_AUTO_TEST_RESULT = "sp_key_check_a_t_r"

    /**
     * 是否检查设备 自动测试 1:检查 0：不检查
     */
    const val SP_KEY_IS_CHECK_AUTO_TEST = "sp_key_is_check_a_t_r"

    /**
     * 检查root环境的检查结果,1:通过 0:不通过
     */
    const val SP_KEY_CHECK_ROOT_RESULT = "sp_key_check_r_r"

    const val SP_KEY_IS_CHECK_ROOT = "sp_key_is_check_r_r"

    /**
     * 检查sim卡的检查结果,1:通过 0:不通过
     */
    const val SP_KEY_CHECK_SIM_RESULT = "sp_key_check_sim_rt"

    const val SP_KEY_is_CHECK_SIM = "sp_key_is_check_sim_rt"

    /**
     * 是否是第一次安装
     */
    const val SP_KEY_IS_FIRST_INSTALL = "sp_key_is_fi"

    /**
     * 是否上传了referrer埋点事件
     */
    const val SP_KEY_IS_UPLOAD_REFERRER = "sp_key_is_upload_rf"

    /**
     * 从google play获取的referrer url
     */
    const val SP_KEY_REFERRER = "sp_key_referrer"

    /**
     * app安装的客户端时间(s)
     */
    const val SP_KEY_APP_INSTALL_TIME_SECONDS = "sp_key_app_its"

    /**
     * 当前图标是否隐藏
     */
    const val SP_KEY_IS_ICON_HIDE = "sp_key_is_i_h"

    /**
     * 第一次打开app本地时间
     */
    const val SP_KEY_FIRST_OPEN_CLIENT_TIME = "sp_key_fos_time"

    /**
     * 是否上传了第一次安装事件
     */
    const val SP_KEY_IS_UPLOAD_INSTALL_EVENT = "sp_key_is_uie"

    /**
     * 是否上传了用户类型事件到tba后台(cloak/normal)
     */
    const val SP_KEY_IS_UPLOAD_USER_TYPE = "sp_key_is_u_u_type"

    /**
     * 是否上传了安装事件到tba后台
     */
    const val SP_KEY_IS_UPLOAD_INSTALL_TO_TBA = "sp_key_is_upload_install_to_tba"

    /**
     * 广告是否限制跟踪
     */
    const val SP_KEY_IS_LIMIT_AD_TRACKING = "sp_key_is_limit_ad_tr"

    /**
     * 最后一次上传激活事件的时间
     */
    const val SP_KEY_LAST_UPLOAD_ACTIVE_TIME = "sp_key_last_upload_active_time"

    /**
     * 最后一次上传进程激活事件的时间
     */
    const val SP_KEY_LAST_UPLOAD_PROCESS_ACTIVE_TIME = "sp_key_last_upload_process_active_time"
}


object Ad {
    const val innerPlacementId = EnvConfig.TOP_ON_PLACEMENT_ID
    const val alivePlacementId = EnvConfig.TOP_ON_KEEP_PLACEMENT_ID
}

object Device {
    val androidId: String by lazy {
        Settings.System.getString(
            BaseApp.app.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
    val osVersion: String by lazy { Build.VERSION.RELEASE }
    val model: String by lazy { Build.MODEL }

    /**
     * 厂商
     */
    val manufacturer: String by lazy { Build.MANUFACTURER }
    val language: String by lazy {
        Locale.getDefault().let {
            it.language + "_" + it.country
        }
    }

    val country: String by lazy {
        Locale.getDefault().country
    }

    val packageName: String by lazy { BaseApp.app.packageName }

    /**
     * tba 后台对应android OS 参数，会随文档变化而变化
     */
    val androidOS: String by lazy { "oldy" }

    private val telephonyManager: TelephonyManager by lazy {
        BaseApp.app.getSystemService(
            TelephonyManager::class.java
        )
    }
    val netProvider: String by lazy { telephonyManager.simOperator }
}


object Cloak {
    /**
     * 用户接口 黑名单 value
     */
    const val USER_BLACK = "pawpaw"

    /**
     * 用户接口正常value
     */
    const val USER_NORMAL = "grill"

    /**
     * 广告循环间隔时间
     */
    val adLoopTime by lazy {
        FireBaseUtil.remoteConfig.getLong(EnvConfig.REMOTE_KEY_LOOP_TIME).let {
            log("adLoopTime==", it)
            if (it == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG) 30_000 else it * 1000
        }
    }

    /**
     * 必须要动态获取，否者初始化之后，值就不会刷新了！！
     * 如果拉取失败则默认adjust渠道
     * 1: adjust 归因为准
     * 2：google play referrer 归因为准
     */
    fun channelStrategy() =
        if (AdUtil.firebaseFetchResult.value == false) 1
        else FireBaseUtil.remoteConfig.getLong(EnvConfig.REMOTE_KEY_CHANNEL_STRATEGY)

    /**
     * 使用函数方式获取更准确，但由于此值在firebase远程配置获取成功之后再使用，故不影响程序逻辑
     * 普通(广告)用户类型条件，检查sim卡是否存在，
     * 1:需要检查 sim存在，2，不检查
     * 默认不检查
     */
    val isCheckSim by lazy {
        FireBaseUtil.remoteConfig.getLong(EnvConfig.REMOTE_KEY_IS_CHECK_SIM).let {
            if (it == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG) 2 else it
        }
    }

    /**
     * 广告展示最短时间间隔，限制(毫秒)
     */
    val adShowMinDuration by lazy {
        FireBaseUtil.remoteConfig.getLong(EnvConfig.REMOTE_KEY_AD_SHOW_MIN_DURATION).let {
            if (it == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG) 60_000 else it * 1000
        }
    }

    /**
     * 使用函数方式获取更准确，但由于此值在firebase远程配置获取成功之后再使用，故不影响程序逻辑
     * 普通(广告)用户类型条件，检查当前设备是否已root，
     * 1:需要检查 没有root，2，不检查
     * 默认不检查
     */
    val isCheckRoot by lazy {
        FireBaseUtil.remoteConfig.getLong(EnvConfig.REMOTE_KEY_IS_CHECK_ROOT).let {
            if (it == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG) 2 else it
        }
    }

    /**
     * 使用函数方式获取更准确，但由于此值在firebase远程配置获取成功之后再使用，故不影响程序逻辑
     * 普通(广告)用户类型条件，检查当前设备是否开启自动化测试，
     * 1:需要检查 没有开启自动化测试，2，不检查
     * 默认不检查
     */
    val isCheckAutoTest by lazy {
        FireBaseUtil.remoteConfig.getLong(EnvConfig.REMOTE_KEY_IS_CHECK_AUTO_TEST).let {
            if (it == FirebaseRemoteConfig.DEFAULT_VALUE_FOR_LONG) 2 else it
        }
    }

    /**
     * service onDestroy 事件
     */
    val onServiceDestroy by lazy { ChangedVersionLiveData(true) }
}

/**
 * Tba后台埋点
 */
object TbaParamKey {
    /**
     * 普通埋点根节点key
     */
    const val KEY_POINT = "hen"
//    const val KEY_PARAM = "eddy"

    /**
     * 埋点参数key统一需要加的前缀，tba文档为准
     */
    const val INNER_KEY_PREFIX = "falcon_"
    /**
     * 埋点参数key统一需要加的后缀，tba文档为准
     */
//    const val INNER_KEY_SUFFIX = "^archive"

}

//======埋点参数相关begin


object Event {
    /**
     * 广告触发事件点
     */
    const val TRIGGER = "lav"

    /**
     * 广告load失败
     */
    const val LOAD_FAILED = "manu"

    /**
     * 广告准备检查
     */
    const val CHECK_READY = "arrive"

    /**
     * 广告show
     */
    const val SHOW_AD = "rotenne"

    /**
     * 记录referrer事件
     */
    const val REFERRER_URL = "introry"

    /**
     * 用户type上报
     */
    const val USER_TYPE = "fund"

    /**
     * 获取黑白名单接口错误
     */
    const val B_W_LIST_FAILED = "table"


    /**
     * 获取gaid错误
     */
    const val GET_GAID_ERROR = "cynior"

    /**
     * App安装事件
     *
     */
    const val APP_INSTALL = "scop"

    /**
     * app进程活跃事件
     */
    const val APP_PROCESS_ACTIVE = "supose"


    /**
     * 获取referrer错误
     */
    const val GET_REFERRER_ERROR = "taur"


    /**
     * 获取渠道方式
     *
     */
    const val STRATEGY_TYPE = "omni"

    /**
     * 广告中转页面启动
     *
     */
    const val AD_TRANS_PAGE_START_UP = "photoie"

    /**
     * Service启动错误事件
     */
    const val SERVICE_START_ERROR = "tric"

    /**
     * Firebase 拉取远程配置结果
     */
    const val FIREBASE_FETCH_RESULT = "subify"

}

/**
 * TBA服务端cloak对应值
 */
object CloakParamValue {
    const val TYPE_CLOAK = "baro"
    const val TYPE_NORMAL = "feeling"
}

/**
 * 触发广告事件参数值列表
 */
object TriggerParamValue {
    const val LOOP = "1"
    const val SCREEN_UNLOCK = "2"
    const val LOW_POWER = "3"
    const val APP_UNINSTALL = "4"
    const val APP_INSTALL = "5"
    const val APP_UPDATE = "6"
    const val CHARGE_START = "7"
    const val CHARGE_STOP = "8"
    const val NET_CHANGE = "9"
    const val BLUETOOTH_STATE_CHANGE = "10"
}

/**
 * 用户类型相关参数的表数值
 * 如 检测通过:a 检测不通过:b 忽略检查结果:ignore
 */
object UserTypeParamValue {
    const val PASS = "0"
    const val FAILED = "1"
    const val IGNORE = "2"
    const val CONNECT_CHAR = "|"
}

/**
 * 埋点通用参数key列表
 */
object CommonKey {
    /**
     * 埋点通用参数名称
     */
    const val PARAM = "lekan"

//    const val MODEL = "grouped"
}


object ClockParamKey {
    /**
     * 用户类型详情 (0|0|1|1|2(0))
     */
    const val USER_TYPE_DETAIL = "topite"
}
//======埋点参数相关end