package todoktodok.backend.discussion.presentation;

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
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.SlicedDiscussionResponse;
import todoktodok.backend.discussion.domain.DiscussionFilterType;
import todoktodok.backend.global.exception.ErrorResponse;

@Tag(name = "토론방 API")
public interface DiscussionApiDocs {

    @Operation(summary = "토론방 생성 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "토론방 생성 성공"
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
                                            value = "{\"code\":400, \"message\":\"[ERROR] 토론방 내용을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "내용 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 토론방 내용은 1자 이상, 1500자 이하여야 합니다\"}"
                                    )
                            }
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
                                            name = "책 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 책을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> createDiscussion(
            @Parameter(hidden = true) final Long memberId,
            @RequestBody(
                    description = "생성할 토론방 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"bookId\":1, \"discussionTitle\":\"토론방 제목\", \"discussionOpinion\":\"토론방 내용입니다.\"}"
                            )
                    )
            ) final DiscussionRequest discussionRequest
    );

    @Operation(summary = "토론방 신고 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "토론방 신고 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "자신 토론방 신고",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신의 토론방은 신고할 수 없습니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "중복 신고",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 신고한 토론방입니다\"}"
                                    )
                            }
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
    ResponseEntity<Void> report(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId
    );

    @Operation(summary = "토론방 단일 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 단일 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "discussionId": 1,
                                              "title": "토론방 제목",
                                              "content": "토론방 내용입니다.",
                                              "author": {
                                                "id": 1,
                                                "nickname": "듀이",
                                                "email": "user@example.com",
                                                "profileImage": "https://example.com/image.png"
                                              },
                                              "createdAt": "2025-08-14T10:00:00",
                                              "commentCount": 5,
                                              "likeCount": 10,
                                              "isLikedByMe": true
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
    ResponseEntity<DiscussionResponse> getDiscussion(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId
    );

    @Operation(summary = "토론방 필터링 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "discussionId": 1,
                                                "title": "토론방 제목1",
                                                "content": "토론방 내용1",
                                                "author": {
                                                  "id": 1,
                                                  "nickname": "듀이",
                                                  "email": "user1@example.com",
                                                  "imageUrl": "https://example.com/image1.png"
                                                },
                                                "createdAt": "2025-08-14T10:00:00",
                                                "commentCount": 5,
                                                "likeCount": 10,
                                                "isLikedByMe": true
                                              },
                                              {
                                                "discussionId": 2,
                                                "title": "토론방 제목2",
                                                "content": "토론방 내용2",
                                                "author": {
                                                  "id": 2,
                                                  "nickname": "모다",
                                                  "email": "user2@example.com",
                                                  "imageUrl": "https://example.com/image2.png"
                                                },
                                                "createdAt": "2025-08-14T10:05:00",
                                                "commentCount": 3,
                                                "likeCount": 5,
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
                            examples = @ExampleObject(
                                    name = "JWT 오류",
                                    value = "{\"code\":401, \"message\":\"[ERROR] 잘못된 로그인 시도입니다. 다시 시도해 주세요\"}"
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
    ResponseEntity<List<DiscussionResponse>> getDiscussionsByKeywordAndType(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "도서 제목 혹은 저자",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "오브젝트")
                    )
            ) final String keyword,
            @Parameter(
                    description = "필터링 타입",
                    content = @Content(
                            schema = @Schema(implementation = DiscussionFilterType.class),
                            examples = @ExampleObject(value = "ALL")
                    )
            ) final DiscussionFilterType type
    );

    @Operation(summary = "토론방 최신순 전체 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 최신순 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SlicedDiscussionResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "items": [
                                                  {
                                                    "discussionId": 2,
                                                    "title": "토론방 제목1",
                                                    "content": "토론방 내용1",
                                                    "author": {
                                                      "id": 1,
                                                      "nickname": "듀이",
                                                      "email": "user1@example.com",
                                                      "profileImage": "https://example.com/image1.png"
                                                    },
                                                    "createdAt": "2025-08-14T10:00:00",
                                                    "commentCount": 5,
                                                    "likeCount": 10,
                                                    "isLikedByMe": true
                                                  },
                                                  {
                                                    "discussionId": 1,
                                                    "title": "토론방 제목2",
                                                    "content": "토론방 내용2",
                                                    "author": {
                                                      "id": 2,
                                                      "nickname": "모다",
                                                      "email": "user2@example.com",
                                                      "profileImage": "https://example.com/image2.png"
                                                    },
                                                    "createdAt": "2025-08-14T10:05:00",
                                                    "commentCount": 3,
                                                    "likeCount": 5,
                                                    "isLikedByMe": false
                                                  }
                                              ],
                                              "pageInfo": {
                                                "hasNext": true,
                                                "nextCursor": "NA=="
                                              }
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
    ResponseEntity<SlicedDiscussionResponse> getDiscussions(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "페이지 사이즈",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "20")
                    )
            ) final int size,
            @Parameter(
                    description = "페이지 커서",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "NA==")
                    )
            ) final String cursor
    );

    @Operation(summary = "토론방 수정 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 수정 성공"
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
                                            value = "{\"code\":400, \"message\":\"[ERROR] 토론방 내용을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "내용 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 토론방 내용은 1자 이상, 1500자 이하여야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "작성자 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신의 토론방만 수정/삭제 가능합니다\"}"
                                    )
                            }
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
    ResponseEntity<Void> updateDiscussion(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId,
            @RequestBody(
                    description = "수정할 토론방 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionUpdateRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"discussionTitle\":\"수정된 토론방 제목\", \"discussionOpinion\":\"수정된 토론방 내용입니다.\"}"
                            )
                    )
            ) final DiscussionUpdateRequest discussionUpdateRequest
    );

    @Operation(summary = "토론방 삭제 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "토론방 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "댓글 존재",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 댓글이 존재하는 토론방은 삭제할 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "작성자 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신의 토론방만 수정/삭제 가능합니다\"}"
                                    )
                            }
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
    ResponseEntity<Void> deleteDiscussion(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId
    );

    @Operation(summary = "토론방 좋아요 토글 API")
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
    ResponseEntity<Void> toggleLike(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long discussionId
    );
}
