package todoktodok.backend.notification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import todoktodok.backend.global.common.TimeStamp;
import todoktodok.backend.member.domain.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE notification SET deleted_at = NOW() WHERE id = ?")
public class Notification extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member recipient;

    @Column(nullable = false)
    private Long discussionId;

    private Long commentId;

    private Long replyId;

    @Column(nullable = false)
    private String memberNickname;

    @Column(nullable = false)
    private String discussionTitle;

    @Column(length = 2550)
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private NotificationTarget notificationTarget;

    @Builder
    public static Notification create(
            final Member recipient,
            final Long discussionId,
            final Long commentId,
            final Long replyId,
            final String memberNickname,
            final String discussionTitle,
            final String content,
            final NotificationType notificationType,
            final NotificationTarget notificationTarget
    ) {
        validateNotNullFields(recipient, discussionId, memberNickname, discussionTitle, notificationType, notificationTarget);

        return new Notification(
                null,
                false,
                recipient,
                discussionId,
                commentId,
                replyId,
                memberNickname,
                discussionTitle,
                content,
                notificationType,
                notificationTarget
        );
    }

    public boolean isOwnedBy(final Member recipient) {
        return this.recipient.equals(recipient);
    }

    public void read() {
        this.isRead = true;
    }

    private static void validateNotNullFields(
            final Member recipient,
            final Long discussionId,
            final String memberNickname,
            final String discussionTitle,
            final NotificationType type,
            final NotificationTarget target
    ) {
        if (recipient == null
                || discussionId == null
                || memberNickname == null || memberNickname.isBlank()
                || discussionTitle == null || discussionTitle.isBlank()
                || type == null
                || target == null
        ) {
            throw new IllegalStateException("알람을 생성하기 위한 값이 존재하지 않습니다");
        }
    }
}
