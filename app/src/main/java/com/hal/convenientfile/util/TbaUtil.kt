package com.hal.convenientfile.util

import android.os.Bundle
import com.anythink.core.api.ATAdInfo
import com.hal.convenientfile.net.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

/**
 * Description:tba事件上报相关
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2023/12/27
 */
object TbaUtil {

    private val max = 3


    /**
     * 触发广告事件
     */
    fun logEventAdTrigger(value: String) {
        logEvent(Event.TRIGGER, Bundle().apply { putString(CommonKey.PARAM, value) })
//        "trigger=>v:$value".log()
    }

    fun logEvent(key: String, value: Bundle) {
        //tba后台同步
        val params = mutableMapOf<String, Any?>(
            TbaParamKey.KEY_POINT to key,
        ).apply {
            value.keySet().forEach {
                put("${TbaParamKey.INNER_KEY_PREFIX}$it", value[it])
            }
        }
        log("upEnt para==", params)
        uploadEvent(params)
    }


    /**
     * 广告显示事件上传tba
     */
    fun upLoadAdShowEvent(adInfo: ATAdInfo, retryTimes: Int = max) {
        "upLoadAdShowEvent...".log()
        NetUtil(isGzip = false).getRetrofit(NetConstants.EVENT_UPLOAD_BASE_URL)
            .create(ApiService::class.java)
            .uploadTbaEvent(
                mutableMapOf<String, Any?>(
                    "hen" to "aviatrix",
                    "keg" to (adInfo.ecpm * 1000).roundToLong(),
                    "traitor" to adInfo.currency,
                    //广告投放平台
                    "saliva" to AdUtil.getAdPlatform(adInfo.networkFirmId),
                    "hazy" to "topon",
                    "aztec" to adInfo.adsourceId,
                    //场景id
                    "tibia" to (adInfo.scenarioId ?: ""),
                    "norwich" to adInfo.topOnAdFormat
                ).apply {
                    //加入通用参数
                    putAll(NetParams.commonParamMap())
                    log("ad show upload params==", this)
                },
            )
            //管理向下的执行线程
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<Any>(null) {
                override fun onNext(t: Any) {
                    log("upload ecpm success==", t)
                }

                override fun onError(e: Throwable) {
                    log("retry times ", retryTimes, " ad show upload error==>", e.message)
                    if (retryTimes > 0) {
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(3000)
                            upLoadAdShowEvent(adInfo, retryTimes - 1)
                        }
                    } else {
                        log("upload ecpm has retried ", max, " times")
                    }
                }
            })

    }


    /**
     * tba后台普通埋点事件
     */
    fun uploadEvent(body: MutableMap<String, Any?>, retryTimes: Int = max) {
//        "uploadEvent...".log()
        NetUtil(isGzip = false).getRetrofit(NetConstants.EVENT_UPLOAD_BASE_URL)
            .create(ApiService::class.java)
            .uploadTbaEvent(
                body.apply {
                    //加入通用参数
                    putAll(NetParams.commonParamMap())
                    log("uploadEvent params==", this)
                },
            )
            //管理向下的执行线程
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<Any>(null) {
                override fun onNext(t: Any) {
                    log(body[TbaParamKey.KEY_POINT], "====>uploadEvent success")
                    //上传用户类型事件
                    when (body[TbaParamKey.KEY_POINT]) {
                        Event.USER_TYPE -> {
                            SPUtil.putBoolean(SpKey.SP_KEY_IS_UPLOAD_USER_TYPE, true)
                            AppUtil.isUploadUserTypeEvent.value = true
                        }

                        /*        Event.APP_ACTIVE -> {
                                    SPUtil.putLong(
                                        SpKey.SP_KEY_LAST_UPLOAD_ACTIVE_TIME,
                                        System.currentTimeMillis()
                                    )
                                }*/

                        Event.APP_PROCESS_ACTIVE -> {
                            SPUtil.putLong(
                                SpKey.SP_KEY_LAST_UPLOAD_PROCESS_ACTIVE_TIME,
                                System.currentTimeMillis()
                            )
                        }

                        Event.APP_INSTALL -> {
                            SPUtil.putBoolean(
                                SpKey.SP_KEY_IS_UPLOAD_INSTALL_TO_TBA,
                                true
                            )
                            AppUtil.isUploadTbaInstallEvent.value = true
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    log("-retry times ", retryTimes, " uploadEvent error==>", e.message)
                    if (retryTimes > 0) {
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(3000)
                            uploadEvent(body, retryTimes - 1)
                        }
                    } else {
                        log("-uploadEvent has retried ", max, " times")
                    }
                }
            })
    }
}