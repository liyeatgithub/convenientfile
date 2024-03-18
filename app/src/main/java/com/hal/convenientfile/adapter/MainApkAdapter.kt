package com.hal.convenientfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.hal.convenientfile.databinding.CellMainApksItemBinding
import com.hal.convenientfile.entity.SourceEntity

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description 主页的apk浏览适配器
 */
class MainApkAdapter : ListAdapter<SourceEntity, MainApkAdapter.ViewHolder>(DiffCallback()) {

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
            CellMainApksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = getItem(position)
        when (val binding = holder.binding) {
            is CellMainApksItemBinding -> {
                Glide.with(binding.ivCover)
                    .load(entity.data)
                    .into(binding.ivCover)
                binding.tvApkName.text = entity.displayName
                val sizeInMB = entity.size.toDouble() / (1024 * 1024)
                binding.tvApkSize.text = String.format("%.2f MB", sizeInMB)
            }
        }
    }
}