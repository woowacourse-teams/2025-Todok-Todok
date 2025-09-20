package com.team.domain.model.exception

sealed class ImageLoadExceptions(
    override val message: String?,
) : TodokTodokExceptions() {
    data object UriInputStreamNotFoundException :
        ImageLoadExceptions("[ERROR] URI에서 InputStream을 찾지 못했습니다")
}
