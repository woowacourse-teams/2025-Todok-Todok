package todoktodok.backend.member.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.member.application.dto.response.BlockMemberResponse;
import todoktodok.backend.member.domain.MemberDiscussionFilterType;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class MemberQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private MemberQueryService memberQueryService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("존재하지 않는 회원의 프로필을 조회하면 예외가 발생한다")
    void getProfileTest_memberNotFound_fail() {
        // given
        final Long notExistsMemberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberQueryService.getProfile(notExistsMemberId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 활동 도서를 조회하면 예외가 발생한다")
    void getActiveBooksTest_memberNotFound_fail() {
        // given
        final Long notExistsMemberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberQueryService.getActiveBooks(notExistsMemberId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("회원이 생성한 토론방을 조회한다")
    void getMemberDiscussionsByTypeTest_created() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultDiscussionInfo();

        // when
        final List<DiscussionResponse> createdDiscussions = memberQueryService.getMemberDiscussionsByType(
                1L, MemberDiscussionFilterType.CREATED
        );

        // then
        assertThat(createdDiscussions).hasSize(1);
    }

    @Test
    @DisplayName("회원이 참여한 토론방을 조회한다")
    void getMemberDiscussionsByTypeTest_participated() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo("user2@gmail.com", "user2", "https://user2.png", "user2");

        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setBookInfo("토비의 스프링", "스프링 설명", "토비", "출판사", "1234567890123", "spring.png");
        databaseInitializer.setBookInfo("자바의정석", "자바 설명", "남궁성", "출판사", "1234567890123", "java.png");

        // 생성한 토론방
        databaseInitializer.setDefaultDiscussionInfo();

        // 댓글을 작성한 토론방
        databaseInitializer.setDiscussionInfo("스프링 토론ㄱ", "스프링 짱", 2L, 2L);
        databaseInitializer.setCommentInfo("맞지맞지 스프링 짱", 1L, 2L);

        // 대댓글 작성한 토론방
        databaseInitializer.setDiscussionInfo("자바 토론ㄱ", "자바 짱", 2L, 3L);
        databaseInitializer.setCommentInfo("user2가 user2에 단 댓글", 2L, 3L);
        databaseInitializer.setReplyInfo("저도 자바좋아해요", 1L, 2L);

        // when
        final List<DiscussionResponse> participatedDiscussions = memberQueryService.getMemberDiscussionsByType(
                1L, MemberDiscussionFilterType.PARTICIPATED
        );

        // then
        assertThat(participatedDiscussions).hasSize(3);
    }

    @ParameterizedTest
    @CsvSource(value = {"CREATED", "PARTICIPATED"})
    @DisplayName("존재하지 않는 회원의 토론방을 조회하면 예외가 발생한다")
    void getMemberDiscussionsByTypeTest_memberNotFound_fail(final MemberDiscussionFilterType type) {
        // given
        final Long notExistsMemberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberQueryService.getMemberDiscussionsByType(notExistsMemberId, type))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 차단 전체 목록을 조회하면 예외가 발생한다")
    void getBlockMembersTest_memberNotFound_fail() {
        // given
        final Long notExistsMemberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberQueryService.getBlockMembers(notExistsMemberId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("차단한 회원이 없다면 빈 리스트를 반환한다")
    void getBlockMembersTest_emptyBlock() {
        // given
        databaseInitializer.setDefaultUserInfo();

        // when
        final List<BlockMemberResponse> blockMembers = memberQueryService.getBlockMembers(1L);

        // then
        assertThat(blockMembers).hasSize(0);
    }
}
