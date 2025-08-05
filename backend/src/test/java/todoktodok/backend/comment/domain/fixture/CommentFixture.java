package todoktodok.backend.comment.domain.fixture;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public class CommentFixture {

    public static Comment create(
            final String content,
            final Member member,
            final Discussion discussion
    ){
        return Comment.builder()
                .content(content)
                .member(member)
                .discussion(discussion)
                .build();
    }
}
