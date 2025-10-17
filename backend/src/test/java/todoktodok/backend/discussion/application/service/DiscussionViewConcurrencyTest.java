package todoktodok.backend.discussion.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.discussion.application.service.query.DiscussionQueryService;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class DiscussionViewConcurrencyTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private DiscussionQueryService discussionQueryService;

    @Autowired
    private DiscussionRepository discussionRepository;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("특정 토론방을 동시에 조회하면 조회수가 증가한다")
    void getDiscussion_updateViewCount_inConcurrency() throws Exception {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("email2@gmail.com", "user2", "", "");
        databaseInitializer.setUserInfo("email3@gmail.com", "user3", "", "");
        databaseInitializer.setUserInfo("email4@gmail.com", "user4", "", "");

        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long discussionId = 1L;
        final List<Long> memberIds = List.of(2L, 3L, 4L);
        final int threads = memberIds.size();

        final CountDownLatch ready = new CountDownLatch(threads);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threads);

        final List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        // 각 회원마다 동시에 getDiscussion() 호출
        for (final Long memberId : memberIds) {
            new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    discussionQueryService.getDiscussion(memberId, discussionId);
                } catch (final Throwable t) {
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            }).start();
        }

        // when : 모든 스레드 준비 완료 후 동시에 시작
        ready.await();
        start.countDown();
        done.await();

        // then : 최종 조회수
        final Discussion discussion = discussionRepository.findById(discussionId).orElseThrow();
        assertAll(
                () -> assertThat(discussion.getViewCount()).isEqualTo(threads),
                () -> assertThat(errors).isEmpty()
        );
    }
}
