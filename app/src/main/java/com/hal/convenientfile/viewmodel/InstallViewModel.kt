package com.hal.convenientfile.viewmodel

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import com.hal.convenientfile.net.*
import com.hal.convenientfile.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/3/11
 */
object InstallViewModel : ViewModel(), LifecycleOwner {

    private val mLifecycleRegistry by lazy { LifecycleRegistry(this) }
    private val disposable by lazy { CompositeDisposable() }

    /**
     * install 事件
     */
    private var retryCount = 3

    init {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    /**
     * install 上报接口必须参数
     */
    private val installEventParams by lazy {
        listOf(
            AdUtil.isLimitAdTracking,
            AppUtil.referrerClickTime,
            AppUtil.referrerClickServerTime,
            AppUtil.installBeginTime,
            AppUtil.installBeginServerTime,
            AppUtil.referrerUrl,
            AppUtil.installVersion
        )
    }

    fun tryUploadInstallEvent() {
        //还未上传安装参数
        if (AppUtil.isUploadInstallEvent.value.apply {
                log("sp isUploadInstallEvent=", this)
            } != true) {
            if (checkParams()) {
                //参数已经准备好
                upLoadInstallEvent()
            } else {
                //监听参数变化(BlingWallApplication中已经开始请求)
                Handler(Looper.getMainLooper()).post {
                    installEventParams.filter { it.value == null }.forEach { unPeekLiveData ->
                        unPeekLiveData.observe(this@InstallViewModel) {
                            if (checkParams()) {
                                upLoadInstallEvent()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun upLoadInstallEvent() {
        if (AppUtil.isUploadInstallEvent.value == true) {
            return
        }
        Handler(Looper.getMainLooper()).post {
            NetUtil(isGzip = false).getRetrofit(NetConstants.EVENT_UPLOAD_BASE_URL)
                .create(ApiService::class.java)
                .uploadTbaEvent(
                    mutableMapOf<String, Any?>(
                        "bourn" to mapOf(
                            "altruism" to Build.ID,
                            "eeoc" to AppUtil.userWebViewAgent,
                            "awl" to if (AdUtil.isLimitAdTracking.value != "0") "usa" else "accuse",
                            "burg" to AppUtil.referrerClickTime.value,
                            "quanta" to AppUtil.installBeginTime.value,
                            "cowbird" to AppUtil.referrerClickServerTime.value,
                            "ulan" to AppUtil.installBeginServerTime.value,
                            "forge" to AppUtil.firstInstallTime,
                            "bun" to AppUtil.lastUpdateTime
                        )
                    ).apply {
                        //加入通用参数
                        putAll(NetParams.commonParamMap())
                        log("===>install params==", this)
                    },
                )
                //管理向下的执行线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<Any>(disposable) {
                    override fun onNext(t: Any) {
                        log("===>install event success==", t)
                        SPUtil.putBoolean(
                            SpKey.SP_KEY_IS_UPLOAD_INSTALL_EVENT,
                            true
                        )
                        AppUtil.isUploadInstallEvent.value = true
                    }

                    override fun onError(e: Throwable) {
                        log("===>install event error==>", e.message)
                        //重试机制
                        if (retryCount > 0) {
                            retryCount--
                            "retry upload install event...".log()
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                upLoadInstallEvent()
                            }
                        }
                    }
                })
        }
    }

    /**
     * 检查 install 事件所需参数是否已经准备完毕
     */
    private fun checkParams() = installEventParams.count { it.value == null } == 0

    override fun onCleared() {
        disposable.dispose()
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    override val lifecycle: Lifecycle
        get() = mLifecycleRegistry

}