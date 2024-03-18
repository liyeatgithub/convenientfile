package com.hal.convenientfile.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.hal.convenientfile.adapter.BrowseSourceAdapter
import com.hal.convenientfile.databinding.ActivityBrowseBinding
import com.hal.convenientfile.entity.SourceEntity
import com.hal.convenientfile.utils.Constant
import com.hal.convenientfile.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description 浏览界面，包括图片，视频，音频
 */

class BrowseActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBrowseBinding.inflate(layoutInflater)
    }

    private val isSelectedModeLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    //所有项
    private var allSource: MutableList<SourceEntity>? = null

    companion object {

        private const val TYPE = "type"

        @JvmStatic
        fun start(activity: Activity, type: Int) {
            val intent = Intent(activity, BrowseActivity::class.java)
            intent.putExtra(TYPE, type)
            activity.startActivity(intent)
        }
    }

    private val type: Int by lazy {
        intent.getIntExtra(TYPE, -1)
    }

    private val browseSourceAdapter by lazy {
        BrowseSourceAdapter().apply {
            setOnSelectedModeChangeListener(object :
                BrowseSourceAdapter.OnSelectedModeChangeListener {
                override fun onSelectedModeChange(isSelectMode: Boolean) {
                    isSelectedModeLiveData.value = isSelectMode
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        initData()

        initEvent()

    }

    //获取已选的数据
    private fun getSelected(): List<SourceEntity> {
        val list: MutableList<SourceEntity> = mutableListOf()
        allSource?.forEach {
            if (it.isSelected) list.add(it)
        }
        return list
    }

    private fun initEvent() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        //全选
        binding.tvAllSelect.setOnClickListener {
            allSource?.forEach {
                it.isSelected = true
            }
            browseSourceAdapter.notifyDataSetChanged()
        }
        //删除
        binding.llDelete.setOnClickListener {
            if (getSelected().isEmpty()) {
                Toast.makeText(this, "Select at least one item", Toast.LENGTH_SHORT).show()
            } else {
                //弹出确认弹窗
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm Delete")
                builder.setMessage("Are you sure you want to delete the selected items?")
                builder.setPositiveButton("Delete") { _, _ ->
                    deleteSelected()
                }
                builder.setNegativeButton("Cancel") { _, _ -> }
                val dialog = builder.create()
                dialog.show()
            }
        }
        //重命名
        binding.llEdit.setOnClickListener {
            if (getSelected().size != 1) {
                Toast.makeText(this, "Please select one item", Toast.LENGTH_SHORT).show()
            } else {
                val entity = getSelected()[0]
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Rename File")

                // 创建一个 EditText 用于用户输入新的文件名
                val input = EditText(this)
                input.setPadding(30)
                // 设置输入框的初始文本为当前选中文件的名称
                input.setText(entity.displayName)
                builder.setView(input)

                builder.setPositiveButton("Rename") { _, _ ->
                    val newName = input.text.toString()
                    if (newName.isEmpty()) {
                        Toast.makeText(this, "Please enter a new name", Toast.LENGTH_SHORT).show()
                    } else reName(entity, newName)
                }
                builder.setNegativeButton("Cancel") { _, _ -> }
                val dialog = builder.create()
                dialog.show()
            }
        }
        //信息
        binding.llInfo.setOnClickListener {
            if (getSelected().size != 1) {
                Toast.makeText(this, "Please select one item", Toast.LENGTH_SHORT).show()
            } else {
                InfoActivity.start(this, getSelected()[0])
            }
        }
    }

    private fun reName(entity: SourceEntity, newName: String) {
        // 获取文件的Uri和更新的名称
        val uri = MediaStore.Files.getContentUri("external")
        val selection = "${MediaStore.Files.FileColumns.DATA} = ?"
        val selectionArgs = arrayOf(entity.data)

        val values = ContentValues()
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, newName)

        try {
            contentResolver.update(uri, values, selection, selectionArgs)
            // 更新成功后更新entity的displayName
            entity.displayName = newName
            Toast.makeText(this, "Rename success", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("halo", "e: $e")
            Toast.makeText(this, "Rename failed", Toast.LENGTH_SHORT).show()
        }
        isSelectedModeLiveData.value = false
    }

    private fun deleteSelected() {
        var isAllDelete = true
        val iterator = allSource?.iterator()  // 获取迭代器
        getSelected().forEach { entity ->
            val uri = MediaStore.Files.getContentUri("external")
            val selection = "${MediaStore.Files.FileColumns.DATA} = ?"
            val selectionArgs = arrayOf(entity.data)
            try {
                contentResolver.delete(uri, selection, selectionArgs)
                // 从allSource中移除该entity
                while (iterator?.hasNext() == true) {
                    val item = iterator.next()
                    if (item == entity) {
                        iterator.remove()  // 通过迭代器移除元素
                        break  // 找到并移除元素后退出循环
                    }
                }
            } catch (e: Exception) {
                isAllDelete = false
            }
        }
        if (isAllDelete) {
            Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        isSelectedModeLiveData.value = false
    }

    private fun initData() {

        isSelectedModeLiveData.observe(this) {
            browseSourceAdapter.setSelectMode(it)
            binding.clEdit.isVisible = it
            binding.tvAllSelect.isVisible = it
            if (!it) {
                allSource?.forEach { s ->
                    s.isSelected = false
                }
            }
        }

        when (type) {
            Constant.IMAGE -> {
                binding.tvTitle.text = "Image"
                binding.rvContent.adapter = browseSourceAdapter
                binding.rvContent.layoutManager = GridLayoutManager(this, 4)
                lifecycleScope.launch {
                    try {
                        allSource = FileUtils.getImages(contentResolver)
                        allSource?.let {
                            withContext(Dispatchers.Main) {
                                browseSourceAdapter.submitList(it)
                                binding.clNoFileFound.isVisible = it.isEmpty()
                                binding.rvContent.isVisible = it.isNotEmpty()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.clNoFileFound.visibility = View.VISIBLE
                            binding.rvContent.isVisible = false
                        }
                    }
                }
            }

            Constant.VIDEO -> {
                binding.tvTitle.text = "Video"
                binding.rvContent.adapter = browseSourceAdapter
                binding.rvContent.layoutManager = GridLayoutManager(this, 2)
                lifecycleScope.launch {
                    try {
                        allSource = FileUtils.getVideos(contentResolver)
                        allSource?.let {
                            withContext(Dispatchers.Main) {
                                browseSourceAdapter.submitList(it)
                                binding.clNoFileFound.isVisible = it.isEmpty()
                                binding.rvContent.isVisible = it.isNotEmpty()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.clNoFileFound.visibility = View.VISIBLE
                            binding.rvContent.isVisible = false
                        }
                    }
                }
            }

            Constant.AUDIO -> {
                binding.tvTitle.text = "Music"
                binding.rvContent.adapter = browseSourceAdapter
                binding.rvContent.layoutManager = LinearLayoutManager(this)
                lifecycleScope.launch {
                    try {
                        allSource = FileUtils.getAudios(contentResolver)
                        allSource?.let {
                            withContext(Dispatchers.Main) {
                                browseSourceAdapter.submitList(it)
                                binding.clNoFileFound.isVisible = it.isEmpty()
                                binding.rvContent.isVisible = it.isNotEmpty()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.clNoFileFound.visibility = View.VISIBLE
                            binding.rvContent.isVisible = false
                        }
                    }
                }
            }

            Constant.APKS -> {
                binding.tvTitle.text = "Apks"
                binding.rvContent.adapter = browseSourceAdapter
                binding.rvContent.layoutManager = LinearLayoutManager(this)
                lifecycleScope.launch {
                    try {
                        allSource = FileUtils.getApks(contentResolver)
                        allSource?.let {
                            withContext(Dispatchers.Main) {
                                browseSourceAdapter.submitList(it)
                                binding.clNoFileFound.isVisible = it.isEmpty()
                                binding.rvContent.isVisible = it.isNotEmpty()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.clNoFileFound.visibility = View.VISIBLE
                            binding.rvContent.isVisible = false
                        }
                    }
                }
            }

            Constant.DOWNLOAD -> {
                binding.tvTitle.text = "Downloads"
                binding.rvContent.adapter = browseSourceAdapter
                binding.rvContent.layoutManager = LinearLayoutManager(this)
                lifecycleScope.launch {
                    try {
                        allSource = FileUtils.getDownloads()
                        allSource?.let {
                            withContext(Dispatchers.Main) {
                                browseSourceAdapter.submitList(it)
                                binding.clNoFileFound.isVisible = it.isEmpty()
                                binding.rvContent.isVisible = it.isNotEmpty()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.clNoFileFound.visibility = View.VISIBLE
                            binding.rvContent.isVisible = false
                        }
                    }
                }
            }

            Constant.DOCUMENT -> {
                binding.tvTitle.text = "Documents"
                binding.rvContent.adapter = browseSourceAdapter
                binding.rvContent.layoutManager = LinearLayoutManager(this)
                lifecycleScope.launch {
                    try {
                        allSource = FileUtils.getDocuments(contentResolver)
                        allSource?.let {
                            withContext(Dispatchers.Main) {
                                browseSourceAdapter.submitList(it)
                                binding.clNoFileFound.isVisible = it.isEmpty()
                                binding.rvContent.isVisible = it.isNotEmpty()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.clNoFileFound.visibility = View.VISIBLE
                            binding.rvContent.isVisible = false
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isSelectedModeLiveData.value == true) {
            isSelectedModeLiveData.value = false
        } else super.onBackPressed()
    }
}