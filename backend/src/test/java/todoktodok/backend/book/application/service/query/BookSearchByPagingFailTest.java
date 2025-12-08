package todoktodok.backend.book.application.service.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;
import todoktodok.backend.book.application.service.command.BookCommandService;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.book.infrastructure.aladin.AladinResilienceHandler;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;
import todoktodok.backend.book.infrastructure.aladin.exception.AladinApiException;
import todoktodok.backend.book.presentation.fixture.BookFixture;
import todoktodok.backend.member.domain.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({BookQueryService.class, BookCommandService.class, AladinResilienceHandler.class})
public class BookSearchByPagingFailTest {

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private AladinRestClient aladinRestClient;

    @MockitoBean
    private MemberRepository memberRepository;

    @Autowired
    private BookQueryService bookQueryService;

    @Autowired
    private BookCommandService bookCommandService;

    @Test
    @DisplayName("도서 키워드로 검색 시 외부 API에서 예외 발생 시 2회 재시도 후 내부 DB에서 검색한다")
    void bookSearchByKeyword_exceptionFailTest_thenFallback() {
        // given
        final int size = 3;
        final String page = null;
        final String keyword = "클린";

        final long fakeTotalSize = 5L;
        final int fakeCurrentSize = 3;

        given(aladinRestClient.searchBooksByKeywordWithPaging(anyString(), anyInt(), anyInt())).willThrow(
                new AladinApiException("Aladin Api Exception occurred"));
        given(bookRepository.searchBooksByKeyword(anyString(), any())).willReturn(
                new PageImpl<>(
                        List.of(
                                BookFixture.create("클린 코드1", "로버트 마틴", "publisher", "9788966260901"),
                                BookFixture.create("클린 코드2", "로버트 마틴", "publisher", "9788966260902"),
                                BookFixture.create("클린 코드3", "로버트 마틴", "publisher", "9788966260903")
                        )
                ));
        given(bookRepository.countByKeyword(anyString())).willReturn(fakeTotalSize);

        // when
        final LatestAladinBookPageResponse searchedBooks = bookQueryService.searchByPaging(size, page, keyword);

        verify(aladinRestClient, times(1))
                .searchBooksByKeywordWithPaging(anyString(), anyInt(), anyInt());
        verify(bookRepository, times(1))
                .searchBooksByKeyword(anyString(), any());

        // then
        assertAll(
                () -> assertThat(searchedBooks.items()).hasSize(fakeCurrentSize),
                () -> assertThat(searchedBooks.pageInfo().hasNext()).isTrue(),
                () -> assertThat(searchedBooks.pageInfo().nextCursor()).isNotNull(),
                () -> assertThat(searchedBooks.pageInfo().currentSize()).isEqualTo(fakeCurrentSize),
                () -> assertThat(searchedBooks.totalSize()).isEqualTo(fakeTotalSize)
        );
    }

    @Test
    @DisplayName("도서 Isbn으로 상세 검색 시 외부 API에서 예외 발생 시 2회 재시도 후 에러를 발생시킨다")
    void bookSearchByIsbn_exceptionFailTest_thenFallback() {
        // given
        given(memberRepository.existsByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
        given(aladinRestClient.searchBookByIsbn(anyString())).willThrow(
                new AladinApiException("Aladin Api Exception occurred")
        );
        given(bookRepository.findByIsbn(anyString())).willReturn(
                Optional.empty()
        );

        // when - then
        assertThatThrownBy(() -> bookCommandService.createOrUpdateBook(
                1L, new BookRequest("9788966260901", "클린 코드", "로버트 마틴", "http://image.png")
        ))
                .isInstanceOf(AladinApiException.class)
                .hasMessageContaining("도서 검색 API에 일시적 장애가 발생해 이용이 불가능합니다");

        verify(aladinRestClient, times(1))
                .searchBookByIsbn(anyString());
    }
}
