package todoktodok.backend.comment.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByDiscussion(final Discussion discussion);

    @Query("""
        SELECT DISTINCT c.discussion.book
        FROM Comment c
        WHERE c.member = :member
    """)
    List<Book> findBooksByMember(final Member member);
}
