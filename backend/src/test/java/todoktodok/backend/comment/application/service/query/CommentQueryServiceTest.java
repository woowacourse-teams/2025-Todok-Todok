package todoktodok.backend.comment.application.service.query;

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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.comment.application.dto.response.CommentResponse;

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
    @DisplayName("댓글 단일 조회한다")
    void getCommentTest() {
        // given
        final String content = "댓글 1입니다";

        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setCommentInfo(content, 1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final CommentResponse comment = commentQueryService.getComment(memberId, discussionId, commentId);

        // then
        assertThat(comment.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("토론방별 댓글 목록 조회한다")
    void getCommentsTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        databaseInitializer.setDefaultCommentInfo();
        databaseInitializer.setCommentInfo("댓글 2입니다", 1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when
        final List<CommentResponse> comments = commentQueryService.getComments(memberId, discussionId);

        // then
        assertThat(comments).hasSize(2);
    }

    @Test
    @DisplayName("토론방별 댓글 목록 조회 시 댓글의 좋아요수와 답글수를 반환한다")
    void getCommentsTest_likeCountAndReplyCount() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        databaseInitializer.setDefaultReplyInfo();
        databaseInitializer.setReplyInfo("동의합니다.", 1L, 1L);
        databaseInitializer.setCommentLikeInfo(1L, 1L);

        final Long memberId = 1L;
        final Long discussionId = 1L;

        // when
        final List<CommentResponse> comments = commentQueryService.getComments(memberId, discussionId);
        final CommentResponse comment = comments.getFirst();

        // then
        assertAll(
                () -> assertThat(comment.replyCount()).isEqualTo(2),
                () -> assertThat(comment.likeCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("없는 토론방에 대해 댓글을 조회할 경우 예외가 발생한다")
    void getCommentsTest_discussionNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

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
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 2L;
        final Long discussionId = 1L;

        // when - then
        assertThatThrownBy(() -> commentQueryService.getComments(memberId, discussionId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("단일 댓글 조회 시, 내가 좋아요를 한 댓글인지 확인한다")
    void getComment_isLikedByMeTest_true() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        databaseInitializer.setCommentLikeInfo(memberId, commentId);

        // when
        final CommentResponse comment = commentQueryService.getComment(memberId, discussionId, commentId);

        // then
        assertThat(comment.isLikedByMe()).isTrue();
    }

    @Test
    @DisplayName("단일 댓글 조회 시, 내가 좋아요를 한 댓글이 아니라는 것을 확인한다")
    void getComment_isLikedByMeTest_false() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();
        databaseInitializer.setDefaultCommentInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;
        final Long commentId = 1L;

        // when
        final CommentResponse comment = commentQueryService.getComment(memberId, discussionId, commentId);

        // then
        assertThat(comment.isLikedByMe()).isFalse();
    }

    @Test
    @DisplayName("전체 댓글 조회 시, 댓글별로 내가 좋아요를 한 댓글인지 확인한다")
    void getComments_isLikedByMeTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        final Long memberId = 1L;
        final Long discussionId = 1L;

        databaseInitializer.setCommentInfo("댓글1", memberId, discussionId);
        databaseInitializer.setCommentInfo("댓글2", memberId, discussionId);
        databaseInitializer.setCommentLikeInfo(memberId, 1L);

        // when
        final List<CommentResponse> comments = commentQueryService.getComments(memberId, discussionId);
        CommentResponse likedComment = comments.get(0);
        CommentResponse notLikedComment = comments.get(1);

        // then
        assertAll(
                () -> assertThat(likedComment.isLikedByMe()).isTrue(),
                () -> assertThat(notLikedComment.isLikedByMe()).isFalse()
        );
    }
}
