package todoktodok.backend.book.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.book.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(final String keyword);
}
