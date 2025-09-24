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
                .pathsToExclude("/api/v1/members/login/**")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("unauthenticated")
                .pathsToMatch("/api/v1/members/login/**")
                .addOpenApiCustomizer(buildUnauthenticatedOpenApi())
                .build();
    }

    @Bean
    public OpenAPI springTodokTodokOpenAPI() {
        final String title = "토독토독";
        final String description = """
                ### API 인증 및 테스트 방법
                
                API 테스트 전, 회원가입/로그인 인증 방법은 아래와 같습니다.\n
                (이미 회원가입이 되어 있는 email이라면 **3~5번 과정**을 생략하시면 됩니다.)
                
                ---
                1. 우측 상단 **Select a definition** -> `unauthenticated` 선택
                2. **로그인 API**에서 email 입력 -> Execute\n
                   -> 하단 `Response headers`에서 `authorization: Bearer` 뒤 토큰 **모두 복사** (공백 없이!)\n
                   (복사할 토큰 예시: eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3
                   NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk)
                3. 우측 상단 **Select a definition** -> `authorization` 선택
                4. 2번에서 복사한 토큰 -> 우측 초록색 자물쇠 **Authorize** 버튼 클릭 -> Value에 붙여넣기 후 Authorize 버튼 클릭
                5. **회원가입 API**에서 2번에 입력한 email, 사용할 nickname 입력 -> Execute\n
                   -> 하단 `Response headers`에서 `authorization: Bearer` 뒤 토큰 **모두 복사** (공백 없이!)\n
                   (복사할 토큰 예시: eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3
                   NTU2MTgxNjZ9._-0qTNmPyO1m6LnpEAwkGAB92Es0yBwxNBtmsq_VrGk)
                6. 2번 또는 5번에서 복사한 토큰 -> 우측 초록색 자물쇠 **Authorize** 버튼 클릭 -> Value에 붙여넣기 후 Authorize 버튼 클릭
                7. API 테스트 가능!
                """;

        final Info info = new Info().title(title).description(description).version("1.0.0");
        return new OpenAPI().info(info);
    }
}
