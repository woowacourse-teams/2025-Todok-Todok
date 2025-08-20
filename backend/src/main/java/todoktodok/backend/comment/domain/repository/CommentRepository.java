package todoktodok.backend.comment.domain.repository;

import java.time.LocalDateTime;
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
        LEFT JOIN Comment c ON c.discussion = d
        LEFT JOIN Reply r ON r.comment = c
        WHERE d.id IN :discussionIds
        GROUP BY d.id
    """)
    List<DiscussionCommentCountDto> findCommentCountsByDiscussionIds(@Param("discussionIds") final List<Long> discussionIds);

    @Query("""
        SELECT new todoktodok.backend.discussion.application.service.query.DiscussionCommentCountDto(
            d.id,
            COUNT(DISTINCT c.id),
            COUNT(DISTINCT r.id)
        )
        FROM Discussion d
        LEFT JOIN Comment c ON c.discussion = d AND c.createdAt >= :sinceDate
        LEFT JOIN Reply r ON r.comment.discussion = d AND r.createdAt >= :sinceDate
        WHERE d.id IN :discussionIds
        GROUP BY d.id
    """)
    List<DiscussionCommentCountDto> findCommentCountsByDiscussionIdsSinceDate(
            @Param("discussionIds") final List<Long> discussionIds,
            @Param("sinceDate") final LocalDateTime sinceDate
    );

    @Query("""
        SELECT COUNT(c)
        FROM Comment c
        WHERE c.discussion.id = :discussionId
    """)
    Long countCommentsByDiscussionId(@Param("discussionId") final Long discussionId);
}
