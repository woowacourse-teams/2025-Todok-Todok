package todoktodok.backend.comment.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.comment.domain.Comment;
import todoktodok.backend.discussion.application.service.query.DiscussionCommentCountDto;
import todoktodok.backend.discussion.domain.Discussion;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByDiscussion(final Discussion discussion);

    boolean existsCommentsByDiscussion(final Discussion discussion);

    @Query("""
        SELECT new todoktodok.backend.discussion.application.service.query.DiscussionCommentCountDto(
            d.id,
            COUNT(DISTINCT c.id),
            COUNT(DISTINCT r.id)
        )
        FROM Discussion d
        LEFT JOIN Comment c ON c.discussion = d AND c.deletedAt IS NULL
        LEFT JOIN Reply r ON r.comment = c AND r.deletedAt IS NULL
        WHERE d.id IN :discussionIds
        GROUP BY d.id
    """)
    List<DiscussionCommentCountDto> findCommentCountsByDiscussionIds(@Param("discussionIds") final List<Long> discussionIds);

    @Query("""
        SELECT COUNT(c)
        FROM Comment c
        WHERE c.discussion.id = :discussionId
    """)
    Long findCommentCountsByDiscussionId(@Param("discussionId") final Long discussionId);

}
