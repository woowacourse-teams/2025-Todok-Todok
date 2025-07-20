package todoktodok.backend.discussion.application.service.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.repository.DiscussionRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.note.domain.Note;
import todoktodok.backend.note.domain.repository.NoteRepository;

@Service
@Transactional
@AllArgsConstructor
public class DiscussionCommandService {

    private final DiscussionRepository discussionRepository;
    private final MemberRepository memberRepository;
    private final NoteRepository noteRepository;

    public Long createDiscussion(
            final Long memberId,
            final DiscussionRequest discussionRequest
    ) {
        final Member member = getMember(memberId);
        final Note note = getNote(discussionRequest);

        final Discussion discussion = Discussion.builder()
                .title(discussionRequest.discussionTitle())
                .content(discussionRequest.discussionOpinion())
                .member(member)
                .book(note.getBook())
                .note(note)
                .build();

        final Discussion savedDiscussion = discussionRepository.save(discussion);
        return savedDiscussion.getId();
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다"));
    }

    private Note getNote(final DiscussionRequest discussionRequest) {
        return noteRepository.findById(discussionRequest.noteId())
                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다"));
    }
}
