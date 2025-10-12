package todoktodok.backend.book.presentation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.nullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.member.presentation.fixture.MemberFixture;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = InitializerTimer.class)
public class BookControllerTest {

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseInitializer.clear();
    }

    @Nested
    @DisplayName("도서 검색 테스트")
    class SearchTest {

        @Test
        @DisplayName("검색어로 도서를 검색한다")
        void searchTest() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "오브젝트";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("keyword", keyword)
                    .when().get("/api/v1/books/search")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThanOrEqualTo(1));
        }

        @Test
        @DisplayName("검색어가 1자 미만이면 예외가 발생한다")
        void searchTestFailUnder1Char() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("keyword", keyword)
                    .when().get("/api/v1/books/search")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("검색어 파라미터가 없으면 예외가 발생한다")
        void searchTestFailEmptyParam() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .when().get("/api/v1/books/search")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("도서 검색 테스트 - 페이지네이션 적용")
    class SearchByPagingTest {

        @Test
        @DisplayName("검색어로 도서를 검색한다 - 첫 페이지 조회")
        void searchByPagingTest_firstPage() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "클린";

            final String cursorMeaningTwo = "Mg==";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("size", 10)
                    .param("keyword", keyword)
                    .when().get("/api/v1/books/searchByPaging")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("items.size()", equalTo(10))
                    .body("pageInfo.hasNext", is(true))
                    .body("pageInfo.nextCursor", is(cursorMeaningTwo))
                    .body("totalSize", lessThanOrEqualTo(200));
        }

        @Test
        @DisplayName("검색어로 도서를 검색한다 - 두 번째 페이지 조회")
        void searchByPagingTest_secondPage() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "클린";

            final String cursorMeaningTwo = "Mg==";
            final String cursorMeaningThree = "Mw==";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("size", 10)
                    .param("keyword", keyword)
                    .param("cursor", cursorMeaningTwo)
                    .when().get("/api/v1/books/searchByPaging")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("items.size()", equalTo(10))
                    .body("pageInfo.hasNext", is(true))
                    .body("pageInfo.nextCursor", is(cursorMeaningThree))
                    .body("totalSize", lessThanOrEqualTo(200));
        }

        @Test
        @DisplayName("검색어로 도서를 검색한다 - 마지막 페이지 조회(여섯번째가 마지막일 때)")
        void searchByPagingTest_lastPage() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "클린";

            final String cursorMeaningSix = "Ng==";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("size", 10)
                    .param("keyword", keyword)
                    .param("cursor", cursorMeaningSix)
                    .when().get("/api/v1/books/searchByPaging")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("items.size()", greaterThanOrEqualTo(1))
                    .body("pageInfo.hasNext", is(false))
                    .body("pageInfo.nextCursor", nullValue())
                    .body("totalSize", lessThanOrEqualTo(200));
        }

        @Test
        @DisplayName("검색어로 도서를 검색한다 - 마지막 페이지 조회(최대 20번째)")
        void searchByPagingTest_lastTwentyPage() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "자바";

            final String cursorMeaningTwenty = "MjA=";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("size", 10)
                    .param("keyword", keyword)
                    .param("cursor", cursorMeaningTwenty)
                    .when().get("/api/v1/books/searchByPaging")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("items.size()", greaterThanOrEqualTo(1))
                    .body("pageInfo.nextCursor", nullValue());
        }

        @Test
        @DisplayName("검색어로 도서를 검색한다 - cursor가 20보다 클 때 첫 페이지 응답")
        void searchByPagingTest_upperTwentyPage() {
            // given
            databaseInitializer.setDefaultUserInfo();
            databaseInitializer.setDefaultBookInfo();

            final String token = MemberFixture.getTestAccessToken("user@gmail.com");
            final String keyword = "자바";

            final String cursorMeaningTwentyOne = "MjE=";
            final String cursorMeaningTwo = "Mg==";

            // when - then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .header("Authorization", token)
                    .param("size", 10)
                    .param("keyword", keyword)
                    .param("cursor", cursorMeaningTwentyOne)
                    .when().get("/api/v1/books/searchByPaging")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("items.size()", greaterThanOrEqualTo(1))
                    .body("pageInfo.hasNext", is(true))
                    .body("pageInfo.nextCursor", is(cursorMeaningTwo));
        }
    }

    @Test
    @DisplayName("도서를 생성한다")
    void createBook() {
        // given
        databaseInitializer.setDefaultUserInfo();

        final String token = MemberFixture.getTestAccessToken("user@gmail.com");

        final BookRequest bookRequest = new BookRequest(
                "9791158391409",
                "오브젝트",
                "조영호",
                "image.png"
        );

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(bookRequest)
                .when().post("/api/v1/books")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
