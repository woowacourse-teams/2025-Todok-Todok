package todoktodok.backend.reply.application.dto.response;

import todoktodok.backend.member.application.dto.response.MemberResponse;
import todoktodok.backend.reply.domain.Reply;

import java.time.LocalDateTime;

public record ReplyResponse(
        Long replyId,
        MemberResponse member,
        LocalDateTime createdAt,
        String content,
        int likeCount,
        boolean isLikedByMe
) {

    public ReplyResponse(
            final Reply reply,
            final int likeCount,
            final boolean isLikedByMe
    ) {
        this(
                reply.getId(),
                new MemberResponse(reply.getMember()),
                reply.getCreatedAt(),
                reply.getContent(),
                likeCount,
                isLikedByMe
        );
    }
}
