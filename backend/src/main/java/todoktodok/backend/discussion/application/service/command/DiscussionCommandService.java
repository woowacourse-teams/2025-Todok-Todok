package todoktodok.backend.discussion.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.domain.Discussion;
import todoktodok.backend.discussion.domain.DiscussionReport;
import todoktodok.backend.discussion.domain.repository.DiscussionReportRepository;
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
    private final DiscussionReportRepository discussionReportRepository;

    public Long createDiscussion(
            final Long memberId,
            final DiscussionRequest discussionRequest
    ) {
        final Member member = findMember(memberId);
        final Note note = findNote(discussionRequest);

        validateNoteOwner(note, member);

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

    public void report(
            final Long memberId,
            final Long discussionId
    ) {
        final Discussion discussion = findDiscussion(discussionId);
        final Member member = findMember(memberId);

        validateDuplicatedReport(discussion, member);
        validateSelfReport(discussion, member);

        final DiscussionReport discussionReport = DiscussionReport.builder()
                .discussion(discussion)
                .member(member)
                .build();

        discussionReportRepository.save(discussionReport);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원을 찾을 수 없습니다"));
    }

    private Note findNote(final DiscussionRequest discussionRequest) {
        return noteRepository.findById(discussionRequest.noteId())
                .orElseThrow(() -> new NoSuchElementException("해당 기록을 찾을 수 없습니다"));
    }

    private Discussion findDiscussion(final Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new NoSuchElementException("해당 토론방을 찾을 수 없습니다"));
    }

    private void validateDuplicatedReport(
            final Discussion discussion,
            final Member member
    ) {
        if (discussionReportRepository.existsByDiscussionAndMember(discussion, member)) {
            throw new IllegalArgumentException("이미 신고한 토론방입니다");
        }
    }

    private void validateSelfReport(
            final Discussion discussion,
            final Member member
    ) {
        if (discussion.isOwnedBy(member)) {
            throw new IllegalArgumentException("자기 자신의 토론방을 신고할 수 없습니다");
        }
    }

    private void validateNoteOwner(
            final Note note,
            final Member member
    ) {
        if (!note.isOwnedBy(member)) {
            throw new IllegalArgumentException("해당 기록의 소유자만 토론방을 생성할 수 있습니다");
        }
    }
}
