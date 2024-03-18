package com.hal.convenientfile.utils

import android.content.ContentResolver
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.hal.convenientfile.entity.DirFileEntity
import com.hal.convenientfile.entity.SourceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

/**
 *
 * @author hal
 * @date 2024/3/14
 * @description
 */
object FileUtils {
    /**
     * 获取images数据
     */
    suspend fun getImages(contentResolver: ContentResolver): MutableList<SourceEntity> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<SourceEntity>()

            // 查询需要返回的列
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.MIME_TYPE,
            )

            // 按时间降序排序，最新的在前面
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            // 执行查询
            val query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            query?.use { cursor ->
                // 获取各列的索引
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                val mimeTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

                // 遍历查询结果
                while (cursor.moveToNext()) {
                    // 从查询结果中获取图片文件信息
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val data = cursor.getString(dataColumn)
                    val dateModified = cursor.getLong(dateModifiedColumn)
                    val type = Constant.IMAGE
                    val isSelected = false
                    val mimeType = cursor.getString(mimeTypeColumn)

                    // 创建 Image 对象并添加到列表中
                    val image =
                        SourceEntity(
                            id,
                            title,
                            displayName,
                            size,
                            data,
                            dateModified,
                            mimeType,
                            type,
                            isSelected
                        )
                    list.add(image)
                }
                //关闭游标
                cursor.close()
            }
            return@withContext list
        }


    /**
     * 获取videos数据
     */
    suspend fun getVideos(contentResolver: ContentResolver): MutableList<SourceEntity> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<SourceEntity>()

            // 查询需要返回的列
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE
            )

            // 按时间降序排序，最新的在前面
            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

            // 执行查询
            val query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            query?.use { cursor ->
                // 获取各列的索引
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
                val mimeTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

                // 遍历查询结果
                while (cursor.moveToNext()) {
                    // 从查询结果中获取视频文件信息
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val data = cursor.getString(dataColumn)
                    val dateModified = cursor.getLong(dateModifiedColumn)
                    val type = Constant.VIDEO
                    val isSelected = false
                    val mimeType = cursor.getString(mimeTypeColumn)

                    // 创建 Video 对象并添加到列表中
                    val video =
                        SourceEntity(
                            id,
                            title,
                            displayName,
                            size,
                            data,
                            dateModified,
                            mimeType,
                            type,
                            isSelected
                        )
                    list.add(video)
                }
                //关闭游标
                cursor.close()
            }
            return@withContext list
        }

    /**
     * 获取audio数据
     */
    suspend fun getAudios(contentResolver: ContentResolver): MutableList<SourceEntity> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<SourceEntity>()

            // 查询需要返回的列
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.MIME_TYPE
            )

            // 按时间降序排序，最新的在前面
            val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

            // 执行查询
            val query = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            query?.use { cursor ->
                // 获取各列的索引
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)
                val mimeTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

                // 遍历查询结果
                while (cursor.moveToNext()) {
                    // 从查询结果中获取音频文件信息
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val data = cursor.getString(dataColumn)
                    val dateModified = cursor.getLong(dateModifiedColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val type = Constant.AUDIO
                    val isSelected = false

                    // 创建 Audio 对象并添加到列表中
                    val audio =
                        SourceEntity(
                            id,
                            title,
                            displayName,
                            size,
                            data,
                            dateModified,
                            mimeType,
                            type,
                            isSelected
                        )
                    list.add(audio)
                }
                //关闭游标
                cursor.close()
            }
            return@withContext list
        }

    /**
     * 获取apk文件数据
     */
    suspend fun getApks(contentResolver: ContentResolver): MutableList<SourceEntity> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<SourceEntity>()

            // 查询需要返回的列
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.MIME_TYPE
            )

            // 筛选 apk 文件
            val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
            val selectionArgs = arrayOf("application/vnd.android.package-archive")

            // 按时间降序排序，最新的在前面
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

            // 执行查询
            val query = contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            query?.use { cursor ->
                // 获取各列的索引
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val mimeTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

                // 遍历查询结果
                while (cursor.moveToNext()) {
                    // 从查询结果中获取 APK 文件信息
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val data = cursor.getString(dataColumn)
                    val dateModified = cursor.getLong(dateModifiedColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val type = Constant.APKS
                    val isSelected = false

                    // 创建 SourceEntity 对象并添加到列表中
                    val apk = SourceEntity(
                        id, title, displayName, size, data, dateModified, mimeType, type, isSelected
                    )
                    list.add(apk)
                }
            }

            return@withContext list
        }

    /**
     * 获取下载文件
     */
    suspend fun getDownloads(): MutableList<SourceEntity> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<SourceEntity>()

            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (downloadsDir.exists() && downloadsDir.isDirectory) {
                val files = downloadsDir.listFiles()
                files?.forEach { file ->
                    if (file.isFile) {
                        val id = file.name.hashCode().toLong()
                        val title = file.name
                        val displayName = file.name
                        val size = file.length()
                        val data = file.absolutePath
                        val dateModified = file.lastModified()
                        val mimeType =
                            MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
                        val type = Constant.DOWNLOAD
                        val isSelected = false

                        // 创建 SourceEntity 对象并添加到列表中
                        val download = SourceEntity(
                            id,
                            title,
                            displayName,
                            size,
                            data,
                            dateModified,
                            mimeType!!,
                            type,
                            isSelected
                        )
                        list.add(download)
                    }
                }
            }

            return@withContext list
        }


    /**
     * 获取文档文件
     */
    suspend fun getDocuments(contentResolver: ContentResolver): MutableList<SourceEntity> =
        withContext(Dispatchers.IO) {
            val list = mutableListOf<SourceEntity>()

            // 查询需要返回的列
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.MIME_TYPE
            )

            // 筛选条件：只查询文档类型的文件
            val selection =
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_NONE} " +
                        "AND ${MediaStore.Files.FileColumns.MIME_TYPE} IN ('application/pdf', 'text/plain', 'application/msword', 'application/vnd.ms-excel', 'application/vnd.ms-powerpoint')"

            // 按时间降序排序，最新的在前面
            val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

            // 执行查询
            val query = contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                sortOrder
            )

            query?.use { cursor ->
                // 获取各列的索引
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val mimeTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

                // 遍历查询结果
                while (cursor.moveToNext()) {
                    // 从查询结果中获取文档文件信息
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val data = cursor.getString(dataColumn)
                    val dateModified = cursor.getLong(dateModifiedColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val type = Constant.DOCUMENT
                    val isSelected = false

                    // 创建 SourceEntity 对象并添加到列表中
                    val document = SourceEntity(
                        id, title, displayName, size, data, dateModified, mimeType, type, isSelected
                    )
                    list.add(document)
                }
            }

            return@withContext list
        }


    //获取指定路径下的文件（夹）
    suspend fun getListOfFilesAndFolders(path: String): MutableList<DirFileEntity> =
        withContext(Dispatchers.IO) {
            val fileList = mutableListOf<DirFileEntity>()
            val directory = File(path)

            if (directory.exists() && directory.isDirectory) {
                val files = directory.listFiles()

                files?.forEach { file ->
                    val type = if (file.isDirectory) 1 else 2
                    val fileName = file.name
                    val filePath = file.absolutePath
                    val fileType = if (file.isDirectory) 0 else getFileType(fileName)

                    val dirFileEntity = DirFileEntity(fileName, filePath, type, fileType)
                    if (!fileName.startsWith(".")) fileList.add(dirFileEntity)
                }
            }
            return@withContext fileList
        }

    private fun getFileType(fileName: String): Int {
        val dotIndex = fileName.lastIndexOf('.')
        return when (if (dotIndex > 0) fileName.substring(dotIndex + 1)
            .lowercase(Locale.ROOT) else "") {
            "jpg", "jpeg", "png", "gif" -> 1 // 图片文件
            "mp4", "avi", "mkv", "mov" -> 2 // 视频文件
            "mp3", "wav", "flac" -> 3 // 音频文件
            "apk" -> 4 // apk
            "doc", "docx", "pdf", "txt" -> 5 // 文档文件
            else -> 6 // 其他文件
        }
    }
}