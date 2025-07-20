package todoktodok.backend.book.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
import todoktodok.backend.book.application.dto.response.BookResponse;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class BookQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private BookQueryService bookQueryService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("내 서재의 도서 전체를 조회한다")
    void getMyBooksTest() {
        // given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        databaseInitializer.setShelfInfo();
        final Long memberId = 1L;

        // when
        final List<BookResponse> myBooks = bookQueryService.getMyBooks(memberId);

        // then
        assertThat(myBooks).hasSize(1);
    }
}
