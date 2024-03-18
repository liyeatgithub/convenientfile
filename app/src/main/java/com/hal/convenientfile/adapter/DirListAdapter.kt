package com.hal.convenientfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hal.convenientfile.databinding.CellDirListItemBinding
import com.hal.convenientfile.entity.DirListEntity

/**
 *
 * @author hal
 * @date 2024/3/15
 * @description 目录适配器
 */
class DirListAdapter : ListAdapter<DirListEntity, DirListAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<DirListEntity>() {
        override fun areItemsTheSame(oldItem: DirListEntity, newItem: DirListEntity): Boolean {
            return oldItem.dirPath == newItem.dirPath
        }

        override fun areContentsTheSame(oldItem: DirListEntity, newItem: DirListEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CellDirListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = getItem(position)
        when (val binding = holder.binding) {
            is CellDirListItemBinding -> {
                binding.tvDirName.text = entity.dirName
            }
        }
    }
}