package com.team.todoktodok.presentation.core

import com.team.domain.model.exception.TokdokTodokExceptions
import com.team.todoktodok.R

class ExceptionMessageConverter {
    operator fun invoke(exception: TokdokTodokExceptions): Int =
        when (exception) {
            is TokdokTodokExceptions.NicknameException.DuplicateNicknameException ->
                R.string.error_duplicate_nickname

            is TokdokTodokExceptions.NicknameException.InvalidNicknameLengthException ->
                R.string.error_invalid_nickname_length

            is TokdokTodokExceptions.NicknameException.EmptyNicknameLengthException ->
                R.string.error_empty_nickname

            is TokdokTodokExceptions.SignUpException.DuplicateEmailException ->
                R.string.error_duplicate_email

            is TokdokTodokExceptions.SignUpException.InvalidTokenException ->
                R.string.error_invalid_token

            is TokdokTodokExceptions.SignUpException.InvalidFormatEmailException ->
                R.string.error_invalid_email_format

            is TokdokTodokExceptions.SignUpException.ProfileImageNotExistException ->
                R.string.error_profile_image_not_exist

            is TokdokTodokExceptions.HttpExceptions.AuthenticationException ->
                R.string.error_authentication

            is TokdokTodokExceptions.HttpExceptions.AuthorizationException ->
                R.string.error_authorization

            is TokdokTodokExceptions.HttpExceptions.NotFoundException ->
                R.string.error_not_found

            is TokdokTodokExceptions.HttpExceptions.ServerException ->
                R.string.error_server

            is TokdokTodokExceptions.HttpExceptions.BadRequestException ->
                R.string.error_bad_request

            is TokdokTodokExceptions.UnknownException ->
                R.string.error_unknown

            is TokdokTodokExceptions.MissingLocationHeaderException ->
                R.string.error_missing_location

            is TokdokTodokExceptions.CancellationException ->
                R.string.error_request_cancelled

            is TokdokTodokExceptions.ConnectException ->
                R.string.error_no_internet

            is TokdokTodokExceptions.IOException ->
                R.string.error_io_exception

            is TokdokTodokExceptions.SocketException ->
                R.string.error_no_internet

            is TokdokTodokExceptions.TimeoutError ->
                R.string.error_timeout

            is TokdokTodokExceptions.UnknownHostError ->
                R.string.error_no_internet
        }
}
