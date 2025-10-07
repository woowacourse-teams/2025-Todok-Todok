package todoktodok.backend.book.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;

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

    @Nested
    @DisplayName("도서 검색 테스트")
    class SearchTest {

        @ParameterizedTest
        @ValueSource(strings = {"오브젝트", " 오브젝트", "오브젝트 ", "오", "오브", "오브젝"})
        @DisplayName("검색어로 도서를 검색한다")
        void searchTest(final String keyword) {
            // given - when
            final List<AladinBookResponse> searchedBooks = bookQueryService.search(keyword);

            // then
            assertThat(searchedBooks).hasSizeGreaterThan(1);
        }

        @Test
        @DisplayName("도서 검색 시 일치하는 책이 없으면 빈 리스트를 반환한다")
        void searchTest_notFound() {
            // given
            final String keyword = "notFound";

            // when
            final List<AladinBookResponse> emptyBooks = bookQueryService.search(keyword);

            // then
            assertThat(emptyBooks).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @DisplayName("도서 검색 시 검색어가 입력되지 않으면 예외가 발생한다")
        void searchTest_isEmpty(final String keyword) {
            // when - then
            assertThatThrownBy(() -> bookQueryService.search(keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("검색어는 1자 이상이어야 합니다");
        }

        @Test
        @DisplayName("도서 검색 시 검색어가 null이면 예외가 발생한다")
        void searchTest_isNull() {
            // given
            final String keyword = null;

            // when - then
            assertThatThrownBy(() -> bookQueryService.search(keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("검색어는 1자 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("도서 검색 테스트 - 페이지네이션 적용")
    class SearchByPagingTest {

        @ParameterizedTest
        @ValueSource(strings = {"오브젝트", " 오브젝트", "오브젝트 ", "오", "오브", "오브젝"})
        @DisplayName("검색어로 도서를 검색한다 - size : 10")
        void searchByPagingTest_sizeIsTen(final String keyword) {
            // given
            final int size = 10;
            final String cursor = null;

            // when
            final LatestAladinBookPageResponse searchedBooks = bookQueryService.searchByPaging(size, cursor, keyword);

            // then
            assertAll(
                    () -> assertThat(searchedBooks.items()).hasSizeGreaterThan(1),
                    () -> assertThat(searchedBooks.pageInfo().hasNext()).isTrue(),
                    () -> assertThat(searchedBooks.pageInfo().nextCursor()).isNotNull(),
                    () -> assertThat(searchedBooks.pageInfo().currentSize()).isLessThanOrEqualTo(size),
                    () -> assertThat(searchedBooks.totalSize()).isGreaterThan(1)
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"클린코드", " 클린코드", "클린코드 ", "클린", "클린코"})
        @DisplayName("검색어로 도서를 검색한다 - size : 5")
        void searchByPagingTest_sizeIsFive(final String keyword) {
            // given
            final int size = 5;
            final String cursor = null;

            // when
            final LatestAladinBookPageResponse searchedBooks = bookQueryService.searchByPaging(size, cursor, keyword);

            // then
            assertAll(
                    () -> assertThat(searchedBooks.items()).hasSizeGreaterThan(1),
                    () -> assertThat(searchedBooks.pageInfo().hasNext()).isTrue(),
                    () -> assertThat(searchedBooks.pageInfo().nextCursor()).isNotNull(),
                    () -> assertThat(searchedBooks.pageInfo().currentSize()).isLessThanOrEqualTo(size),
                    () -> assertThat(searchedBooks.totalSize()).isGreaterThan(1)
            );
        }

        @Test
        @DisplayName("도서 검색 시 일치하는 책이 없으면 빈 리스트를 반환한다")
        void searchByPagingTest_keywordNotFound() {
            // given
            final int size = 10;
            final String cursor = null;
            final String keyword = "notFound";

            // when
            final LatestAladinBookPageResponse emptyBooks = bookQueryService.searchByPaging(size, cursor, keyword);

            // then
            assertAll(
                    () -> assertThat(emptyBooks.items()).isEmpty(),
                    () -> assertThat(emptyBooks.pageInfo().hasNext()).isFalse(),
                    () -> assertThat(emptyBooks.pageInfo().nextCursor()).isNull(),
                    () -> assertThat(emptyBooks.totalSize()).isZero()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " "})
        @DisplayName("도서 검색 시 검색어가 입력되지 않으면 예외가 발생한다")
        void searchByPagingTest_keywordIsEmpty(final String keyword) {
            // given
            final int size = 10;
            final String cursor = null;

            // when - then
            assertThatThrownBy(() -> bookQueryService.searchByPaging(size, cursor, keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("검색어는 1자 이상이어야 합니다");
        }

        @Test
        @DisplayName("도서 검색 시 검색어가 null이면 예외가 발생한다")
        void searchByPagingTest_keywordIsNull() {
            // given
            final int size = 10;
            final String cursor = null;
            final String keyword = null;

            // when - then
            assertThatThrownBy(() -> bookQueryService.searchByPaging(size, cursor, keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("검색어는 1자 이상이어야 합니다");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 201})
        @DisplayName("도서 검색 시 유효하지 않은 페이지 사이즈가 입력되면 예외가 발생한다")
        void searchByPagingTest_sizeIsNotValidate(final int invalidSize) {
            // given
            final String cursor = null;
            final String keyword = "오브젝트";

            // when - then
            assertThatThrownBy(() -> bookQueryService.searchByPaging(invalidSize, cursor, keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효하지 않은 페이지 사이즈입니다");
        }

        @ParameterizedTest
        @ValueSource(strings = {"woowa", "1", "cursor"})
        @DisplayName("도서 검색 시 디코딩할 수 없는 커서 값이 입력되면 예외가 발생한다")
        void searchByPagingTest_cursorIsNotValidate(final String cursor) {
            // given
            final int size = 10;
            final String keyword = "오브젝트";

            // when - then
            assertThatThrownBy(() -> bookQueryService.searchByPaging(size, cursor, keyword))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Base64로 디코드할 수 없는 cursor 값입니다");
        }
    }
}
