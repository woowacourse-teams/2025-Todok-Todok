package todoktodok.backend.comment.application.service.query;

public record CommentLikeCountDto(
        Long commentId,
        int likeCount
) {
    public CommentLikeCountDto(final Long commentId, final Long likeCount) {
        this(commentId, likeCount.intValue());
    }
}
