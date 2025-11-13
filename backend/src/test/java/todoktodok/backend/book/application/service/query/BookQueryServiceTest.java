package todoktodok.backend.book.application.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponse;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponses;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class BookQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private BookQueryService bookQueryService;

    @MockitoBean
    private AladinRestClient aladinRestClient;

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
            // given
            final List<AladinItemResponse> mockItems = List.of(
                    new AladinItemResponse("오브젝트", "객체지향 프로그래밍", "조영호", "위키북스", "9791158391409", "https://cover1.jpg"),
                    new AladinItemResponse("클린 코드", "애자일 소프트웨어", "로버트 마틴", "인사이트", "9788966260959",
                            "https://cover2.jpg"),
                    new AladinItemResponse("리팩터링", "코드 품질 개선", "마틴 파울러", "한빛미디어", "9791162242742", "https://cover3.jpg")
            );
            final AladinItemResponses mockResponse = new AladinItemResponses(mockItems, 100);
            given(aladinRestClient.searchBooksByKeyword(anyString())).willReturn(mockResponse);

            // when
            final List<AladinBookResponse> searchedBooks = bookQueryService.search(keyword);

            // then
            assertThat(searchedBooks).hasSizeGreaterThan(1);
        }

        @Test
        @DisplayName("도서 검색 시 일치하는 책이 없으면 빈 리스트를 반환한다")
        void searchTest_notFound() {
            // given
            final String keyword = "notFound";
            final AladinItemResponses mockResponse = new AladinItemResponses(List.of(), 0);
            given(aladinRestClient.searchBooksByKeyword(keyword)).willReturn(mockResponse);

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
            final List<AladinItemResponse> mockItems = List.of(
                    new AladinItemResponse("오브젝트", "객체지향 프로그래밍", "조영호", "위키북스", "9791158391409", "https://cover1.jpg"),
                    new AladinItemResponse("클린 코드", "애자일 소프트웨어", "로버트 마틴", "인사이트", "9788966260959",
                            "https://cover2.jpg"),
                    new AladinItemResponse("리팩터링", "코드 품질 개선", "마틴 파울러", "한빛미디어", "9791162242742",
                            "https://cover3.jpg"),
                    new AladinItemResponse("이펙티브 자바", "자바 베스트 프랙티스", "조슈아 블로크", "인사이트", "9788966262281",
                            "https://cover4.jpg"),
                    new AladinItemResponse("자바의 정석", "자바 프로그래밍", "남궁성", "도우출판", "9788994492032", "https://cover5.jpg"),
                    new AladinItemResponse("토비의 스프링", "스프링 프레임워크", "이일민", "에이콘", "9788960773417", "https://cover6.jpg"),
                    new AladinItemResponse("모던 자바 인 액션", "자바 8, 9, 10", "라울 게이브리얼 우르마", "한빛미디어", "9791162242025",
                            "https://cover7.jpg"),
                    new AladinItemResponse("Head First Design Patterns", "디자인 패턴", "에릭 프리먼", "한빛미디어", "9788968480911",
                            "https://cover8.jpg"),
                    new AladinItemResponse("실용주의 프로그래머", "프로그래밍 철학", "데이비드 토머스", "인사이트", "9788966261031",
                            "https://cover9.jpg"),
                    new AladinItemResponse("CODE", "하드웨어와 소프트웨어", "찰스 펫졸드", "인사이트", "9788968480775",
                            "https://cover10.jpg")
            );
            final AladinItemResponses mockResponse = new AladinItemResponses(mockItems, 100);
            given(aladinRestClient.searchBooksByKeywordWithPaging(anyString(), anyInt(), anyInt())).willReturn(
                    mockResponse);

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
            final List<AladinItemResponse> mockItems = List.of(
                    new AladinItemResponse("클린 코드", "애자일 소프트웨어", "로버트 마틴", "인사이트", "9788966260959",
                            "https://cover1.jpg"),
                    new AladinItemResponse("리팩터링", "코드 품질 개선", "마틴 파울러", "한빛미디어", "9791162242742",
                            "https://cover2.jpg"),
                    new AladinItemResponse("실용주의 프로그래머", "프로그래밍 철학", "데이비드 토머스", "인사이트", "9788966261031",
                            "https://cover3.jpg"),
                    new AladinItemResponse("CODE", "하드웨어와 소프트웨어", "찰스 펫졸드", "인사이트", "9788968480775",
                            "https://cover4.jpg"),
                    new AladinItemResponse("프로그래밍 심화", "고급 프로그래밍", "저자명", "출판사", "9781234567890", "https://cover5.jpg")
            );
            final AladinItemResponses mockResponse = new AladinItemResponses(mockItems, 50);
            given(aladinRestClient.searchBooksByKeywordWithPaging(anyString(), anyInt(), anyInt())).willReturn(
                    mockResponse);

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
            final AladinItemResponses mockResponse = new AladinItemResponses(List.of(), 0);
            given(aladinRestClient.searchBooksByKeywordWithPaging(keyword, 1, size)).willReturn(mockResponse);

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

        @Test
        @DisplayName("도서 검색 시 유효한 페이지 사이즈(1)가 입력되면 예외가 발생하지 않는다")
        void searchByPagingTest_sizeIsOne() {
            // given
            final int validSize = 1;
            final String cursor = null;
            final String keyword = "자바";
            final List<AladinItemResponse> mockItems = List.of(
                    new AladinItemResponse("자바의 정석", "자바 프로그래밍", "남궁성", "도우출판", "9788994492032", "https://cover1.jpg")
            );
            final AladinItemResponses mockResponse = new AladinItemResponses(mockItems, 100);
            given(aladinRestClient.searchBooksByKeywordWithPaging(anyString(), anyInt(), anyInt())).willReturn(
                    mockResponse);

            // when
            final LatestAladinBookPageResponse searchedBooks = bookQueryService.searchByPaging(validSize, cursor,
                    keyword);

            // then
            assertAll(
                    () -> assertThat(searchedBooks.items()).hasSize(1),
                    () -> assertThat(searchedBooks.pageInfo().hasNext()).isTrue(),
                    () -> assertThat(searchedBooks.pageInfo().nextCursor()).isNotNull(),
                    () -> assertThat(searchedBooks.pageInfo().currentSize()).isEqualTo(validSize),
                    () -> assertThat(searchedBooks.totalSize()).isGreaterThan(1)
            );
        }

        @Test
        @DisplayName("도서 검색 시 유효한 페이지 사이즈(50)가 입력되면 예외가 발생하지 않는다")
        void searchByPagingTest_sizeIsFifty() {
            // given
            final int validSize = 50;
            final String cursor = null;
            final String keyword = "자바";
            final List<AladinItemResponse> mockItems = IntStream.range(1, 51)
                    .mapToObj(i -> new AladinItemResponse(
                            "자바 책 " + i,
                            "자바 설명 " + i,
                            "저자 " + i,
                            "출판사 " + i,
                            String.format("978%010d", i),
                            "https://cover" + i + ".jpg"
                    ))
                    .toList();
            final AladinItemResponses mockResponse = new AladinItemResponses(mockItems, 100);
            given(aladinRestClient.searchBooksByKeywordWithPaging(anyString(), anyInt(), anyInt())).willReturn(
                    mockResponse);

            // when
            final LatestAladinBookPageResponse searchedBooks = bookQueryService.searchByPaging(validSize, cursor,
                    keyword);

            // then
            assertAll(
                    () -> assertThat(searchedBooks.items()).hasSizeGreaterThanOrEqualTo(1),
                    () -> assertThat(searchedBooks.pageInfo().hasNext()).isTrue(),
                    () -> assertThat(searchedBooks.pageInfo().nextCursor()).isNotNull(),
                    () -> assertThat(searchedBooks.pageInfo().currentSize()).isLessThanOrEqualTo(validSize),
                    () -> assertThat(searchedBooks.totalSize()).isGreaterThan(1)
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 51})
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

    @Test
    @DisplayName("도서 아이디로 도서를 단일 조회한다")
    void getBookTest() {
        // given
        final String bookTitle = "오브젝트";
        final String bookSummary = "오브젝트 설명";
        final String bookAuthor = "조영호";
        final String bookPublisher = "위키북스";
        final String bookIsbn = "9791158391409";
        final String bookImage = "https://image.png";
        databaseInitializer.setBookInfo(bookTitle, bookSummary, bookAuthor, bookPublisher, bookIsbn, bookImage);

        final Long bookId = 1L;

        // when
        final BookResponse bookResponse = bookQueryService.getBook(bookId);

        // then
        assertAll(
                () -> assertThat(bookResponse.bookId()).isEqualTo(bookId),
                () -> assertThat(bookResponse.bookTitle()).isEqualTo(bookTitle),
                () -> assertThat(bookResponse.bookSummary()).isEqualTo(bookSummary),
                () -> assertThat(bookResponse.bookAuthor()).isEqualTo(bookAuthor),
                () -> assertThat(bookResponse.bookPublisher()).isEqualTo(bookPublisher),
                () -> assertThat(bookResponse.bookImage()).isEqualTo(bookImage)
        );
    }

    @Test
    @DisplayName("존재하지 않는 도서 아이디로 도서를 조회하면 예외가 발생한다")
    void getBookTest_notExist_bookId() {
        // given
        databaseInitializer.setDefaultBookInfo();

        final Long notExistBookId = 999L;

        // when - then
        assertThatThrownBy(() -> bookQueryService.getBook(notExistBookId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당 도서를 찾을 수 없습니다");
    }
}
