package com.hal.convenientfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.hal.convenientfile.R
import com.hal.convenientfile.databinding.CellDirFileItemBinding
import com.hal.convenientfile.entity.DirFileEntity

/**
 *
 * @author hal
 * @date 2024/3/15
 * @description 目录适配器
 */
class DirFileAdapter : ListAdapter<DirFileEntity, DirFileAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<DirFileEntity>() {
        override fun areItemsTheSame(oldItem: DirFileEntity, newItem: DirFileEntity): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(oldItem: DirFileEntity, newItem: DirFileEntity): Boolean {
            return oldItem == newItem
        }

    }

    interface OnFileClickListener {
        fun onFileClick(entity: DirFileEntity)
    }

    var onFileClickListener: OnFileClickListener? = null

    @JvmName("setOnFileClickListenerUnique")
    fun setOnFileClickListener(listener: OnFileClickListener) {
        onFileClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CellDirFileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = getItem(position)
        when (val binding = holder.binding) {
            is CellDirFileItemBinding -> {
                binding.tvName.text = entity.fileName
                when (entity.type) {
                    1 -> {
                        //文件夹
                        Glide.with(binding.ivCover).load(R.mipmap.folder).into(binding.ivCover)
                    }

                    2 -> {
                        when (entity.fileType) {
                            1 -> {
                                //图片
                                Glide.with(binding.ivCover).load(R.mipmap.image)
                                    .into(binding.ivCover)
                            }

                            2 -> {
                                //视频
                                Glide.with(binding.ivCover).load(R.mipmap.video)
                                    .into(binding.ivCover)
                            }

                            3 -> {
                                //音频
                                Glide.with(binding.ivCover).load(R.mipmap.audio)
                                    .into(binding.ivCover)
                            }

                            4 -> {
                                //apk
                                Glide.with(binding.ivCover).load(R.mipmap.apks)
                                    .into(binding.ivCover)
                            }

                            5 -> {
                                //文档
                                Glide.with(binding.ivCover).load(R.mipmap.documents)
                                    .into(binding.ivCover)
                            }

                            else -> {
                                //其他文件
                                Glide.with(binding.ivCover).load(R.mipmap.documents)
                                    .into(binding.ivCover)
                            }
                        }
                    }
                }
                binding.root.setOnClickListener {
                    if (entity.type == 1) {
                        onFileClickListener?.onFileClick(entity)
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Cannot open this file",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}