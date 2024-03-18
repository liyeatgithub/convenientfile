package com.hal.convenientfile.util

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.ATSDK
import com.anythink.interstitial.api.ATInterstitial
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.config.EnvConfig
import com.hal.convenientfile.listener.InnerAdListener
import com.hal.convenientfile.listener.ScreenAdListener
import kotlin.concurrent.thread

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/10/28
 */
object AdUtil {
    private val app by lazy { BaseApp.app }

    /**
     * 插屏广告
     */
    val interstitialAd by lazy { ATInterstitial(app, Ad.innerPlacementId) }

    /**
     * 体外广告
     */
    val aliveAd by lazy {
        ATInterstitial(app, Ad.alivePlacementId).apply {
            setAdListener(ScreenAdListener(needRetry = true))
        }
    }

    /**
     * 原生广告加载livedata
     */
    val nativeAdLoadedFlag by lazy { MutableLiveData(false) }


    /**
     * 全局广告id
     */
    val gaid by lazy {
        ChangedVersionLiveData(SPUtil.getString(SpKey.SP_KEY_GAID, null))
    }


    /**
     * 用户是否在黑名单("1":在黑名单,"0":不在黑名单)
     */
    val isInBlackList by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_IN_BLACK_LIST,
                null
            )
        )
    }

    /**
     * 是否在非投放渠道下载(非投放渠道下载则判断为黑名单,"1":是第三方平台下载，"0":是投放平台下载)
     */
    val isInThirdPlatform by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_IN_THIRD_PLATFORM,
                null
            )
        )
    }

    /**
     * 检查自动测试的结果(检查过程由firebase下发的检查策略(可能检查或忽略此条件)决定)
     */
    val checkAutoTestResult by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_CHECK_AUTO_TEST_RESULT,
                null
            )
        )
    }

    /**
     * 是否检查自动测试，上传用户事件会用到
     * 1:需要检查 0:不需要检查
     */
    val isCheckAutoTest by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_IS_CHECK_AUTO_TEST,
                null
            )
        )
    }

    /**
     * 检查root环境的结果(检查过程由firebase下发的检查策略(可能检查或忽略此条件)决定)
     */
    val checkRootResult by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_CHECK_ROOT_RESULT,
                null
            )
        )
    }

    /**
     * 是否检查root，上传用户事件会用到
     * 1:需要检查 0:不需要检查
     */
    val isCheckRoot by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_CHECK_ROOT_RESULT,
                null
            )
        )
    }

    /**
     * 检查sim卡结果(检查过程由firebase下发的检查策略(可能检查或忽略此条件)决定)
     */
    val checkSimResult by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_CHECK_SIM_RESULT,
                null
            )
        )
    }

    /**
     * 是否检查sim卡，上传用户事件会用到
     * 1:需要检查 0:不需要检查
     */
    val isCheckSim by lazy {
        ChangedVersionLiveData(
            SPUtil.getString(
                SpKey.SP_KEY_CHECK_SIM_RESULT,
                null
            )
        )
    }


    /**
     * 广告分钟是否被限制("1":限制  "0":不限制)
     */
    val isLimitAdTracking by lazy {
        ChangedVersionLiveData(SPUtil.getString(SpKey.SP_KEY_IS_LIMIT_AD_TRACKING, null))
    }

    /**
     * 获取用户渠道的途径
     * 1:使用adjust返回渠道
     * 2:使用app store ref渠道
     */
//    val channelStrategy by lazy { NoStickyLiveData<Long?>(null) }

    /**
     * adjust返回的渠道字符串
     */
    val adjustChannel by lazy { ChangedVersionLiveData<String?>(null) }

    /**
     * app store 返回ref字符串
     */
    val refChannel by lazy { ChangedVersionLiveData<String?>(null) }

    /**
     * firebase远程配置拉取结果
     */
    val firebaseFetchResult by lazy { ChangedVersionLiveData<Boolean?>(null) }


    /**
     * 体内最后一次显示内部广告的时间
     */
    @Volatile
    var lastInnerShowTime = 0L

    /**
     * 体内广告最短显示时间间隔
     */
    val aDdurationTime = 120_000


    fun initUserAdInfo() {
//        "androidId=${Device.androidId}".log()
        val gaid = AdUtil.gaid.value
        val limitAdTracking = AdUtil.isLimitAdTracking.value
//        "get gaid from sp == $gaid".log()
//        "get limitAdTracking from sp == $limitAdTracking".log()
        if (gaid.isNullOrEmpty() || limitAdTracking.isNullOrEmpty()) {
            thread {
                //必须子线程start
                //获取广告id
                try {
                    AdvertisingIdClient(BaseApp.app).apply {
                        start()
                        AdUtil.gaid.postValue(info.id)
                        //不管是否为空都存入
                        SPUtil.putString(SpKey.SP_KEY_GAID, info.id ?: "")
                        val isLimitAdTracking = if (info.isLimitAdTrackingEnabled) "1" else "0"
                        SPUtil.putString(SpKey.SP_KEY_IS_LIMIT_AD_TRACKING, isLimitAdTracking)
                        AdUtil.isLimitAdTracking.postValue(isLimitAdTracking)
                        log(
                            "info gaid==",
                            info.id,
                            "\nlimit tracking==",
                            info.isLimitAdTrackingEnabled
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    log("get gaid error==>", e.message)
                    SPUtil.putString(SpKey.SP_KEY_GAID, "")
                    AdUtil.gaid.postValue("")
                    SPUtil.putString(SpKey.SP_KEY_IS_LIMIT_AD_TRACKING, "0")
                    isLimitAdTracking.postValue("0")
                    TbaUtil.logEvent(Event.GET_GAID_ERROR, Bundle().apply {
                        putString(CommonKey.PARAM, e.message ?: "unknown error")
                    })
                }
            }
        }
    }


    fun initTopOn() {
        //应用上线前须关闭
//        ATSDK.setNetworkLogDebug(BlingWallApplication.app.isDebug)
        //初始化首页插屏广告
        val topOnId = EnvConfig.TOP_ON_APP_ID
        val topOnKey = EnvConfig.TOP_ON_APP_KEY
        ATSDK.init(BaseApp.app, topOnId, topOnKey)
        //预加载广告
        interstitialAd.load()
        aliveAd.load()
    }

    fun checkInnerAdAndGoOn(activity: Activity, onAdClose: () -> Unit) {
        val now = System.currentTimeMillis()
        if ((now - lastInnerShowTime).apply {
                log(
                    "innerAd time duration is  = ",
                    this,
                    " ready = ",
                    interstitialAd.isAdReady
                )
            } > aDdurationTime) {
            //show ad
            interstitialAd.let {
                if (it.isAdReady) {
                    it.setAdListener(InnerAdListener(onAdClose = {
                        onAdClose()
                    }))
                    it.show(activity)
                    lastInnerShowTime = System.currentTimeMillis()
                    it.load()
                } else {
                    it.load()
                    onAdClose()
                }
            }
        } else {
            onAdClose()
        }
    }

    /**
     * 上传广告数据跟踪事件到adjust后台
     *
     */
    fun handleAdjustRevenueReport(atAdInfo: ATAdInfo) {
        val adjustAdRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_SOURCE_PUBLISHER).apply {
            setRevenue(atAdInfo.publisherRevenue, atAdInfo.currency)
            adRevenueNetwork = getAdPlatform(atAdInfo.networkFirmId)
            adRevenueUnit = atAdInfo.adsourceId
            adRevenuePlacement = atAdInfo.topOnPlacementId
        }
        //发送收益数据
        Adjust.trackAdRevenue(adjustAdRevenue)
    }

    fun getAdPlatform(networkFirmId: Int): String = when (networkFirmId) {
        1 -> {
            "Facebook"
        }

        2 -> {
            "Admob"
        }

        3 -> {
            "Inmobi"
        }

        4 -> {
            "Flurry"
        }

        5 -> {
            "Applovin"
        }

        6 -> {
            "Mintegral"
        }

        12 -> {
            "UnityAds"
        }

        13 -> {
            "Vungle"
        }

        34 -> {
            "Yandex"
        }

        50 -> {
            "Pangle"
        }

        59 -> {
            "Bigo"
        }

        66 -> {
            "Adx"
        }

        else -> {
            "other"
        }
    }

}