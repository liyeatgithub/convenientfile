package com.hal.convenientfile.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.R
import com.hal.convenientfile.databinding.ActivityInfoBinding
import com.hal.convenientfile.entity.SourceEntity
import com.hal.convenientfile.util.AdUtil
import com.hal.convenientfile.utils.Constant
import java.text.SimpleDateFormat
import java.util.Date

/**
 *
 * @author hal
 * @date 2024/3/15
 * @description 信息页面
 */

class InfoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityInfoBinding.inflate(layoutInflater)
    }

    companion object {

        private const val ENTITY = "sourceEntity"

        @JvmStatic
        fun start(activity: Activity, sourceEntity: SourceEntity) {
            val intent = Intent(activity, InfoActivity::class.java)
            intent.putExtra(ENTITY, sourceEntity)
            activity.startActivity(intent)
        }
    }

    private var sourceEntity: SourceEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        sourceEntity = intent.getParcelableExtra(ENTITY)

        initData()

        initEvent()

    }

    private fun initEvent() {
        binding.ivBack.setOnClickListener {
            AdUtil.checkInnerAdAndGoOn(this) {
                onBackPressed()
            }
        }
    }

    private fun initData() {
        sourceEntity?.let {
            binding.tvNameTitle.text = it.displayName
            binding.tvName.text = it.displayName
            val sizeInMB = it.size.toDouble() / (1024 * 1024)
            binding.tvSize.text = String.format("%.2f MB", sizeInMB)
            binding.tvPath.text = it.data
            binding.tvType.text = it.mimeType
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            val date = Date(it.modifyDate * 1000)
            binding.tvLastModified.text = dateFormat.format(date)
            when (it.type) {
                Constant.IMAGE, Constant.VIDEO -> {
                    Glide.with(binding.ivCover)
                        .load(it.data)
                        .into(binding.ivCover)
                }

                Constant.AUDIO -> {
                    Glide.with(binding.ivCover)
                        .load(R.mipmap.music)
                        .into(binding.ivCover)
                }

                else -> {
                    Glide.with(binding.ivCover)
                        .load(R.mipmap.documents)
                        .into(binding.ivCover)
                }
            }
            binding.ivCover.setOnClickListener { v ->
                when (it.type) {
                    Constant.IMAGE -> {
                        CheckActivity.start(this, v, it.data)
                    }

                    Constant.VIDEO -> {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(Uri.parse(it.data), "video/*")
                        binding.root.context.startActivity(
                            Intent.createChooser(
                                intent,
                                "Open with"
                            )
                        )
                    }
                }
            }
        }
    }
}