package todoktodok.backend.comment.application.service.query;

import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class CommentQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private CommentQueryService commentQueryService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("없는 토론방에 대해 댓글을 조회할 경우 예외가 발생한다")
    void createCommentTest_discussionNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when - then
        assertThatThrownBy(() -> commentQueryService.getComments(memberId, discussionId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 토론방을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 댓글을 작성할 경우 예외가 발생한다")
    void createCommentTest_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 2L;
        final Long discussionId = 1L;

        // when - then
        assertThatThrownBy(() -> commentQueryService.getComments(memberId, discussionId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }
}
