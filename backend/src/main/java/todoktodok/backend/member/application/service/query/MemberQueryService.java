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
import todoktodok.backend.discussion.application.service.query.DiscussionLikeSummaryDto;
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
        final List<Long> createdDiscussionIds = discussionRepository.findDiscussionIdsByMember(member);
        final Map<Long, Discussion> createdDiscussions = discussionRepository.findDiscussionsInIds(createdDiscussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));
        final List<Discussion> sortedCreatedDiscussions = createdDiscussionIds.stream()
                .map(createdDiscussions::get)
                .toList();

        return getDiscussionsResponses(sortedCreatedDiscussions, member);
    }

    private List<DiscussionResponse> getParticipatedDiscussions(final Member member) {
        final List<Long> participatedDiscussionIds = discussionRepository.findParticipatedDiscussionIdsByMember(member.getId());
        final Map<Long, Discussion> participatedDiscussions = discussionRepository.findDiscussionsInIds(participatedDiscussionIds).stream()
                .collect(Collectors.toMap(Discussion::getId, discussion -> discussion));
        final List<Discussion> sortedParticipatedDiscussions = participatedDiscussionIds.stream()
                .map(participatedDiscussions::get)
                .toList();

        return getDiscussionsResponses(sortedParticipatedDiscussions, member);
    }

    private List<DiscussionResponse> getDiscussionsResponses(
            final List<Discussion> discussions,
            final Member member
    ) {
        if (discussions.isEmpty()) {
            return List.of();
        }

        final List<Long> discussionIds = discussions.stream()
                .map(Discussion::getId)
                .toList();

        final Map<Long, Boolean> likesByDiscussionId = discussionLikeRepository.findLikeSummaryByDiscussionIds(member, discussionIds)
                .stream().collect(Collectors.toMap(DiscussionLikeSummaryDto::discussionId, DiscussionLikeSummaryDto::isLikedByMe));

        return makeResponsesFrom(discussions, likesByDiscussionId);
    }

    private List<DiscussionResponse> makeResponsesFrom(
            final List<Discussion> discussions,
            final Map<Long, Boolean> likeSummaryByDiscussionId
    ) {
        return discussions.stream()
                .map(discussion -> {
                    final Long discussionId = discussion.getId();
                    final boolean isLikedByMe = likeSummaryByDiscussionId.get(discussionId);
                    return new DiscussionResponse(discussion, discussion.getLikeCount(), discussion.getCommentCount(), isLikedByMe);
                })
                .toList();
    }
}
