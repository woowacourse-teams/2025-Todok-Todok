package todoktodok.backend.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import todoktodok.backend.global.exception.ErrorResponse;
import todoktodok.backend.member.application.dto.request.BypassLoginRequest;
import todoktodok.backend.member.application.dto.response.RefreshTokenResponse;

@Tag(name = "관리자 API")
public interface AdminApiDocs {

    @Operation(summary = "[관리자 전용] 로그인 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "회원일 때 - 리프레시 토큰 존재",
                                            value = "{\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3 NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk\"}"
                                    ),
                                    @ExampleObject(
                                            name = "임시회원일 때",
                                            value = "{\"refreshToken\":null}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "이메일 형식 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 올바른 이메일 형식을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "리프레시 토큰 중복 발급 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 중복된 리프레시 토큰 발급 요청입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "관리자가 아닌 유저의 로그인 시도 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 올바르지 않은 로그인입니다. /login으로 로그인 해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 비밀번호 입력 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 비밀번호가 올바르지 않습니다\"}"
                                    )
                            }
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 오류",
                                    value = "{\"code\":500, \"message\":\"[ERROR] 서버 내부 오류가 발생했습니다\"}"
                            )
                    ))
    })
    ResponseEntity<RefreshTokenResponse> adminLogin(
            @RequestBody(
                    description = "로그인 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BypassLoginRequest.class),
                            examples = @ExampleObject(value = "{\"email\":\"user@example.com\", \"password\":\"password\"}")
                    )
            ) final BypassLoginRequest loginRequest
    );
}
