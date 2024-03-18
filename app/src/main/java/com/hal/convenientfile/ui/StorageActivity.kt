package com.hal.convenientfile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.adapter.DirFileAdapter
import com.hal.convenientfile.adapter.DirListAdapter
import com.hal.convenientfile.databinding.ActivityStorageBinding
import com.hal.convenientfile.entity.DirFileEntity
import com.hal.convenientfile.entity.DirListEntity
import com.hal.convenientfile.utils.FileUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author hal
 * @date 2024/3/15
 * @description 文件管理路径
 */

class StorageActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = Intent(activity, StorageActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private val binding by lazy {
        ActivityStorageBinding.inflate(layoutInflater)
    }

    //根路径具体
    private val rootPath = Environment.getExternalStorageDirectory().path

    //当前路径名称
    private val currentPathLiveData: MutableLiveData<String> = MutableLiveData("")

    private var currentPath = ""

    private val dirListAdapter by lazy {
        DirListAdapter()
    }

    private val dirFileAdapter by lazy {
        DirFileAdapter().apply {
            setOnFileClickListener(object : DirFileAdapter.OnFileClickListener {
                override fun onFileClick(entity: DirFileEntity) {
                    currentPathLiveData.value = entity.fileName
                }
            })
        }
    }

    //当前的所有目录
    private val dirListEntities: MutableLiveData<MutableList<DirListEntity>> = MutableLiveData()

    //当前的所有文件
    private val dirFileEntities: MutableLiveData<MutableList<DirFileEntity>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        initData()

        initEvent()

    }

    private fun initData() {
        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvList.adapter = dirListAdapter

        //添加根路径
        currentPath = rootPath
        dirListEntities.value = mutableListOf(DirListEntity(currentPath, currentPath))

        binding.rvFolder.layoutManager = GridLayoutManager(this, 3)
        binding.rvFolder.adapter = dirFileAdapter
        //获取根路径上的所有文件
        lifecycleScope.launch {
            dirFileEntities.value = FileUtils.getListOfFilesAndFolders(currentPath)
        }

        currentPathLiveData.observe(this) { newPathName ->
            if (newPathName != "") {
                currentPath += "/$newPathName"
                val newList = ArrayList(dirListEntities.value ?: emptyList())
                newList.add(DirListEntity(newPathName, currentPath))
                dirListEntities.value = newList

                lifecycleScope.launch {
                    dirFileEntities.value = FileUtils.getListOfFilesAndFolders(currentPath)
                }
            }
        }

        dirListEntities.observe(this) {
            dirListAdapter.submitList(it)
            lifecycleScope.launch {
                delay(100)
                binding.rvList.smoothScrollToPosition(dirListAdapter.itemCount - 1)
            }
        }

        dirFileEntities.observe(this) {
            dirFileAdapter.submitList(it)
            binding.clNoFileFound.isVisible = it.isEmpty()
        }

    }

    private fun initEvent() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        if (currentPath == rootPath) {
            super.onBackPressed()
        } else {
            currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"))
            lifecycleScope.launch {
                dirFileEntities.value = FileUtils.getListOfFilesAndFolders(currentPath)
                val newList = ArrayList(dirListEntities.value ?: emptyList())
                newList.removeAt(newList.size - 1)
                dirListEntities.value = newList
            }
        }
    }


}