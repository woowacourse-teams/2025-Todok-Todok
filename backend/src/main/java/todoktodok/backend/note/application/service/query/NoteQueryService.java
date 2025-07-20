package todoktodok.backend.note.application.service.query;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.note.application.dto.response.MyNoteResponse;
import todoktodok.backend.note.domain.repository.NoteRepository;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NoteQueryService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public List<MyNoteResponse> getMyNotes(final Long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다"));

        return noteRepository.findNotesByMember(member).stream()
                .map(MyNoteResponse::new)
                .toList();
    }
}
