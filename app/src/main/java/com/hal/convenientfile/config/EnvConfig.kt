package com.hal.convenientfile.config

/**
 * Description:环境变量配置，测试Config,发布时替换成正式值
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/27
 */
object EnvConfig {
    /**
     * 投放广告平台
     */
    val adChannels by lazy { listOf("commontest") }

    /**
     * topOn应用 aapId
     */
    const val TOP_ON_APP_ID = "a65a79f4776954"

    /**
     * topOn应用 aapKey
     */
    const val TOP_ON_APP_KEY = "059b50db4590ae6f43c9be5783994cc9"

    /**
     * 开屏插屏广告replacementId
     */
    const val TOP_ON_PLACEMENT_ID = "b65a79f7fcc81c"

    /**
     * 保活sdk体外广告replacementId
     */
    const val TOP_ON_KEEP_PLACEMENT_ID = "b65a79f7f4c892"

    /**
     * adjust appToken
     */
    const val ADJUST_APP_TOKEN = "l9arfeq33400"

    /**
     * 广告轮训时间，数字类型，秒
     */
    const val REMOTE_KEY_LOOP_TIME = "adint"

    /**
     * 1: adjust 归因为准
     * 2：google play referrer 归因为准
     */
    const val REMOTE_KEY_CHANNEL_STRATEGY = "channelStrategy"

    /**
     * 广告最短显示时间间隔限制,数字(单位秒)
     */
    const val REMOTE_KEY_AD_SHOW_MIN_DURATION = "adShowMinDuration"

    /**
     * 普通(广告)用户类型条件，检查sim卡是否存在，
     * 1:需要检查 sim存在，2，不检查
     */
    const val REMOTE_KEY_IS_CHECK_SIM = "isCheckSim"

    /**
     * 普通(广告)用户类型条件，检查当前设备是否已root，
     * 1:需要检查 没有root，2，不检查
     */
    const val REMOTE_KEY_IS_CHECK_ROOT = "isCheckRoot"

    /**
     * 普通(广告)用户类型条件，检查当前设备是否开启自动化测试，
     * 1:需要检查 没有开启自动化测试，2，不检查
     */
    const val REMOTE_KEY_IS_CHECK_AUTO_TEST = "isCheckAutoTest"

    /**
     * 隐私连接
     */
    const val PRIVACY_URL = "https://www.google.com"
}