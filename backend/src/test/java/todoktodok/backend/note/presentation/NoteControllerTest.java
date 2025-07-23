package todoktodok.backend.note.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import todoktodok.backend.DatabaseInitializer;
import todoktodok.backend.InitializerTimer;
import todoktodok.backend.member.presentation.fixture.MemberFixture;
import todoktodok.backend.note.application.dto.request.NoteRequest;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
class NoteControllerTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseInitializer.clear();
    }

    @Test
    @DisplayName("기록을 생성한다")
    void createNoteTest() {
        //given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultShelfInfo();

        final NoteRequest noteRequest = new NoteRequest(
                1L,
                "DI와 IoC는 스프링의 중요한 개념이다.",
                "Spring의 동작 원리를 이해하는 데 큰 도움이 됐다."
        );
        final String token = MemberFixture.login("user@gmail.com");

        //when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(noteRequest)
                .when().post("/api/v1/notes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("내 기록들을 조회한다")
    void readMyNotesTest() {
        //given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultShelfInfo();
        databaseInitializer.setDefaultNoteInfo();

        final String token = MemberFixture.login("user@gmail.com");

        //when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/notes/mine")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
