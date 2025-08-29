package com.team.domain.model.exception

sealed class BookException(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyKeywordException : BookException("[ERROR] 검색어는 1자 이상이어야 합니다") {
        private fun readResolve(): Any = EmptyKeywordException
    }

    data object EmptyISBNException : BookException("[ERROR] 도서 ISBN을 입력해주세요") {
        private fun readResolve(): Any = EmptyISBNException
    }

    data object EmptySelectedBook : BookException("[ERROR] 도서를 선택해주세요") {
        private fun readResolve(): Any = EmptySelectedBook
    }
}
