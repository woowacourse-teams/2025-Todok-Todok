package todoktodok.backend.comment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import todoktodok.backend.comment.application.dto.request.CommentReportRequest;
import todoktodok.backend.comment.application.dto.request.CommentRequest;
import todoktodok.backend.comment.application.dto.response.CommentResponse;
import todoktodok.backend.global.exception.ErrorResponse;

@Tag(name = "댓글 API")
public interface CommentApiDocs {

    @Operation(summary = "댓글 생성 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "내용 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 댓글 내용을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "내용 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 댓글 내용은 1자 이상, 2000자 이하여야 합니다\"}"
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> createComment(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @RequestBody(
                    description = "생성할 댓글 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentRequest.class),
                            examples = @ExampleObject(value = "{\"content\":\"댓글 내용입니다.\"}")
                    )
            ) final CommentRequest commentRequest
    );

    @Operation(summary = "댓글 좋아요 토글 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "좋아요 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "좋아요 취소 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "토론방과 댓글 불일치",
                                    value = "{\"code\":400, \"message\":\"[ERROR] 해당 토론방의 댓글이 아닙니다\"}"
                            )
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 댓글을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> toggleLike(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @Parameter(
                    description = "댓글 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long commentId
    );

    @Operation(summary = "댓글 신고 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 신고 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "자신댓글 신고",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신의 댓글은 신고할 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "중복 신고",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 신고한 댓글입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "토론방과 댓글 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 해당 토론방의 댓글이 아닙니다\"}"
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 댓글을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> report(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @Parameter(
                    description = "댓글 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long commentId,
            @RequestBody(
                    description = "댓글 신고 사유",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentReportRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"reason\":\"부적절한 내용\"}"
                            )
                    )
            ) final CommentReportRequest commentReportRequest
    );

    @Operation(summary = "토론방별 댓글 목록 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "commentId": 1,
                                                "member": {
                                                  "id": 1,
                                                  "nickname": "듀이",
                                                  "email": "user1@example.com",
                                                  "imageUrl": "https://example.com/image1.png"
                                                },
                                                "createdAt": "2025-08-14T10:00:00",
                                                "content": "첫 번째 댓글입니다.",
                                                "likeCount": 10,
                                                "replyCount": 2,
                                                "isLikedByMe": true
                                              },
                                              {
                                                "commentId": 2,
                                                "member": {
                                                  "id": 2,
                                                  "nickname": "모찌",
                                                  "email": "user2@example.com",
                                                  "imageUrl": "https://example.com/image2.png"
                                                },
                                                "createdAt": "2025-08-14T10:05:00",
                                                "content": "두 번째 댓글입니다.",
                                                "likeCount": 5,
                                                "replyCount": 0,
                                                "isLikedByMe": false
                                              }
                                            ]
                                            """
                            )
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
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
    ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId
    );

    @Operation(summary = "댓글 단일 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 단일 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "commentId": 1,
                                              "member": {
                                                "id": 1,
                                                "nickname": "모다",
                                                "email": "user@example.com",
                                                "imageUrl": "https://example.com/image.png"
                                              },
                                              "createdAt": "2025-08-14T10:00:00",
                                              "content": "이것은 댓글 내용입니다.",
                                              "likeCount": 10,
                                              "replyCount": 5,
                                              "isLikedByMe": true
                                            }
                                            """
                            )
            )),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "토론방과 댓글 불일치",
                                    value = "{\"code\":400, \"message\":\"[ERROR] 해당 토론방의 댓글이 아닙니다\"}"
                            )
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 댓글을 찾을 수 없습니다\"}"
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
    ResponseEntity<CommentResponse> getComment(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @Parameter(
                    description = "댓글 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long commentId
    );

    @Operation(summary = "댓글 수정 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "내용 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 댓글 내용을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "내용 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 댓글 내용은 1자 이상, 2000자 이하여야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "작성자 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신의 댓글만 수정/삭제 가능합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "토론방과 댓글 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 해당 토론방의 댓글이 아닙니다\"}"
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 댓글을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> updateComment(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @Parameter(
                    description = "댓글 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long commentId,
            @RequestBody(
                    description = "수정할 댓글 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentRequest.class),
                            examples = @ExampleObject(value = "{\"content\":\"수정된 댓글 내용입니다.\"}")
                    )
            ) final CommentRequest commentRequest
    );

    @Operation(summary = "댓글 삭제 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "댓글 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "대댓글 존재",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 대댓글이 존재하는 댓글은 삭제할 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "작성자 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신의 댓글만 수정/삭제 가능합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "토론방과 댓글 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 해당 토론방의 댓글이 아닙니다\"}"
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
                                    ),
                                    @ExampleObject(
                                            name = "토론방 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 토론방을 찾을 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "댓글 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 댓글을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> deleteComment(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @Parameter(
                    description = "댓글 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long commentId
    );
}
