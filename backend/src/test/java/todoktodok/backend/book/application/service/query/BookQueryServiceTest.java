package todoktodok.backend.book.application.service.query;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @ValueSource(strings = {"오브젝트", " 오브젝트", "오브젝트 ", "오", "오브", "오브젝"})
    @DisplayName("검색어로 도서를 검색한다")
    void searchTest(String keyword) {
        // given
        databaseInitializer.setDefaultBookInfo();

        // when
        final List<BookResponse> books = bookQueryService.search(keyword);

        // then
        assertThat(books).hasSize(1);
    }

    @Test
    @DisplayName("도서 검색 시 일치하는 책이 없으면 빈 리스트를 반환한다")
    void searchTest_notFound() {
        // given
        final String keyword = "오브젝트";

        // when
        final List<BookResponse> books = bookQueryService.search(keyword);

        // then
        assertThat(books).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("도서 검색 시 검색어가 입력되지 않으면 빈 리스트를 반환한다")
    void searchTest_isEmpty(final String keyword) {
        // when
        final List<BookResponse> books = bookQueryService.search(keyword);

        // then
        assertThat(books).isEmpty();
    }

    @Test
    @DisplayName("도서 검색 시 검색어가 null이면 빈 리스트를 반환한다")
    void searchTest_isNull() {
        // given
        final String keyword = null;

        // when
        final List<BookResponse> books = bookQueryService.search(keyword);

        // then
        assertThat(books).isEmpty();
    }
}
