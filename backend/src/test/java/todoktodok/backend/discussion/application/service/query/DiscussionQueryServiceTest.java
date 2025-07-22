package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class DiscussionQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private DiscussionQueryService discussionQueryService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("전체 토론방을 조회한다")
    void getDiscussions() {
        // given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        databaseInitializer.setNoteInfo();
        databaseInitializer.setDiscussionInfo();

        final Long memberId = 1L;

        // when
        List<DiscussionResponse> discussions = discussionQueryService.getDiscussions(memberId);

        // then
        assertThat(discussions).hasSize(1);
    }
}
