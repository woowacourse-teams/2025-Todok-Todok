package todoktodok.backend.discussion.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.notification.infrastructure.FcmPushNotifier;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class DiscussionCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private DiscussionCommandService discussionCommandService;

    @MockitoBean
    private FcmPushNotifier fcmPushNotifier;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 토론방 생성 시 예외가 발생한다")
    void createDiscussion_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 999L;
        final Long bookId = 1L;

        final DiscussionRequest discussionRequest = new DiscussionRequest(
                bookId,
                "이 책의 의존성 주입 방식에 대한 생각",
                "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
        );

        // when - then
        assertThatThrownBy(() -> discussionCommandService.createDiscussion(memberId, discussionRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 도서로 토론방 생성 시 예외가 발생한다")
    void createDiscussion_NoteNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 999L;

        final DiscussionRequest discussionRequest = new DiscussionRequest(
                bookId,
                "이 책의 의존성 주입 방식에 대한 생각",
                "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
        );

        // when - then
        assertThatThrownBy(
                () -> discussionCommandService.createDiscussion(memberId, discussionRequest)
        )
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 도서를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("자신의 것이 아닌 토론방을 수정하면 예외가 발생한다")
    void validateDiscussionMemberUpdateTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");

        final Long memberId = 2L;
        final Long discussionId = 1L;

        final String updatedTitle = "상속과 조합은 어떤 상황에 쓰이나요?";
        final String updatedContent = "상속과 조합의 차이점이 궁금합니다.";
        final DiscussionUpdateRequest discussionUpdateRequest = new DiscussionUpdateRequest(
                updatedTitle,
                updatedContent
        );

        // when - then
        assertThatThrownBy(() -> discussionCommandService.updateDiscussion(memberId, discussionId, discussionUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신의 토론방만 수정/삭제 가능합니다");
    }

    @Test
    @DisplayName("자신의 것이 아닌 토론방을 삭제하면 예외가 발생한다")
    void validateDiscussionMemberDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setUserInfo("user2@gmail.com", "user", "https://image.png", "프로필 메시지");

        final Long memberId = 2L;
        final Long discussionId = 1L;

        // when - then
        assertThatThrownBy(() -> discussionCommandService.deleteDiscussion(memberId, discussionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신의 토론방만 수정/삭제 가능합니다");
    }

    @Test
    @DisplayName("댓글이 존재하는 토론방을 삭제하면 예외가 발생한다")
    void validateHasCommentDiscussionDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when - then
        assertThatThrownBy(() -> discussionCommandService.deleteDiscussion(memberId, discussionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("댓글이 존재하는 토론방은 삭제할 수 없습니다");
    }

    @Test
    @DisplayName("같은 회원이 토론방을 중복 신고하면 예외가 발생한다")
    void report_duplicated_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user123@gmail.com", "user123", "https://image.png", "message");
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 2L;
        final Long discussionId = 1L;
        final String reason = "토론 주제와 무관한 내용";

        // when
        discussionCommandService.report(memberId, discussionId, reason);

        // then
        assertThatThrownBy(() -> discussionCommandService.report(memberId, discussionId, reason))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 신고한 토론방입니다");
    }

    @Test
    @DisplayName("회원이 자신의 토론방을 신고하면 예외가 발생한다")
    void report_selfReport_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final String reason = "토론 주제와 무관한 내용";

        // when - then
        assertThatThrownBy(() -> discussionCommandService.report(memberId, discussionId, reason))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신의 토론방을 신고할 수 없습니다");
    }

    @Test
    @DisplayName("좋아요를 누르지 않았던 토론방에 좋아요를 생성한다")
    void discussionToggleLikeTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when
        final boolean isLiked = discussionCommandService.toggleLike(memberId, discussionId);

        // then
        assertThat(isLiked).isTrue();
    }

    @Test
    @DisplayName("이미 좋아요를 누른 토론방에 다시 좋아요를 누르면 좋아요가 취소된다")
    void discussionToggleLikeDeleteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDiscussionLikeInfo(1L, 1L);
        databaseInitializer.increaseDiscussionLikeCount(1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when
        final boolean isLiked = discussionCommandService.toggleLike(memberId, discussionId);

        // then
        assertThat(isLiked).isFalse();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("토론방 좋아요를 생성하면 알이 토론방 작성자에게 가는 이벤트가 발생한다")
    void discussionLikeCreatedNotificationTest() {
        // given
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setUserInfo("user1@gmail.com", "user1", "", "");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");

        final Long recipientId = 1L;

        databaseInitializer.setDiscussionInfo("캡슐화 왜 해요?", "진짜 궁금해요", recipientId, 1L);

        final Long discussionId = 1L;
        final Long authorId = 2L;

        // when
        discussionCommandService.toggleLike(authorId, discussionId);

        // then
        verify(fcmPushNotifier, times(1)).sendPush(any(), any());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("토론방 좋아요를 취소하면 알림이 토론방 작성자에게 가는 이벤트가 발생하지 않는다")
    void discussionLikeCreatedNotificationTest_notCreated() {
        // given
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setUserInfo("user1@gmail.com", "user1", "", "");
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "", "");

        final Long recipientId = 1L;
        final Long discussionId = 1L;
        final Long authorId = 2L;

        databaseInitializer.setDiscussionInfo("캡슐화 왜 해요?", "진짜 궁금해요", recipientId, 1L);
        databaseInitializer.setDiscussionLikeInfo(authorId, discussionId);
        databaseInitializer.increaseDiscussionLikeCount(discussionId);

        // when
        discussionCommandService.toggleLike(authorId, discussionId);

        // then
        verifyNoInteractions(fcmPushNotifier);
    }
}
