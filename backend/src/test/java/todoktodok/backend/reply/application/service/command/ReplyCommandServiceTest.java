package todoktodok.backend.reply.application.service.command;

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
import todoktodok.backend.reply.application.dto.request.ReplyRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class ReplyCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private ReplyCommandService replyCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("없는 토론방에 대해 대댓글을 작성할 경우 예외가 발생한다")
    void createReplyTest_discussionNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        // when - then
        assertThatThrownBy(() -> replyCommandService.createReply(1L, 1L, 1L, replyRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 토론방을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("없는 댓글에 대해 대댓글을 작성할 경우 예외가 발생한다")
    void createReplyTest_commentNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        // when - then
        assertThatThrownBy(() -> replyCommandService.createReply(1L, 1L, 1L, replyRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 댓글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 대댓글을 작성할 경우 예외가 발생한다")
    void createReplyTest_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final ReplyRequest replyRequest = new ReplyRequest("저도 그 의견에 동의합니다!");

        // when - then
        assertThatThrownBy(() -> replyCommandService.createReply(2L, 1L, 1L, replyRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }
}
