package todoktodok.backend.comment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
