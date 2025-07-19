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
    public void setNoteInfo() {
        em.createNativeQuery( //todo book_id 추가
                """
                INSERT INTO NOTE (snap, memo, member_id, created_at, modified_at)
                VALUES
                ('DI와 IoC는 스프링의 중요한 개념이다.', 'Spring의 동작 원리를 이해하는 데 큰 도움이 됐다.', 1L, CURRENT_TIME, CURRENT_TIME)
                """
        ).executeUpdate();
    }
}
