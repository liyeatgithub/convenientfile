package com.hal.convenientfile.entity

/**
 *
 * @author hal
 * @date 2024/3/15
 * @description 文件或者文件夹实体类
 */
data class DirFileEntity(
    var fileName: String, //该文件（夹）的名称
    var filePath: String, //该文件（夹）的路径
    var type: Int, //1 文件夹 2 文件
    var fileType: Int, //0 文件夹 1 image 2 video 3 audio 4 apk 5 document 6 others
)