package todoktodok.backend.book.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.member.domain.Member;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(final String isbn);

    @Query("""
    SELECT DISTINCT d.book FROM Discussion d
    WHERE d.member = :member
    UNION
    SELECT DISTINCT c.discussion.book FROM Comment c
    WHERE c.member = :member
    UNION
    SELECT DISTINCT r.comment.discussion.book FROM Reply r
    WHERE r.member = :member
    """)
    List<Book> findActiveBooksByMember(@Param("member") final Member member);
}
