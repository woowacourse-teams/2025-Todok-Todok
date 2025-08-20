package com.team.domain.model.exception

sealed class BooksExceptions(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyKeywordException : BooksExceptions("[ERROR] 검색어는 1자 이상이어야 합니다")

    data object EmptyISBNException : BooksExceptions("[ERROR] 도서 ISBN을 입력해주세요")
}
