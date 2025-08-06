package todoktodok.backend.reply.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.ReplyLike;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {

    Optional<ReplyLike> findByMemberAndReply(final Member member, final Reply reply);
}
