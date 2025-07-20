package todoktodok.backend.book.application.service.query;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.shelf.domain.repository.ShelfRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookQueryService {

    private final ShelfRepository shelfRepository;
    private final BookRepository bookRepository;

    public List<BookResponse> getMyBooks(final Long memberId) {
        final List<Book> myBooks = shelfRepository.findBookByMemberId(memberId);

        return myBooks.stream()
                .map(BookResponse::new)
                .toList();
    }

    public List<BookResponse> search(final String keyword) {
        final List<Book> books = bookRepository.findAll();
        final String cleanLowerKeyword = keyword.trim().toLowerCase();

        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(cleanLowerKeyword))
                .map(BookResponse::new)
                .toList();
    }
}
