package todoktodok.backend.member.application.service.query;

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
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원의 활동 도서를 조회하면 예외가 발생한다")
    void getActiveBooksTest_memberNotFound_fail() {
        // given
        final Long notExistsMemberId = 1L;

        // when - then
        assertThatThrownBy(() -> memberQueryService.getActiveBooks(notExistsMemberId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 회원을 찾을 수 없습니다");
    }
}
