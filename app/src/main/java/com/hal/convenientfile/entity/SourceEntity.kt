package com.hal.convenientfile.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description 图片，视频，apk，音频，下载，文档实体类
 */

@Parcelize
data class SourceEntity(
    val id: Long, // 图片文件在媒体存储中的唯一标识符
    val title: String, // 图片文件的标题
    var displayName: String, // 图片文件的显示名称
    val size: Long, // 图片文件的大小
    val data: String, // 图片文件的数据路径 - uri
    val modifyDate: Long,// 最后编辑时间
    val mimeType: String, // 文件的 mime 类型
    val type: Int, //类型 1.图片 2.视频 3.音频 4.apk  5.下载 6.文档
    var isSelected: Boolean// 是否被选中，用于编辑
) : Parcelable
