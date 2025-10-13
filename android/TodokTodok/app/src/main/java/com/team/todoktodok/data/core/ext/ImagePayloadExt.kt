package com.team.todoktodok.data.core.ext

import com.team.domain.model.ImagePayload
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

fun ImagePayload.toMultipartPart(paramName: String): MultipartBody.Part {
    val multipart = mediaType.toMediaTypeOrNull()

    val requestBody =
        object : RequestBody() {
            override fun contentType(): MediaType? = multipart

            override fun writeTo(sink: BufferedSink) {
                openStream().use { input ->
                    sink.writeAll(input.source())
                }
            }
        }

    return MultipartBody.Part.createFormData(
        name = paramName,
        filename = fileName,
        body = requestBody,
    )
}
