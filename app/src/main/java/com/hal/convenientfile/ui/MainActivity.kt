package com.hal.convenientfile.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.adapter.MainApkAdapter
import com.hal.convenientfile.adapter.MainSourceAdapter
import com.hal.convenientfile.databinding.ActivityMainBinding
import com.hal.convenientfile.utils.Constant
import com.hal.convenientfile.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description 主页
 */

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private val isAllGranted: MutableLiveData<Boolean> = MutableLiveData(true)

    private val mainImageAdapter by lazy {
        MainSourceAdapter()
    }

    private val mainVideoAdapter by lazy {
        MainSourceAdapter()
    }

    private val mainApkAdapter by lazy {
        MainApkAdapter()
    }

    //大于等于安卓13的时候申请的权限
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionArraysHigher = arrayOf(
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    //小于安卓13的时候申请的权限
    private val permissionArraysLower = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        initData()

        initEvent()

    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isAllPermissionsGranted(permissionArraysHigher)) {
                lifecycleScope.launch {
                    loadData()
                }
            }
        } else {
            if (isAllPermissionsGranted(permissionArraysLower)) {
                lifecycleScope.launch {
                    loadData()
                }
            }
        }
    }

    private fun initEvent() {

        binding.clStorage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Toast.makeText(
                    this,
                    "Android system version too high",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (isAllPermissionsGranted(permissionArraysLower)) {
                    StorageActivity.start(this)
                } else {
                    Toast.makeText(
                        this,
                        "Please grant permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        //图片
        binding.llImage.setOnClickListener {
            BrowseActivity.start(this, Constant.IMAGE)
        }
        binding.tvViewMoreImage.setOnClickListener {
            BrowseActivity.start(this, Constant.IMAGE)
        }

        //视频
        binding.llVideo.setOnClickListener {
            BrowseActivity.start(this, Constant.VIDEO)
        }
        binding.tvViewMoreVideo.setOnClickListener {
            BrowseActivity.start(this, Constant.VIDEO)
        }
        //music
        binding.llAudio.setOnClickListener {
            BrowseActivity.start(this, Constant.AUDIO)
        }

        //apk
        binding.llApks.setOnClickListener {
            BrowseActivity.start(this, Constant.APKS)
        }
        binding.tvViewMoreApks.setOnClickListener {
            BrowseActivity.start(this, Constant.APKS)
        }
        //下载
        binding.llDownloads.setOnClickListener {
            BrowseActivity.start(this, Constant.DOWNLOAD)
        }

        //文档
        binding.llDocuments.setOnClickListener {
            BrowseActivity.start(this, Constant.DOCUMENT)
        }
    }

    private fun isAllPermissionsGranted(permission: Array<String>): Boolean {
        for (str in permission) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    str
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    private fun initData() {

        //计算所使用容量
        calculateUsedSpace()

        isAllGranted.observe(this) {
            if (it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (isAllPermissionsGranted(permissionArraysHigher)) {
                        lifecycleScope.launch {
                            loadData()
                        }
                    }
                } else {
                    if (isAllPermissionsGranted(permissionArraysLower)) {
                        lifecycleScope.launch {
                            loadData()
                        }
                    }
                }
            }
        }

        /**
         * 申请权限
         */
        //判断当前系统版本是否大于等于Android 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsGranted(permissionArraysHigher)
        } else {
            permissionsGranted(permissionArraysLower)
        }

        binding.rvImage.adapter = mainImageAdapter
        binding.rvVideo.adapter = mainVideoAdapter
        binding.rvApks.adapter = mainApkAdapter

    }

    //加载数据
    private suspend fun loadData() {

        //加载图像
        try {
            FileUtils.getImages(contentResolver).also {
                withContext(Dispatchers.Main) {
                    mainImageAdapter.submitList(it)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mainImageAdapter.submitList(mutableListOf())
            }
        }

        try {
            //加载视频
            FileUtils.getVideos(contentResolver).also {
                withContext(Dispatchers.Main) {
                    mainVideoAdapter.submitList(it)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mainVideoAdapter.submitList(mutableListOf())
            }
        }

        try {
            //加载apk文件
            FileUtils.getApks(contentResolver).also {
                withContext(Dispatchers.Main) {
                    mainApkAdapter.submitList(it)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mainApkAdapter.submitList(mutableListOf())
            }
        }
    }

    private fun permissionsGranted(permissionArrays: Array<String>) {
        if (!isAllPermissionsGranted(permissionArrays)) {
            val activityResultLauncher =
                registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { result: Map<String, Boolean> ->
                    for ((_, isGranted) in result) {
                        if (!isGranted) {
                            isAllGranted.value = false
                            return@registerForActivityResult
                        }
                    }
                    isAllGranted.value = true
                }
            activityResultLauncher.launch(permissionArrays)
        }
    }

    private fun calculateUsedSpace() {
        // 读取内置储存卡的大小，并按内存使用比例设置进度所占大小
        val internalStorageSize = String.format(
            "%.2f",
            getInternalStorageSize() / (1024.0 * 1024.0 * 1024.0)
        ) // 转换为GB并保留两位小数
        Log.e("halo", "internalStorageSize: $internalStorageSize")

        val internalStorageUsed = String.format(
            "%.2f",
            calculateUsedSpaceForInternalStorage() / (1024.0 * 1024.0 * 1024.0)
        ) // 转换为GB并保留两位小数
        Log.e("halo", "internalStorageUsed: $internalStorageUsed")

        val internalProgress =
            ((internalStorageUsed.toDouble() / internalStorageSize.toDouble()) * 100).toInt() // 计算已使用空间的百分比

        binding.pbPhone.max = internalStorageSize.toDouble().toInt()
        binding.pbPhone.progress = internalProgress
        binding.tvPhoneTotal.text = "${internalStorageSize}GB USD"
        val phoneRemain =
            String.format(
                "%.2f",
                internalStorageSize.toDouble() - internalStorageUsed.toDouble()
            )
        binding.tvPhoneRemain.text = "Remaining: ${phoneRemain}GB USD"

        // 读取SD储存卡的大小，并按内存使用比例设置进度所占大小
        val sdCardSize =
            String.format("%.2f", getSDCardSize() / (1024.0 * 1024.0 * 1024.0)) // 转换为GB并保留两位小数
        Log.e("halo", "sdCardSize: $sdCardSize")

        val sdCardUsed = String.format(
            "%.2f",
            calculateUsedSpaceForSDCard() / (1024.0 * 1024.0 * 1024.0)
        ) // 转换为GB并保留两位小数
        Log.e("halo", "sdCardUsed: $sdCardUsed")

        val sdProgress =
            ((sdCardUsed.toDouble() / sdCardSize.toDouble()) * 100).toInt() // 计算已使用空间的百分比

        binding.pbSd.max = sdCardSize.toDouble().toInt()
        binding.pbSd.progress = sdProgress
        binding.tvSdTotal.text = "${sdCardSize}GB USD"
        val remainSD = String.format("%.2f", sdCardSize.toDouble() - sdCardUsed.toDouble())
        binding.tvSdRemain.text = "Remaining: ${remainSD}GB USD"

    }

    private fun getInternalStorageSize(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize: Long = stat.blockSizeLong
        val totalBlocks: Long = stat.blockCountLong
        return totalBlocks * blockSize
    }

    private fun getSDCardSize(): Long {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val blockSize: Long = stat.blockSizeLong
        val totalBlocks: Long = stat.blockCountLong
        return totalBlocks * blockSize
    }

    private fun calculateUsedSpaceForInternalStorage(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize: Long = stat.blockSizeLong
        val availableBlocks: Long = stat.availableBlocksLong
        return (stat.blockCountLong - availableBlocks) * blockSize
    }

    private fun calculateUsedSpaceForSDCard(): Long {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val blockSize: Long = stat.blockSizeLong
        val availableBlocks: Long = stat.availableBlocksLong
        return (stat.blockCountLong - availableBlocks) * blockSize
    }

}