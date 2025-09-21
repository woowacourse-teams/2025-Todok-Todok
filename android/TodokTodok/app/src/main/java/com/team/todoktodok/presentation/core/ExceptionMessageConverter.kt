package com.team.todoktodok.presentation.core

import com.team.domain.model.exception.BlockException
import com.team.domain.model.exception.BookAuthorException
import com.team.domain.model.exception.BookException
import com.team.domain.model.exception.BookImageException
import com.team.domain.model.exception.BookTitleException
import com.team.domain.model.exception.BooksExceptions
import com.team.domain.model.exception.CommentExceptions
import com.team.domain.model.exception.DiscussionExceptions
import com.team.domain.model.exception.ISBNException
import com.team.domain.model.exception.ImageLoadExceptions
import com.team.domain.model.exception.KeywordException
import com.team.domain.model.exception.NicknameException
import com.team.domain.model.exception.ReplyExceptions
import com.team.domain.model.exception.ReportException
import com.team.domain.model.exception.SignUpException
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.R

class ExceptionMessageConverter {
    operator fun invoke(exception: TodokTodokExceptions): Int =
        when (exception) {
            is NicknameException.DuplicateNicknameException ->
                R.string.error_duplicate_nickname

            is NicknameException.InvalidNicknameLengthException ->
                R.string.error_invalid_nickname_length

            is NicknameException.EmptyNicknameLengthException ->
                R.string.error_empty_nickname

            is SignUpException.DuplicateEmailException ->
                R.string.error_duplicate_email

            is SignUpException.InvalidTokenException ->
                R.string.error_invalid_token

            is SignUpException.InvalidFormatEmailException ->
                R.string.error_invalid_email_format

            is SignUpException.ProfileImageNotExistException ->
                R.string.error_profile_image_not_exist

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

            TodokTodokExceptions.MissingFieldException ->
                R.string.error_missing_missing_field

            is TodokTodokExceptions.CancellationException ->
                R.string.error_request_cancelled

            is TodokTodokExceptions.ConnectException ->
                R.string.error_no_internet

            TodokTodokExceptions.RefreshTokenNotReceivedException ->
                R.string.error_refresh_token_not_received

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

            BlockException.AlreadyBlockedException ->
                R.string.error_already_blocked

            ReportException.AlreadyReportedException ->
                R.string.error_already_reported

            DiscussionExceptions.AlreadyReported -> R.string.error_already_reported
            DiscussionExceptions.CannotDeleteWithComments -> R.string.error_discussion_cannot_delete_with_comments
            DiscussionExceptions.OnlyOwnerCanModifyOrDelete -> R.string.error_discussion_only_owner_can_modify_or_delete
            DiscussionExceptions.SelfReportNotAllowed -> R.string.error_discussion_self_report_not_allowed
            DiscussionExceptions.EmptyContent -> R.string.error_empty_content
            DiscussionExceptions.EmptyTitle -> R.string.error_empty_title

            BooksExceptions.EmptyKeywordException -> R.string.select_book_error_empty_keyword
            BooksExceptions.EmptyISBNException -> R.string.select_book_error_empty_isbn

            CommentExceptions.AlreadyReported -> R.string.error_comment_already_reported
            CommentExceptions.CannotDeleteWithReplies -> R.string.error_comment_cannot_delete_with_replies
            CommentExceptions.EmptyContent -> R.string.error_comment_empty_content
            CommentExceptions.InvalidContentLength -> R.string.error_comment_invalid_content_length
            CommentExceptions.NotBelongToDiscussion -> R.string.error_comment_not_belong_to_discussion
            CommentExceptions.OnlyOwnerCanModifyOrDelete -> R.string.error_comment_only_owner_can_modify_or_delete
            CommentExceptions.SelfReportNotAllowed -> R.string.error_comment_self_report_not_allowed

            ReplyExceptions.EmptyContent -> R.string.error_reply_empty_content
            ReplyExceptions.InvalidContentLength -> R.string.error_reply_invalid_length
            ReplyExceptions.SelfReportNotAllowed -> R.string.error_reply_self_report_not_allowed
            ReplyExceptions.AlreadyReported -> R.string.error_reply_already_reported
            ReplyExceptions.CommentNotBelongToDiscussion -> R.string.error_reply_comment_not_in_discussion
            ReplyExceptions.ReplyNotBelongToComment -> R.string.error_reply_not_in_comment
            ReplyExceptions.OnlyOwnerCanModifyOrDelete -> R.string.error_reply_only_owner_can_modify_or_delete

            BookAuthorException.EmptyBookAuthor -> R.string.select_book_error_empty_author
            BookException.EmptyISBNException -> R.string.select_book_error_empty_isbn
            BookException.EmptyKeywordException -> R.string.select_book_error_empty_keyword
            BookException.EmptySelectedBook -> R.string.select_book_error_no_selected_book
            BookImageException.InvalidUrl -> R.string.select_book_error_invalid_url
            BookTitleException.EmptyBookTitle -> R.string.select_book_error_empty_book_title
            ISBNException.InvalidFormat -> R.string.select_book_error_invalid_isbn_format
            ISBNException.InvalidLength -> R.string.select_book_error_invalid_isbn_length

            KeywordException.BlankKeyword -> R.string.select_book_error_blank_keyword
            KeywordException.EmptyKeyword -> R.string.select_book_error_empty_keyword

            ImageLoadExceptions.UriInputStreamNotFoundException -> R.string.error_uri_input_stream_not_found
        }
}
