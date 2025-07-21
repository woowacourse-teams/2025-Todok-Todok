package todoktodok.backend.discussion.application.service.command;

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
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
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
        //given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();

        final Long memberId = 999L;

        final DiscussionRequest discussionRequest = new DiscussionRequest(
                1L,
                "이 책의 의존성 주입 방식에 대한 생각",
                "스프링의 DI 방식은 유지보수에 정말 큰 도움이 된다고 느꼈습니다."
        );

        // when - then
        assertThatThrownBy(() -> discussionCommandService.createDiscussion(memberId, discussionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 기록으로 토론방 생성 시 예외가 발생한다")
    void createDiscussion_NoteNotFound_fail() {
        //given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        databaseInitializer.setNoteInfo();

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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 기록을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("기록 소유자가 아닌 경우 토론방 생성 시 예외가 발생한다")
    void createDiscussion_whenNoteNotOwnedByMember_fail() {
        //given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        databaseInitializer.setNoteInfo();

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
