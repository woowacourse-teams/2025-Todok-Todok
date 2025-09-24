package com.team.domain.model.exception

sealed class SearchedBooksTotalSizeException(
    override val message: String,
) : TodokTodokExceptions() {
    data object InvalidSize :
        SearchedBooksTotalSizeException("[ERROR] 책 검색 요청시 총 책의 개수는 0에서 200까지만 가능합니다.") {
        private fun readResolve(): Any = InvalidSize
    }
}
