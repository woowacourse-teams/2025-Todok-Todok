package todoktodok.backend.notification.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import todoktodok.backend.global.exception.ErrorResponse;
import todoktodok.backend.notification.application.dto.response.NotificationResponse;
import todoktodok.backend.notification.application.dto.response.UnreadNotificationResponse;

@Tag(name = "알림 API")
public interface NotificationApiDocs {

    @Operation(summary = "알림 목록 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "알림 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                              {
                                                "unreadCount": 10,
                                                "notifications": [
                                                  {
                                                    "data": {
                                                      "notificationId": 1,
                                                      "discussionId": 1,
                                                      "commentId": 1,
                                                      "replyId": null,
                                                      "memberNickname": "모찌",
                                                      "discussionTitle": "캡슐화 왜 함?",
                                                      "content": "모찌가 적은 댓글 내용입니다.",
                                                      "type": "COMMENT",
                                                      "target": "COMMENT"
                                                    },
                                                    "isRead": false,
                                                    "createdAt": "2025-09-19T12:00:00"
                                                  },
                                                  {
                                                    "data": {
                                                      "notificationId": 2,
                                                      "discussionId": 1,
                                                      "commentId": 1,
                                                      "replyId": 1,
                                                      "memberNickname": "모찌",
                                                      "discussionTitle": "캡슐화 왜 함?",
                                                      "content": "모찌가 적은 대댓글 내용입니다.",
                                                      "type": "REPLY",
                                                      "target": "REPLY"
                                                    },
                                                    "isRead": true,
                                                    "createdAt": "2025-09-18T12:00:00"
                                                  },
                                                  {
                                                    "data": {
                                                      "notificationId": 2,
                                                      "discussionId": 1,
                                                      "commentId": null,
                                                      "replyId": null,
                                                      "memberNickname": "모찌",
                                                      "discussionTitle": "캡슐화 왜 함?",
                                                      "content": "",
                                                      "type": "LIKE",
                                                      "target": "DISCUSSION"
                                                    },
                                                    "isRead": true,
                                                    "createdAt": "2025-09-17T12:00:00"
                                                  },
                                                  {
                                                    "data": {
                                                      "notificationId": 7,
                                                      "discussionId": 1,
                                                      "commentId": 1,
                                                      "replyId": null,
                                                      "memberNickname": "모찌",
                                                      "discussionTitle": "캡슐화 왜 함?",
                                                      "content": "",
                                                      "type": "LIKE",
                                                      "target": "COMMENT"
                                                    },
                                                    "isRead": false,
                                                    "createdAt": "2025-09-16T12:00:00"
                                                  },
                                                  {
                                                    "data": {
                                                      "notificationId": 8,
                                                      "discussionId": 1,
                                                      "commentId": 1,
                                                      "replyId": 1,
                                                      "memberNickname": "모찌",
                                                      "discussionTitle": "캡슐화 왜 함?",
                                                      "content": "",
                                                      "type": "LIKE",
                                                      "target": "REPLY"
                                                    },
                                                    "isRead": false,
                                                    "createdAt": "2025-09-15T12:00:00"
                                                  }
                                                ]
                                              }
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 인증 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "JWT 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 잘못된 로그인 시도입니다. 다시 시도해 주세요\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "액세스 토큰 만료 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "액세스 토큰 만료 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 리소스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "회원 없음",
                                    value = "{\"code\":404, \"message\":\"[ERROR] 해당 회원을 찾을 수 없습니다\"}"
                            )
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
    ResponseEntity<NotificationResponse> getNotifications(
            @Parameter(hidden = true) final Long memberId
    );

    @Operation(summary = "알림 읽음 처리 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "알림 읽음 처리 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 인증 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "JWT 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 잘못된 로그인 시도입니다. 다시 시도해 주세요\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "액세스 토큰 만료 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "액세스 토큰 만료 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 리소스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "회원 없음",
                                    value = "{\"code\":404, \"message\":\"[ERROR] 해당 회원을 찾을 수 없습니다\"}"
                            )
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
    ResponseEntity<Void> readNotification(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "알림 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long notificationId
    );

    @Operation(summary = "알림 삭제 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "알림 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 인증 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "JWT 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 잘못된 로그인 시도입니다. 다시 시도해 주세요\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "액세스 토큰 만료 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "액세스 토큰 만료 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "403",
                    description = "본인 알림이 아닌 경우 접근 거부",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "알림 접근 거부",
                                    value = "{\"code\":403, \"message\":\"[ERROR] 본인 알림이 아닙니다\"}"
                            )
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
    ResponseEntity<Void> deleteNotification(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "알림 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long notificationId
    );

    @Operation(summary = "안 읽은 알림 유무 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "안 읽은 알림 유무 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UnreadNotificationResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"exist\":true}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 인증 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "JWT 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 잘못된 로그인 시도입니다. 다시 시도해 주세요\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "액세스 토큰 만료 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "액세스 토큰 만료 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
                            )
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
    ResponseEntity<UnreadNotificationResponse> hasUnreadNotifications(
            @Parameter(hidden = true) final Long memberId
    );
}
