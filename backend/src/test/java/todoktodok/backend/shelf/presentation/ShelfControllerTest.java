package todoktodok.backend.shelf.presentation;

import static org.hamcrest.Matchers.is;

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

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
public class ShelfControllerTest {

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
    @DisplayName("내 서재의 도서 전체를 조회한다")
    void getMyBooksTest() {
        // given
        databaseInitializer.setDefaultUserInfo();
        databaseInitializer.setDefaultBookInfo();
        databaseInitializer.setDefaultShelfInfo();

        final String token = MemberFixture.login("user@gmail.com");

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .when().get("/api/v1/shelves")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }
}
