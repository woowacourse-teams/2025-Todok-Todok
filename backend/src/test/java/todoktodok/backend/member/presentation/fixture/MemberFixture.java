package todoktodok.backend.member.presentation.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;
import todoktodok.backend.member.application.dto.request.LoginRequest;
import todoktodok.backend.member.domain.Member;

public class MemberFixture {

    public static String login(final String email) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email))
                .when().post("/api/v1/members/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().header("Authorization");
    }

    public static Member create(
            final String email,
            final String nickname,
            final String profileImage
    ) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    }
}
