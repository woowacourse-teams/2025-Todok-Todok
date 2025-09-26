package todoktodok.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public OpenApiCustomizer buildSecurityOpenApi() {
        final Server authenticated = new Server();

        return openApi -> openApi
                .addSecurityItem(new SecurityRequirement().addList("jwt token"))
                .addServersItem(authenticated)
                .getComponents()
                .addSecuritySchemes("jwt token", new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer"));
    }

    public OpenApiCustomizer buildUnauthenticatedOpenApi() {
        final Server unauthenticated = new Server().url("/");

        return openApi -> openApi.addServersItem(unauthenticated);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("authorization")
                .pathsToExclude("/api/v1/members/login", "/api/v1/admin/login")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("unauthenticated")
                .pathsToMatch("/api/v1/members/login", "/api/v1/admin/login")
                .addOpenApiCustomizer(buildUnauthenticatedOpenApi())
                .build();
    }

    @Bean
    public OpenAPI springTodokTodokOpenAPI() {
        final String title = "토독토독";
        final String description = """
                ### API 인증 및 테스트 방법
                
                API 테스트 전, 회원가입/로그인 인증 방법은 아래와 같습니다.\n
                
                ---
                1. 토독토독에서는 안드로이드가 구글에서 발급받은 토큰으로 로그인하고 있기 때문에, 서버에서 자체적으로 로그인을 할 수 없습니다. \n 따라서 **관리자에게 문의** 후 **테스트용 로그인 이메일과 비밀번호를 받아**, 관리자용 로그인 방식을 사용해주세요.
                2. 우측 상단 **Select a definition** -> `unauthenticated` 선택
                3. **[관리자 전용] 로그인 API**에서 email과 password 입력 -> Execute\n
                   -> 하단 `Response headers`에서 `authorization: Bearer` 뒤 토큰 **모두 복사** (공백 없이!)\n
                   (복사할 토큰 예시: eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3
                   NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk)
                4. 우측 상단 **Select a definition** -> `authorization` 선택
                5. 3번에서 복사한 토큰 -> 우측 초록색 자물쇠 **Authorize** 버튼 클릭 -> Value에 붙여넣기 후 Authorize 버튼 클릭
                6. API 테스트 가능!
                """;

        final Info info = new Info().title(title).description(description).version("1.0.0");
        return new OpenAPI().info(info);
    }
}
