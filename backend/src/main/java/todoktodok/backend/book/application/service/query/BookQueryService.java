package todoktodok.backend.book.application.service.query;

import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;
import todoktodok.backend.book.application.dto.response.PageInfo;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponse;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponses;
import todoktodok.backend.book.infrastructure.aladin.AladinResilienceHandler;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookQueryService {

    private static final String ISBN13_PATTERN = "\\d{13}";
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_CURSOR_SIZE = 20;
    private static final int MAX_SEARCHED_BOOK_SIZE = 200;
    private static final Logger log = LoggerFactory.getLogger(BookQueryService.class);

    private final AladinRestClient aladinRestClient;
    private final AladinResilienceHandler aladinResilienceHandler;
    private final BookRepository bookRepository;

    public BookResponse getBook(final Long bookId) {
        final Book book = findBook(bookId);

        return new BookResponse(book);
    }

    public List<AladinBookResponse> search(final String keyword) {
        validateKeyword(keyword);

        final String cleanKeyword = keyword.trim();
        final AladinItemResponses searchedBooks = aladinRestClient.searchBooksByKeyword(cleanKeyword);

        return searchedBooks.item().stream()
                .filter(book -> book.isbn13() != null && !book.isbn13().isEmpty())
                .filter(book -> book.isbn13().matches(ISBN13_PATTERN))
                .map(AladinBookResponse::new)
                .toList();
    }

    public LatestAladinBookPageResponse searchByPaging(
            final int size,
            final String page,
            final String keyword
    ) {
        validatePageSize(size);
        validateKeyword(keyword);

        final int decodedPage = decodePage(page);
        final String cleanKeyword = keyword.trim();

        final AladinItemResponses aladinItemResponses = aladinResilienceHandler.applyWithResilienceAndReturnOrFallback(
                () -> aladinRestClient.searchBooksByKeywordWithPaging(cleanKeyword, decodedPage, size),
                aladinResilienceHandler::applyBookSearch,
                exception -> fallbackBookSearch(cleanKeyword, decodedPage, size, exception)
        );

        final List<AladinBookResponse> searchedBooks = aladinItemResponses.item().stream()
                .filter(book -> book.isbn13() != null && !book.isbn13().isEmpty())
                .filter(book -> book.isbn13().matches(ISBN13_PATTERN))
                .map(AladinBookResponse::new)
                .toList();
        final PageInfo pageInfo = createNextPage(aladinItemResponses, searchedBooks, decodedPage, size);
        final int totalSize = getTotalSize(aladinItemResponses);

        return new LatestAladinBookPageResponse(searchedBooks, pageInfo, totalSize);
    }

    private AladinItemResponses fallbackBookSearch(
            final String keyword,
            final int page,
            final int size,
            final Throwable exception
    ) {
        log.error("알라딘 API 호출 실패 (원인 : {}). 데이터베이스에서 검색을 시도합니다", exception.getMessage());

        final String keywordWithPrefix = String.format("+%s*", keyword);
        final int totalSize = Long.valueOf(bookRepository.countByKeyword(keywordWithPrefix)).intValue();
        final Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        final List<Book> searchedBooks = bookRepository.searchBooksByKeyword(keywordWithPrefix, pageable).getContent();

        return new AladinItemResponses(
                searchedBooks.stream()
                        .map(book -> new AladinItemResponse(
                                book.getTitle(),
                                book.getSummary(),
                                book.getAuthor(),
                                book.getPublisher(),
                                book.getIsbn(),
                                book.getImage()
                        ))
                        .toList(),
                totalSize
        );
    }

    private void validateKeyword(final String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(String.format("검색어는 1자 이상이어야 합니다: keyword = %s", keyword));
        }
    }

    private void validatePageSize(final int size) {
        if (size < MIN_PAGE_SIZE || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("유효하지 않은 페이지 사이즈입니다: size = %d", size));
        }
    }

    private int decodePage(final String page) {
        try {
            if (page == null || page.isBlank()) {
                return 1;
            }

            final String decoded = new String(Base64.getUrlDecoder().decode(page));
            return Integer.parseInt(decoded);
        } catch (final Exception e) {
            throw new IllegalArgumentException(String.format("Base64로 디코드할 수 없는 page 값입니다: page = %s", page));
        }
    }

    private int getTotalSize(final AladinItemResponses aladinItemResponses) {
        final int totalResultsFromAladin = aladinItemResponses.totalResults();
        return Math.min(totalResultsFromAladin, MAX_SEARCHED_BOOK_SIZE);
    }

    private PageInfo createNextPage(
            final AladinItemResponses aladinItemResponses,
            final List<AladinBookResponse> searchedBooks,
            final int page,
            final int requestedSize
    ) {
        final int currentSize = searchedBooks.size();
        final int fetchedItemSize = aladinItemResponses.item().size();

        if (fetchedItemSize < requestedSize || page >= MAX_CURSOR_SIZE) { //TODO totalSize = 5, fetchItemSize = 5, requestedSize = 5인 경우?
            // TODO page같은 경우는 1씩 증가하는데, MAX_CURSOR_SIZE랑 비교하는 게 맞나?
            return new PageInfo(false, null, currentSize);
        }

        final String nextCursor = encodePage(page + 1);
        return new PageInfo(true, nextCursor, currentSize);
    }

    private String encodePage(final Integer page) {
        return Base64.getUrlEncoder().encodeToString(page.toString().getBytes());
    }

    private Book findBook(final Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 도서를 찾을 수 없습니다: bookId = %s", bookId)
                        )
                );
    }
}
