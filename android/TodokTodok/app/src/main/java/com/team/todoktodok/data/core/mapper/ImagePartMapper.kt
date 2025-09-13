package com.team.todoktodok.data.core.mapper

import android.net.Uri
import okhttp3.MultipartBody

interface ImagePartMapper {
    fun toPart(
        uri: Uri,
        partName: String,
    ): MultipartBody.Part
}
