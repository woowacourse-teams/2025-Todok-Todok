package todoktodok.backend.reply.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.reply.application.dto.response.ReplyResponse;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class ReplyQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private ReplyQueryService replyQueryService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("댓글별 대댓글 목록 조회 시 대댓글의 좋아요수를 반환한다")
    void getRepliesWithLikeCountTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        databaseInitializer.setReplyLikeInfo(1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final List<ReplyResponse> replies = replyQueryService.getReplies(memberId, discussionId, commentId);
        final ReplyResponse reply = replies.getFirst();

        // then
        assertThat(reply.likeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글별 대댓글 목록 조회 시 대댓글이 없는 경우 좋아요수 0개를 반환한다")
    void getRepliesWithLikeCountTest_noReply() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final List<ReplyResponse> replies = replyQueryService.getReplies(memberId, discussionId, commentId);

        // then
        assertThat(replies).isEmpty();
    }

    @Test
    @DisplayName("댓글별 대댓글 목록 조회 시 대댓글 좋아요가 없는 경우 좋아요수 0개를 반환한다")
    void getRepliesWithLikeCountTest_noReplyLike() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setDefaultReplyInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final List<ReplyResponse> replies = replyQueryService.getReplies(memberId, discussionId, commentId);
        final ReplyResponse reply = replies.getFirst();

        // then
        assertThat(reply.likeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 댓글에 대해 답글을 조회할 경우 예외가 발생한다")
    void getRepliesWithLikeCountTest_noComment() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 2L;

        // when - then
        assertThatThrownBy(() -> replyQueryService.getReplies(memberId, discussionId, commentId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 댓글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("없는 토론방에 대해 답글을 조회할 경우 예외가 발생한다")
    void getRepliesWithLikeCountTest_noDiscussion() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 2L;
        final Long commentId = 1L;

        // when - then
        assertThatThrownBy(() -> replyQueryService.getReplies(memberId, discussionId, commentId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 토론방을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("대댓글을 조회할 때 토론방과 댓글이 매치되지 않으면 예외가 발생한다")
    void getRepliesWithLikeCountTest_commentAndDiscussionNotMatch() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        databaseInitializer.setDiscussionInfo("토론1", "토론1입니다", 1L, 1L);
        databaseInitializer.setDiscussionInfo("토론2", "토론2입니다", 1L, 1L);
        databaseInitializer.setCommentInfo("토론1의 댓글입니다", 1L, 1L);
        databaseInitializer.setCommentInfo("토론2의 댓글입니다", 1L, 2L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 2L;

        // when - then
        assertThatThrownBy(() -> replyQueryService.getReplies(memberId, discussionId, commentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 토론방에 있는 댓글이 아닙니다");
    }

    @Test
    @DisplayName("대댓글을 조회할 때 좋아요 생성 여부를 반환한다")
    void getReplies_isLikedTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setReplyInfo("좋은데요?", 1L, 1L);
        databaseInitializer.setReplyInfo("별론데요?", 1L, 1L);

        databaseInitializer.setReplyLikeInfo(1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final List<ReplyResponse> replies = replyQueryService.getReplies(memberId, discussionId, commentId);
        final ReplyResponse likedReply = replies.get(0);
        final ReplyResponse notLikedReply = replies.get(1);

        // then
        assertAll(
                () -> assertThat(likedReply.isLikedByMe()).isTrue(),
                () -> assertThat(notLikedReply.isLikedByMe()).isFalse()
        );
    }
}
