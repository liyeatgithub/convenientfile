package com.hal.convenientfile.listener

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialListener
import com.hal.convenientfile.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/23
 */
class ScreenAdListener(
    /**
     * 是否需要开启重试机制
     */
    private val needRetry: Boolean = false
) : ATInterstitialListener {
    /**
     * 最大重试次数
     */
    private var retryMax = 2

    @Volatile
    private var retryTime = 0
    private var retryHandler: Handler? = null

    init {
        if (needRetry) {
            retryHandler = Handler(Looper.getMainLooper())
        }
    }

    override fun onInterstitialAdLoaded() {
        "onScreenAdLoaded...".apply {
            log()
//            toast()
        }
        if (needRetry) {
            retryTime = 0
        }
    }

    override fun onInterstitialAdLoadFail(p0: AdError?) {
        "onScreenAdLoadFail...".log()
        log("rT=", retryTime, " ad ld failed ", p0?.desc)
        TbaUtil.logEvent(Event.LOAD_FAILED, Bundle().apply {
            putString(
                CommonKey.PARAM,
                p0?.desc?.let { if (it.length > 90) it.substring(0, 90) else it }
                    ?: "unknown error"
            )
        })
        if (needRetry) {
            checkReload()
        }
    }

    @Synchronized
    private fun checkReload() {
        if (retryTime++ < retryMax) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(12_000)
                AdUtil.aliveAd.load()
            }
        } else {
            retryTime = 0
        }
    }

    override fun onInterstitialAdClicked(p0: ATAdInfo?) {
        "onScreenAdClicked...".log()
    }

    override fun onInterstitialAdShow(adInfo: ATAdInfo?) {
        "onScreenAdShow...".log()
        TbaUtil.logEvent(Event.SHOW_AD, Bundle().apply {
            putString(CommonKey.PARAM, "1")
        })
        adInfo?.let {
            log("adInfo===", it)
            //收入上传adjust
            AdUtil.handleAdjustRevenueReport(it)
            //收入上传tba
            TbaUtil.upLoadAdShowEvent(it)
        }
        "ad show success".apply {
            log()
//                        toast()
        }
    }

    override fun onInterstitialAdClose(p0: ATAdInfo?) {
        "onScreenAdClose...".log()
    }

    override fun onInterstitialAdVideoStart(p0: ATAdInfo?) {
        "onScreenAdVideoStart...".apply {
            log()
//                        toast()
        }

    }

    override fun onInterstitialAdVideoEnd(p0: ATAdInfo?) {
        "onScreenAdVideoEnd...".log()
    }

    override fun onInterstitialAdVideoError(p0: AdError?) {
        "onScreenAdVideoError...".log()
        val errorMsg = p0?.let { error ->
            log("ad video err==", error.fullErrorInfo)
            error.desc.let { if (it.length > 90) it.substring(0, 90) else it }
        } ?: "unknown error"
        TbaUtil.logEvent(Event.SHOW_AD, Bundle().apply {
            putString(CommonKey.PARAM, errorMsg)
        })
    }
}