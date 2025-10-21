package todoktodok.backend.book.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.book.application.dto.request.BookRequest;

@Disabled
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class BookCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private BookCommandService bookCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("존재하지 않는 회원이 도서를 생성하면 예외가 발생한다")
    void createOrUpdateBook_memberNotFound_fail() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final Long memberId = 999L;

        final BookRequest bookRequest = new BookRequest(
                "9791158391409", "오브젝트", "조영호", "image.png"
        );

        // when - then
        assertThatThrownBy(() -> bookCommandService.createOrUpdateBook(memberId, bookRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("이미 존재하는 도서를 생성하면 수정된 정보만 업데이트된다")
    void createOrUpdateBook_duplicateUpdate_success() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String isbn = "1234567890123";
        final String author = "조영호";

        databaseInitializer.setBookInfo(
                "오브젝트",
                "오브젝트 내용",
                author,
                "인사이트",
                isbn,
                "image.png"
        );

        final String updatedTitle = "업데이트된 오브젝트";
        final String updatedImage = "image2.png";

        final Long memberId = 1L;
        final BookRequest bookRequest = new BookRequest(
                isbn, updatedTitle, author, updatedImage
        );

        // when
        final Long bookId = bookCommandService.createOrUpdateBook(memberId, bookRequest);

        // then
        assertThat(bookId).isEqualTo(1L);
    }
}
