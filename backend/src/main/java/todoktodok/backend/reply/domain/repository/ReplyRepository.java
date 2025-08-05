package todoktodok.backend.reply.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.reply.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
