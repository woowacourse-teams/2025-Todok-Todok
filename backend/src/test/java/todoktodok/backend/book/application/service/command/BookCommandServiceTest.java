package todoktodok.backend.book.application.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponse;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponses;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
public class BookCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private BookCommandService bookCommandService;

    @MockitoBean
    private AladinRestClient aladinRestClient;

    @Autowired
    private BookRepository bookRepository;

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
    @DisplayName("새로운 도서를 생성한다")
    void createOrUpdateBook_createNew_success() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final Long memberId = 1L;
        final String isbn = "9791158391409";
        final BookRequest bookRequest = new BookRequest(
                isbn, "오브젝트", "조영호", "image.png"
        );

        final AladinItemResponse mockAladinResponse = new AladinItemResponse(
                "오브젝트",
                "객체지향 프로그래밍",
                "조영호",
                "위키북스",
                isbn,
                "image.png"
        );
        final AladinItemResponses mockResponses = new AladinItemResponses(List.of(mockAladinResponse), 1);
        given(aladinRestClient.searchBookByIsbn(anyString())).willReturn(mockResponses);

        // when
        final Long bookId = bookCommandService.createOrUpdateBook(memberId, bookRequest);

        // then
        assertThat(bookId).isNotNull();
        final Book createdBook = bookRepository.findById(bookId).orElseThrow();
        assertAll(
                () -> assertThat(createdBook.getTitle()).isEqualTo("오브젝트"),
                () -> assertThat(createdBook.getAuthor()).isEqualTo("조영호"),
                () -> assertThat(createdBook.getPublisher()).isEqualTo("위키북스"),
                () -> assertThat(createdBook.getSummary()).isEqualTo("객체지향 프로그래밍")
        );
    }

    @Test
    @DisplayName("이미 존재하는 도서를 생성하면 수정된 정보만 업데이트된다")
    void createOrUpdateBook_duplicateUpdate_success() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String isbn = "9791158391409";
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
        final String updatedSummary = "업데이트된 설명";
        final String updatedPublisher = "업데이트된 출판사";

        final Long memberId = 1L;
        final BookRequest bookRequest = new BookRequest(
                isbn, updatedTitle, author, updatedImage
        );

        final AladinItemResponse mockAladinResponse = new AladinItemResponse(
                updatedTitle,
                updatedSummary,
                author,
                updatedPublisher,
                isbn,
                updatedImage
        );
        final AladinItemResponses mockResponses = new AladinItemResponses(List.of(mockAladinResponse), 1);
        given(aladinRestClient.searchBookByIsbn(isbn)).willReturn(mockResponses);

        // when
        final Long bookId = bookCommandService.createOrUpdateBook(memberId, bookRequest);

        // then
        assertThat(bookId).isEqualTo(1L);
        final Book updatedBook = bookRepository.findById(bookId).orElseThrow();
        assertAll(
                () -> assertThat(updatedBook.getTitle()).isEqualTo(updatedTitle),
                () -> assertThat(updatedBook.getSummary()).isEqualTo(updatedSummary),
                () -> assertThat(updatedBook.getPublisher()).isEqualTo(updatedPublisher),
                () -> assertThat(updatedBook.getImage()).isEqualTo(updatedImage)
        );
    }
}