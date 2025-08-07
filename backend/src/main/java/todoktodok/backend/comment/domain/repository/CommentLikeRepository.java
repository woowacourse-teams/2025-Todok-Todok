package todoktodok.backend.comment.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.comment.application.service.query.CommentLikeCountDto;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.comment.domain.CommentLike;
import todoktodok.backend.member.domain.Member;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByMemberAndComment(final Member member, final Comment comment);

    @Query("""
                SELECT new todoktodok.backend.comment.application.service.query.CommentLikeCountDto(c.id, COUNT(cl))
                FROM Comment c
                LEFT JOIN CommentLike cl ON cl.comment = c
                WHERE c.id IN :commentIds
                GROUP BY c.id
            """)
    List<CommentLikeCountDto> findLikeCountsByCommentIds(@Param("commentIds") final List<Long> commentIds);

    boolean existsByMemberIdAndCommentId(
            @Param("memberId") final Long memberId,
            @Param("commentId") final Long commentId
    );

    @Query("""
            SELECT cl.comment.id
            FROM CommentLike cl
            WHERE cl.member.id = :memberId AND cl.comment.id IN :commentIds
            """)
    List<Long> findLikedCommentIdsByMember(
            @Param("memberId") final Long memberId,
            @Param("commentIds") final List<Long> commentIds);
}
