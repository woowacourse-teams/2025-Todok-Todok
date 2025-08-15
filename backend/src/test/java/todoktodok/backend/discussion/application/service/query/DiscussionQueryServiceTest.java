package todoktodok.backend.discussion.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
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
    void getAllDiscussions() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;

        // when
        final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                memberId, "", DiscussionFilterType.ALL);

        // then
        assertThat(discussions).hasSize(1);
    }

    @Test
    @DisplayName("특정 토론방을 조회한다")
    void getDiscussion() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 1L;

        databaseInitializer.setDiscussionInfo(
                "클린코드에 대해 논의해볼까요",
                "클린코드만세",
                memberId,
                bookId
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
                .hasMessageContaining("해당 토론방을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("특정 토론방의 좋아요 수와 댓글 수를 조회할 수 있다")
    void getDiscussionLikeCountAndCommentCount() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 1L;

        databaseInitializer.setDiscussionInfo(
                "클린코드에 대해 논의해볼까요",
                "클린코드만세",
                memberId,
                bookId
        );

        databaseInitializer.setDiscussionLikeInfo(1L, 1L);
        databaseInitializer.setCommentInfo("클린코드 만만세", 1L, 1L);

        // when
        final DiscussionResponse discussion = discussionQueryService.getDiscussion(1L, 1L);

        // then
        assertAll(
                () -> assertThat(discussion.commentCount()).isEqualTo(1),
                () -> assertThat(discussion.likeCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("특정 토론방의 전체 댓글 수는 댓글 수와 대댓글 수의 합이다")
    void getDiscussion_TotalCommentCountTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 1L;

        databaseInitializer.setDiscussionInfo(
                "클린코드에 대해 논의해볼까요",
                "클린코드만세",
                memberId,
                bookId
        );

        final Long discussionId = 1L;

        databaseInitializer.setCommentInfo("댓글1", memberId, discussionId);
        databaseInitializer.setReplyInfo("대댓글1", memberId, 1L);

        // when
        final DiscussionResponse discussion = discussionQueryService.getDiscussion(memberId, discussionId);

        // then
        assertThat(discussion.commentCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("토론방 단일 조회 시 내가 좋아요를 생성한 글인지 확인한다")
    void getDiscussion_isLikedTest_true() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDiscussionLikeInfo(1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when
        final DiscussionResponse discussion = discussionQueryService.getDiscussion(memberId, discussionId);

        // then
        assertThat(discussion.isLikedByMe()).isTrue();
    }

    @Test
    @DisplayName("토론방 단일 조회 시 내가 좋아요를 생성하지 않은 글인지 확인한다")
    void getDiscussion_isLikedTest_false() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when
        final DiscussionResponse discussion = discussionQueryService.getDiscussion(memberId, discussionId);

        // then
        assertThat(discussion.isLikedByMe()).isFalse();
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
                    "조영호의 오브젝트-v1", "book1입니다", "book1Author", "book1Publisher", "1233", ""
            );
            databaseInitializer.setBookInfo(
                    "클린코드", "book2입니다", "book2Author", "book2Publisher", "1234", ""
            );

            databaseInitializer.setDiscussionInfo(
                    "user1의 객체 지향 토론", "book1에 대한 토론입니다", 1L, 1L
            );
            databaseInitializer.setDiscussionInfo(
                    "user1의 메서드 분리 토론", "boo2에 대한 토론입니다", 1L, 2L
            );
            databaseInitializer.setDiscussionInfo(
                    "user2의 메서드 분리 토론", "boo2에 대한 토론입니다", 2L, 2L
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
            // given
            final String keyword = "객체 지향";

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, keyword, DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(1),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(discussions.get(0).discussionTitle()).contains(keyword)
            );
        }

        @Test
        @DisplayName("나의 토론방을 조회할 수 있다")
        void getMyDiscussionsTest() {
            // given
            final Long memberId = 1L;

            //when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    memberId, null, DiscussionFilterType.MINE
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(2),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(discussions.get(1).discussionId()).isEqualTo(2L),
                    () -> assertThat(discussions.get(0).member().memberId()).isEqualTo(memberId),
                    () -> assertThat(discussions.get(1).member().memberId()).isEqualTo(memberId)
            );
        }

        @Test
        @DisplayName("나의 토론방을 대상으로 키워드로 조회할 수 있다")
        void getMyDiscussionsByKeywordTest() {
            // given
            final String keyword = "객체 지향";

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, keyword, DiscussionFilterType.MINE
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(1),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(discussions.get(0).discussionTitle()).contains(keyword)
            );
        }

        @Test
        @DisplayName("키워드 조회 시 책 제목에 키워드가 포함되면 조회된다")
        void getDiscussionsByBookTitleKeywordTest() {
            // given
            final String keyword = "오브젝트";

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, keyword, DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(1),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(discussions.get(0).book().bookTitle()).contains(keyword)
            );
        }

        @Test
        @DisplayName("키워드 조회 시 토론방 제목에 키워드가 포함되면 조회된다")
        void getDiscussionsByDiscussionTitleKeywordTest() {
            // given
            final String keyword = "메서드";

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, keyword, DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions).hasSize(2),
                    () -> assertThat(discussions.get(0).discussionId()).isEqualTo(2L),
                    () -> assertThat(discussions.get(1).discussionId()).isEqualTo(3L),
                    () -> assertThat(discussions.get(0).discussionTitle()).contains(keyword),
                    () -> assertThat(discussions.get(1).discussionTitle()).contains(keyword)
            );
        }

        @Test
        @DisplayName("전체 토론방을 조회할 시 토론방의 좋아요수와 댓글수를 반환한다")
        void getAllDiscussions_LikeCountAndCommentCountTest() {
            // given
            // 사용자 3명
            databaseInitializer.setUserInfo(
                    "user3@gmail.com", "user3", "", ""
            );

            // 토론방 1: 좋아요 1개, 댓글 1개
            databaseInitializer.setDiscussionLikeInfo(1L, 1L);
            databaseInitializer.setCommentInfo("첫 번째 댓글", 1L, 1L);

            // 토론방 2: 좋아요 2개 (서로 다른 사용자), 댓글 2개
            databaseInitializer.setDiscussionLikeInfo(1L, 2L);
            databaseInitializer.setDiscussionLikeInfo(2L, 2L); // 다른 사용자
            databaseInitializer.setCommentInfo("두 번째 토론 첫 댓글", 1L, 2L);
            databaseInitializer.setCommentInfo("두 번째 토론 둘째 댓글", 2L, 2L);

            // 토론방 3: 좋아요 3개, 댓글 3개 (모두 서로 다른 사용자/내용)
            databaseInitializer.setDiscussionLikeInfo(1L, 3L);
            databaseInitializer.setDiscussionLikeInfo(2L, 3L);
            databaseInitializer.setDiscussionLikeInfo(3L, 3L); // 세 번째 사용자 필요
            databaseInitializer.setCommentInfo("세 번째 토론 첫 댓글", 1L, 3L);
            databaseInitializer.setCommentInfo("세 번째 토론 둘째 댓글", 2L, 3L);
            databaseInitializer.setCommentInfo("세 번째 토론 셋째 댓글", 3L, 3L);

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    1L, null, DiscussionFilterType.ALL
            );

            // then
            assertAll(
                    () -> assertThat(discussions.get(0).likeCount()).isEqualTo(1L),
                    () -> assertThat(discussions.get(0).commentCount()).isEqualTo(1L),
                    () -> assertThat(discussions.get(1).likeCount()).isEqualTo(2L),
                    () -> assertThat(discussions.get(1).commentCount()).isEqualTo(2L),
                    () -> assertThat(discussions.get(2).likeCount()).isEqualTo(3L),
                    () -> assertThat(discussions.get(2).commentCount()).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName("전체 토론방을 조회할 때, 조회되는 댓글 수는 댓글과 대댓글 수의 합이다")
        void getAllDiscussions_totalCommentCountTest() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long discussionId = 1L;

            databaseInitializer.setCommentInfo("댓글1", memberId, discussionId);

            final Long commentId = 1L;

            databaseInitializer.setReplyInfo("대댓글1", memberId, commentId);

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    memberId, null, DiscussionFilterType.ALL
            );

            // then
            assertThat(discussions.get(0).commentCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("토론방 필터링 조회 시 나의 좋아요 여부를 반환한다")
        void getDiscussions_isLikedTest() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDiscussionInfo("토론방 2", "토론방 2입니다", 1L, 1L);

            databaseInitializer.setDiscussionLikeInfo(1L, 1L);

            final Long memberId = 1L;

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeywordAndType(
                    memberId, "", DiscussionFilterType.ALL);
            final DiscussionResponse likedDiscussion = discussions.get(0);
            final DiscussionResponse notLikedDiscussion = discussions.get(1);

            // then
            assertAll(
                    () -> assertThat(likedDiscussion.isLikedByMe()).isTrue(),
                    () -> assertThat(notLikedDiscussion.isLikedByMe()).isFalse()
            );
        }
    }
}
