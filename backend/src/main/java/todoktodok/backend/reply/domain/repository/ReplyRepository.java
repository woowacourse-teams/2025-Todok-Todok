package todoktodok.backend.reply.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.comment.application.service.query.CommentReplyCountDto;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.reply.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    int countRepliesByComment(final Comment comment);

    @Query("""
        SELECT new todoktodok.backend.comment.application.service.query.CommentReplyCountDto(c.id, COUNT(r))
        FROM Comment c
        LEFT JOIN Reply r ON r.comment = c
        WHERE c.id IN :commentIds
        GROUP BY c.id
    """)
    List<CommentReplyCountDto> findReplyCountsByCommentIds(@Param("commentIds") final List<Long> commentIds);
}
