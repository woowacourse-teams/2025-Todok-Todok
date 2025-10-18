package com.team.domain.model.exception

sealed class BooksExceptions(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyKeyword : BooksExceptions("[ERROR] 검색어는 1자 이상이어야 합니다") {
        private fun readResolve(): Any = EmptyKeyword
    }

    data object EmptyISBN : BooksExceptions("[ERROR] 도서 ISBN을 입력해주세요") {
        private fun readResolve(): Any = EmptyISBN
    }
}
