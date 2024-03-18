package com.hal.convenientfile.util

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.hal.convenientfile.base.BaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/3/13
 */
object ReferrerUtil {
    private val referrerClient by lazy { InstallReferrerClient.newBuilder(BaseApp.app).build() }

    fun initInstallRefer() {
        //没有上传安装事件信息，则需要获取相应参数
        //或者没有上传referrer，则需要获取referrerUrl
        if (AppUtil.isUploadInstallEvent.value != true || AppUtil.isUploadReferrer.value != true) {
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                // Connection established.
//                            "Connection established...".log()
                                CoroutineScope(Dispatchers.IO).launch {
                                    //安装好最好只调用一次
                                    val response: ReferrerDetails = referrerClient.installReferrer
                                    var referrerUrl: String =
                                        response.installReferrer /*+ "adjust"*/
                                    log("ref===", response.installReferrer)
                                    if (AppUtil.isUploadReferrer.value != true) {
                                        AppUtil.referrerUrl.postValue(referrerUrl)
                                        //通知用户判断策略监听(不重复通知)
                                        changeRefChannel(referrerUrl)
                                        if (referrerUrl.length > 90) {
                                            referrerUrl = referrerUrl.substring(0, 90)
                                        }
                                        TbaUtil.logEvent(
                                            Event.REFERRER_URL,
                                            Bundle().apply {
                                                putString(
                                                    CommonKey.PARAM,
                                                    referrerUrl
                                                )
                                            })
//                                    "referrerUrl==$referrerUrl".log()
                                        SPUtil.putBoolean(SpKey.SP_KEY_IS_UPLOAD_REFERRER, true)
                                        AppUtil.isUploadReferrer.postValue(true)
                                    }
                                    //获取安装事件需要参数
                                    if (AppUtil.isUploadInstallEvent.value != true) {
                                        //引荐来源网址点击事件发生时的客户端时间戳
                                        val referrerClickTime =
                                            response.referrerClickTimestampSeconds
                                        AppUtil.referrerClickTime.postValue(referrerClickTime)
                                        //引荐来源网址点击事件发生时的服务端时间戳
                                        val referrerClickServerTime =
                                            response.referrerClickTimestampServerSeconds
                                        AppUtil.referrerClickServerTime.postValue(
                                            referrerClickServerTime
                                        )
                                        //应用安装时客户端时间
                                        val appInstallTime = response.installBeginTimestampSeconds
//                                    "get appInstallTime==$appInstallTime".log()
                                        //记录安装时间到本地
                                        SPUtil.putLong(
                                            SpKey.SP_KEY_APP_INSTALL_TIME_SECONDS,
                                            appInstallTime
                                        )
                                        AppUtil.installBeginTime.postValue(appInstallTime)
                                        //应用安装时服务端时间
                                        val appInstallServerTime =
                                            response.installBeginTimestampServerSeconds
                                        AppUtil.installBeginServerTime.postValue(
                                            appInstallServerTime
                                        )

                                        //referrerUrl install事件会使用,adjust 渠道校验
                                        AppUtil.referrerUrl.postValue(referrerUrl)
                                        //通知用户判断策略监听(如果有值不重复通知)
                                        changeRefChannel(referrerUrl)
                                        //installVersion install事件会使用
                                        val installVersion: String =
                                            response.installVersion ?: BaseApp.app.packageName
                                        AppUtil.installVersion.postValue(installVersion)
                                    }
                                    referrerClient.endConnection()
                                }
                            }

                            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                                // API not available on the current Play Store app.
                                "FEATURE_NOT_SUPPORTED...".log()
                                postReferrerParams()
                                uploadReferrerError("feature not supported")
                            }

                            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                                // Connection couldn't be established.
                                "SERVICE_UNAVAILABLE...".log()
                                postReferrerParams()
                                uploadReferrerError("service unavailable")
                            }

                            InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
                                // DEVELOPER_ERROR
                                "DEVELOPER_ERROR...".log()
                                postReferrerParams()
                                uploadReferrerError("developer error")
                            }

                            InstallReferrerClient.InstallReferrerResponse.PERMISSION_ERROR -> {
                                // PERMISSION_ERROR
                                "PERMISSION_ERROR...".log()
                                postReferrerParams()
                                uploadReferrerError("permission error")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }
    }

    private fun changeRefChannel(referrerUrl: String) {
        Handler(Looper.getMainLooper()).post {
            if (AdUtil.refChannel.value == null) {
                AdUtil.refChannel.value = referrerUrl
            }
        }
    }

    private fun uploadReferrerError(msg: String) {
        TbaUtil.logEvent(
            Event.GET_REFERRER_ERROR,
            Bundle().apply {
                putString(
                    CommonKey.PARAM,
                    msg
                )
            })
    }

    /**
     * 当获取referrer错误时通知install事件
     */

    private fun postReferrerParams() {
        val now = System.currentTimeMillis()
        AdUtil.isLimitAdTracking.postValue("0")
        AppUtil.referrerClickTime.postValue(now)
        AppUtil.referrerClickServerTime.postValue(now)
        AppUtil.installBeginTime.postValue(now)
        AppUtil.installBeginServerTime.postValue(now)
        AppUtil.referrerUrl.postValue("")
        //通知用户判断策略监听(如果有值不重复通知)
        if (AdUtil.refChannel.value == null) {
            AdUtil.refChannel.postValue("")
        }
        AppUtil.installVersion.postValue(BaseApp.app.packageName)
    }

}