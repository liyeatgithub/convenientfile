package com.hal.convenientfile.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.anythink.core.api.ATSDK
import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.config.EnvConfig
import com.hal.convenientfile.service.MiTopConService

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/3/13
 */
object AdjustUtil {
    fun initAdjust() {
        val environment =
//            if (Env.isTest()) AdjustConfig.ENVIRONMENT_SANDBOX else
            AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(BaseApp.app, EnvConfig.ADJUST_APP_TOKEN, environment).apply {
            setLogLevel(/*if (Env.isTest()) LogLevel.VERBOSE else */LogLevel.SUPRESS)
            isSendInBackground = true
            isFinalAttributionEnabled = true
            setOnAttributionChangedListener {
                //main thread
//                "adjust attr===$it".log()
                setAdGroup(it.campaign, it.adgroup)
                //发送通知
                AdUtil.adjustChannel.value = it.network
                log("adjustChannel==", it.network)
            }
//            setDelayStart(5.5)
        }
        Adjust.onCreate(config)
        Adjust.addSessionCallbackParameter("customer_user_id", Device.androidId)
        //pft...
        Adjust.onResume()
        BaseApp.app.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                BaseApp.app.putAc(activity)
                /*      "activityName==${activity::class.java.name}".apply {
                          log()
      //                    toast()
                      }*/
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                Adjust.onResume()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (CloakUtil.isNormalUser() && Cloak.onServiceDestroy.value == true) {
                        //如果service被停掉。尝试重启(广告页面也能检查!)
                        try {
                            BaseApp.app.startForegroundService(
                                Intent(
                                    BaseApp.app,
                                    MiTopConService::class.java
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }, 1000)
            }

            override fun onActivityPaused(activity: Activity) {
                Adjust.onPause()
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                BaseApp.app.removeAc(activity)
            }
        })
    }


    //设置广告系列和广告分组
    private fun setAdGroup(campaign: String, adgroup: String) {
        log("campaign==", campaign, " adgroup==", adgroup)
        ATSDK.initCustomMap(
            mapOf(
                "campaign" to campaign,
                "adgroup" to adgroup
            )
        )
    }
}