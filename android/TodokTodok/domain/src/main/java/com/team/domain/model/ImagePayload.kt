package com.team.domain.model

import java.io.InputStream

data class ImagePayload(
    val fileName: String,
    val mediaType: String,
    val openStream: () -> InputStream,
)
