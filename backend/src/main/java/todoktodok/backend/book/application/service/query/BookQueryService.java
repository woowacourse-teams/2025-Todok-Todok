package todoktodok.backend.book.application.service.query;

import jakarta.annotation.Nullable;
import java.util.Base64;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;
import todoktodok.backend.book.application.dto.response.PageInfo;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponses;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookQueryService {

    private static final String ISBN13_PATTERN = "\\d{13}";
    private static final int PAGE_SIZE = 10;
    private static final int MAX_TOTAL_SIZE = 200;

    private final AladinRestClient aladinRestClient;

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
            @Nullable final String cursor,
            final String keyword
    ) {
        validatePageSize(size);
        validateKeyword(keyword);

        final int page = decodeCursor(cursor);
        final String cleanKeyword = keyword.trim();
        final AladinItemResponses aladinItemResponses =
                aladinRestClient.searchBooksByKeywordWithPaging(cleanKeyword, page, size);
        final int totalSize = getTotalSize(aladinItemResponses);
        final List<AladinBookResponse> searchedBooks = aladinItemResponses.item().stream()
                .filter(book -> book.isbn13() != null && !book.isbn13().isEmpty())
                .filter(book -> book.isbn13().matches(ISBN13_PATTERN))
                .map(AladinBookResponse::new)
                .toList();
        final PageInfo pageInfo = createNextCursor(searchedBooks, page, size);

        return new LatestAladinBookPageResponse(searchedBooks, pageInfo, totalSize);
    }

    private void validateKeyword(final String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(String.format("검색어는 1자 이상이어야 합니다: keyword = %s", keyword));
        }
    }

    private void validatePageSize(final int size) {
        if (size != PAGE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("유효하지 않은 페이지 사이즈입니다: size = %d", size));
        }
    }

    private int decodeCursor(final String cursor) {
        try {
            if (cursor == null || cursor.isBlank()) {
                return 1;
            }

            final String decoded = new String(Base64.getUrlDecoder().decode(cursor));
            return Integer.parseInt(decoded);
        } catch (final Exception e) {
            throw new IllegalArgumentException(String.format("Base64로 디코드할 수 없는 cursor 값입니다: cursor = %s", cursor));
        }
    }

    private int getTotalSize(final AladinItemResponses aladinItemResponses) {
        final int totalResultsFromAladin = aladinItemResponses.totalResults();

        if (totalResultsFromAladin > MAX_TOTAL_SIZE) {
            return MAX_TOTAL_SIZE;
        }

        return totalResultsFromAladin;
    }

    private PageInfo createNextCursor(
            final List<AladinBookResponse> searchedBooks,
            final int page,
            final int size
    ) {
        if (searchedBooks.size() < size || page == MAX_TOTAL_SIZE / PAGE_SIZE) {
            return new PageInfo(false, null);
        }

        final String nextCursor = encodeCursorId(page + 1);
        return new PageInfo(true, nextCursor);
    }

    private String encodeCursorId(final Integer id) {
        return Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
    }
}
