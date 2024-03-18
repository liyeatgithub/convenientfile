package com.hal.convenientfile.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.hal.convenientfile.base.BaseApp
import java.net.URLEncoder

/**
 * Description:
 * Author: icarus
 * Email: 769247877@qq.com
 * Date: 2023/12/21
 */

fun <T> T.log(tag: String = "liye") {
    if (BaseApp.app.isDebug) {
        Log.i(tag, toString())
    }
//    this.toast()
}

fun <T> T.toast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.getMainLooper() != Looper.myLooper()) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(BaseApp.app, toString(), duration).show()
        }
    } else {
        Toast.makeText(BaseApp.app, toString(), duration).show()
    }
}

/**
 * 将字符转化此Url格式编码
 */
fun String.urlEncoding() = URLEncoder.encode(this, "UTF-8")

/**
 * 使用此方法可避免Stringfog不加密,
 * kotlin 使用$拼接的字符串(编译后会替换成StringBuilder,会触发Stringfog的bug)问题
 */
fun log(vararg args: Any?) {
    var result = ""
    args.forEach {
        result += it.toString()
    }
    Log.i("liye", result)
//    result.toast()
}

fun tryCatch(catch: ((Exception) -> Unit)? = null, block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        if (catch != null) catch(e) else {
//            e.printStackTrace()
            log("error==>\n", e.message)
        }
    }
}