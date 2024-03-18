package com.hal.convenientfile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hal.convenientfile.R
import com.hal.convenientfile.util.AdUtil
import com.hal.convenientfile.util.Event
import com.hal.convenientfile.util.TbaUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConmiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conmi)

        handleStartAd()
    }

    private fun handleStartAd() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            //延迟500毫秒，避免sdk 弹出系统设置页面，遮挡住广告的bug
            if (AdUtil.aliveAd.isAdReady) {
                AdUtil.aliveAd.let {
                    it.show(this@ConmiActivity)
                    //加载下次广告资源
                    it.load()
                }
            } else {
                //加载下次广告资源
                AdUtil.aliveAd.load()
            }
            delay(1500)
            finish()
        }
        TbaUtil.logEvent(Event.AD_TRANS_PAGE_START_UP, Bundle())
    }
}