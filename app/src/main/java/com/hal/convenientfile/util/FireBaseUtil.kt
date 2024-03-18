package com.hal.convenientfile.util

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.hal.convenientfile.base.BaseApp

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/10/30
 */
object FireBaseUtil {
//    val analytics by lazy { Firebase.analytics }

    val remoteConfig by lazy { Firebase.remoteConfig }

    private val retryMax = 10
    private var retryCount = 0

    /**
     * 第一次拉取配置时间
     */
    private var firstFetchTime = 0L

    /**
     * 拉取配置超时时间(ms)，部分老机型每次拉取事件很长(1分钟以上)
     */
    private val fetchTimeout = 120_000

    fun initializeApp() {
        FirebaseApp.initializeApp(BaseApp.app)
        firstFetchTime = System.currentTimeMillis()
        val completeListener = object : OnCompleteListener<Boolean> {
            override fun onComplete(task: Task<Boolean>) {
                //main thread
                log(
                    "====>fetchAndActivate count ",
                    retryCount,
                    " isSuccessful==", task.isSuccessful
                )
                if (!task.isSuccessful) {
                    val now = System.currentTimeMillis()
                    //没成功并且没超时则重试
                    if (retryCount++ < retryMax && now - firstFetchTime <= fetchTimeout) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            remoteConfig.fetchAndActivate().addOnCompleteListener(this)
                        }, 5000)
                    } else {
                        //上传失败事件,重试了10次
                        TbaUtil.logEvent(
                            Event.FIREBASE_FETCH_RESULT,
                            Bundle().apply { putString(CommonKey.PARAM, "-1") })
                        //发送拉取失败通知
                        AdUtil.firebaseFetchResult.value = false
                    }
                } else {
                    //上传获取成功事件
                    TbaUtil.logEvent(
                        Event.FIREBASE_FETCH_RESULT,
                        Bundle().apply { putString(CommonKey.PARAM, retryCount.toString()) })
                    //发送成功通知
                    AdUtil.firebaseFetchResult.value = true
                }
            }
        }
        remoteConfig.fetchAndActivate().addOnCompleteListener(completeListener)
    }

    /**
     * 触发广告事件
     */
    /*    fun logEventAdTrigger(value: String) {
            logEvent(Event.TRIGGER, Bundle().apply { putString(CommonKey.PARAM, value) })
    //        "trigger=>v:$value".log()
        }*/

    /* fun logEvent(key: String, value: Bundle) {
         analytics.logEvent(key, value)
         //tba后台同步
         val params = mutableMapOf<String, Any?>(
             TbaParamKey.KEY_POINT to key,
         ).apply {
             value.keySet().forEach {
                 put("$it${TbaParamKey.INNER_KEY_SUFFIX}", value[it])
             }
         }
         log("upEnt para==", params)
         TbaUtil.uploadEvent(params)
     }*/

    /*    fun logEventToFirebase(key: String, value: Bundle) {
            *//*   value.apply {
               putString(CommonKey.MODEL, Device.model)
           }*//*
        analytics.logEvent(key, value)
    }*/
}