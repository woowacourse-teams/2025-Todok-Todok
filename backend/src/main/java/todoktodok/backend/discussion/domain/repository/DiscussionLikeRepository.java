package todoktodok.backend.discussion.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todoktodok.backend.discussion.application.service.query.DiscussionLikeCountDto;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionLike;
import todoktodok.backend.member.domain.Member;

public interface DiscussionLikeRepository extends JpaRepository<DiscussionLike, Long> {

    Optional<DiscussionLike> findByMemberAndDiscussion(final Member member, final Discussion discussion);

    @Query("""
                SELECT COUNT(dl)
                FROM DiscussionLike dl
                WHERE dl.discussion.id = :discussionId
            """)
    Long findLikeCountsByDiscussionId(@Param("discussionId") final Long discussionId);

    @Query("""
                SELECT new todoktodok.backend.discussion.application.service.query.DiscussionLikeCountDto(d.id, COUNT(dl))
                FROM Discussion d
                LEFT JOIN DiscussionLike dl ON dl.discussion = d
                WHERE d.id IN :discussionIds
                GROUP BY d.id
            """)
    List<DiscussionLikeCountDto> findLikeCountsByDiscussionIds(@Param("discussionIds") final List<Long> discussionIds);

    @Query("""
                SELECT new todoktodok.backend.discussion.application.service.query.DiscussionLikeCountDto(d.id, COUNT(dl))
                FROM Discussion d
                LEFT JOIN DiscussionLike dl ON dl.discussion = d AND dl.createdAt >= :sinceDate
                WHERE d.id IN :discussionIds
                GROUP BY d.id
            """)
    List<DiscussionLikeCountDto> findLikeCountsByDiscussionIdsSinceDate(
            @Param("discussionIds") final List<Long> discussionIds,
            @Param("sinceDate") final LocalDateTime sinceDate
    );

    @Query("""
                SELECT dl.discussion.id
                FROM DiscussionLike dl
                WHERE dl.member = :member
                AND dl.discussion.id IN :discussionIds
            """)
    List<Long> findLikedDiscussionIdsByMember(
            @Param("member") final Member member,
            @Param("discussionIds") final List<Long> discussionIds
    );

    boolean existsByMemberAndDiscussion(final Member member, final Discussion discussion);
}
