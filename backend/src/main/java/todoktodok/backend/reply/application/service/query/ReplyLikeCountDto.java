package todoktodok.backend.reply.application.service.query;

public record ReplyLikeCountDto(
        Long replyId,
        int likeCount
) {

    public ReplyLikeCountDto(
            final Long replyId,
            final Long likeCount
    ) {
        this(
                replyId,
                Math.toIntExact(likeCount)
        );
    }
}
