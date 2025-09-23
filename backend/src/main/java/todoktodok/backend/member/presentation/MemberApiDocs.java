package todoktodok.backend.member.presentation;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.discussion.application.dto.response.DiscussionResponse;
import todoktodok.backend.global.exception.ErrorResponse;
import todoktodok.backend.member.application.dto.request.*;
import todoktodok.backend.member.application.dto.response.BlockMemberResponse;
import todoktodok.backend.member.application.dto.response.ProfileImageUpdateResponse;
import todoktodok.backend.member.application.dto.response.ProfileResponse;
import todoktodok.backend.member.application.dto.response.ProfileUpdateResponse;
import todoktodok.backend.member.application.dto.response.RefreshTokenResponse;
import todoktodok.backend.member.domain.MemberDiscussionFilterType;

@Tag(name = "회원 API")
public interface MemberApiDocs {

    @Operation(summary = "로그인 API")
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
                                            name = "유효하지 않은 idToken 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 유효하지 않은 토큰입니다\"}"
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
    ResponseEntity<RefreshTokenResponse> login(
            @RequestBody(
                    description = "로그인 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = "{\"googleIdToken\":\"{googleIdToken}\"}")
                    )
            ) final LoginRequest loginRequest
    );

    @Operation(summary = "[DEPRECATED] 로그인 API")
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
    ResponseEntity<RefreshTokenResponse> loginLegacy(
            @RequestBody(
                    description = "로그인 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestLegacy.class),
                            examples = @ExampleObject(value = "{\"email\":\"user@example.com\"}")
                    )
            ) final LoginRequestLegacy loginRequest
    );

    @Operation(summary = "회원가입 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "리프레시 토큰",
                                            value = "{\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3 NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk\"}"
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
                                            name = "닉네임 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 닉네임을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "닉네임 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 닉네임은 1자 이상, 8자 이하여야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "프로필 이미지 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 프로필 이미지를 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이메일 형식 오류",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 올바른 이메일 형식을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "닉네임 중복",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 존재하는 닉네임입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이메일 중복",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 가입된 이메일입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "입력한 이메일과 토큰 이메일 불일치",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 소셜 로그인을 하지 않은 이메일입니다\"}"
                                    ),
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
    ResponseEntity<RefreshTokenResponse> signup(
            @Parameter(hidden = true) final String memberEmail,
            @RequestBody(
                    description = "회원가입 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignupRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "nickname":"동전",
                                              "profileImage":"https://image.png",
                                              "email":"user@example.com"
                                            }
                                            """
                            )
                    )
            ) final SignupRequest signupRequest
    );

    @Operation(summary = "토큰 재발급 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "재발급한 리프레시 토큰",
                                            value = "{\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3 NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk\"}"
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
                            examples = @ExampleObject(
                                    name = "리프레시 토큰 중복 발급 오류",
                                    value = "{\"code\":400, \"message\":\"[ERROR] 중복된 리프레시 토큰 발급 요청입니다\"}"
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
                    description = "이미 사용한 리프레시 토큰 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "이미 사용한 리프레시 토큰 오류",
                                    value = "{\"code\":404, \"message\":\"[ERROR] 해당 리프레시 토큰을 찾을 수 없습니다\"}"
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
    ResponseEntity<RefreshTokenResponse> refresh(
            @RequestBody(
                    description = "리프레시 토큰",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3 NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk\"}"
                            )
                    )
            ) final RefreshTokenRequest refreshTokenRequest
    );

    @Operation(summary = "작성자 차단 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "작성자 차단 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "자기 자신 차단",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신은 차단할 수 없습니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이미 차단한 회원",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 차단한 회원입니다\"}"
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
    ResponseEntity<Void> block(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "차단할 회원 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long targetId
    );

    @Operation(summary = "작성자 신고 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "작성자 신고 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "자기 자신 신고",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 자기 자신은 신고할 수 없습니다.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이미 신고한 회원",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 신고한 회원입니다\"}"
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
    ResponseEntity<Void> report(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "신고할 회원 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long targetId,
            @RequestBody(
                    description = "회원 신고 사유",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberReportRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"reason\":\"부적절한 내용\"}"
                            )
                    )
            ) final MemberReportRequest memberReportRequest
    );

    @Operation(summary = "회원별 프로필 정보 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfileResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "memberId":1,
                                              "nickname":"페토",
                                              "profileMessage":"상태메시지",
                                              "profileImage":"https://image.png"
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
    ResponseEntity<ProfileResponse> getProfile(
            @Parameter(
                    description = "조회할 회원 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long memberId
    );

    @Operation(summary = "회원별 활동 도서 전체 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동 도서 전체 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "bookId": 1,
                                                "bookTitle": "오브젝트",
                                                "bookAuthor": "조영호",
                                                "bookImage": "https://image.png"
                                              },
                                              {
                                                "bookId": 2,
                                                "bookTitle": "스프링",
                                                "bookAuthor": "토비",
                                                "bookImage": "https://image.png"
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
    ResponseEntity<List<BookResponse>> getActiveBooks(
            @Parameter(
                    description = "조회할 회원 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long memberId
    );

    @Operation(summary = "회원별 토론방 필터 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토론방 필터 조회 성공",
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
    ResponseEntity<List<DiscussionResponse>> getMemberDiscussionsByType(
            @Parameter(
                    description = "조회할 회원 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long memberId,
            @Parameter(
                    description = "필터링 타입",
                    content = @Content(
                            schema = @Schema(implementation = MemberDiscussionFilterType.class),
                            examples = @ExampleObject(value = "CREATED")
                    )
            ) final MemberDiscussionFilterType type
    );

    @Operation(summary = "차단한 회원 전체 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "차단한 회원 전체 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BlockMemberResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "memberId": 2,
                                                "nickname": "링크",
                                                "createdAt": "2025-08-14T10:00:00"
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
    ResponseEntity<List<BlockMemberResponse>> getBlockMembers(
            @Parameter(hidden = true) final Long memberId
    );

    @Operation(summary = "프로필 정보 수정 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfileUpdateResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"nickname\":\"수정된닉네임\", \"profileMessage\":\"수정된상태메시지\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 값 입력 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "닉네임 없음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 닉네임을 입력해주세요\"}"
                                    ),
                                    @ExampleObject(
                                            name = "닉네임 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 닉네임은 1자 이상, 8자 이하여야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "상태메시지 길이 조건 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 상태메세지는 40자 이하여야 합니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "닉네임 중복",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미 존재하는 닉네임입니다\"}"
                                    ),
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
    ResponseEntity<ProfileUpdateResponse> updateProfile(
            @Parameter(hidden = true) final Long memberId,
            @RequestBody(
                    description = "수정할 프로필 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfileUpdateRequest.class),
                            examples = @ExampleObject(value = "{\"nickname\":\"수정된닉네임\", \"profileMessage\":\"수정된상태메시지\"}")
                    )
            ) final ProfileUpdateRequest profileUpdateRequest
    );

    @Operation(summary = "프로필 사진 수정 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 사진 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProfileImageUpdateResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"profileImage\":\"https://awsbucketname.s3.region.amazonaws.com/todoktodok\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 값 입력 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "파일 비어있음",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 파일이 비어있습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "파일 크기 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 파일 크기가 5MB 초과입니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "파일 타입 부적합",
                                            value = "{\"code\":400, \"message\":\"[ERROR] 이미지 파일이 아닙니다\"}"
                                    ),
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
                            examples = {
                                    @ExampleObject(
                                            name = "서버 오류",
                                            value = "{\"code\":500, \"message\":\"[ERROR] 서버 내부 오류가 발생했습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "AWS 오류",
                                            value = "{\"code\":500, \"message\":\"[ERROR] AWS 처리 중 오류가 발생했습니다\"}"
                                    ),
                            }
                    ))
    })
    ResponseEntity<ProfileImageUpdateResponse> updateProfileImage(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(description = "수정할 프로필 이미지")
            @RequestPart("profileImage") MultipartFile profileImage
    );

    @Operation(summary = "회원 탈퇴 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "회원 탈퇴 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "리프레시 토큰과 액세스 토큰의 회원 id 불일치",
                                    value = "{\"code\":400, \"message\":\"[ERROR] 리프레시 토큰과 액세스 토큰의 회원 정보가 일치하지 않습니다\"}"
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
                                            name = "리프레시 토큰 없음",
                                            value = "{\"code\":404, \"message\":\"[ERROR] 해당 리프레시 토큰을 찾을 수 없습니다\"}"
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
    ResponseEntity<Void> deleteMember(
            @Parameter(hidden = true) final Long memberId,
            @RequestBody(
                    description = "리프레시 토큰",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(value = "{\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3 NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk\"}")
                    )
            ) final RefreshTokenRequest refreshTokenRequest
    );

    @Operation(summary = "차단 해제 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "차단 해제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "차단자 불일치",
                                    value = "{\"code\":400, \"message\":\"[ERROR] 차단한 회원이 아닙니다\"}"
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
    ResponseEntity<Void> deleteBlock(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(
                    description = "차단 해제할 회원 ID",
                    content = @Content(
                            schema = @Schema(implementation = Long.class),
                            examples = @ExampleObject(value = "1")
                    )
            ) final Long targetId
    );
}
