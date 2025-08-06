package todoktodok.backend.member.application.service.query;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.comment.domain.repository.CommentRepository;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.application.dto.response.ProfileResponse;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.reply.domain.repository.ReplyRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public ProfileResponse getProfile(final Long memberId) {
        final Member member = findMember(memberId);

        return new ProfileResponse(member);
    }

    public List<BookResponse> getActiveBooks(final Long memberId) {
        final Member member = findMember(memberId);

        final List<Book> activeBooksFromDiscussion = discussionRepository.findBooksByMember(member);
        final List<Book> activeBooksFromComment = commentRepository.findBooksByMember(member);
        final List<Book> activeBooksFromReply = replyRepository.findBooksByMember(member);

        return Stream.of(
                        activeBooksFromDiscussion,
                        activeBooksFromComment,
                        activeBooksFromReply
                ).flatMap(List::stream)
                .distinct()
                .map(BookResponse::new)
                .toList();
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }
}
