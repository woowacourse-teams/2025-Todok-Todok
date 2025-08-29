package com.team.domain.model.book

import com.team.domain.model.exception.BookImageException

@JvmInline
value class BookImage(
    val value: String,
) {
    init {
        require(value.startsWith(IMAGE_URL_PREFIX)) { BookImageException.InvalidUrl.message }
    }

    companion object {
        private const val IMAGE_URL_PREFIX: String = "https://image.aladin.co.kr/product/"
    }
}
