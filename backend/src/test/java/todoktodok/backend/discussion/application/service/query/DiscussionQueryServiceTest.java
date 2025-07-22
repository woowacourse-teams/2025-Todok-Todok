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
        databaseInitializer.setDefaultUserInfo();;
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();
        databaseInitializer.setDefaultDiscussionInfo();;

        final Long memberId = 1L;

        // when
        List<DiscussionResponse> discussions = discussionQueryService.getDiscussions(memberId);

        // then
        assertThat(discussions).hasSize(1);
    }

    @Test
    @DisplayName("특정 토론방을 조회한다")
    void getDiscussion() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();
        final Long memberId = 1L;
        final Long bookId = 1L;
        final Long noteId = 1L;

        databaseInitializer.setDiscussionInfo(
                "클린코드에 대해 논의해볼까요",
                "클린코드만세",
                memberId,
                bookId,
                noteId
        );
        final Long discussionId = 1L;

        // when
        DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(memberId, discussionId);

        // then
        assertThat(discussionResponse.discussionId()).isEqualTo(discussionId);
        assertThat(discussionResponse.discussionTitle()).isEqualTo("클린코드에 대해 논의해볼까요");
        assertThat(discussionResponse.discussionOpinion()).isEqualTo("클린코드만세");
    }
}
