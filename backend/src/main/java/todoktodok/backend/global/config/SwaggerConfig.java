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
        Server authenticated = new Server();

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

    // Authenticated and Unauthenticated need to be Varied : Since the grouping
    public OpenApiCustomizer buildUnauthenticatedOpenApi() {
        Server unauthenticated = new Server();
        unauthenticated.url("/").setDescription("Swagger for Unauthenticated");

        return openApi -> openApi.addServersItem(unauthenticated);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("authorization")
                .pathsToExclude("/api/v1/members/login")
                .pathsToMatch("/api/v1/**")
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("unauthenticated")
                .pathsToMatch("/api/v1/members/login")
                .addOpenApiCustomizer(buildUnauthenticatedOpenApi())
                .build();
    }

    @Bean
    public OpenAPI springTodokTodokOpenAPI() {
        String title = "Todok-Todok";
        String description = "토독토독 프로젝트";

        Info info = new Info().title(title).description(description).version("1.0.0");
        return new OpenAPI().info(info);
    }
}
