package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
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
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.DiscussionFilterType;

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
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultNoteInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;

        // when
        final List<DiscussionResponse> discussions = discussionQueryService.getDiscussions(memberId);

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
        final DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(memberId, discussionId);

        // then
        assertAll(
                () -> assertThat(discussionResponse.discussionId()).isEqualTo(discussionId),
                () -> assertThat(discussionResponse.discussionTitle()).isEqualTo("클린코드에 대해 논의해볼까요"),
                () -> assertThat(discussionResponse.discussionOpinion()).isEqualTo("클린코드만세")
        );
    }

    @Test
    @DisplayName("특정 토론방을 찾을 수 없는 경우 예외가 발생한다")
    void getDiscussion_notFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final Long memberId = 1L;
        final Long discussionId = 999L;

        // when - then
        assertThatThrownBy(() -> discussionQueryService.getDiscussion(memberId, discussionId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 토론방을 찾을 수 없습니다");
    }

    @Nested
    @DisplayName("토론방 필터링 테스트")
    class DiscussionFilterTest {

        @BeforeEach
        void setUp() {
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setUserInfo(
                    "user2@gmail.com", "user2", "", ""
            );

            databaseInitializer.setBookInfo(
                    "오브젝트v1", "book1입니다", "book1Author", "book1Publisher", "1233", ""
            );
            databaseInitializer.setBookInfo(
                    "book2", "book2입니다", "book2Author", "book2Publisher", "1234", ""
            );

            databaseInitializer.setDiscussionInfo(
                    "user1의 book1 토론", "토론1입니다", 1L, 1L, null
            );
            databaseInitializer.setDiscussionInfo(
                    "user1의 클린코드 토론", "토론1입니다", 1L, 2L, null
            );
            databaseInitializer.setDiscussionInfo(
                    "user2의 클린코드 토론", "토론2입니다", 2L, 2L, null
            );
        }

        @Test
        @DisplayName("전체 토론방을 조회할 수 있다")
        void getAllDiscussionsTest() {
            // given - when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, null, DiscussionFilterType.ALL
            );

            // then
            assertThat(discussions).hasSize(3);
        }

        @Test
        @DisplayName("전체 토론방을 대상으로 키워드로 조회할 수 있다")
        void getAllDiscussionsByKeywordTest() {
            // given - when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, "book2", DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(2),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(2L),
                    () -> assertThat(discussions.get(1).discussionId()).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName("나의 토론방을 조회할 수 있다")
        void getMyDiscussionsTest() {
            // given - when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, null, DiscussionFilterType.MINE
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(2),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(discussions.get(1).discussionId()).isEqualTo(2L)
            );
        }

        @Test
        @DisplayName("나의 토론방을 대상으로 키워드로 조회할 수 있다")
        void getMyDiscussionsByKeywordTest() {
            // given - when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, "클린코드", DiscussionFilterType.MINE
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(1),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(2L)
            );
        }

        @Test
        @DisplayName("키워드 조회 시 책 제목에 키워드가 포함되면 조회된다")
        void getDiscussionsByBookTitleKeywordTest() {
            // given - when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, "오브젝트", DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(1),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(1L)
            );
        }

        @Test
        @DisplayName("키워드 조회 시 토론방 제목에 키워드가 포함되면 조회된다")
        void getDiscussionsByDiscussionTitleKeywordTest() {
            // given - when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, "클린코드", DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(2),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(2L),
                    () -> assertThat(discussions.get(1).discussionId()).isEqualTo(3L)
            );
        }
    }
}
