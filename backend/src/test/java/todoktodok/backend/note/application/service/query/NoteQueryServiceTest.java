package todoktodok.backend.note.application.service.query;

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

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ContextConfiguration(initializers = InitializerTimer.class)
class NoteQueryServiceTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private NoteQueryService noteQueryService;

    @BeforeEach
    void setUp() {
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("기록 단일 조회 시 내 기록이 아니라면 예외가 발생한다")
    void validateIsMyNoteTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setUserInfo(
                "user2@gmail.com",
                "user2",
                "https://image.png",
                ""
        );
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultShelfInfo();
        databaseInitializer.setDefaultNoteInfo();

        // when - then
        assertThatThrownBy(() -> noteQueryService.getNoteById(2L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자신의 기록만 조회 가능합니다");
    }
}
