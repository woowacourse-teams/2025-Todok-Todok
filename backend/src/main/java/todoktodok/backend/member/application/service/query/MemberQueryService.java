package todoktodok.backend.member.application.service.query;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.service.query.DiscussionCommentCountDto;
import todoktodok.backend.discussion.application.service.query.DiscussionLikeSummaryDto;
import todoktodok.backend.discussion.application.service.query.LikeCountAndIsLikedByMeDto;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionLikeRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.application.dto.response.BlockMemberResponse;
import todoktodok.backend.member.application.dto.response.ProfileResponse;
import todoktodok.backend.member.domain.Block;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.MemberDiscussionFilterType;
import todoktodok.backend.member.domain.repository.BlockRepository;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final DiscussionRepository discussionRepository;
    private final BlockRepository blockRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final CommentRepository commentRepository;

    public ProfileResponse getProfile(final Long memberId) {
        final Member member = findMember(memberId);

        return new ProfileResponse(member);
    }

    public List<BookResponse> getActiveBooks(final Long memberId) {
        final Member member = findMember(memberId);

        final List<Book> activeBooks = bookRepository.findActiveBooksByMember(member);

        return activeBooks.stream()
                .distinct()
                .map(BookResponse::new)
                .toList();
    }

    public List<DiscussionResponse> getMemberDiscussionsByType(
            final Long memberId,
            final MemberDiscussionFilterType type
    ) {
        final Member member = findMember(memberId);

        if (type.isTypeCreated()) {
            return getCreatedDiscussions(member);
        }

        return getParticipatedDiscussions(member);
    }

    public List<BlockMemberResponse> getBlockMembers(final Long memberId) {
        final Member member = findMember(memberId);

        final List<Block> blockMembers = blockRepository.findBlocksByMember(member);

        return blockMembers.stream()
                .map(BlockMemberResponse::new)
                .toList();
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: memberId = %s", memberId)));
    }

    private List<DiscussionResponse> getCreatedDiscussions(final Member member) {
        final List<Long> createdDiscussionIds = discussionRepository.findIdsByMember(member);

        return getDiscussionsResponses(createdDiscussionIds, member);
    }

    private List<DiscussionResponse> getParticipatedDiscussions(final Member member) {
        final List<Long> participatedDiscussionIds = discussionRepository.findParticipatedDiscussionIdsByMember(member.getId());

        return getDiscussionsResponses(participatedDiscussionIds, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Long> discussionIds,
            final Member member
    ) {
        if (discussionIds.isEmpty()) {
            return List.of();
        }

        final List<DiscussionLikeSummaryDto> likeSummaries = discussionLikeRepository.findLikeSummaryByDiscussionIds(
                member, discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);
        final Map<Long, LikeCountAndIsLikedByMeDto> likesByDiscussionId = mapLikeSummariesByDiscussionId(likeSummaries);
        final Map<Long, Integer> commentsByDiscussionId = mapTotalCommentCountsByDiscussionId(commentCounts);

        return makeResponsesFrom(discussionIds, likesByDiscussionId, commentsByDiscussionId);
    }

    private Map<Long, Integer> mapTotalCommentCountsByDiscussionId(final List<DiscussionCommentCountDto> commentCounts) {
        return commentCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionCommentCountDto::discussionId,
                        dto -> dto.commentCount() + dto.replyCount()
                ));
    }

    private Map<Long, LikeCountAndIsLikedByMeDto> mapLikeSummariesByDiscussionId(final List<DiscussionLikeSummaryDto> likeCounts) {
        return likeCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionLikeSummaryDto::discussionId,
                        discussionLikeSummaryDto ->
                                new LikeCountAndIsLikedByMeDto(
                                        discussionLikeSummaryDto.likeCount(),
                                        discussionLikeSummaryDto.isLikedByMe()
                                )
                ));
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Long> discussionIds,
            final Map<Long, LikeCountAndIsLikedByMeDto> likeSummaryByDiscussionId,
            final Map<Long, Integer> commentCountsByDiscussionId
    ) {
        final Map<Long, Discussion> discussions = discussionRepository.findDiscussionsInIds(discussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));

        return discussionIds.stream()
                .map(discussionId -> {
                    final Discussion discussion = discussions.get(discussionId);
                    final int likeCount = likeSummaryByDiscussionId.get(discussionId).likeCount();
                    final int commentCount = commentCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final boolean isLikedByMe = likeSummaryByDiscussionId.get(discussionId).isLikedByMe();
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();
    }
}
