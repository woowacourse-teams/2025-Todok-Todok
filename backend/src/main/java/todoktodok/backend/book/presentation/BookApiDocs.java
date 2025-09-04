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
                    responseCode = "403",
                    description = "액세스 토큰 만료 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "액세스 토큰 만료 오류",
                                    value = "{\"code\":403, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
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
                    responseCode = "403",
                    description = "액세스 토큰 만료 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "액세스 토큰 만료 오류",
                                    value = "{\"code\":403, \"message\":\"[ERROR] 액세스 토큰이 만료되었습니다\"}"
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
}
