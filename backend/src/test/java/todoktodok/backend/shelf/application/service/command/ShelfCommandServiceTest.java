package todoktodok.backend.shelf.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
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
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.shelf.application.dto.request.ShelfRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class ShelfCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private ShelfCommandService shelfCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("서재에 도서를 추가하면 추가한 도서 정보를 반환한다")
    void addBookTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setBookInfo(
                "좋은코드",
                "좋은코드 설명",
                "톰 롱",
                "제이펍",
                "9791191600896",
                "image.png"
        );

        final Long memberId = 1L;
        final ShelfRequest shelfRequest = new ShelfRequest(1L);

        // when
        BookResponse bookResponse = shelfCommandService.addBook(memberId, shelfRequest);
        BookResponse expected = new BookResponse(
                1L,
                "좋은코드",
                "톰 롱",
                "image.png"
        );

        // then
        assertThat(bookResponse).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 서재에 도서를 추가하면 예외가 발생한다")
    void addBookTest_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 999L;
        final ShelfRequest shelfRequest = new ShelfRequest(1L);

        // when - then
        assertThatThrownBy(() -> shelfCommandService.addBook(memberId, shelfRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 책을 서재에 추가하면 예외가 발생한다")
    void addBookTest_bookNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();

        final Long memberId = 1L;
        final ShelfRequest shelfRequest = new ShelfRequest(999L);

        // when - then
        assertThatThrownBy(() -> shelfCommandService.addBook(memberId, shelfRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 도서를 찾을 수 없습니다");
    }
}
