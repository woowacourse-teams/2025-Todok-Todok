package todoktodok.backend.discussion.application.service.query;

import jakarta.annotation.Nullable;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.PageInfo;
import todoktodok.backend.discussion.application.dto.response.SlicedDiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionLikeRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DiscussionQueryService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int MIN_PAGE_SIZE = 1;

    private final DiscussionRepository discussionRepository;
    private final DiscussionLikeRepository discussionLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        final Member member = findMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        final int likeCount = Math.toIntExact(discussionLikeRepository.findLikeCountsByDiscussionId(discussionId));
        final int commentCount = Math.toIntExact(
                commentRepository.countCommentsByDiscussionId(discussionId)
                        + replyRepository.countRepliesByDiscussionId(discussionId)
        );
        final boolean isLikedByMe = discussionLikeRepository.existsByMemberAndDiscussion(member, discussion);

        return new DiscussionResponse(
                discussion,
                likeCount,
                commentCount,
                isLikedByMe
        );
    }

    public SlicedDiscussionResponse getDiscussions(
            final Long memberId,
            final int size,
            @Nullable final String cursor
    ) {
        validatePageSize(size);
        final Member member = findMember(memberId);

        final Slice<Discussion> slicedDiscussions = sliceDiscussionsBy(cursor, size);

        final List<Discussion> discussions = slicedDiscussions.getContent();
        final boolean hasNextPage = slicedDiscussions.hasNext();
        final String nextCursor = findNextCursor(hasNextPage, discussions);

        return new SlicedDiscussionResponse(
                getDiscussionsResponses(discussions, member),
                new PageInfo(hasNextPage, nextCursor)
        );
    }

    public List<DiscussionResponse> getDiscussionsByKeyword(
            final Long memberId,
            final String keyword
    ) {
        final Member member = findMember(memberId);

        if (isKeywordBlank(keyword)) {
            return getAllDiscussions(member);
        }

        return getDiscussionsByKeyword(keyword, member);
    }

    private boolean isKeywordBlank(final String keyword) {
        return keyword == null || keyword.isBlank();
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 토론방을 찾을 수 없습니다: discussionId= %s", discussionId)
                        )
                );
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException(
                                String.format("해당 회원을 찾을 수 없습니다: memberId= %s", memberId)
                        )
                );
    }

    private Slice<Discussion> sliceDiscussionsBy(
            final String cursor,
            final int size
    ) {
        final Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "id");

        if (cursor == null || cursor.isBlank()) {
            return discussionRepository.findAllBy(pageable);
        }

        final Long cursorId = decodeCursor(cursor);
        return discussionRepository.findByIdLessThan(cursorId, pageable);
    }

    private String findNextCursor(
            final boolean hasNextPage,
            final List<Discussion> discussions
    ) {
        if (!hasNextPage || discussions.isEmpty()) {
            return null;
        }
        return encodeCursorId(discussions.getLast().getId());
    }

    private Long decodeCursor(final String cursor) {
        try {
            if (cursor == null || cursor.isBlank()) {
                return null;
            }

            final String decoded = new String(Base64.getUrlDecoder().decode(cursor));
            return Long.valueOf(decoded);
        } catch (final Exception e) {
            throw new IllegalArgumentException(String.format("Base64로 디코드할 수 없는 cursor 값입니다: cursor = %s", cursor));
        }
    }

    public String encodeCursorId(final Long id) {
        return Base64.getUrlEncoder().encodeToString(id.toString().getBytes());
    }

    private List<DiscussionResponse> getAllDiscussions(final Member member) {
        final List<Discussion> discussions = discussionRepository.findAll();
        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getDiscussionsByKeyword(
            final String keyword,
            final Member member
    ) {
        final List<Discussion> discussions = discussionRepository.searchByKeyword(keyword);

        return getDiscussionsResponses(discussions, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Discussion> discussions,
            final Member member
    ) {
        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        final List<DiscussionLikeCountDto> likeCountsById = discussionLikeRepository.findLikeCountsByDiscussionIds(
                discussionIds);
        final List<DiscussionCommentCountDto> commentCountsById = commentRepository.findCommentCountsByDiscussionIds(
                discussionIds);
        final List<Long> likedDiscussionIds = discussionLikeRepository.findLikedDiscussionIdsByMember(member,
                discussionIds);

        return discussions.stream()
                .map(discussion -> new DiscussionResponse(
                        discussion,
                        findLikeCount(discussion, likeCountsById),
                        findCommentCount(discussion, commentCountsById),
                        checkIsLikedByMe(discussion, likedDiscussionIds)
                ))
                .toList();
    }

    private int findCommentCount(
            final Discussion discussion,
            final List<DiscussionCommentCountDto> commentCountsById
    ) {
        return commentCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(dto -> dto.commentCount() + dto.replyCount())
                .orElseThrow(() -> new IllegalStateException(
                                String.format("토론방의 댓글 수를 찾을 수 없습니다: discussionId= %s", discussion.getId())
                        )
                );
    }

    private int findLikeCount(
            final Discussion discussion,
            final List<DiscussionLikeCountDto> likeCountsById
    ) {
        return likeCountsById.stream()
                .filter(count -> discussion.isSameId(count.discussionId()))
                .findFirst()
                .map(DiscussionLikeCountDto::likeCount)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("토론방의 좋아요 수를 찾을 수 없습니다: discussionId= %s", discussion.getId()))
                );
    }

    private boolean checkIsLikedByMe(
            final Discussion discussion,
            final List<Long> likedDiscussionIds
    ) {
        return likedDiscussionIds.contains(discussion.getId());
    }

    private void validatePageSize(final int size) {
        if (size < MIN_PAGE_SIZE || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(String.format("유효하지 않은 페이지 사이즈입니다. 1 이상 50 이하의 페이징을 시도해주세요: size = %d", size));
        }
    }
}
