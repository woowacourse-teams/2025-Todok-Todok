package com.team.data.network.request

import okhttp3.MultipartBody

data class ProfileImageRequest(
    val profileImage: MultipartBody.Part,
)
