package todoktodok.backend.note.application.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.note.application.dto.request.NoteRequest;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class NoteCommandServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private NoteCommandService noteCommandService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("서재에 없는 도서에 대한 기록을 생성할 경우 예외가 발생한다")
    void createNoteTest_fail() {
        //given
        databaseInitializer.setUserInfo();
        databaseInitializer.setBookInfo();
        NoteRequest noteRequest = new NoteRequest(
                1L,
                "DI와 IoC는 스프링의 중요한 개념이다.",
                "Spring의 동작 원리를 이해하는 데 큰 도움이 됐다."
        );

        //when - then
        assertThatThrownBy(() -> noteCommandService.createNote(1L, noteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("서재 등록한 도서만 기록 가능합니다");
    }
}
