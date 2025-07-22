package todoktodok.backend.discussion.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
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

        return new DiscussionResponse(findDiscussion(discussionId));
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
}
