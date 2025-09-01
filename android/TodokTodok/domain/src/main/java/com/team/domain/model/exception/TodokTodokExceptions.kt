package com.team.domain.model.exception

import com.team.domain.model.exception.TodokTodokExceptions.NicknameException.DuplicateNicknameException
import com.team.domain.model.exception.TodokTodokExceptions.NicknameException.EmptyNicknameLengthException
import com.team.domain.model.exception.TodokTodokExceptions.NicknameException.InvalidNicknameLengthException
import com.team.domain.model.exception.TodokTodokExceptions.SignUpException.DuplicateEmailException
import com.team.domain.model.exception.TodokTodokExceptions.SignUpException.InvalidFormatEmailException
import com.team.domain.model.exception.TodokTodokExceptions.SignUpException.InvalidTokenException
import com.team.domain.model.exception.TodokTodokExceptions.SignUpException.ProfileImageNotExistException
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * 애플리케이션에서 발생할 수 있는 네트워크/도메인 예외를 정의한 sealed class.
 *
 * - 서버 HTTP 상태 코드 및 서버에서 내려주는 메시지를 기반으로 도메인 레이어에서 처리
 * - 화면에서는 이 예외를 기반으로 UI 메시지를 표시하거나 로직을 분기 처리 가능
 */
sealed class TodokTodokExceptions : Throwable() {
    data object ConnectException : TodokTodokExceptions()

    data object TimeoutError : TodokTodokExceptions()

    data object UnknownHostError : TodokTodokExceptions()

    data object SocketException : TodokTodokExceptions()

    data object IOException : TodokTodokExceptions()

    data object CancellationException : TodokTodokExceptions()

    /** 알 수 없는 예외 발생 */
    data class UnknownException(
        val e: Throwable?,
    ) : TodokTodokExceptions() {
        init {
            if (e != null) initCause(e)
        }
    }

    /** 헤더의 Location 필드가 누락된 경우 발생 */
    data object MissingLocationHeaderException : TodokTodokExceptions()

    /** 바디가 비어있는 경우 발생 */
    data object EmptyBodyException : TodokTodokExceptions()

    /** Json 직렬화 객체 프로퍼티가 일치하지 않을 경우 발생 */
    data object MissingFieldException : TodokTodokExceptions()

    /**
     * HTTP 상태 코드 기반 예외
     */
    sealed class HttpExceptions : TodokTodokExceptions() {
        /** 400 Bad Request */
        data object BadRequestException : HttpExceptions()

        /** 401 Unauthorized */
        data object AuthenticationException : HttpExceptions()

        /** 403 Forbidden */
        data object AuthorizationException : HttpExceptions()

        /** 404 Not Found */
        data object NotFoundException : HttpExceptions()

        /** 5xx 서버 에러 */
        data object ServerException : HttpExceptions()
    }

    /**
     * 닉네임 관련 도메인 예외
     */
    sealed class NicknameException(
        override val message: String,
    ) : TodokTodokExceptions() {
        data object DuplicateNicknameException : NicknameException("[ERROR] 이미 존재하는 닉네임입니다")

        data object InvalidNicknameLengthException :
            NicknameException("[ERROR] 닉네임은 1자 이상, 8자 이하여야 합니다")

        data object EmptyNicknameLengthException : NicknameException("[ERROR] 닉네임을 입력해주세요")
    }

    /**
     * 회원가입 관련 도메인 예외
     */
    sealed class SignUpException(
        override val message: String,
    ) : TodokTodokExceptions() {
        data object DuplicateEmailException : SignUpException("[ERROR] 이미 가입된 이메일입니다")

        data object InvalidTokenException : SignUpException("[ERROR] 소셜 로그인을 하지 않은 이메일입니다")

        data object InvalidFormatEmailException : SignUpException("[ERROR] 올바른 이메일 형식을 입력해주세요")

        data object ProfileImageNotExistException : SignUpException("[ERROR] 프로필 이미지를 입력해주세요")
    }

    /**
     * 신고 관련 도메인 예외
     */
    sealed class ReportException(
        override val message: String,
    ) : TodokTodokExceptions() {
        data object AlreadyReportedException : ReportException("[ERROR] 이미 신고한 회원입니다")
    }

    /**
     * 차단 관련 도메인 예외
     */
    sealed class BlockException(
        override val message: String,
    ) : TodokTodokExceptions() {
        data object AlreadyBlockedException : BlockException("[ERROR] 이미 차단한 회원입니다")
    }

    companion object {
        /**
         * 서버 HTTP 상태 코드와 메시지를 기반으로 [TodokTodokExceptions] 객체를 생성
         *
         * @param statusCode HTTP 상태 코드 (e.g., 400, 401, 500)
         * @param message 서버에서 내려준 에러 메시지
         * @return 대응되는 [TodokTodokExceptions] 객체
         */
        fun from(
            statusCode: Int,
            message: String?,
        ): TodokTodokExceptions =
            when (statusCode) {
                400 -> fromTokdokTodokExceptions(message) // 400 Bad Request: 서버 메시지에 따라 도메인 예외 매핑
                401 -> HttpExceptions.AuthenticationException // 401 Unauthorized
                403 -> HttpExceptions.AuthorizationException // 403 Forbidden
                404 -> HttpExceptions.NotFoundException // 404 Not Found
                in 500..599 -> HttpExceptions.ServerException // 서버 5xx 에러
                else -> HttpExceptions.BadRequestException // 그 외는 BadRequest 예외로 처리
            }

        /**
         * 400 Bad Request 일 때, 서버 메시지를 기반으로 도메인 예외 매핑
         *
         * @param message 서버에서 내려준 에러 메시지
         * @return 대응되는 [TodokTodokExceptions] 객체
         */
        private fun fromTokdokTodokExceptions(message: String?): TodokTodokExceptions =
            when (message) {
                // 닉네임 관련 예외
                DuplicateNicknameException.message -> DuplicateNicknameException
                InvalidNicknameLengthException.message -> InvalidNicknameLengthException
                EmptyNicknameLengthException.message -> EmptyNicknameLengthException

                // 회원가입 관련 예외
                DuplicateEmailException.message -> DuplicateEmailException
                InvalidTokenException.message -> InvalidTokenException
                InvalidFormatEmailException.message -> InvalidFormatEmailException
                ProfileImageNotExistException.message -> ProfileImageNotExistException

                // 신고 관련 예외
                ReportException.AlreadyReportedException.message -> ReportException.AlreadyReportedException

                // 차단 관련 예외
                BlockException.AlreadyBlockedException.message -> BlockException.AlreadyBlockedException

                // 토론 관련 예외
                DiscussionExceptions.AlreadyReported.message -> DiscussionExceptions.AlreadyReported
                DiscussionExceptions.SelfReportNotAllowed.message -> DiscussionExceptions.SelfReportNotAllowed
                DiscussionExceptions.CannotDeleteWithComments.message -> DiscussionExceptions.CannotDeleteWithComments
                DiscussionExceptions.OnlyOwnerCanModifyOrDelete.message -> DiscussionExceptions.OnlyOwnerCanModifyOrDelete
                DiscussionExceptions.EmptyTitle.message -> DiscussionExceptions.EmptyTitle
                DiscussionExceptions.EmptyContent.message -> DiscussionExceptions.EmptyContent

                // 도서 관련 예외
                BookException.EmptyKeywordException.message -> BookException.EmptyKeywordException
                BookException.EmptyISBNException.message -> BookException.EmptyISBNException
                BookException.EmptySelectedBook.message -> BookException.EmptySelectedBook

                // 도서 저자 관련 예외
                BookAuthorException.EmptyBookAuthor.message -> BookAuthorException.EmptyBookAuthor

                // 도서 제목 관련 예외
                BookTitleException.EmptyBookTitle.message -> BookTitleException.EmptyBookTitle

                // 도서 이미지 관련 예외
                BookImageException.InvalidUrl.message -> BookImageException.InvalidUrl

                // 도서 ISBN 관련 예외
                ISBNException.InvalidLength.message -> ISBNException.InvalidLength
                ISBNException.InvalidFormat.message -> ISBNException.InvalidFormat

                // 도서 키워드 검색 예외
                KeywordException.BlankKeyword.message -> KeywordException.BlankKeyword
                KeywordException.EmptyKeyword.message -> KeywordException.EmptyKeyword

                // 댓글 관련 예외
                CommentExceptions.EmptyContent.message -> CommentExceptions.EmptyContent
                CommentExceptions.InvalidContentLength.message -> CommentExceptions.InvalidContentLength
                CommentExceptions.SelfReportNotAllowed.message -> CommentExceptions.SelfReportNotAllowed
                CommentExceptions.AlreadyReported.message -> CommentExceptions.AlreadyReported
                CommentExceptions.NotBelongToDiscussion.message -> CommentExceptions.NotBelongToDiscussion
                CommentExceptions.CannotDeleteWithReplies.message -> CommentExceptions.CannotDeleteWithReplies
                CommentExceptions.OnlyOwnerCanModifyOrDelete.message -> CommentExceptions.OnlyOwnerCanModifyOrDelete

                // 대댓글 관련 예외
                ReplyExceptions.EmptyContent.message -> ReplyExceptions.EmptyContent
                ReplyExceptions.InvalidContentLength.message -> ReplyExceptions.InvalidContentLength
                ReplyExceptions.SelfReportNotAllowed.message -> ReplyExceptions.SelfReportNotAllowed
                ReplyExceptions.AlreadyReported.message -> ReplyExceptions.AlreadyReported
                ReplyExceptions.CommentNotBelongToDiscussion.message -> ReplyExceptions.CommentNotBelongToDiscussion
                ReplyExceptions.ReplyNotBelongToComment.message -> ReplyExceptions.ReplyNotBelongToComment
                ReplyExceptions.OnlyOwnerCanModifyOrDelete.message -> ReplyExceptions.OnlyOwnerCanModifyOrDelete

                // 서버 메시지와 일치하지 않으면 기본 BadRequest 예외 반환
                else -> HttpExceptions.BadRequestException
            }
    }
}

/**
 * Throwable 객체를 [TodokTodokExceptions] 도메인 예외로 변환합니다.
 *
 * 네트워크 오류, IO 예외, 취소 예외 등 클라이언트 측에서 발생할 수 있는 예외를
 * 대응되는 도메인 예외로 매핑합니다.
 */
@OptIn(ExperimentalSerializationApi::class)
fun Throwable.toDomain(): TodokTodokExceptions =
    when (this) {
        is java.net.ConnectException -> TodokTodokExceptions.ConnectException // 서버 연결 실패
        is java.util.concurrent.TimeoutException -> TodokTodokExceptions.TimeoutError // 요청 시간 초과
        is java.net.UnknownHostException -> TodokTodokExceptions.UnknownHostError // 호스트를 찾을 수 없음
        is java.net.SocketException -> TodokTodokExceptions.SocketException // 소켓 오류
        is java.io.IOException -> TodokTodokExceptions.IOException // 일반 IO 예외
        is kotlin.coroutines.cancellation.CancellationException -> TodokTodokExceptions.CancellationException // 코루틴/작업 취소
        is kotlinx.serialization.MissingFieldException -> TodokTodokExceptions.MissingFieldException // Json 직렬화 오류
        else -> TodokTodokExceptions.UnknownException(this) // 그 외 알 수 없는 예외
    }
