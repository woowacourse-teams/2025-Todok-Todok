package todoktodok.backend.discussion.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.application.service.query.DiscussionLikeSummaryDto;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionLike;
import todoktodok.backend.member.domain.Member;

public interface DiscussionLikeRepository extends JpaRepository<DiscussionLike, Long> {

    Optional<DiscussionLike> findByMemberAndDiscussion(final Member member, final Discussion discussion);

    @Query("""
                SELECT (
                        CASE WHEN SUM(CASE WHEN dl.member = :member THEN 1 ELSE 0 END) > 0 
                             THEN true 
                             ELSE false END
                )
                FROM DiscussionLike dl
                WHERE dl.discussion.id = :discussionId
            """)
    boolean findIsLikedByMeByDiscussionId(
            @Param("member") final Member member,
            @Param("discussionId") final Long discussionId
    );

    @Query("""
                    SELECT new todoktodok.backend.discussion.application.service.query.DiscussionLikeSummaryDto(
                        d.id,
                        CASE WHEN SUM(CASE WHEN dl.member = :member THEN 1 ELSE 0 END) > 0 
                             THEN true 
                             ELSE false END
                    )
                    FROM Discussion d
                    LEFT JOIN DiscussionLike dl ON dl.discussion = d
                    WHERE d.id IN :discussionIds
                    GROUP BY d.id
            """)
    List<DiscussionLikeSummaryDto> findLikeSummaryByDiscussionIds(
            @Param("member") final Member member,
            @Param("discussionIds") final List<Long> discussionIds
    );

    @Query("""
                SELECT new todoktodok.backend.discussion.application.service.query.DiscussionLikeSummaryDto(
                    d.id,
                    CASE WHEN SUM(CASE WHEN dl.member = :member THEN 1 ELSE 0 END) > 0 
                     THEN true 
                     ELSE false END
                )
                FROM Discussion d
                LEFT JOIN DiscussionLike dl ON dl.discussion = d AND dl.createdAt >= :sinceDate
                WHERE d.id IN :discussionIds
                GROUP BY d.id
            """)
    List<DiscussionLikeSummaryDto> findLikeSummariesByDiscussionIdsSinceDate(
            @Param("member") final Member member,
            @Param("discussionIds") final List<Long> discussionIds,
            @Param("sinceDate") final LocalDateTime sinceDate
    );

    boolean existsByMemberAndDiscussion(final Member member, final Discussion discussion);

    @EntityGraph(value = "Discussion.withMemberAndBook", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
                SELECT dl.discussion
                FROM DiscussionLike dl
                WHERE dl.member = :member
                ORDER BY dl.discussion.id DESC
            """)
    List<Discussion> findLikedDiscussionIdsByMember(@Param("member") final Member member);
}
