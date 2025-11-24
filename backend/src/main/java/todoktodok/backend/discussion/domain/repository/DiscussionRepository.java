package todoktodok.backend.discussion.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    @Query("""
                   SELECT d.id 
                   FROM Discussion d
            """)
    List<Long> findAllIds();

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
                   SELECT d
                   FROM Discussion d
                   WHERE d.member = :member
            """)
    List<Discussion> findDiscussionsByMember(@Param("member") final Member member);

    @Query("""
                   SELECT d
                   FROM Discussion d
                   JOIN FETCH d.member
                   JOIN FETCH d.book
                   WHERE d.id = :discussionId
            """)
    Optional<Discussion> findByIdWithMemberAndBook(final Long discussionId);

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
                   SELECT d
                   FROM Discussion d
                   WHERE d.id IN :discussionIds
            """)
    List<Discussion> findDiscussionsInIds(@Param("discussionIds") final List<Long> discussionIds);

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
                    SELECT d
                    FROM Discussion d
            """)
    Slice<Discussion> findAllBy(final Pageable pageable);

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
                    SELECT d
                    FROM Discussion d
                    WHERE :cursorId IS NULL OR d.id < :cursorId
            """)
    Slice<Discussion> findDiscussionsLessThan(
            @Param("cursorId") final Long cursorId,
            final Pageable pageable
    );

    @Query(value = """
            SELECT d.id
            FROM discussion d
            WHERE MATCH(d.title) AGAINST(:keyword IN BOOLEAN MODE)
              AND d.deleted_at IS NULL
              
            UNION
            
            SELECT d.id
            FROM discussion d
            JOIN book b ON d.book_id = b.id
            WHERE MATCH(b.title) AGAINST(:keyword IN BOOLEAN MODE)
              AND d.deleted_at IS NULL
              AND b.deleted_at IS NULL
            """, nativeQuery = true)
    List<Long> searchIdsByKeyword(@Param("keyword") final String keyword);

    @Query(
            value = """
            SELECT
                activity.discussion_id
            FROM (
                SELECT dl.discussion_id
                FROM discussion_like dl
                WHERE dl.created_at >= :sinceDate AND dl.deleted_at IS NULL
                
                UNION ALL
                SELECT c.discussion_id
                FROM comment c
                WHERE c.created_at >= :sinceDate AND c.deleted_at IS NULL
                
                UNION ALL
                SELECT c.discussion_id
                FROM reply r
                JOIN comment c ON r.comment_id = c.id
                WHERE r.created_at >= :sinceDate AND r.deleted_at IS NULL AND c.deleted_at IS NULL
            ) AS activity
            GROUP BY
                activity.discussion_id
            ORDER BY
                COUNT(activity.discussion_id) DESC, activity.discussion_id DESC
            """,
            nativeQuery = true
    )
    List<Long> findHotDiscussionIds(@Param("sinceDate") final LocalDateTime sinceDate, final Pageable pageable);

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = """
            SELECT DISTINCT t FROM (
                SELECT d
                FROM discussion d
                WHERE d.member_id = :memberId AND d.deleted_at IS NULL
                
                UNION ALL
                SELECT d
                FROM discussion d
                JOIN comment c ON c.discussion_id = d.id
                WHERE c.member_id = :memberId
                    AND d.deleted_at IS NULL
                    AND c.deleted_at IS NULL
                
                UNION ALL
                SELECT d
                FROM discussion d
                JOIN comment c ON c.discussion_id = d.id
                JOIN reply r ON r.comment_id = c.id
                WHERE r.member_id = :memberId
                    AND d.deleted_at IS NULL
                    AND c.deleted_at IS NULL
                    AND r.deleted_at IS NULL
            ) AS t
            """, nativeQuery = true)
    List<Discussion> findParticipatedDiscussionIdsByMember2(@Param("memberId") final Long memberId);

    @Query(value = """
                SELECT d.id
                FROM discussion d
                WHERE d.member_id = :memberId
                AND d.deleted_at IS NULL
                    
                UNION
                    
                SELECT d.id
                FROM discussion d
                JOIN comment c ON c.discussion_id = d.id
                WHERE c.member_id = :memberId
                AND d.deleted_at IS NULL
                AND c.deleted_at IS NULL
                    
                UNION
                    
                SELECT d.id
                FROM discussion d
                JOIN comment c ON c.discussion_id = d.id
                JOIN reply r ON r.comment_id = c.id
                WHERE r.member_id = :memberId
                AND d.deleted_at IS NULL
                AND c.deleted_at IS NULL
                AND r.deleted_at IS NULL
            """, nativeQuery = true)
    List<Long> findParticipatedDiscussionIdsByMember(@Param("memberId") final Long memberId);

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
        SELECT d
        FROM Discussion d
        JOIN Comment c ON c.discussion = d
        WHERE c.createdAt >= :periodStart
        GROUP BY d
        HAVING (
           :lastDiscussionLatestCommentId IS NULL
            OR MAX(c.id) < :lastDiscussionLatestCommentId
        )
        ORDER BY MAX(c.id) DESC
   """)
    List<Discussion> findActiveDiscussionsByCursor(
            @Param("periodStart") final LocalDateTime periodStart,
            @Param("lastDiscussionLatestCommentId") final Long lastDiscussionLatestCommentId,
            final Pageable pageable
    );

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
        SELECT d
        FROM Discussion d
        WHERE d.book.id = :bookId
    """)
    Slice<Discussion> findDiscussionsByBookId(
            @Param("bookId") final Long bookId,
            final Pageable pageable
    );

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
        SELECT d
        FROM Discussion d
        WHERE d.book.id = :bookId 
        AND (:cursorId IS NULL OR d.id < :cursorId)
    """)
    Slice<Discussion> findDiscussionsByBookIdLessThan(
            @Param("bookId") final Long bookId,
            @Param("cursorId") final Long cursorId,
            final Pageable pageable
    );

    @Modifying(clearAutomatically = true)
    @Query("""
                UPDATE Discussion d
                SET d.viewCount = d.viewCount + 1
                WHERE d.id = :discussionId
            """)
    void increaseViewCount(@Param("discussionId") final Long discussionId);

    @Modifying(clearAutomatically = true)
    @Query("""
                UPDATE Discussion d
                SET d.commentCount = d.commentCount + 1
                WHERE d.id = :discussionId
            """)
    void increaseCommentCount(@Param("discussionId") final Long discussionId);

    @Modifying(clearAutomatically = true)
    @Query("""
                UPDATE Discussion d
                SET d.likeCount = d.likeCount + 1
                WHERE d.id = :discussionId
            """)
    void increaseLikeCount(@Param("discussionId") final Long discussionId);

    @Modifying(clearAutomatically = true)
    @Query("""
                UPDATE Discussion d
                SET d.commentCount = d.commentCount -1
                WHERE d.id = :discussionId
            """)
    void decreaseCommentCount(@Param("discussionId") final Long discussionId);

    @Modifying(clearAutomatically = true)
    @Query("""
                UPDATE Discussion d
                SET d.likeCount = d.likeCount - 1
                WHERE d.id = :discussionId
            """)
    void decreaseLikeCount(@Param("discussionId") final Long discussionId);

    @Query("""
            SELECT new todoktodok.backend.discussion.application.dto.response.DiscussionResponse(
                d.id,
                d.title,
                d.content,
                d.createdAt,
                d.viewCount,
                d.book,
                d.member,
                (SELECT COUNT(dl.id) FROM DiscussionLike dl WHERE dl.discussion = d),
                (SELECT COUNT(c.id) FROM Comment c WHERE c.discussion = d) + (SELECT COUNT(r.id) FROM Reply r WHERE r.comment.discussion = d),
                EXISTS(SELECT 1 FROM DiscussionLike dl2 WHERE dl2.discussion = d AND dl2.member = :member)
            )
            FROM Discussion d
            LEFT JOIN d.book b
            LEFT JOIN d.member m
            WHERE d.id IN :discussionIds
            """)
    List<DiscussionResponse> findDiscussionResponses(
            @Param("discussionIds") final List<Long> discussionIds,
            @Param("member") final Member member
    );
}
