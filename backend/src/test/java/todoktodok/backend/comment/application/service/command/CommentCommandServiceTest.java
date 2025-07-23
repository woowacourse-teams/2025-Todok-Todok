package todoktodok.backend.comment.application.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
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
import todoktodok.backend.comment.application.dto.request.CommentRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class CommentCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private CommentCommandService commentCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("없는 토론방에 대해 댓글을 작성할 경우 예외가 발생한다")
    void createCommentTest_discussionNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();

        final CommentRequest commentRequest = new CommentRequest("상속의 핵심 목적은 타입 계층의 구축입니다!");

        // when - then
        assertThatThrownBy(() -> commentCommandService.createComment(1L, 1L, commentRequest))
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

        final CommentRequest commentRequest = new CommentRequest("상속의 핵심 목적은 타입 계층의 구축입니다!");

        // when - then
        assertThatThrownBy(() -> commentCommandService.createComment(2L, 1L, commentRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("자기 자신을 신고하면 예외가 발생한다")
    void validateSelfReportTest() {
        //given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        //when - then
        assertThatThrownBy(() -> commentCommandService.report(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신이 작성한 댓글을 신고할 수 없습니다");
    }

    @Test
    @DisplayName("이미 자신이 신고한 댓글을 중복 신고하면 예외가 발생한다")
    void validateDuplicatedReportTest() {
        //given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setCommentInfo("상속의 핵심 목적은 타입 계층의 구축입니다!", 2L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        commentCommandService.report(memberId, discussionId, commentId);

        //when - then
        assertThatThrownBy(() -> commentCommandService.report(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신고한 댓글입니다");
    }
}
