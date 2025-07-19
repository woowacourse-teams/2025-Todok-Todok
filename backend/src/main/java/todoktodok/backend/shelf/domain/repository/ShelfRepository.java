package todoktodok.backend.shelf.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.shelf.domain.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {

    @Query("""
        SELECT s.book
        FROM Shelf s
        WHERE s.member.id = :memberId
    """)
    List<Book> findBookByMemberId(@Param("memberId") final Long memberId);
}
