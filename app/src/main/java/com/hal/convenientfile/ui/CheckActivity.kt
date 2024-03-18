package com.hal.convenientfile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.databinding.ActivityCheckBinding

/**
 *
 * @author hal
 * @date 2024/3/15
 * @description 查看图片界面
 */

class CheckActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity, view: View, uri: String) {
            val intent = Intent(activity, CheckActivity::class.java)
            intent.putExtra("uri", uri)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                view,
                "image"
            )

            activity.startActivity(intent, options.toBundle())
        }
    }


    private val binding by lazy {
        ActivityCheckBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
        val uri = intent.getStringExtra("uri")
        Glide.with(binding.ivImage)
            .load(uri)
            .into(binding.ivImage)
        binding.ivImage.setOnClickListener {
            onBackPressed()
        }
    }
}