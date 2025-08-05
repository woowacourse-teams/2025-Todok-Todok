package todoktodok.backend.reply.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.reply.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    int countRepliesByComment(final Comment comment);
}
