package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionFilterType;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DiscussionQueryService {

    private final DiscussionRepository discussionRepository;
    private final MemberRepository memberRepository;

    public List<DiscussionResponse> getDiscussions(final Long memberId) {
        validateMember(memberId);

        final List<Discussion> discussions = discussionRepository.findAll();

        return discussions.stream()
                .map(DiscussionResponse::new)
                .toList();
    }

    public DiscussionResponse getDiscussion(
            final Long memberId,
            final Long discussionId
    ) {
        validateMember(memberId);
        final Discussion discussion = findDiscussion(discussionId);

        return new DiscussionResponse(discussion);
    }

    public List<DiscussionResponse> getDiscussionsByKeywordAndType(
            final Long memberId,
            final String keyword,
            final DiscussionFilterType type
    ) {
        Member member = findMember(memberId);
        if (keyword == null || keyword.isBlank()) {
            if (type.isTypeMine()) {
                return getMyDiscussions(member);
            }
            return getDiscussions(memberId);
        }
        if (type.isTypeMine()) {
            return getMyDiscussionsByKeyword(keyword, member);
        }
        return getDiscussionsByKeyword(keyword, type);
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
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));
    }

    private List<DiscussionResponse> getMyDiscussions(final Member member) {
        return discussionRepository.findDiscussionsByMember(member).stream()
                .map(DiscussionResponse::new)
                .toList();
    }

    private List<DiscussionResponse> getMyDiscussionsByKeyword(
            final String keyword,
            final Member member
    ) {
        return discussionRepository.searchByKeywordAndMember(keyword, member).stream()
                .filter(discussion -> discussion.isOwnedBy(member))
                .map(DiscussionResponse::new)
                .toList();
    }

    private List<DiscussionResponse> getDiscussionsByKeyword(
            final String keyword,
            final DiscussionFilterType type
    ) {
        return discussionRepository.searchByKeyword(keyword).stream()
                .map(DiscussionResponse::new)
                .toList();
    }
}
