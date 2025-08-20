package todoktodok.backend.discussion.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.application.dto.response.DiscussionCursorResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.member.domain.Member;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    List<Discussion> findDiscussionsByMember(final Member member);

    @Query("""
        SELECT d FROM Discussion d
        WHERE UPPER(d.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        UNION
        SELECT d FROM Discussion d
        JOIN d.book b
        WHERE UPPER(b.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        AND b.deletedAt IS NULL
    """)
    List<Discussion> searchByKeyword(
            @Param("keyword") final String keyword
    );

    @Query("""
        SELECT d FROM Discussion d
        WHERE UPPER(d.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        AND d.member = :member
        UNION
        SELECT d FROM Discussion d
        JOIN d.book b
        WHERE UPPER(b.title) LIKE UPPER(CONCAT('%', :keyword, '%'))
        AND d.deletedAt IS NULL
        AND b.deletedAt IS NULL
        AND d.member = :member
    """)
    List<Discussion> searchByKeywordAndMember(
            @Param("keyword") final String keyword,
            @Param("member") final Member member
    );

    @Query(value = """
        SELECT d.* 
        FROM discussion d
        WHERE d.member_id = :memberId
        AND d.deleted_at IS NULL
            
        UNION
            
        SELECT d.* 
        FROM discussion d
        JOIN comment c ON c.discussion_id = d.id
        WHERE c.member_id = :memberId
        AND d.deleted_at IS NULL
        AND c.deleted_at IS NULL
            
        UNION
            
        SELECT d.* 
        FROM discussion d
        JOIN comment c ON c.discussion_id = d.id
        JOIN reply r ON r.comment_id = c.id
        WHERE r.member_id = :memberId
        AND d.deleted_at IS NULL
        AND c.deleted_at IS NULL
        AND r.deleted_at IS NULL
    """, nativeQuery = true)
    List<Discussion> findParticipatedDiscussionsByMember(@Param("memberId") final Long memberId);

    @Query("""
            SELECT new todoktodok.backend.discussion.application.dto.response.DiscussionCursorResponse(
                         d,
                         COUNT(DISTINCT c.id),
                         COUNT(DISTINCT dl.id),
                         CASE WHEN COUNT(DISTINCT dlByMe.id) > 0 THEN true ELSE false END,
                         MAX(c.createdAt)
             )
            FROM Discussion d
            JOIN Comment c ON c.discussion = d AND c.createdAt >= :periodStart
            LEFT JOIN DiscussionLike dl ON dl.discussion = d
            LEFT JOIN DiscussionLike dlByMe ON dlByMe.discussion = d AND dlByMe.member = :member
            GROUP BY d
            HAVING (
                :lastCommentedAt IS NULL
                OR MAX(c.createdAt) < :lastCommentedAt
                OR MAX(c.createdAt) = :lastCommentedAt AND d.id < :cursorId
            )
            ORDER BY MAX(c.createdAt) DESC, d.id DESC
    """)
    List<DiscussionCursorResponse> findActiveDiscussionsByCursor(
            @Param("member") Member member,
            @Param("periodStart") LocalDateTime periodStart,
            @Param("lastCommentedAt") LocalDateTime lastCommentedAt,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
