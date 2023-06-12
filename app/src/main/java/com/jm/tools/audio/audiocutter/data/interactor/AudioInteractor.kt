package com.jm.tools.audio.audiocutter.data.interactor

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.jm.tools.audio.audiocutter.data.model.AudioModel

class AudioInteractor(private val ctx: Context) : BaseInteractor(ctx) {

    fun loadAudios(folderPath: String? = null): List<AudioModel> {
        val result = ArrayList<AudioModel>()

        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_ADDED
        )

        val selection = folderPath?.let {
            "${MediaStore.Images.ImageColumns.DATA} like ?"
        }
        val selectionArgs: Array<String>? = folderPath?.let {
            arrayOf("${it}%")
        }
        val sortOrder = "${MediaStore.Images.ImageColumns.DATE_ADDED} DESC"
        val baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ctx.applicationContext.contentResolver.query(
            baseUri, projection, selection, selectionArgs, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
            val pathCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
            val bucketNameCol =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val addedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)

            while (cursor.moveToNext()) {
                result.add(
                    AudioModel().apply {
                        fileId = cursor.getLong(idCol)
                        path = cursor.getString(pathCol)
                        name = cursor.getString(nameCol)
                        bucketName = cursor.getString(bucketNameCol)
                        fileId?.also { id ->
                            uri = ContentUris.withAppendedId(baseUri, id).toString()
                        }
                        addedDate = cursor.getInt(addedCol)
                    }
                )
            }
        }

        return result
    }
}