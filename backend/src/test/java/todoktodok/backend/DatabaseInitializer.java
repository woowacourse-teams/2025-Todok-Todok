package todoktodok.backend;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        em.createNativeQuery(
                """
                        INSERT INTO MEMBER (email, nickname, profile_image, profile_message, created_at, modified_at)
                        VALUES
                        ('user@gmail.com', 'user', 'https://user.png', 'user', CURRENT_TIME, CURRENT_TIME)
                        """
        ).executeUpdate();
    }

    @Transactional
    public void setUserInfo(
            final String email,
            final String nickname,
            final String profileImage,
            final String profileMessage
    ) {
        em.createNativeQuery(
                """
                        INSERT INTO MEMBER (email, nickname, profile_image, profile_message, created_at, modified_at)
                        VALUES 
                        (:email, :nickname, :profileImage, :profileMessage, CURRENT_TIME, CURRENT_TIME)
                        """
        )
        .setParameter("email", email)
        .setParameter("nickname", nickname)
        .setParameter("profileImage", profileImage)
        .setParameter("profileMessage", profileMessage)
        .executeUpdate();
    }

    @Transactional
    public void setDefaultBookInfo() {
        em.createNativeQuery(
                """
                        INSERT INTO BOOK (title, summary, author, publisher, isbn, image, created_at, modified_at)
                        VALUES
                        ('오브젝트', '오브젝트 설명', '조영호', '위키북스', '9791158391409', 'image.png', CURRENT_TIME, CURRENT_TIME)
                        """
        ).executeUpdate();
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
        em.createNativeQuery(
                """
                        INSERT INTO BOOK (title, summary, author, publisher, isbn, image, created_at, modified_at)
                        VALUES 
                        (:title, :summary, :author, :publisher, :isbn, :image, CURRENT_TIME, CURRENT_TIME)
                        """
        )
        .setParameter("title", title)
        .setParameter("summary", summary)
        .setParameter("author", author)
        .setParameter("publisher", publisher)
        .setParameter("isbn", isbn)
        .setParameter("image", image)
        .executeUpdate();
    }

    @Transactional
    public void setDefaultShelfInfo() {
        em.createNativeQuery(
                """
                        INSERT INTO SHELF (member_id, book_id, created_at, modified_at)
                        VALUES
                        (1, 1, CURRENT_TIME, CURRENT_TIME)
                        """
        ).executeUpdate();
    }

    @Transactional
    public void setShelfInfo(
            final Long memberId,
            final Long bookId
    ) {
        em.createNativeQuery(
                """
                        INSERT INTO SHELF (member_id, book_id, created_at, modified_at)
                        VALUES 
                        (:memberId, :bookId, CURRENT_TIME, CURRENT_TIME)
                        """
        )
        .setParameter("memberId", memberId)
        .setParameter("bookId", bookId)
        .executeUpdate();
    }

    @Transactional
    public void setDefaultNoteInfo() {
        em.createNativeQuery(
                """
                        INSERT INTO NOTE (snap, memo, book_id, member_id, created_at, modified_at)
                        VALUES
                        ('코드 재사용을 위해서는 객체 합성이 클래스 상속보다 더 좋은 방법이다.', '변경에 유연하게 대처할 수 있는 설계가 대부분 정답이다.', 1L, 1L, CURRENT_TIME, CURRENT_TIME)
                        """
        ).executeUpdate();
    }

    @Transactional
    public void setNoteInfo(
            final String snap,
            final String memo,
            final Long bookId,
            final Long memberId
    ) {
        em.createNativeQuery(
                """
                        INSERT INTO NOTE (snap, memo, book_id, member_id, created_at, modified_at)
                        VALUES 
                        (:snap, :memo, :bookId, :memberId, CURRENT_TIME, CURRENT_TIME)
                        """
        )
        .setParameter("snap", snap)
        .setParameter("memo", memo)
        .setParameter("bookId", bookId)
        .setParameter("memberId", memberId)
        .executeUpdate();
    }

    @Transactional
    public void setDefaultDiscussionInfo() {
        em.createNativeQuery(
                """
                        INSERT INTO DISCUSSION (title, content, member_id, book_id, note_id, created_at, modified_at)
                        VALUES 
                        ('상속과 조합의 차이', '코드 재사용에 있어 조합이 유리하다면, 상속의 목적은 무엇인가요?', 1L, 1L, 1L, CURRENT_TIME, CURRENT_TIME)
                        """
        ).executeUpdate();
    }

    @Transactional
    public void setDiscussionInfo(
            final String title,
            final String content,
            final Long memberId,
            final Long bookId,
            final Long noteId
    ) {
        em.createNativeQuery(
                """
                        INSERT INTO DISCUSSION (title, content, member_id, book_id, note_id, created_at, modified_at)
                        VALUES 
                        (:title, :content, :memberId, :bookId, :noteId, CURRENT_TIME, CURRENT_TIME)
                        """
        )
        .setParameter("title", title)
        .setParameter("content", content)
        .setParameter("memberId", memberId)
        .setParameter("bookId", bookId)
        .setParameter("noteId", noteId)
        .executeUpdate();
    }
}
