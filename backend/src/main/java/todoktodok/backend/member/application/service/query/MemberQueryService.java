package todoktodok.backend.member.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.application.dto.response.BlockMemberResponse;
import todoktodok.backend.member.application.dto.response.MemberDiscussionResponse;
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

    public List<MemberDiscussionResponse> getMemberDiscussionsByType(
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
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 회원을 찾을 수 없습니다: %d", memberId)));
    }

    private List<MemberDiscussionResponse> getCreatedDiscussions(final Member member) {
        return discussionRepository.findDiscussionsByMember(member).stream()
                .map(MemberDiscussionResponse::new)
                .toList();
    }

    private List<MemberDiscussionResponse> getParticipatedDiscussions(final Member member) {
        return discussionRepository.findParticipatedDiscussionsByMember(member.getId()).stream()
                .map(MemberDiscussionResponse::new)
                .toList();
    }
}
