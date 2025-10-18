package com.team.domain.model.exception

sealed class BookAuthorException(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyBookAuthor : BookAuthorException("[ERROR] 도서의 저자가 없습니다") {
        private fun readResolve(): Any = EmptyBookAuthor
    }
}
