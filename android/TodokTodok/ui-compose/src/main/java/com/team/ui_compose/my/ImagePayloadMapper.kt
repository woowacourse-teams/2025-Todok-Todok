package com.team.ui_compose.my

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.team.domain.model.ImagePayload

class ImagePayloadMapper(
    private val contentResolver: ContentResolver,
) {
    @SuppressLint("Recycle")
    fun from(uri: Uri) =
        ImagePayload(
            fileName = queryName(contentResolver, uri) ?: "upload.bin",
            mediaType = contentResolver.getType(uri) ?: "application/octet-stream",
            openStream = {
                contentResolver.openInputStream(uri)
                    ?: throw IllegalArgumentException()
            },
        )

    private fun queryName(
        contentResolver: ContentResolver,
        uri: Uri,
    ): String? {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                return cursor.getString(nameIndex)
            }
        }
        return null
    }
}
