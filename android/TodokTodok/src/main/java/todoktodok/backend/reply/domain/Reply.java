package todoktodok.backend.reply.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.global.common.TimeStamp;
import todoktodok.backend.member.domain.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE reply SET deleted_at = NOW() WHERE id = ?")
public class Reply extends TimeStamp {

    public static final int CONTENT_MAX_LENGTH = 2000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Comment comment;

    @Builder
    public static Reply create(
            final String content,
            final Member member,
            final Comment comment
    ) {
        validateContent(content);

        return new Reply(
                null, content, member, comment
        );
    }

    public void updateContent(final String content) {
        validateContent(content);
        this.content = content;
    }

    public boolean isOwnedBy(final Member member) {
        return this.member.equals(member);
    }

    public boolean isSameId(final Long replyId) {
        return this.id.equals(replyId);
    }

    public void validateMatchWithComment(final Comment comment) {
        if (!this.comment.equals(comment)) {
            throw new IllegalArgumentException(String.format("해당 댓글에 있는 대댓글이 아닙니다: commentId = %s, replyId = %s"
                    , comment.getId(), this.id));
        }
    }

    public void validateSelfReport(final Member member) {
        if (this.member.equals(member)) {
            throw new IllegalArgumentException(String.format("자기 자신이 작성한 대댓글을 신고할 수 없습니다: memberId = %s, replyId = %s"
                    , member.getId(), this.getId()));
        }
    }

    private static void validateContent(final String content) {
        if (content.isEmpty() || content.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("대댓글 내용은 1자 이상, 2000자 이하여야 합니다: %s자", content.length()));
        }
    }
}
