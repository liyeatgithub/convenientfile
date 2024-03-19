package com.hal.convenientfile.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.databinding.ActivitySplashBinding
import com.hal.convenientfile.listener.InnerAdListener
import com.hal.convenientfile.util.AdUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    @Volatile
    private var ndShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
        handler.postDelayed({
            jumpToNextPage()
        }, 5000)
        initInnerAd()
    }


    private fun initInnerAd() {
        AdUtil.interstitialAd.let {
            if (!it.isAdReady) {
                it.setAdListener(InnerAdListener(onAdLoaded = {
                    if (it.isAdReady && ndShow) {
                        //防止load方法循环调用
                        ndShow = false
                        handler.removeCallbacksAndMessages(null)
                        it.show(this@SplashActivity)
                        it.load()
                        AdUtil.lastInnerShowTime = System.currentTimeMillis()
                    }
                }, onAdClose = {
                    jumpToNextPage()
                }))
            } else {
                it.setAdListener(InnerAdListener(onAdClose = {
                    jumpToNextPage()
                }))
                it.show(this)
                it.load()
                AdUtil.lastInnerShowTime = System.currentTimeMillis()
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    private fun jumpToNextPage() {
        //去掉监听
        AdUtil.interstitialAd.setAdListener(null)
        handler.removeCallbacksAndMessages(null)
        ndShow = false
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}