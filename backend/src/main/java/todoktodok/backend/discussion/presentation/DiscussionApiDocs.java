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
import org.springframework.web.bind.annotation.GetMapping;
import todoktodok.backend.discussion.application.dto.request.DiscussionReportRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionRequest;
import todoktodok.backend.discussion.application.dto.request.DiscussionUpdateRequest;
import todoktodok.backend.discussion.application.dto.response.ActiveDiscussionPageResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.auth.Role;
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
            ) final Long discussionId,
            @RequestBody(
                    description = "토론방 신고 사유",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionReportRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"reason\":\"부적절한 내용\"}"
                            )
                    )
            ) final DiscussionReportRequest discussionReportRequest
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
                                              "book": {
                                                "bookId": 1,
                                                "bookTitle": "엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향",
                                                "bookAuthor": "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                                                "bookImage": "https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg"
                                              },
                                              "member": {
                                                "memberId": 2,
                                                "nickname": "모찌",
                                                "profileImage": "https://user.png"
                                              },
                                              "createdAt": "2025-08-20T10:59:48",
                                              "discussionTitle": "토론방 제목",
                                              "discussionOpinion": "토론방 내용입니다",
                                              "viewCount": 2,
                                              "likeCount": 0,
                                              "commentCount": 4,
                                              "isLikedByMe": false
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

    @Operation(summary = "토론방 최신순 전체 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 최신순 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LatestDiscussionPageResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "items": [
                                                  {
                                                    "discussionId": 2,
                                                    "book": {
                                                        "bookId": 1,
                                                        "bookTitle": "오브젝트",
                                                        "bookAuthor": "조영호",
                                                        "bookImage": "https://image.png"
                                                    },
                                                    "member": {
                                                        "memberId": 1,
                                                        "nickname": "듀이",
                                                        "profileImage": "https://example.com/image1.png"
                                                    },
                                                    "createdAt": "2025-08-14T10:00:00",
                                                    "discussionTitle": "토론방 제목1",
                                                    "discussionOpinion": "토론방 내용1",
                                                    "viewCount": 2,
                                                    "likeCount": 10,
                                                    "commentCount": 5,
                                                    "isLikedByMe": true
                                                  },
                                                  {
                                                    "discussionId": 1,
                                                    "book": {
                                                        "bookId": 1,
                                                        "bookTitle": "오브젝트",
                                                        "bookAuthor": "조영호",
                                                        "bookImage": "https://image.png"
                                                    },
                                                    "member": {
                                                        "memberId": 2,
                                                        "nickname": "모다",
                                                        "profileImage": "https://example.com/image2.png"
                                                    },
                                                    "createdAt": "2025-08-14T10:05:00",
                                                    "discussionTitle": "토론방 제목2",
                                                    "discussionOpinion": "토론방 내용2",
                                                    "viewCount": 2,
                                                    "likeCount": 5,
                                                    "commentCount": 3,
                                                    "isLikedByMe": false
                                                  }
                                              ],
                                              "pageInfo": {
                                                "hasNext": true,
                                                "nextCursor": "Mw=="
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
    ResponseEntity<LatestDiscussionPageResponse> getDiscussions(
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

    @Operation(summary = "도서별 토론방 최신순 전체 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도서별 토론방 최신순 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LatestDiscussionPageResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "items": [
                                                  {
                                                    "discussionId": 2,
                                                    "book": {
                                                        "bookId": 1,
                                                        "bookTitle": "오브젝트",
                                                        "bookAuthor": "조영호",
                                                        "bookImage": "https://image.png",
                                                        "bookPublisher": "위키북스",
                                                        "bookSummary": "역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법, 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법, 설계를 유연하게 만드는 다양한 의존성 관리 기법, 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념, 다양한 설계 원칙과 디자인 패턴 등을 다룬다."
                                                    },
                                                    "member": {
                                                        "memberId": 1,
                                                        "nickname": "듀이",
                                                        "profileImage": "https://example.com/image1.png"
                                                    },
                                                    "createdAt": "2025-08-14T10:00:00",
                                                    "discussionTitle": "토론방 제목1",
                                                    "discussionOpinion": "토론방 내용1",
                                                    "viewCount": 2,
                                                    "likeCount": 10,
                                                    "commentCount": 5,
                                                    "isLikedByMe": true
                                                  },
                                                  {
                                                    "discussionId": 1,
                                                    "book": {
                                                        "bookId": 1,
                                                        "bookTitle": "오브젝트",
                                                        "bookAuthor": "조영호",
                                                        "bookImage": "https://image.png"
                                                        "bookPublisher": "위키북스",
                                                        "bookSummary": "역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법, 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법, 설계를 유연하게 만드는 다양한 의존성 관리 기법, 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념, 다양한 설계 원칙과 디자인 패턴 등을 다룬다."
                                                    },
                                                    "member": {
                                                        "memberId": 2,
                                                        "nickname": "모다",
                                                        "profileImage": "https://example.com/image2.png"
                                                    },
                                                    "createdAt": "2025-08-14T10:05:00",
                                                    "discussionTitle": "토론방 제목2",
                                                    "discussionOpinion": "토론방 내용2",
                                                    "viewCount": 2,
                                                    "likeCount": 5,
                                                    "commentCount": 3,
                                                    "isLikedByMe": false
                                                  }
                                              ],
                                              "pageInfo": {
                                                "hasNext": true,
                                                "nextCursor": "Mw=="
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
    ResponseEntity<LatestDiscussionPageResponse> getDiscussionsByBook(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "토론방 관련 책 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long bookId,
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

    @Operation(summary = "토론방 필터링 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 필터링 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "discussionId": 1,
                                                  "book": {
                                                    "bookId": 1,
                                                    "bookTitle": "엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향",
                                                    "bookAuthor": "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                                                    "bookImage": "https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg"
                                                  },
                                                  "member": {
                                                    "memberId": 2,
                                                    "nickname": "모찌",
                                                    "profileImage": "https://user.png"
                                                  },
                                                  "createdAt": "2025-08-20T10:59:48",
                                                  "discussionTitle": "토론방 제목",
                                                  "discussionOpinion": "토론방 내용입니다",
                                                  "viewCount": 2,
                                                  "likeCount": 0,
                                                  "commentCount": 4,
                                                  "isLikedByMe": false
                                                },
                                                {
                                                  "discussionId": 2,
                                                  "book": {
                                                    "bookId": 1,
                                                    "bookTitle": "엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향",
                                                    "bookAuthor": "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                                                    "bookImage": "https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg"
                                                  },
                                                  "member": {
                                                    "memberId": 2,
                                                    "nickname": "모찌",
                                                    "profileImage": "https://user.png"
                                                  },
                                                  "createdAt": "2025-08-20T10:59:48",
                                                  "discussionTitle": "토론방 제목 2",
                                                  "discussionOpinion": "토론방 내용 2입니다",
                                                  "viewCount": 2,
                                                  "likeCount": 0,
                                                  "commentCount": 4,
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
    ResponseEntity<List<DiscussionResponse>> getDiscussionsByKeyword(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "도서 제목 혹은 저자",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "오브젝트")
                    )
            ) final String keyword
    );

    @Operation(summary = "인기 토론방 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "인기 토론방 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiscussionResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                 {
                                                   "discussionId": 1,
                                                   "book": {
                                                     "bookId": 1,
                                                     "bookTitle": "엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향",
                                                     "bookAuthor": "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                                                     "bookImage": "https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg"
                                                   },
                                                   "member": {
                                                     "memberId": 2,
                                                     "nickname": "모찌",
                                                     "profileImage": "https://user.png"
                                                   },
                                                   "createdAt": "2025-08-20T10:59:48",
                                                   "discussionTitle": "토론방 제목",
                                                   "discussionOpinion": "토론방 내용입니다",
                                                   "viewCount": 2,
                                                   "likeCount": 5,
                                                   "commentCount": 4,
                                                   "isLikedByMe": true
                                                 },
                                                 {
                                                   "discussionId": 3,
                                                   "book": {
                                                     "bookId": 1,
                                                     "bookTitle": "엘레강트 오브젝트 - 새로운 관점에서 바라본 객체지향",
                                                     "bookAuthor": "Yegor Bugayenko (지은이), 조영호 (옮긴이)",
                                                     "bookImage": "https://image.aladin.co.kr/product/25837/40/coversum/k762736538_1.jpg"
                                                   },
                                                   "member": {
                                                     "memberId": 2,
                                                     "nickname": "모찌",
                                                     "profileImage": "https://user.png"
                                                   },
                                                   "createdAt": "2025-08-20T10:59:48",
                                                   "discussionTitle": "토론방 제목 3",
                                                   "discussionOpinion": "토론방 내용 3입니다",
                                                   "viewCount": 2,
                                                   "likeCount": 6,
                                                   "commentCount": 0,
                                                   "isLikedByMe": false
                                                 }
                                            ]
                                            """
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "잘못된 개수 값",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 유효하지 않은 개수입니다. 양수의 개수를 조회해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 기간 값",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 유효하지 않은 기간 값입니다. 0일 ~ 365일 이내로 조회해주세요\"}"
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
    @Auth(value = Role.USER)
    @GetMapping("/hot")
    ResponseEntity<List<DiscussionResponse>> getHotDiscussions(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "조회 기간 (일)",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "7")
                    )
            ) final int period,
            @Parameter(
                    description = "조회 개수",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "10")
                    )
            ) final int count
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

    @Operation(summary = "활성화된 토론방 조회 API(커서 기반)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활성 토론방 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActiveDiscussionPageResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "첫 페이지 예시",
                                            value = """
                                                    {
                                                      "items": [
                                                        {
                                                          "discussionId": 4,
                                                          "book": {
                                                            "bookId": 1,
                                                            "bookTitle": "오브젝트",
                                                            "bookAuthor": "조영호",
                                                            "bookImage": "https://image.png"
                                                          },
                                                          "member": {
                                                            "memberId": 1,
                                                            "nickname": "듀이",
                                                            "profileImage": "https://example.com/image1.png"
                                                          },
                                                          "createdAt": "2025-08-14T10:00:00",
                                                          "discussionTitle": "토론방4",
                                                          "discussionOpinion": "내용4",
                                                          "viewCount": 2,
                                                          "likeCount": 0,
                                                          "commentCount": 1,
                                                          "isLikedByMe": false,
                                                          "lastCommentedAt": "2025-08-20T14:49:00"
                                                        },
                                                        {
                                                          "discussionId": 3,
                                                          "book": {
                                                            "bookId": 1,
                                                            "bookTitle": "오브젝트",
                                                            "bookAuthor": "조영호",
                                                            "bookImage": "https://image.png"
                                                          },
                                                          "member": {
                                                            "memberId": 2,
                                                            "nickname": "모다",
                                                            "profileImage": "https://example.com/image2.png"
                                                          },
                                                          "createdAt": "2025-08-14T10:00:00",
                                                          "discussionTitle": "토론방3",
                                                          "discussionOpinion": "내용3",
                                                          "viewCount": 2,
                                                          "likeCount": 2,
                                                          "commentCount": 3,
                                                          "isLikedByMe": true,
                                                          "lastCommentedAt": "2025-08-20T14:50:00"
                                                        }
                                                      ],
                                                      "pageInfo": {
                                                        "hasNext": true,
                                                        "nextCursor": "MjAyNS0wOC0yMFQxNDo1MDowMF8z"
                                                      }
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "마지막 페이지 예시",
                                            value = """
                                                    {
                                                      "items": [
                                                        {
                                                          "discussionId": 2,
                                                          "book": {
                                                            "bookId": 1,
                                                            "bookTitle": "오브젝트",
                                                            "bookAuthor": "조영호",
                                                            "bookImage": "https://image.png"
                                                          },
                                                          "member": {
                                                            "memberId": 1,
                                                            "nickname": "듀이",
                                                            "profileImage": "https://example.com/image1.png"
                                                          },
                                                          "createdAt": "2025-08-14T10:00:00",
                                                          "discussionTitle": "토론방2",
                                                          "discussionOpinion": "내용2",
                                                          "viewCount": 2,
                                                          "likeCount": 1,
                                                          "commentCount": 2,
                                                          "isLikedByMe": false,
                                                          "lastCommentedAt": "2025-08-20T15:30:00"
                                                        }
                                                      ],
                                                      "pageInfo": {
                                                        "hasNext": false,
                                                        "nextCursor": null
                                                      }
                                                    }
                                                    """
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
                                            name = "유효하지 않은 size",
                                            value = "{\"code\":400, \"message\":\"[ERROR] size는 1 이상이어야 합니다\"}"
                                    )
                            }
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
                    )
            ),
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
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 오류",
                                    value = "{\"code\":500, \"message\":\"[ERROR] 서버 내부 오류가 발생했습니다\"}"
                            )
                    )
            )
    })
    ResponseEntity<ActiveDiscussionPageResponse> getActiveDiscussions(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "조회 기간(일)",
                    schema = @Schema(implementation = Integer.class, example = "7")
            ) final int period,
            @Parameter(
                    description = "페이지 크기(요청 개수)",
                    schema = @Schema(implementation = Integer.class, example = "10")
            ) final int size,
            @Parameter(
                    description = "직전 응답의 nextCursor (첫 페이지면 null)",
                    schema = @Schema(implementation = String.class, example = "MjAyNS0wOC0yMFQxNDo1MDowMF8z")
            ) final String cursor
    );
}
