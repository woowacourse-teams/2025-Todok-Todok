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
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    @Query("""
                   SELECT d.id
                   FROM Discussion d
                   WHERE d.member = :member
            """)
    List<Long> findDiscussionIdsByMember(@Param("member") final Member member);

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
                    SELECT d.id
                    FROM Discussion d
            """)
    Slice<Long> findAllIdsBy(final Pageable pageable);

    @Query("""
                    SELECT d.id
                    FROM Discussion d
                    WHERE :cursorId IS NULL OR d.id < :cursorId
            """)
    Slice<Long> findDiscussionIdsLessThan(
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

    @Query(value = """
            SELECT DISTINCT t FROM (
                SELECT d.id
                FROM discussion d
                WHERE d.member_id = :memberId AND d.deleted_at IS NULL
                
                UNION ALL
                SELECT d.id
                FROM discussion d
                JOIN comment c ON c.discussion_id = d.id
                WHERE c.member_id = :memberId
                    AND d.deleted_at IS NULL
                    AND c.deleted_at IS NULL
                
                UNION ALL
                SELECT d.id
                FROM discussion d
                JOIN comment c ON c.discussion_id = d.id
                JOIN reply r ON r.comment_id = c.id
                WHERE r.member_id = :memberId
                    AND d.deleted_at IS NULL
                    AND c.deleted_at IS NULL
                    AND r.deleted_at IS NULL
            ) AS t
            """, nativeQuery = true)
    List<Long> findParticipatedDiscussionIdsByMember(@Param("memberId") final Long memberId);

    @Query(
        value = """
        SELECT d1_0.id
        FROM discussion d1_0
        JOIN (
            SELECT
                c.discussion_id, MAX(c.id) AS max_comment_id
            FROM
                comment c
            WHERE
                c.deleted_at IS NULL
                AND c.created_at >= :periodStart
            GROUP BY
                c.discussion_id
            HAVING
                (:lastDiscussionLatestCommentId IS NULL OR MAX(c.id) < :lastDiscussionLatestCommentId)
            ORDER BY
                max_comment_id DESC
            LIMIT :pageSize
        ) AS latest_discussions ON latest_discussions.discussion_id = d1_0.id
        WHERE d1_0.deleted_at IS NULL;
        """,
        nativeQuery = true
    )
    List<Long> findActiveDiscussionsByCursor(
            @Param("periodStart") final LocalDateTime periodStart,
            @Param("lastDiscussionLatestCommentId") final Long lastDiscussionLatestCommentId,
            @Param("pageSize") final int pageSize
    );

    @Query("""
        SELECT d.id
        FROM Discussion d
        WHERE d.book.id = :bookId
    """)
    Slice<Long> findDiscussionIdsByBookId(
            @Param("bookId") final Long bookId,
            final Pageable pageable
    );

    @Query("""
        SELECT d.id
        FROM Discussion d
        WHERE d.book.id = :bookId 
        AND (:cursorId IS NULL OR d.id < :cursorId)
    """)
    Slice<Long> findDiscussionIdsByBookIdLessThan(
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

    @Query(value = """
            SELECT COUNT(DISTINCT id) FROM (
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
            ) AS search_results
            """, nativeQuery = true)
    long countSearchResultsByKeyword(@Param("keyword") final String keyword);

    @Query(value = """
            SELECT * FROM (
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
            ) AS search_results
            WHERE :cursorId IS NULL OR id < :cursorId
            ORDER BY id DESC
            """, nativeQuery = true)
    Slice<Long> searchIdsByKeywordWithCursor(
            @Param("keyword") final String keyword,
            @Param("cursorId") final Long cursorId,
            final Pageable pageable
    );
}
