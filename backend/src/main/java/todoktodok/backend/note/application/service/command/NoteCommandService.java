package todoktodok.backend.note.application.service.command;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.note.application.dto.request.NoteRequest;
import todoktodok.backend.note.domain.Note;
import todoktodok.backend.note.domain.repository.NoteRepository;

@Service
@Transactional
@AllArgsConstructor
public class NoteCommandService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public void createNote(
            final NoteRequest noteRequest,
            final Long memberId
    ) {
        final Note note = Note.builder()
                .snap(noteRequest.snap())
                .memo(noteRequest.memo())
//                .book()
                .member(memberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다")))
                .build();
        noteRepository.save(note);
    }
}
