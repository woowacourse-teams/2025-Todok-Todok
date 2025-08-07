package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionFilterType;
import todoktodok.backend.discussion.domain.repository.DiscussionLikeRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DiscussionQueryService {

    private final DiscussionRepository discussionRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public List<DiscussionResponse> getDiscussions(final Long memberId) {
        validateMember(memberId);

        final List<Discussion> discussions = discussionRepository.findAll();
        return getDiscussionsResponses(discussions);
    }

    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        validateMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final int likeCount = Math.toIntExact(discussionLikeRepository.findLikeCountsByDiscussionId(discussionId));
        final int commentCount = Math.toIntExact(commentRepository.findCommentCountsByDiscussionId(discussionId));

        return new DiscussionResponse(
                discussion,
                likeCount,
                commentCount
        );
    }

    public List<DiscussionResponse> getDiscussionsByKeywordAndType(
            final Long memberId,
            final String keyword,
            final DiscussionFilterType type
    ) {
        final Member member = findMember(memberId);

        if (isKeywordBlank(keyword)) {
            return getDiscussionsByType(memberId, type, member);
        }

        if (type.isTypeMine()) {
            return getMyDiscussionsByKeyword(keyword, member);
        }

        return getDiscussionsByKeyword(keyword);
    }

    private List<DiscussionResponse> getDiscussionsResponses(final List<Discussion> discussions) {
        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        final List<DiscussionLikeCountDto> likeCountsById = discussionLikeRepository.findLikeCountsByDiscussionIds(
                discussionIds);
        final List<DiscussionCommentCountDto> commentCountsById = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);

        return discussions.stream()
                .map(discussion -> new DiscussionResponse(
                        discussion,
                        findLikeCount(discussion, likeCountsById),
                        findCommentCount(discussion, commentCountsById)
                ))
                .toList();
    }

    private boolean isKeywordBlank(String keyword) {
        return keyword == null || keyword.isBlank();
    }

    private void validateMember(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NoSuchElementException("해당 회원을 찾을 수 없습니다");
        }
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }

    private List<DiscussionResponse> getDiscussionsByType(Long memberId, DiscussionFilterType type, Member member) {
        if (type.isTypeMine()) {
            return getMyDiscussions(member);
        }
        return getDiscussions(memberId);
    }

    private List<DiscussionResponse> getMyDiscussions(final Member member) {
        final List<Discussion> discussions = discussionRepository.findDiscussionsByMember(member);

        return getDiscussionsResponses(discussions);
    }

    private List<DiscussionResponse> getMyDiscussionsByKeyword(
            final String keyword,
            final Member member
    ) {
        final List<Discussion> discussions = discussionRepository.searchByKeywordAndMember(keyword, member).stream()
                .filter(discussion -> discussion.isOwnedBy(member))
                .toList();

        return getDiscussionsResponses(discussions);
    }

    private List<DiscussionResponse> getDiscussionsByKeyword(final String keyword) {
        final List<Discussion> discussions = discussionRepository.searchByKeyword(keyword);

        return getDiscussionsResponses(discussions);
    }

    private int findCommentCount(
            final Discussion discussion,
            final List<DiscussionCommentCountDto> commentCountsById
    ) {
        return commentCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(DiscussionCommentCountDto::commentCount)
                .orElseThrow(() -> new IllegalStateException("토론방의 댓글 수를 찾을 수 없습니다"));
    }

    private int findLikeCount(
            final Discussion discussion,
            final List<DiscussionLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(DiscussionLikeCountDto::likeCount)
                .orElseThrow(() -> new IllegalStateException("댓글의 좋아요 수를 찾을 수 없습니다"));
    }
}
