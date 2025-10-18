package com.team.domain.model.exception

sealed class BookException(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyKeyword : BookException("[ERROR] 검색어는 1자 이상이어야 합니다") {
        private fun readResolve(): Any = EmptyKeyword
    }

    data object EmptyISBN : BookException("[ERROR] 도서 ISBN을 입력해주세요") {
        private fun readResolve(): Any = EmptyISBN
    }

    data object EmptySelectedBook : BookException("[ERROR] 도서를 선택해주세요") {
        private fun readResolve(): Any = EmptySelectedBook
    }
}
