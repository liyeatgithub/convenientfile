package com.hal.convenientfile.util

import android.content.Context
import com.hal.convenientfile.base.BaseApp

/**
 * Description:
 * Author: Icarus.Lee
 * Email: 769247877@qq.com
 * Date: 2022/10/10
 */
object SPUtil {

    private val sp by lazy {
        BaseApp.app.getSharedPreferences(
            "app_sp",
            Context.MODE_PRIVATE
        )
    }

    fun putString(key: String, value: String?) = sp.edit().putString(key, value).apply()

    fun getString(key: String, default: String? = "") = sp.getString(key, default)

    fun putBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()

    fun getBoolean(key: String, default: Boolean = false) = sp.getBoolean(key, default)

    fun putInt(key: String, value: Int) = sp.edit().putInt(key, value).apply()

    fun getInt(key: String, default: Int = 0) = sp.getInt(key, default)
    fun putLong(key: String, value: Long) = sp.edit().putLong(key, value).apply()

    fun getLong(key: String, default: Long = 0) = sp.getLong(key, default)

    fun putStringSet(key: String, value: Set<String>) = sp.edit().putStringSet(key, value).apply()

    fun getStringSet(key: String, default: Set<String> = setOf()) =
        sp.getStringSet(key, default) ?: setOf()

}