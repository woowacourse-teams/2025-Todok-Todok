package todoktodok.backend.book.presentation;

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
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.book.application.dto.response.AladinBookResponse;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.application.dto.response.LatestAladinBookPageResponse;
import todoktodok.backend.discussion.application.dto.response.LatestDiscussionPageResponse;
import todoktodok.backend.global.exception.ErrorResponse;

@Tag(name = "도서 API")
public interface BookApiDocs {

    @Operation(summary = "도서 생성 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "도서 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
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
                                            name = "ISBN null",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 도서 ISBN을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "ISBN 길이 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] ISBN은 13자여야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "제목 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 도서 제목을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "저자 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 도서 저자를 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이미지 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 도서 이미지를 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "회원 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 해당 회원을 찾을 수 없습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "최소 글자수 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 1자 이상 입력해야 하는 정보입니다\"}"
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
    ResponseEntity<Long> createBook(
            @Parameter(hidden = true) final Long memberId,
            @RequestBody(
                    description = "생성할 도서 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "bookIsbn": "1234567890123",
                                              "bookTitle": "오브젝트",
                                              "bookAuthor": "조영호",
                                              "bookImage": "https://image.png"
                                            }
                                            """
                            )
                    )
            ) final BookRequest bookRequest
    );

    @Operation(summary = "도서 검색 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도서 검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AladinBookResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                               {
                                                 "bookId": "1",
                                                 "bookTitle": "오브젝트",
                                                 "bookAuthor": "조영호",
                                                 "bookImage": "https://image.png"
                                               },
                                               {
                                                 "bookId": "2",
                                                 "bookTitle": "오브젝트 디자인",
                                                 "bookAuthor": "마티아스 노박",
                                                 "bookImage": "https://image2.png"
                                               }
                                             ]
                                            """
                            )
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
                                            name = "최소 글자수 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 검색어는 1자 이상이어야 합니다\"}"
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
    ResponseEntity<List<AladinBookResponse>> search(
            @Parameter(
                    description = "조회할 도서 제목 혹은 저자",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "오브젝트")
                    )
            ) final String keyword
    );

    @Operation(summary = "도서 검색 API(페이지네이션 적용)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도서 검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LatestAladinBookPageResponse.class),
                            examples = {
                                @ExampleObject(
                                        name = "첫 번째 ~ 중간 페이지",
                                        value = """
                                                {
                                                  "items": [
                                                    {
                                                      "bookId": "1",
                                                      "bookTitle": "오브젝트",
                                                      "bookAuthor": "조영호",
                                                      "bookImage": "https://image.png"
                                                    },
                                                    {
                                                      "bookId": "2",
                                                      "bookTitle": "오브젝트 디자인",
                                                      "bookAuthor": "마티아스 노박",
                                                      "bookImage": "https://image2.png"
                                                    }
                                                  ],
                                                  "pageInfo": {
                                                    "hasNext": true,
                                                    "nextCursor": "Mg=="
                                                  },
                                                  "totalSize": 200
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "마지막 페이지",
                                        value = """
                                                {
                                                  "items": [
                                                    {
                                                      "bookId": "1",
                                                      "bookTitle": "오브젝트",
                                                      "bookAuthor": "조영호",
                                                      "bookImage": "https://image.png"
                                                    },
                                                    {
                                                      "bookId": "2",
                                                      "bookTitle": "오브젝트 디자인",
                                                      "bookAuthor": "마티아스 노박",
                                                      "bookImage": "https://image2.png"
                                                    }
                                                  ],
                                                  "pageInfo": {
                                                    "hasNext": false,
                                                    "nextCursor": null
                                                  },
                                                  "totalSize": 200
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
                                            name = "최소 글자수 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 검색어는 1자 이상이어야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "페이지 사이즈 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 유효하지 않은 페이지 사이즈입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "커서 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] Base64로 디코드할 수 없는 cursor 값입니다\"}"
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
    ResponseEntity<LatestAladinBookPageResponse> searchByPaging(
            @Parameter(
                    description = "페이지 사이즈(요청 한 번에 가져올 책의 개수, 10 고정)",
                    content = @Content(
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "10")
                    )
            ) final int size,
            @Parameter(
                    description = "직전 응답의 nextCursor(첫 요청에는 보내지 않음)",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Mg==")
                    )
            ) final String cursor,
            @Parameter(
                    description = "조회할 도서 제목 혹은 저자",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "오브젝트")
                    )
            ) final String keyword
    );

    @Operation(summary = "도서 단일 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도서 단일 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "bookId": 1,
                                              "bookTitle": "오브젝트 - 코드로 이해하는 객체지향 설계",
                                              "bookAuthor": "조영호",
                                              "bookImage": "https://example.com/book101.jpg",
                                              "bookPublisher": "위키북스",
                                              "bookSummary": "역할, 책임, 협력에 기반해 객체지향 프로그램을 설계하고 구현하는 방법, 응집도와 결합도를 이용해 설계를 트레이드오프하는 방법, 설계를 유연하게 만드는 다양한 의존성 관리 기법, 타입 계층을 위한 상속과 코드 재사용을 위한 합성의 개념, 다양한 설계 원칙과 디자인 패턴 등을 다룬다."
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
                                            name = "도서 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 도서를 찾을 수 없습니다\"}"
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
    ResponseEntity<BookResponse> getBook(
            @Parameter(
                    description = "도서 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long bookId
    );

    @Operation(summary = "도서별 토론방 최신순 조회 API")
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
                                                        "bookImage": "https://image.png",
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
                    responseCode = "400",
                    description = "페이지 사이즈 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "유효하지 않은 페이지 사이즈",
                                    value = "{\"code\":400, \"message\":\"[ERROR] 유효하지 않은 페이지 사이즈입니다. 1 이상 50 이하의 페이징을 시도해주세요\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "커서 디코딩 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "디코드할 수 없는 cursor",
                                    value = "{\"code\":400, \"message\":\"[ERROR] Base64로 디코드할 수 없는 cursor 값입니다\"}"
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
                    description = "토론방 관련 도서 ID",
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
}
