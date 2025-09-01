package todoktodok.backend.book.application.service.query;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponses;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookQueryService {

    private static final String ISBN13_PATTERN = "\\d{13}";

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

    private void validateKeyword(final String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException(String.format("검색어는 1자 이상이어야 합니다: keyword = %s", keyword));
        }
    }
}
