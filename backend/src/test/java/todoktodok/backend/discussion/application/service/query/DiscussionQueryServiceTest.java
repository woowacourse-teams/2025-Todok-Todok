package todoktodok.backend.discussion.application.service.query;

import static java.time.temporal.ChronoUnit.MICROS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.PageInfo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class DiscussionQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private DiscussionQueryService discussionQueryService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @AfterEach
    void tearDown() {
        // 비동기 테스트 떄문에 현재 큐에 남아있는 작업이나 활성 스레드가 없을 때까지 대기
        await().atMost(5, TimeUnit.SECONDS)
                .until(() ->
                    taskExecutor.getThreadPoolExecutor().getActiveCount() == 0 &&
                        taskExecutor.getThreadPoolExecutor().getQueue().isEmpty()
        );
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

        // when - then
        await().atMost(Duration.ofSeconds(5))
                .pollDelay(Duration.ofMillis(200))
                .pollInterval(Duration.ofMillis(300))
                .untilAsserted(() -> {
                    final DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(memberId, discussionId);

                    assertAll(
                            () -> assertThat(discussionResponse.discussionId()).isEqualTo(discussionId),
                            () -> assertThat(discussionResponse.discussionTitle()).isEqualTo("클린코드에 대해 논의해볼까요"),
                            () -> assertThat(discussionResponse.discussionOpinion()).isEqualTo("클린코드만세"),
                            () -> assertThat(discussionResponse.viewCount()).isEqualTo(1)
                    );
                });
    }

    @Test
    @DisplayName("특정 토론방을 조회한지 10분 이후에 다시 조회하면 조회 횟수가 증가한다")
    void getDiscussion_viewCount_after_10minutes() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 1L;
        final Long discussionId = 1L;

        databaseInitializer.setDiscussionInfo(
                "클린코드에 대해 논의해볼까요",
                "클린코드만세",
                memberId,
                bookId
        );

        databaseInitializer.setDiscussionMemberViewInfo(memberId, discussionId, 10);

        // when - then
        await().atMost(Duration.ofSeconds(5))
                .pollDelay(Duration.ofMillis(200))
                .pollInterval(Duration.ofMillis(300))
                .untilAsserted(() -> {
                    final DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(memberId, discussionId);
                    assertThat(discussionResponse.viewCount()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("특정 토론방을 조회한지 10분 이전에 다시 조회하면 조회 횟수가 증가하지 않는다")
    void getDiscussion_viewCount_before_10minutes() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final Long bookId = 1L;
        final Long discussionId = 1L;

        databaseInitializer.setDiscussionInfo(
                "클린코드에 대해 논의해볼까요",
                "클린코드만세",
                memberId,
                bookId
        );

        databaseInitializer.setDiscussionMemberViewInfo(memberId, discussionId, 9);

        // when - then
        await().atMost(Duration.ofSeconds(5))
                .pollDelay(Duration.ofMillis(200))
                .pollInterval(Duration.ofMillis(300))
                .untilAsserted(() -> {
                    final DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(memberId, discussionId);
                    assertThat(discussionResponse.viewCount()).isEqualTo(0);
                });
    }

    @Test
    @DisplayName("토론방 조회 시 예외가 발생하면 조회수가 증가하지 않고 예외를 반환한다")
    void getDiscussion_ifThrowException_notIncrementViewCount() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;
        final Long existsDiscussionId = 1L;
        final Long notExistsDiscussionId = 999L;

        // when
        assertThatThrownBy(() -> discussionQueryService.getDiscussion(memberId, notExistsDiscussionId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 토론방을 찾을 수 없습니다");

        // then
        final DiscussionResponse discussionResponse = discussionQueryService.getDiscussion(memberId, existsDiscussionId);
        assertThat(discussionResponse.viewCount()).isEqualTo(0);
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
    @DisplayName("토론방 최신순 조회 테스트")
    class getDiscussionsTest {

        @Test
        @DisplayName("토론방 최신순 조회 시 cursor 값에 맞는 토론방의 생성일시 이후에 생성된 토론방 중 최신순으로 조회한다")
        void getDiscussionsFromNewestTest_sliceByIdLessThanCursorId() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursorMeaningFour = "NA==";

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussions(memberId,
                    size, cursorMeaningFour);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(items.get(0).discussionId()).isEqualTo(3L),
                    () -> assertThat(items.get(1).discussionId()).isEqualTo(2L),
                    () -> assertThat(items.get(2).discussionId()).isEqualTo(1L)
            );
        }

        @Test
        @DisplayName("토론방이 없을 때 토론방 최신순을 조회하면 빈 리스트를 반환한다")
        void getDiscussionsFromNewestTest_emptyDiscussion() {
            // given
            databaseInitializer.setDefaultUserInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussions(memberId,
                    size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertThat(items).isEmpty();
        }

        @Test
        @DisplayName("토론방 최신순 조회 시 cursor가 null이면 가장 id가 큰 토론방부터 조회한다")
        void getDiscussionsFromNewestTest_cursorNull() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussions(
                    memberId, size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(items.get(0).discussionId()).isEqualTo(5L),
                    () -> assertThat(items.get(1).discussionId()).isEqualTo(4L),
                    () -> assertThat(items.get(2).discussionId()).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName("""
                토론방 최신순 조회 시 추가로 조회할 토론방이 남아있다면 아래의 값을 반환한다
                1. hasNext 값을 true로 반환한다
                2. nextCursor 값을 조회된 토론방 중 마지막 토론방의 id로 반환한다
                """)
        void getDiscussionsFromNewestTest_hasDiscussionLeft() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussions(memberId,
                    size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();
            final PageInfo pageInfo = firstSlicedDiscussions.pageInfo();
            final String cursorMeaningThree = "Mw==";

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(pageInfo.hasNext()).isTrue(),
                    () -> assertThat(pageInfo.nextCursor()).isEqualTo(cursorMeaningThree)
            );
        }

        @Test
        @DisplayName("""
                토론방 최신순 조회 시 추가로 조회할 토론방이 없다면 아래의 값을 반환한다
                1. hasNext 값을 false로 반환한다
                2. nextCursor 값을 null로 반환한다
                """)
        void getDiscussionsFromNewestTest_NoDiscussionLeft() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussions(memberId,
                    size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();
            final PageInfo pageInfo = firstSlicedDiscussions.pageInfo();

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(pageInfo.hasNext()).isFalse(),
                    () -> assertThat(pageInfo.nextCursor()).isEqualTo(null)
            );
        }

        @Test
        @DisplayName("마지막 토론방 최신순 조회 시 남은 토론방 개수는 size 이하이다")
        void getDiscussionsFromNewestTest_lastDiscussions() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursorMeaningThree = "Mw==";

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussions(memberId,
                    size, cursorMeaningThree);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertThat(items).hasSizeLessThanOrEqualTo(2);
        }

        @ParameterizedTest
        @DisplayName("토론방 최신순 조회 시 사이즈 값이 범위를 초과할 경우 예외가 발생한다")
        @ValueSource(ints = {-1, 0, 51, 100})
        void getDiscussionsFromNewestTest_wrongSize(final int size) {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final String cursor = null;

            // when - then
            assertThatThrownBy(() -> discussionQueryService.getDiscussions(memberId, size, cursor))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효하지 않은 페이지 사이즈입니다. 1 이상 50 이하의 페이징을 시도해주세요");
        }

        @Test
        @DisplayName("토론방 최신순 조회 시 커서 값이 유효하지 않을 경우 예외가 발생한다")
        void getDiscussionsFromNewestTest_wrongCursor() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final int size = 3;
            final String cursor = "woowa";

            // when - then
            assertThatThrownBy(() -> discussionQueryService.getDiscussions(memberId, size, cursor))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Base64로 디코드할 수 없는 cursor 값입니다");
        }
    }

    @Nested
    @DisplayName("도서별 토론방 조회 테스트")
    class GetDiscussionsByBookTest {

        @Test
        @DisplayName("도서별 토론방 최신순 조회 시 cursor 값에 맞는 토론방의 생성일시 이후에 생성된 토론방 중 최신순으로 조회한다")
        void getDiscussionsByBookFromNewestTest_sliceByIdLessThanCursorId() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursorMeaningFour = "NA==";

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussionsByBook(
                    memberId, bookId, size, cursorMeaningFour);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(items.get(0).discussionId()).isEqualTo(3L),
                    () -> assertThat(items.get(1).discussionId()).isEqualTo(2L),
                    () -> assertThat(items.get(2).discussionId()).isEqualTo(1L)
            );
        }

        @Test
        @DisplayName("도서별 토론방이 없을 때 토론방 최신순을 조회하면 빈 리스트를 반환한다")
        void getDiscussionsByBookFromNewestTest_emptyDiscussion() {
            // given
            databaseInitializer.setDefaultUserInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussionsByBook(
                    memberId, bookId, size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertThat(items).isEmpty();
        }

        @Test
        @DisplayName("도서별 토론방 최신순 조회 시 cursor가 null이면 가장 id가 큰 토론방부터 조회한다")
        void getDiscussionsByBookFromNewestTest_cursorNull() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussionsByBook(
                    memberId, bookId, size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(items.get(0).discussionId()).isEqualTo(5L),
                    () -> assertThat(items.get(1).discussionId()).isEqualTo(4L),
                    () -> assertThat(items.get(2).discussionId()).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName("""
                도서별 토론방 최신순 조회 시 추가로 조회할 토론방이 남아있다면 아래의 값을 반환한다
                1. hasNext 값을 true로 반환한다
                2. nextCursor 값을 조회된 토론방 중 마지막 토론방의 id로 반환한다
                """)
        void getDiscussionsByBookFromNewestTest_hasDiscussionLeft() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussionsByBook(
                    memberId, bookId, size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();
            final PageInfo pageInfo = firstSlicedDiscussions.pageInfo();
            final String cursorMeaningThree = "Mw==";

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(pageInfo.hasNext()).isTrue(),
                    () -> assertThat(pageInfo.nextCursor()).isEqualTo(cursorMeaningThree)
            );
        }

        @Test
        @DisplayName("""
                도서별 토론방 최신순 조회 시 추가로 조회할 토론방이 없다면 아래의 값을 반환한다
                1. hasNext 값을 false로 반환한다
                2. nextCursor 값을 null로 반환한다
                """)
        void getDiscussionsByBookFromNewestTest_NoDiscussionLeft() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursor = null;

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussionsByBook(
                    memberId, bookId, size, cursor);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();
            final PageInfo pageInfo = firstSlicedDiscussions.pageInfo();

            // then
            assertAll(
                    () -> assertThat(items).hasSize(size),
                    () -> assertThat(pageInfo.hasNext()).isFalse(),
                    () -> assertThat(pageInfo.nextCursor()).isEqualTo(null)
            );
        }

        @Test
        @DisplayName("마지막 페이지의 도서별 토론방 최신순 조회 시 남은 토론방 개수는 size 이하이다")
        void getDiscussionsByBookFromNewestTest_lastDiscussions() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursorMeaningThree = "Mw==";

            // when
            final LatestDiscussionPageResponse firstSlicedDiscussions = discussionQueryService.getDiscussionsByBook(
                    memberId, bookId, size, cursorMeaningThree);
            final List<DiscussionResponse> items = firstSlicedDiscussions.items();

            // then
            assertThat(items).hasSizeLessThanOrEqualTo(2);
        }

        @ParameterizedTest
        @DisplayName("토론방 최신순 조회 시 사이즈 값이 범위를 초과할 경우 예외가 발생한다")
        @ValueSource(ints = {-1, 0, 51, 100})
        void getDiscussionsByBookFromNewestTest_wrongSize(final int size) {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final String cursor = null;

            // when - then
            assertThatThrownBy(() -> discussionQueryService.getDiscussionsByBook(memberId, bookId, size, cursor))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효하지 않은 페이지 사이즈입니다. 1 이상 50 이하의 페이징을 시도해주세요");
        }

        @Test
        @DisplayName("토론방 최신순 조회 시 커서 값이 유효하지 않을 경우 예외가 발생한다")
        void getDiscussionsByBookFromNewestTest_wrongCursor() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();
            databaseInitializer.setDefaultDiscussionInfo();

            final Long memberId = 1L;
            final Long bookId = 1L;
            final int size = 3;
            final String cursor = "woowa";

            // when - then
            assertThatThrownBy(() -> discussionQueryService.getDiscussionsByBook(memberId, bookId, size, cursor))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Base64로 디코드할 수 없는 cursor 값입니다");
        }
    }

    @Disabled
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
        @DisplayName("전체 토론방을 대상으로 키워드로 조회할 수 있다")
        void getAllDiscussionsByKeywordTest() {
            // given
            final String keyword = "객체 지향";

            // when
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeyword(
                    1L, keyword
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
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeyword(
                    1L, keyword
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
            final List<DiscussionResponse> discussions = discussionQueryService.getDiscussionsByKeyword(
                    1L, keyword
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

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "\t", "\n"})
        @DisplayName("키워드가 null 또는 공백이면 IllegalArgumentException이 발생한다")
        void getDiscussionsByKeyword_whenKeywordBlank_thenThrows(final String keyword) {
            // when & then
            assertThatThrownBy(() -> discussionQueryService.getDiscussionsByKeyword(1L, keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith("검색 키워드를 입력해야 합니다");
        }

        @ParameterizedTest
        @ValueSource(strings = {"hello", "오브젝트", "키워드"})
        @DisplayName("키워드가 정상 문자열이면 예외가 발생하지 않는다")
        void getDiscussionsByKeyword_whenKeywordValid_thenNoThrow(final String keyword) {
            // when & then
            discussionQueryService.getDiscussionsByKeyword(1L, keyword);
        }
    }

    @Nested
    @DisplayName("인기 토론방 조회 테스트")
    class HotDiscussionsTest {

        @BeforeEach
        void setUp() {
            final LocalDateTime today = LocalDate.now().atStartOfDay();
            final LocalDateTime aWeekAgo = today.minusDays(7);

            databaseInitializer.setUserInfo("user1@gmail.com", "user1", "user1.png", "");
            databaseInitializer.setUserInfo("user2@gmail.com", "user2", "user2.png", "");
            databaseInitializer.setUserInfo("user3@gmail.com", "user3", "user3.png", "");
            databaseInitializer.setUserInfo("user4@gmail.com", "user4", "user4.png", "");
            databaseInitializer.setUserInfo("user5@gmail.com", "user5", "user5.png", "");
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDiscussionInfo("토론방 1", "토론방 1입니다", 1L, 1L, aWeekAgo);
            databaseInitializer.setDiscussionInfo("토론방 2", "토론방 2입니다", 1L, 1L, aWeekAgo);
            databaseInitializer.setDiscussionInfo("토론방 3", "토론방 3입니다", 1L, 1L, aWeekAgo);
            databaseInitializer.setDiscussionInfo("토론방 4", "토론방 4입니다", 1L, 1L, aWeekAgo);

            // 토론방 1 : 일주일 기준 총 6개 / 오늘 기준 총 1개
            databaseInitializer.setDiscussionLikeInfo(1L, 1L, aWeekAgo);
            databaseInitializer.setDiscussionLikeInfo(2L, 1L, aWeekAgo);
            databaseInitializer.setCommentInfo("토론방 1의 댓글입니다", 1L, 1L, aWeekAgo);
            databaseInitializer.setCommentInfo("토론방 1의 댓글입니다", 1L, 1L, aWeekAgo);
            databaseInitializer.setReplyInfo("댓글 1의 대댓글입니다", 1L, 1L, aWeekAgo);

            databaseInitializer.setReplyInfo("댓글 2의 대댓글입니다", 1L, 2L, today);

            // 토론방 2 : 일주일 기준 총 4개 / 오늘 기준 총 2개
            databaseInitializer.setDiscussionLikeInfo(1L, 2L, aWeekAgo);
            databaseInitializer.setDiscussionLikeInfo(2L, 2L, aWeekAgo);

            databaseInitializer.setDiscussionLikeInfo(3L, 2L, today);
            databaseInitializer.setDiscussionLikeInfo(4L, 2L, today);

            // 토론방 3 : 일주일 기준 총 3개 / 오늘 기준 총 3개
            databaseInitializer.setCommentInfo("토론방 3의 댓글입니다", 1L, 3L, today);
            databaseInitializer.setCommentInfo("토론방 3의 댓글입니다", 1L, 3L, today);
            databaseInitializer.setCommentInfo("토론방 3의 댓글입니다", 1L, 3L, today);

            // 토론방 4 : 일주일 기준 총 4개 / 오늘 기준 총 1개
            databaseInitializer.setCommentInfo("토론방 4의 댓글입니다", 1L, 4L, aWeekAgo);
            databaseInitializer.setReplyInfo("댓글 6의 대댓글입니다", 1L, 6L, aWeekAgo);
            databaseInitializer.setReplyInfo("댓글 6의 대댓글입니다", 1L, 6L, aWeekAgo);
            databaseInitializer.setReplyInfo("댓글 6의 대댓글입니다", 1L, 6L, today);
        }

        @Test
        @DisplayName("좋아요, 댓글, 대댓글의 합이 높은 순서대로 조회하고, 합이 같은 경우 최신순으로 조회된다")
        void hotDiscussionsTest_sortBySum() {
            // given - when
            final Long memberId = 1L;
            final int period = 7;
            final int count = 5;

            final List<DiscussionResponse> hotDiscussions = discussionQueryService.getHotDiscussions(memberId, period,
                    count);

            // then
            assertAll(
                    () -> assertThat(hotDiscussions).hasSize(4),
                    () -> assertThat(hotDiscussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(hotDiscussions.get(1).discussionId()).isEqualTo(4L),
                    () -> assertThat(hotDiscussions.get(2).discussionId()).isEqualTo(2L),
                    () -> assertThat(hotDiscussions.get(3).discussionId()).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName("인기 토론방으로 조회되는 개수는 count 값 이하이다")
        void hotDiscussionsTest_sliceByCount() {
            // given - when
            final Long memberId = 1L;
            final int period = 7;
            final int count = 2;

            final List<DiscussionResponse> hotDiscussions = discussionQueryService.getHotDiscussions(memberId, period,
                    count);

            // then
            assertAll(
                    () -> assertThat(hotDiscussions).hasSize(count),
                    () -> assertThat(hotDiscussions.get(0).discussionId()).isEqualTo(1L),
                    () -> assertThat(hotDiscussions.get(1).discussionId()).isEqualTo(4L)
            );
        }

        @Test
        @DisplayName("period로 주어진 기간 안에 생성된 좋아요, 댓글, 대댓를 수를 기준으로 인기 토론방을 조회한다")
        void hotDiscussionsTest_findByPeriod() {
            // given - when
            final Long memberId = 1L;
            final int period = 0;
            final int count = 5;

            final List<DiscussionResponse> hotDiscussions = discussionQueryService.getHotDiscussions(memberId, period,
                    count);

            // then
            assertAll(
                    // 3 2 4 1
                    // 3 2 1 4
                    () -> assertThat(hotDiscussions).hasSize(4),
                    () -> assertThat(hotDiscussions.get(0).discussionId()).isEqualTo(3L),
                    () -> assertThat(hotDiscussions.get(1).discussionId()).isEqualTo(2L),
                    () -> assertThat(hotDiscussions.get(2).discussionId()).isEqualTo(4L),
                    () -> assertThat(hotDiscussions.get(3).discussionId()).isEqualTo(1L)
            );
        }

    }

    @Nested
    @DisplayName("활성화 된 토론방 무한 스크롤 조회")
    class GetActiveDiscussions {

        private LocalDateTime baseTime;

        @BeforeEach
        void setUp() {
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDiscussionInfo("게시글1", "내용1", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글2", "내용2", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글3", "내용3", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글4", "내용4", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글5", "내용4", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글6", "내용4", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글7", "내용4", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글8", "내용4", 1L, 1L);
            databaseInitializer.setDiscussionInfo("게시글9", "내용4", 1L, 1L);

            baseTime = LocalDateTime.now().truncatedTo(MICROS);

            // 활성화 된 토론방 순서: 게시글 1 ~ 9 순
            databaseInitializer.setCommentInfo("댓글9-1", 1L, 9L, baseTime.minusMinutes(110));
            databaseInitializer.setCommentInfo("댓글8-1", 1L, 8L, baseTime.minusMinutes(110));
            databaseInitializer.setCommentInfo("댓글7-1", 1L, 7L, baseTime.minusMinutes(100));
            databaseInitializer.setCommentInfo("댓글6-1", 1L, 6L, baseTime.minusMinutes(80));
            databaseInitializer.setCommentInfo("댓글5-1", 1L, 5L, baseTime.minusMinutes(80));
            databaseInitializer.setCommentInfo("댓글4-1", 1L, 4L, baseTime.minusMinutes(70));
            databaseInitializer.setCommentInfo("댓글3-1", 1L, 3L, baseTime.minusMinutes(60));
            databaseInitializer.setCommentInfo("댓글3-2", 1L, 3L, baseTime.minusMinutes(50));
            databaseInitializer.setCommentInfo("댓글3-3", 1L, 3L, baseTime.minusMinutes(40));
            databaseInitializer.setCommentInfo("댓글2-1", 1L, 2L, baseTime.minusMinutes(30));
            databaseInitializer.setCommentInfo("댓글2-2", 1L, 2L, baseTime.minusMinutes(20));
            databaseInitializer.setCommentInfo("댓글1-1", 1L, 1L, baseTime.minusMinutes(10));

            databaseInitializer.setReplyInfo("대댓글1-1-1", 1L, 12L);
            databaseInitializer.setReplyInfo("대댓글1-2-1", 1L, 12L);
        }

        @Test
        @DisplayName("첫 페이지 조회 시, 정해진 기간동안 조회된 전체 활성 토론방 수가 size보다 적으면 다음 페이지 없음")
        void getActiveDiscussions_firstPage_whenResultLessThanSize_hasNextFalse() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 11;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            // then
            assertThat(firstPage.pageInfo().hasNext()).isFalse();
        }

        @Test
        @DisplayName("첫 페이지 조회 시, 정해진 기간동안 조회된 전체 활성 토론방 수가 size와 같으면 다음 페이지 없음")
        void getActiveDiscussions_firstPage_whenResultEqualSize_hasNextFalse() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 10;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            // then
            assertThat(firstPage.pageInfo().hasNext()).isFalse();
        }

        @Test
        @DisplayName("첫 페이지 조회 시, 정해진 기간동안 조회된 전체 활성 토론방 수가 size보다 많으면 다음 페이지 있음")
        void getActiveDiscussions_firstPage_whenResultGreaterThanSize_hasNextTrue() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 3;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            // then
            assertThat(firstPage.pageInfo().hasNext()).isTrue();
        }

        @Test
        @DisplayName("중간 페이지 조회 시, cursor 이후 데이터가 반환되고 다음 페이지 있음")
        void getActiveDiscussions_middlePage_hasNextTrue() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 3;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            final ActiveDiscussionPageResponse middlePage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, firstPage.pageInfo().nextCursor()
            );

            // then
            assertAll(
                    () -> assertThat(middlePage.items()).hasSize(3),
                    () -> assertThat(middlePage.pageInfo().hasNext()).isTrue()
            );
        }

        @Test
        @DisplayName("마지막 페이지 조회 시, 남은 데이터만 반환되고 다음 페이지 없음")
        void getActiveDiscussions_lastPage_hasNextTrue() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 3;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            final ActiveDiscussionPageResponse middlePage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, firstPage.pageInfo().nextCursor()
            );

            final ActiveDiscussionPageResponse lastPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, middlePage.pageInfo().nextCursor()
            );

            // then
            assertAll(
                    () -> assertThat(lastPage.items()).hasSize(3),
                    () -> assertThat(firstPage.pageInfo().hasNext()).isTrue(),
                    () -> assertThat(middlePage.pageInfo().hasNext()).isTrue(),
                    () -> assertThat(lastPage.pageInfo().hasNext()).isFalse()
            );
        }

        @Test
        @DisplayName("커서 기반 조회 시, 이전 페이지의 토론방이 다음 페이지에 포함되지 않는다")
        void getActiveDiscussions_noDuplicateItemsBetweenPages() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 3;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            final ActiveDiscussionPageResponse middlePage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, firstPage.pageInfo().nextCursor()
            );

            final ActiveDiscussionPageResponse lastPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, middlePage.pageInfo().nextCursor()
            );

            // then
            assertAll(
                    () -> assertThat(firstPage.items()).doesNotContainAnyElementsOf(middlePage.items()),
                    () -> assertThat(middlePage.items()).doesNotContainAnyElementsOf(lastPage.items())
            );
        }

        @Test
        @DisplayName("댓글을 기준으로 활성화된 토론방을 조회한다")
        void getActiveDiscussions() {
            // given
            final Long memberId = 1L;

            final int periodDays = 2;
            final int size = 2;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            // then
            assertAll(
                    () -> assertThat(firstPage.items()).hasSize(size),
                    () -> assertThat(firstPage.pageInfo().hasNext()).isTrue()
            );
        }

        @Test
        @DisplayName("활성화 된 토론방 조회 시, 전체 댓글 수는 댓글 수와 대댓글 수의 합이다")
        void getActiveDiscussions_totalCommentCount() {
            // given
            final Long memberId = 1L;
            final int periodDays = 2;
            final int size = 1;

            // when
            final ActiveDiscussionPageResponse firstPage = discussionQueryService.getActiveDiscussions(
                    memberId, periodDays, size, null
            );

            // then
            assertThat(firstPage.items().get(0).commentCount()).isEqualTo(3);
        }

        @ParameterizedTest(name = "size={0} 일 때 예외 없음")
        @ValueSource(ints = {1, 50})
        @DisplayName("페이지 크기 경계값(1, 50)에서는 예외가 발생하지 않는다")
        void getActiveDiscussions_sizeBoundary_ok(final int size) {
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final Long memberId = 1L;
            final int periodDays = 2;

            assertThatCode(() ->
                    discussionQueryService.getActiveDiscussions(memberId, periodDays, size, null)
            ).doesNotThrowAnyException();
        }

        @ParameterizedTest(name = "size={0} 일 때 예외 발생")
        @ValueSource(ints = {0, 51})
        @DisplayName("페이지 크기가 경계 밖(0, 51)이면 예외가 발생한다")
        void getActiveDiscussions_sizeOutOfRange_fail(final int size) {
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final Long memberId = 1L;
            final int periodDays = 2;

            assertThatThrownBy(() ->
                    discussionQueryService.getActiveDiscussions(memberId, periodDays, size, null)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효하지 않은 페이지 사이즈입니다. 1 이상 50 이하의 페이징을 시도해주세요");
        }
    }

    @Nested
    @DisplayName("좋아요한 토론방 목록 조회 테스트")
    class GetLikedDiscussionsTest {

        @BeforeEach
        void setUp() {
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            databaseInitializer.setDiscussionInfo("좋아요 토론1", "내용1", 1L, 1L);
            databaseInitializer.setDiscussionLikeInfo(1L, 1L);

            databaseInitializer.setDiscussionInfo("좋아요 토론2", "내용2", 1L, 1L);
            databaseInitializer.setDiscussionLikeInfo(1L, 2L);

            databaseInitializer.setDiscussionInfo("좋아요 토론3", "내용3", 1L, 1L);
            databaseInitializer.setDiscussionLikeInfo(1L, 3L);
        }

        @Test
        @DisplayName("좋아요한 토론방을 전체 조회한다")
        void getLikedDiscussions() {
            // given
            final Long memberId = 1L;

            // when
            final List<DiscussionResponse> response = discussionQueryService.getLikedDiscussionsByMe(memberId);

            // then
            assertAll(
                    () -> assertThat(response).hasSize(3),
                    () -> assertThat(response.get(0).discussionId()).isEqualTo(3L),
                    () -> assertThat(response.get(1).discussionId()).isEqualTo(2L),
                    () -> assertThat(response.get(2).discussionId()).isEqualTo(1L)
            );
        }

        @Test
        @DisplayName("좋아요한 토론방이 없으면 빈 리스트를 반환한다")
        void getLikedDiscussions_empty() {
            // given
            databaseInitializer.clear();
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final Long memberId = 1L;

            // when
            final List<DiscussionResponse> response = discussionQueryService.getLikedDiscussionsByMe(memberId);

            // then
            assertThat(response).isEmpty();
        }
    }

}
