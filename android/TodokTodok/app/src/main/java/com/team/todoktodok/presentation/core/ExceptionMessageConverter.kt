package com.team.todoktodok.presentation.core

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.R

class ExceptionMessageConverter {
    operator fun invoke(exception: TodokTodokExceptions): Int =
        when (exception) {
            is TodokTodokExceptions.NicknameException.DuplicateNicknameException ->
                R.string.error_duplicate_nickname

            is TodokTodokExceptions.NicknameException.InvalidNicknameLengthException ->
                R.string.error_invalid_nickname_length

            is TodokTodokExceptions.NicknameException.EmptyNicknameLengthException ->
                R.string.error_empty_nickname

            is TodokTodokExceptions.SignUpException.DuplicateEmailException ->
                R.string.error_duplicate_email

            is TodokTodokExceptions.SignUpException.InvalidTokenException ->
                R.string.error_invalid_token

            is TodokTodokExceptions.SignUpException.InvalidFormatEmailException ->
                R.string.error_invalid_email_format

            is TodokTodokExceptions.SignUpException.ProfileImageNotExistException ->
                R.string.error_profile_image_not_exist

            is TodokTodokExceptions.HttpExceptions.AuthenticationException ->
                R.string.error_authentication

            is TodokTodokExceptions.HttpExceptions.AuthorizationException ->
                R.string.error_authorization

            is TodokTodokExceptions.HttpExceptions.NotFoundException ->
                R.string.error_not_found

            is TodokTodokExceptions.HttpExceptions.ServerException ->
                R.string.error_server

            is TodokTodokExceptions.HttpExceptions.BadRequestException ->
                R.string.error_bad_request

            is TodokTodokExceptions.UnknownException ->
                R.string.error_unknown

            is TodokTodokExceptions.MissingLocationHeaderException ->
                R.string.error_missing_location

            is TodokTodokExceptions.CancellationException ->
                R.string.error_request_cancelled

            is TodokTodokExceptions.ConnectException ->
                R.string.error_no_internet

            is TodokTodokExceptions.IOException ->
                R.string.error_io_exception

            is TodokTodokExceptions.SocketException ->
                R.string.error_no_internet

            is TodokTodokExceptions.TimeoutError ->
                R.string.error_timeout

            is TodokTodokExceptions.UnknownHostError ->
                R.string.error_no_internet

            TodokTodokExceptions.EmptyBodyException ->
                R.string.error_empty_body

            TodokTodokExceptions.BlockException.AlreadyBlockedException ->
                R.string.error_already_blocked

            TodokTodokExceptions.ReportException.AlreadyReportedException ->
                R.string.error_already_reported
        }
}
