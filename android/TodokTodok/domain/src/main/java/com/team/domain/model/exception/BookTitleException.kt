package com.team.domain.model.exception

sealed class BookTitleException(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyBookTitle : BookTitleException("[ERROR] 도서의 제목이 없습니다") {
        private fun readResolve(): Any = EmptyBookTitle
    }
}
