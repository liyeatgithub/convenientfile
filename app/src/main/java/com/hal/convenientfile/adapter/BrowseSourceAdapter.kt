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
import com.hal.convenientfile.databinding.CellBrowseAudioItemBinding
import com.hal.convenientfile.databinding.CellBrowseImageVideoItemBinding
import com.hal.convenientfile.databinding.CellBrowseOthersItemBinding
import com.hal.convenientfile.entity.SourceEntity
import com.hal.convenientfile.ui.CheckActivity
import com.hal.convenientfile.utils.Constant

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description 浏览页的图片、视频、音频浏览适配器
 */
class BrowseSourceAdapter :
    ListAdapter<SourceEntity, BrowseSourceAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<SourceEntity>() {
        override fun areItemsTheSame(oldItem: SourceEntity, newItem: SourceEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SourceEntity, newItem: SourceEntity): Boolean {
            return oldItem == newItem
        }

    }

    //是否是选择模式
    private var isSelectMode = false

    fun setSelectMode(isSelectMode: Boolean) {
        this.isSelectMode = isSelectMode
        notifyDataSetChanged()
    }

    interface OnSelectedModeChangeListener {
        fun onSelectedModeChange(isSelectMode: Boolean)
    }

    private var listener: OnSelectedModeChangeListener? = null

    fun setOnSelectedModeChangeListener(listener: OnSelectedModeChangeListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Constant.IMAGE, Constant.VIDEO -> {
                val binding =
                    CellBrowseImageVideoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ViewHolder(binding)
            }

            Constant.AUDIO -> {
                val binding =
                    CellBrowseAudioItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ViewHolder(binding)
            }

            else -> {
                val binding =
                    CellBrowseOthersItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = getItem(position)
        when (val binding = holder.binding) {
            is CellBrowseImageVideoItemBinding -> {
                //是否显示那个播放标识
                binding.ivVideoPlay.isVisible = entity.type == 2

                //设置显示的高度
                val displayMetrics = holder.itemView.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                binding.ivCover.layoutParams.height = screenWidth / if (entity.type == 2) 2 else 4
                Glide.with(binding.ivCover)
                    .load(entity.data)
                    .into(binding.ivCover)
                //编辑模式才出现选择框
                binding.ivIsSelect.isVisible = isSelectMode
                binding.ivIsSelect.isSelected = entity.isSelected
                binding.root.setOnLongClickListener {
                    if (!isSelectMode) {
                        //将这个item设置为选中
                        entity.isSelected = true
                        listener?.onSelectedModeChange(true)
                    }
                    true
                }
                binding.root.setOnClickListener {
                    //是选择模式则将其置反
                    if (isSelectMode) {
                        entity.isSelected = !entity.isSelected
                        notifyItemChanged(position)
                    } else {
                        when (entity.type) {
                            Constant.IMAGE -> {
                                CheckActivity.start(
                                    holder.itemView.context as Activity,
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

            is CellBrowseAudioItemBinding -> {
                binding.tvAudioName.text = entity.displayName
                val sizeInMB = entity.size.toDouble() / (1024 * 1024)
                binding.tvAudioSize.text = String.format("%.2f MB", sizeInMB)
                //编辑模式才出现选择框
                binding.ivIsSelect.isVisible = isSelectMode
                binding.ivIsSelect.isSelected = entity.isSelected
                binding.root.setOnLongClickListener {
                    if (!isSelectMode) {
                        //将这个item设置为选中
                        entity.isSelected = true
                        listener?.onSelectedModeChange(true)
                    }
                    true
                }
                binding.root.setOnClickListener {
                    //是选择模式则将其置反
                    if (isSelectMode) {
                        entity.isSelected = !entity.isSelected
                        notifyItemChanged(position)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(Uri.parse(entity.data), "audio/*")
                        binding.root.context.startActivity(
                            Intent.createChooser(
                                intent,
                                "Open with"
                            )
                        )
                    }
                }
            }

            is CellBrowseOthersItemBinding -> {
                binding.tvFileName.text = entity.displayName
                val sizeInMB = entity.size.toDouble() / (1024 * 1024)
                binding.tvFileSize.text = String.format("%.2f MB", sizeInMB)
                //编辑模式才出现选择框
                binding.ivIsSelect.isVisible = isSelectMode
                binding.ivIsSelect.isSelected = entity.isSelected
                binding.root.setOnLongClickListener {
                    if (!isSelectMode) {
                        //将这个item设置为选中
                        entity.isSelected = true
                        listener?.onSelectedModeChange(true)
                    }
                    true
                }
                binding.root.setOnClickListener {
                    //是选择模式则将其置反
                    if (isSelectMode) {
                        entity.isSelected = !entity.isSelected
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }
}