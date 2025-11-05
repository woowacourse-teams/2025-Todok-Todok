package todoktodok.backend.notification.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import todoktodok.backend.global.exception.ErrorResponse;
import todoktodok.backend.notification.application.dto.request.NotificationTokenRequest;

@Tag(name = "알림 토큰 API")
public interface NotificationTokenApiDocs {

    @Operation(summary = "알림 토큰 생성 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "알림 토큰 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "중복된 토큰 저장",
                                            value = "{\"code\":400, \"message\":\"중복된 알림 토큰 발급 요청입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "토큰 입력 안함",
                                            value = "{\"code\":400, \"message\":\"FCM registration token을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "FID 입력 안함",
                                            value = "{\"code\":400, \"message\":\"Firebase Installation Id를 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "다른 회원의 토큰 저장",
                                            value = "{\"code\":400, \"message\":\"다른 계정에 등록된 토큰입니다\"}"
                                    )
                            }
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 인증 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "JWT 오류",
                                            value = "{\"code\":401, \"message\":\"[ERROR] 잘못된 로그인 시도입니다. 다시 시도해 주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "액세스 토큰 만료 오류",
                                            value = "{\"code\":401, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
                                    )
                            }
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 리소스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "회원 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 회원을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> createNotificationToken(
            @Parameter(hidden = true) final Long memberId,
            @RequestBody(
                    description = "생성할 알림 토큰 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationTokenRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"token\":\"token_number\", \"fid\":\"fid_number\"}"
                            )
                    )
            ) final NotificationTokenRequest notificationTokenRequest
    );
}
