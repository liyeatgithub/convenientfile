package com.hal.convenientfile.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.hal.convenientfile.databinding.CellMainSourceItemBinding
import com.hal.convenientfile.entity.SourceEntity
import com.hal.convenientfile.ui.CheckActivity
import com.hal.convenientfile.utils.Constant

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description 主页的图片、视频浏览适配器
 */
class MainSourceAdapter : ListAdapter<SourceEntity, MainSourceAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<SourceEntity>() {
        override fun areItemsTheSame(oldItem: SourceEntity, newItem: SourceEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SourceEntity, newItem: SourceEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CellMainSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = getItem(position)
        when (val binding = holder.binding) {
            is CellMainSourceItemBinding -> {
                Glide.with(binding.ivCover)
                    .load(entity.data)
                    .into(binding.ivCover)
                binding.ivVideoPlay.isVisible = entity.type == 2
                binding.root.setOnClickListener {
                    when (entity.type) {
                        Constant.IMAGE -> {
                            CheckActivity.start(
                                binding.root.context as Activity,
                                binding.root,
                                entity.data
                            )
                        }

                        Constant.VIDEO -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(Uri.parse(entity.data), "video/*")
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
}