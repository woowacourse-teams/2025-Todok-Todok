package todoktodok.backend.discussion.application.dto.response;

import java.time.LocalDateTime;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.application.dto.response.MemberResponse;
import todoktodok.backend.member.domain.Member;

public record DiscussionResponse(
        Long discussionId,
        BookResponse book,
        MemberResponse member,
        LocalDateTime createdAt,
        String discussionTitle,
        String discussionOpinion,
        int viewCount,
        int likeCount,
        int commentCount,
        boolean isLikedByMe,
        LocalDateTime lastCommentedAt
) {

    private static final LocalDateTime fakeLastCommentedAt = LocalDateTime.now();

    public DiscussionResponse(
            final Discussion discussion,
            final int likeCount,
            final int commentCount,
            final boolean isLikedByMe
    ) {
        this(
                discussion.getId(),
                new BookResponse(discussion.getBook()),
                new MemberResponse(discussion.getMember()),
                discussion.getCreatedAt(),
                discussion.getTitle(),
                discussion.getContent(),
                Math.toIntExact(discussion.getViewCount()),
                likeCount,
                commentCount,
                isLikedByMe,
                fakeLastCommentedAt
        );
    }

    public DiscussionResponse(
            final Long discussionId,
            final String discussionTitle,
            final String discussionOpinion,
            final LocalDateTime createdAt,
            final Long viewCount,
            final Book book,
            final Member member,
            final Long likeCount,
            final Long commentCount,
            final boolean isLikedByMe
    ) {
        this(
                discussionId,
                new BookResponse(book),
                new MemberResponse(member),
                createdAt,
                discussionTitle,
                discussionOpinion,
                Math.toIntExact(viewCount),
                Math.toIntExact(likeCount),
                Math.toIntExact(commentCount),
                isLikedByMe,
                fakeLastCommentedAt
        );
    }
}
