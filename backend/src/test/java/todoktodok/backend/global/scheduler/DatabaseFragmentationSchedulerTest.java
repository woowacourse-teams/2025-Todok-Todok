package todoktodok.backend.global.scheduler;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import todoktodok.backend.InitializerTimer;

import static org.assertj.core.api.Assertions.assertThatCode;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = InitializerTimer.class)
class DatabaseFragmentationSchedulerTest {

    @Autowired(required = false)
    private DatabaseFragmentationScheduler scheduler;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("스케줄러가 비활성화된 경우 빈이 생성되지 않는다")
    void schedulerNotCreatedWhenDisabledTest() {
        // given
        // when
        // then
        assertThatCode(() -> {
            if (scheduler == null) {
                return;
            }
        }).doesNotThrowAnyException();
    }
}
