package todoktodok.backend.discussion.application.service.command;

import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class DiscussionCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private DiscussionCommandService discussionCommandService;

    @Autowired
    private MemberRepository memberRepository;

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
                .hasMessage("해당 회원을 찾을 수 없습니다");
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
                .hasMessage("해당 도서를 찾을 수 없습니다");
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
        final String updatedContent= "상속과 조합의 차이점이 궁금합니다.";
        final DiscussionUpdateRequest discussionUpdateRequest = new DiscussionUpdateRequest(
                updatedTitle,
                updatedContent
        );

        // when - then
        assertThatThrownBy(() -> discussionCommandService.updateDiscussion(memberId, discussionId, discussionUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신의 토론방만 수정/삭제 가능합니다");
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
                .hasMessage("자기 자신의 토론방만 수정/삭제 가능합니다");
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
                .hasMessage("댓글이 존재하는 토론방은 삭제할 수 없습니다");
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

        // when
        discussionCommandService.report(memberId, discussionId);

        // then
        assertThatThrownBy(() -> discussionCommandService.report(memberId, discussionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신고한 토론방입니다");
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

        // when - then
        assertThatThrownBy(() -> discussionCommandService.report(memberId, discussionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신의 토론방을 신고할 수 없습니다");
    }

    @Nested
    @Disabled
    @DisplayName("미사용 테스트")
    class DisabledTest {
        @Test
        @DisplayName("존재하지 않는 회원으로 토론방 생성 시 예외가 발생한다")
        void createDiscussion_memberNotFound_fail() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final Long memberId = 999L;

            final DiscussionRequest discussionRequest = new DiscussionRequest(
                    1L,
                    "이 책의 의존성 주입 방식에 대한 생각",
                    "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
            );

            // when - then
            assertThatThrownBy(() -> discussionCommandService.createDiscussion(memberId, discussionRequest))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 회원을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("존재하지 않는 기록으로 토론방 생성 시 예외가 발생한다")
        void createDiscussion_NoteNotFound_fail() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final Long memberId = 1L;
            final Long noteId = 999L;

            final DiscussionRequest discussionRequest = new DiscussionRequest(
                    noteId,
                    "이 책의 의존성 주입 방식에 대한 생각",
                    "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
            );

            // when - then
            assertThatThrownBy(
                    () -> discussionCommandService.createDiscussion(memberId, discussionRequest)
            )
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 기록을 찾을 수 없습니다");
        }

        @Test
        @DisplayName("기록 소유자가 아닌 경우 토론방 생성 시 예외가 발생한다")
        void createDiscussion_whenNoteNotOwnedByMember_fail() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final Member member = MemberFixture.create(
                    "user12@gmail.com",
                    "user12",
                    "https://image.jpg"
            );
            memberRepository.save(member);

            final Long memberId = 2L;
            final Long noteId = 1L;

            final DiscussionRequest discussionRequest = new DiscussionRequest(
                    noteId,
                    "이 책의 의존성 주입 방식에 대한 생각",
                    "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
            );

            // when - then
            assertThatThrownBy(() -> discussionCommandService.createDiscussion(memberId, discussionRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 기록의 소유자만 토론방을 생성할 수 있습니다");
        }
    }
}
