package todoktodok.backend.note.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoktodok.backend.member.domain.Member;

class NoteTest {

    @Test
    @DisplayName("기록 생성 시 스냅과 메모가 모두 입력되지 않으면 예외가 발생한다.")
    void validateBlankSnapAndMemoTest() {
        //given
        Member member = Member.builder()
                .email("user@gmail.com")
                .nickname("user")
                .profileImage("https://image.png")
                .build();

        //when - then
        assertThatThrownBy(() -> Note.builder()
                .snap("")
                .memo("")
                .member(member)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("스냅과 메모 중 최소 하나를 입력해주세요");
    }
}
