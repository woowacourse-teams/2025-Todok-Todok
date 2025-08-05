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

    public static final int CONTENT_MAX_LENGTH = 1500;

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

    public void validateMatchWithComment(final Comment comment) {
        if (!this.comment.equals(comment)) {
            throw new IllegalArgumentException("해당 댓글에 있는 대댓글이 아닙니다");
        }
    }

    public void validateSelfReport(final Member member) {
        if (this.member.equals(member)) {
            throw new IllegalArgumentException("자기 자신이 작성한 대댓글을 신고할 수 없습니다");
        }
    }

    private static void validateContent(final String content) {
        if (content.isEmpty() || content.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException("대댓글 내용은 1자 이상, 1500자 이하여야 합니다");
        }
    }
}

