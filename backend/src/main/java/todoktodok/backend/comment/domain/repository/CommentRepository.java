package todoktodok.backend.comment.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.domain.Discussion;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByDiscussion(final Discussion discussion);
}
