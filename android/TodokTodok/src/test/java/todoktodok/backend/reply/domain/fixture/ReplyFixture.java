package todoktodok.backend.reply.domain.fixture;

import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.reply.domain.Reply;

public class ReplyFixture {

    public static Reply create(
            final String content,
            final Member member,
            final Comment comment
    ) {
        return Reply.builder()
                .content(content)
                .member(member)
                .comment(comment)
                .build();
    }
}
