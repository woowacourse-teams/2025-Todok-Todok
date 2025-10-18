package com.team.domain.model.exception

sealed class KeywordException(
    override val message: String,
) : TodokTodokExceptions() {
    data object EmptyKeyword : KeywordException("[ERROR] 키워드가 없습니다") {
        private fun readResolve(): Any = EmptyKeyword
    }

    data object BlankKeyword : KeywordException("[ERROR] 키워드가 공백입니다") {
        private fun readResolve(): Any = BlankKeyword
    }
}
