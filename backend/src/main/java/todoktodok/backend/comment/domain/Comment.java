package todoktodok.backend.comment.domain;

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
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.global.common.TimeStamp;
import todoktodok.backend.member.domain.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE id = ?")
public class Comment extends TimeStamp {

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
    private Discussion discussion;

    @Builder
    public static Comment create(
            final String content,
            final Member member,
            final Discussion discussion
    ) {
        validateContent(content);

        return new Comment(
                null, content, member, discussion
        );
    }

    public void updateContent(final String content) {
        validateContent(content);
        this.content = content;
    }

    public boolean isOwnedBy(final Member member) {
        return this.member.equals(member);
    }

    public boolean isSameId(final Long commentId) {
        return this.id.equals(commentId);
    }

    public void validateMatchWithDiscussion(final Discussion discussion) {
        if (!this.discussion.equals(discussion)) {
            throw new IllegalArgumentException(String.format("해당 토론방에 있는 댓글이 아닙니다: discussion-%d, comment-%d"
                    , discussion.getId(), this.id));
        }
    }

    public void validateSelfReport(final Member member) {
        if (this.member.equals(member)) {
            throw new IllegalArgumentException(String.format("자기 자신이 작성한 댓글을 신고할 수 없습니다: %d -> %d"
                    , member.getId(), this.id));
        }
    }

    private static void validateContent(final String content) {
        if (content.isEmpty() || content.length() > CONTENT_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("댓글 내용은 1자 이상, 1500자 이하여야 합니다: %d자", content.length()));
        }
    }
}
