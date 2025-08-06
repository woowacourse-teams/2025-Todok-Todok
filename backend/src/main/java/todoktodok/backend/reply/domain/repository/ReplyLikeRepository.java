package todoktodok.backend.reply.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.reply.application.service.query.ReplyLikeCountDto;
import todoktodok.backend.reply.domain.Reply;
import todoktodok.backend.reply.domain.ReplyLike;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {

    Optional<ReplyLike> findByMemberAndReply(final Member member, final Reply reply);

    @Query("""
        SELECT new todoktodok.backend.reply.application.service.query.ReplyLikeCountDto(r.id, COUNT(rl))
        FROM Reply r
        LEFT JOIN ReplyLike rl ON rl.reply = r
        WHERE r.id IN :replyIds
        GROUP BY r.id
    """)
    List<ReplyLikeCountDto> findLikeCountsByReplyIds(@Param("replyIds") final List<Long> replyIds);
}
