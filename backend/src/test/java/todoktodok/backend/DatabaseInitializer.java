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
    public void setUserInfo() {
        em.createNativeQuery(
                """
                INSERT INTO MEMBER (email, nickname, profile_image, profile_message, created_at, modified_at)
                VALUES
                ('user@gmail.com', 'user', 'https://user.png', 'user', CURRENT_TIME, CURRENT_TIME)
                """
        ).executeUpdate();
    }

    @Transactional
    public void setBookInfo() {
        em.createNativeQuery(
                """
                INSERT INTO BOOK (title, summary, author, publisher, isbn, image, created_at, modified_at)
                VALUES
                ('오브젝트', '오브젝트 설명', '조영호', '위키북스', '9791158391409', 'image.png', CURRENT_TIME, CURRENT_TIME)
                """
        ).executeUpdate();
    }

    @Transactional
    public void setShelfInfo() {
        em.createNativeQuery(
                """
                INSERT INTO SHELF (member_id, book_id, created_at, modified_at)
                VALUES
                (1, 1, CURRENT_TIME, CURRENT_TIME)
                """
        ).executeUpdate();
    }
}
