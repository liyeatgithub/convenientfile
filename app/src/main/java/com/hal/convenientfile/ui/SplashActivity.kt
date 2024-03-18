package com.hal.convenientfile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
        lifecycleScope.launch {
            delay(500)
            MainActivity.start(this@SplashActivity)
            finish()
        }
    }
}