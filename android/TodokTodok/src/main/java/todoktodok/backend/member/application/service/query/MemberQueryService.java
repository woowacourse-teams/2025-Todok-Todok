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
import todoktodok.backend.discussion.application.service.query.DiscussionLikeCountDto;
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
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: memberId = %s", memberId)));
    }

    private List<DiscussionResponse> getCreatedDiscussions(final Member member) {
        final List<Discussion> createdDiscussions = discussionRepository.findDiscussionsByMember(member);

        return getDiscussionsResponses(createdDiscussions, member);
    }

    private List<DiscussionResponse> getParticipatedDiscussions(final Member member) {
        final List<Discussion> participatedDiscussions = discussionRepository.findParticipatedDiscussionsByMember(member.getId());

        return getDiscussionsResponses(participatedDiscussions, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Discussion> discussions,
            final Member member
    ) {
        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        if (discussionIds.isEmpty()) {
            return List.of();
        }

        final List<DiscussionLikeCountDto> likeCounts = discussionLikeRepository.findLikeCountsByDiscussionIds(
                discussionIds);
        final List<DiscussionCommentCountDto> commentCounts = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);
        final Map<Long, Integer> likesByDiscussionId = getLikeCountsByDiscussionId(likeCounts);
        final Map<Long, Integer> commentsByDiscussionId = getCommentCountsByDiscussionId(commentCounts);
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member, discussionIds);

        return makeResponsesFrom(discussions, likesByDiscussionId, commentsByDiscussionId, likedDiscussionIds);
    }

    private Map<Long, Integer> getLikeCountsByDiscussionId(final List<DiscussionLikeCountDto> likeCounts) {
        return likeCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionLikeCountDto::discussionId,
                        DiscussionLikeCountDto::likeCount
                ));
    }

    private Map<Long, Integer> getCommentCountsByDiscussionId(final List<DiscussionCommentCountDto> commentCounts) {
        return commentCounts.stream()
                .collect(Collectors.toMap(
                        DiscussionCommentCountDto::discussionId,
                        dto -> dto.commentCount() + dto.replyCount()
                ));
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Discussion> discussions,
            final Map<Long, Integer> likeCountsByDiscussionId,
            final Map<Long, Integer> commentCountsByDiscussionId,
            final List<Long> likedDiscussionIds
    ) {
        return discussions.stream()
                .map(discussion -> {
                    final long discussionId = discussion.getId();
                    final int likeCount = likeCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final int commentCount = commentCountsByDiscussionId.getOrDefault(discussionId, 0);
                    final boolean isLikedByMe = likedDiscussionIds.contains(discussionId);
                    return new DiscussionResponse(discussion, likeCount, commentCount, isLikedByMe);
                })
                .toList();
    }
}
