package todoktodok.backend.comment.application.service.query;

public record CommentReplyCountDto(
        Long commentId,
        int replyCount
) {
    public CommentReplyCountDto(final Long commentId, final Long replyCount) {
        this(commentId, replyCount.intValue());
    }
}
