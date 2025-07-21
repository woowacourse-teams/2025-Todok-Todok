package todoktodok.backend.shelf.application.service.query;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.shelf.domain.repository.ShelfRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ShelfQueryService {

    private final ShelfRepository shelfRepository;

    public List<BookResponse> getMyBooks(final Long memberId) {
        final List<Book> myBooks = shelfRepository.findBookByMemberId(memberId);

        return myBooks.stream()
                .map(BookResponse::new)
                .toList();
    }
}
