package com.team.todoktodok.data.core.mapper

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class DefaultImagePartMapper(
    context: Context,
) : ImagePartMapper {
    private val contentResolver = context.contentResolver

    override fun toPart(
        uri: Uri,
        partName: String,
    ): MultipartBody.Part {
        val mime = contentResolver.getType(uri) ?: "application/octet-stream"
        val fileName =
            contentResolver
                .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { if (it.moveToFirst()) it.getString(0) else null } ?: "upload.bin"

        val body =
            object : RequestBody() {
                override fun contentType(): MediaType? = mime.toMediaTypeOrNull()

                override fun writeTo(sink: BufferedSink) {
                    contentResolver
                        .openInputStream(uri)!!
                        .use { input -> sink.writeAll(input.source()) }
                }
            }
        return MultipartBody.Part.createFormData(partName, fileName, body)
    }
}
