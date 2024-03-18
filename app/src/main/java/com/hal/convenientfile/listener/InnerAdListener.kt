package com.hal.convenientfile.listener

import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialListener

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/3/8
 */
class InnerAdListener(
    private val onAdClose: ((ATAdInfo?) -> Unit)? = null,
    private val onAdLoaded: (() -> Unit)? = null
) : ATInterstitialListener {
    override fun onInterstitialAdLoaded() {
        onAdLoaded?.invoke()
    }

    override fun onInterstitialAdLoadFail(p0: AdError?) {
    }

    override fun onInterstitialAdClicked(p0: ATAdInfo?) {
    }

    override fun onInterstitialAdShow(p0: ATAdInfo?) {
    }

    override fun onInterstitialAdClose(p0: ATAdInfo?) {
        onAdClose?.invoke(p0)
    }

    override fun onInterstitialAdVideoStart(p0: ATAdInfo?) {
    }

    override fun onInterstitialAdVideoEnd(p0: ATAdInfo?) {
    }

    override fun onInterstitialAdVideoError(p0: AdError?) {
    }
}