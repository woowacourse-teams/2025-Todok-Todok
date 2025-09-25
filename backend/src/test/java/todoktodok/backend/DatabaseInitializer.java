package todoktodok.backend;

import static java.time.temporal.ChronoUnit.MICROS;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseInitializer {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void clear() {
        em.clear();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY=0").executeUpdate();
        final List<String> tables = em.createNativeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'"
        ).getResultList();

        for (String table : tables) {
            em.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            try {
                em.createNativeQuery("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
            } catch (Exception e) {
            }
        }
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY=1").executeUpdate();
    }

    @Transactional
    public void setDefaultUserInfo() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO MEMBER (email, nickname, profile_image, profile_message, created_at, modified_at)
                                VALUES
                                ('user@gmail.com', 'user', 'https://user.png', 'user', :createdAt, :modifiedAt)
                                """
                )
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setUserInfo(
            final String email,
            final String nickname,
            final String profileImage,
            final String profileMessage
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO MEMBER (email, nickname, profile_image, profile_message, created_at, modified_at)
                                VALUES 
                                (:email, :nickname, :profileImage, :profileMessage, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("email", email)
                .setParameter("nickname", nickname)
                .setParameter("profileImage", profileImage)
                .setParameter("profileMessage", profileMessage)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void deleteUserInfo(
            final String email
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                UPDATE Member m 
                                SET m.deleted_at = :deletedAt
                                WHERE m.email = :email
                                """
                )
                .setParameter("email", email)
                .setParameter("deletedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDefaultBookInfo() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO BOOK (title, summary, author, publisher, isbn, image, created_at, modified_at)
                                VALUES
                                ('오브젝트', '오브젝트 설명', '조영호', '위키북스', '9791158391409', 'image.png', :createdAt, :modifiedAt)
                                """
                )
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setBookInfo(
            final String title,
            final String summary,
            final String author,
            final String publisher,
            final String isbn,
            final String image
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO BOOK (title, summary, author, publisher, isbn, image, created_at, modified_at)
                                VALUES 
                                (:title, :summary, :author, :publisher, :isbn, :image, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("title", title)
                .setParameter("summary", summary)
                .setParameter("author", author)
                .setParameter("publisher", publisher)
                .setParameter("isbn", isbn)
                .setParameter("image", image)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDefaultDiscussionInfo() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION (title, content, member_id, book_id, created_at, modified_at)
                                VALUES 
                                ('상속과 조합의 차이', '코드 재사용에 있어 조합이 유리하다면, 상속의 목적은 무엇인가요?', 1L, 1L, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDiscussionInfo(
            final String title,
            final String content,
            final Long memberId,
            final Long bookId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION (title, content, member_id, book_id, created_at, modified_at)
                                VALUES 
                                (:title, :content, :memberId, :bookId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("title", title)
                .setParameter("content", content)
                .setParameter("memberId", memberId)
                .setParameter("bookId", bookId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDiscussionInfo(
            final String title,
            final String content,
            final Long memberId,
            final Long bookId,
            final LocalDateTime createdAt
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION (title, content, member_id, book_id, created_at, modified_at)
                                VALUES 
                                (:title, :content, :memberId, :bookId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("title", title)
                .setParameter("content", content)
                .setParameter("memberId", memberId)
                .setParameter("bookId", bookId)
                .setParameter("createdAt", createdAt)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDefaultDiscussionMemberViewInfo() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION_MEMBER_VIEW (discussion_id, member_id, created_at, modified_at)
                                VALUES 
                                (:discussionId, :memberId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("discussionId", 1L)
                .setParameter("memberId", 1L)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDiscussionMemberViewInfo(
            final Long memberId,
            final Long discussionId,
            final int minusMinute
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);
        final LocalDateTime before = now.minusMinutes(minusMinute);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION_MEMBER_VIEW (discussion_id, member_id, created_at, modified_at)
                                VALUES 
                                (:discussionId, :memberId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("discussionId", discussionId)
                .setParameter("memberId", memberId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", before)
                .executeUpdate();
    }

    @Transactional
    public void setDiscussionLikeInfo(
            final Long memberId,
            final Long discussionId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION_LIKE (member_id, discussion_id, created_at, modified_at)
                                VALUES 
                                (:memberId, :discussionId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("memberId", memberId)
                .setParameter("discussionId", discussionId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDiscussionLikeInfo(
            final Long memberId,
            final Long discussionId,
            final LocalDateTime createdAt
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO DISCUSSION_LIKE (member_id, discussion_id, created_at, modified_at)
                                VALUES 
                                (:memberId, :discussionId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("memberId", memberId)
                .setParameter("discussionId", discussionId)
                .setParameter("createdAt", createdAt)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDefaultCommentInfo() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO COMMENT (content, member_id, discussion_id, created_at, modified_at)
                                VALUES 
                                ('상속의 핵심 목적은 타입 계층의 구축입니다!', 1L, 1L, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setCommentInfo(
            final String content,
            final Long memberId,
            final Long discussionId,
            LocalDateTime createdAt
    ) {
        em.createNativeQuery(
                        """
                                INSERT INTO COMMENT (content, member_id, discussion_id, created_at, modified_at)
                                VALUES 
                                (:content, :memberId, :discussionId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("content", content)
                .setParameter("memberId", memberId)
                .setParameter("discussionId", discussionId)
                .setParameter("createdAt", createdAt)
                .setParameter("modifiedAt", createdAt)
                .executeUpdate();
    }

    @Transactional
    public void setCommentInfo(
            final String content,
            final Long memberId,
            final Long discussionId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO COMMENT (content, member_id, discussion_id, created_at, modified_at)
                                VALUES 
                                (:content, :memberId, :discussionId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("content", content)
                .setParameter("memberId", memberId)
                .setParameter("discussionId", discussionId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setCommentLikeInfo(
            final Long memberId,
            final Long commentId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO COMMENT_LIKE (member_id, comment_id, created_at, modified_at)
                                VALUES 
                                (:memberId, :commentId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("memberId", memberId)
                .setParameter("commentId", commentId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setDefaultReplyInfo() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO REPLY (content, member_id, comment_id, created_at, modified_at)
                                VALUES 
                                ('저도 같은 의견입니다!', 1L, 1L, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setReplyInfo(
            final String content,
            final Long memberId,
            final Long commentId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO REPLY (content, member_id, comment_id, created_at, modified_at)
                                VALUES 
                                (:content, :memberId, :commentId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("content", content)
                .setParameter("memberId", memberId)
                .setParameter("commentId", commentId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setReplyInfo(
            final String content,
            final Long memberId,
            final Long commentId,
            final LocalDateTime createdAt
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO REPLY (content, member_id, comment_id, created_at, modified_at)
                                VALUES 
                                (:content, :memberId, :commentId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("content", content)
                .setParameter("memberId", memberId)
                .setParameter("commentId", commentId)
                .setParameter("createdAt", createdAt)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setReplyLikeInfo(
            final Long memberId,
            final Long replyId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO REPLY_LIKE (member_id, reply_id, created_at, modified_at)
                                VALUES 
                                (:memberId, :replyId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("memberId", memberId)
                .setParameter("replyId", replyId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setBlockInfo(
            final Long memberId,
            final Long targetId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO BLOCK (member_id, target_id, created_at, modified_at)
                                VALUES 
                                (:memberId, :targetId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("memberId", memberId)
                .setParameter("targetId", targetId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }

    @Transactional
    public void setNotificationTokenInfo(
            final String token,
            final String fid,
            final Long memberId
    ) {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MICROS);

        em.createNativeQuery(
                        """
                                INSERT INTO NOTIFICATION_TOKEN (token, fid, member_id, created_at, modified_at)
                                VALUES 
                                (:token, :fid, :memberId, :createdAt, :modifiedAt)
                                """
                )
                .setParameter("token", token)
                .setParameter("fid", fid)
                .setParameter("memberId", memberId)
                .setParameter("createdAt", now)
                .setParameter("modifiedAt", now)
                .executeUpdate();
    }
}
