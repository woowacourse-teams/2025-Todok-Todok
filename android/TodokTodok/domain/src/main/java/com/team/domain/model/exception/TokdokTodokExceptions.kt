package com.team.domain.model.exception

import com.team.domain.model.exception.TokdokTodokExceptions.NicknameException.DuplicateNicknameException
import com.team.domain.model.exception.TokdokTodokExceptions.NicknameException.EmptyNicknameLengthException
import com.team.domain.model.exception.TokdokTodokExceptions.NicknameException.InvalidNicknameLengthException
import com.team.domain.model.exception.TokdokTodokExceptions.SignUpException.DuplicateEmailException
import com.team.domain.model.exception.TokdokTodokExceptions.SignUpException.InvalidFormatEmailException
import com.team.domain.model.exception.TokdokTodokExceptions.SignUpException.InvalidTokenException
import com.team.domain.model.exception.TokdokTodokExceptions.SignUpException.ProfileImageNotExistException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

/**
 * 애플리케이션에서 발생할 수 있는 네트워크/도메인 예외를 정의한 sealed class.
 *
 * - 서버 HTTP 상태 코드 및 서버에서 내려주는 메시지를 기반으로 도메인 레이어에서 처리
 * - 화면에서는 이 예외를 기반으로 UI 메시지를 표시하거나 로직을 분기 처리 가능
 */
sealed class TokdokTodokExceptions : Throwable() {
    data object ConnectException : TokdokTodokExceptions()

    data object TimeoutError : TokdokTodokExceptions()

    data object UnknownHostError : TokdokTodokExceptions()

    data object SocketException : TokdokTodokExceptions()

    data object IOException : TokdokTodokExceptions()

    data object CancellationException : TokdokTodokExceptions()

    /** 알 수 없는 예외 발생 */
    data object UnknownException : TokdokTodokExceptions()

    /** 헤더의 Location 필드가 누락된 경우 발생 */
    data object MissingLocationHeaderException : TokdokTodokExceptions()

    /**
     * HTTP 상태 코드 기반 예외
     */
    sealed class HttpExceptions : TokdokTodokExceptions() {
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
    ) : TokdokTodokExceptions() {
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
    ) : TokdokTodokExceptions() {
        data object DuplicateEmailException : SignUpException("[ERROR] 이미 가입된 이메일입니다")

        data object InvalidTokenException : SignUpException("[ERROR] 소셜 로그인을 하지 않은 이메일입니다")

        data object InvalidFormatEmailException : SignUpException("[ERROR] 올바른 이메일 형식을 입력해주세요")

        data object ProfileImageNotExistException : SignUpException("[ERROR] 프로필 이미지를 입력해주세요")
    }

    companion object {
        /**
         * 서버 HTTP 상태 코드와 메시지를 기반으로 [TokdokTodokExceptions] 객체를 생성
         *
         * @param statusCode HTTP 상태 코드 (e.g., 400, 401, 500)
         * @param message 서버에서 내려준 에러 메시지
         * @return 대응되는 [TokdokTodokExceptions] 객체
         */
        fun from(
            statusCode: Int,
            message: String?,
        ): TokdokTodokExceptions =
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
         * @return 대응되는 [TokdokTodokExceptions] 객체
         */
        private fun fromTokdokTodokExceptions(message: String?): TokdokTodokExceptions =
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

                // 서버 메시지와 일치하지 않으면 기본 BadRequest 예외 반환
                else -> HttpExceptions.BadRequestException
            }
    }
}

/**
 * Throwable 객체를 [TokdokTodokExceptions] 도메인 예외로 변환합니다.
 *
 * 네트워크 오류, IO 예외, 취소 예외 등 클라이언트 측에서 발생할 수 있는 예외를
 * 대응되는 도메인 예외로 매핑합니다.
 */
fun Throwable.toDomain(): TokdokTodokExceptions =
    when (this) {
        is ConnectException -> TokdokTodokExceptions.ConnectException // 서버 연결 실패
        is TimeoutException -> TokdokTodokExceptions.TimeoutError // 요청 시간 초과
        is UnknownHostException -> TokdokTodokExceptions.UnknownHostError // 호스트를 찾을 수 없음
        is SocketException -> TokdokTodokExceptions.SocketException // 소켓 오류
        is IOException -> TokdokTodokExceptions.IOException // 일반 IO 예외
        is CancellationException -> TokdokTodokExceptions.CancellationException // 코루틴/작업 취소
        else -> TokdokTodokExceptions.UnknownException // 그 외 알 수 없는 예외
    }
