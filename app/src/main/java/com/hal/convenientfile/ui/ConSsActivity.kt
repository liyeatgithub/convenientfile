package com.hal.convenientfile.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.hal.convenientfile.R
import com.hal.convenientfile.base.BaseApp
import com.hal.convenientfile.service.MiTopConService
import com.hal.convenientfile.util.tryCatch

class ConSsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_con_ss)
        tryCatch { startForegroundService(Intent(BaseApp.app, MiTopConService::class.java)) }
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 1000)
    }
}