package todoktodok.backend.global.config;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import todoktodok.backend.global.interceptor.AuthorizationInterceptor;
import todoktodok.backend.global.interceptor.LogInterceptor;
import todoktodok.backend.global.resolver.MemberArgumentResolver;
import todoktodok.backend.global.resolver.TempMemberArgumentResolver;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final MemberArgumentResolver memberArgumentResolver;
    private final TempMemberArgumentResolver tempMemberArgumentResolver;
    private final LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/h2-console/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );

        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/h2-console/**",
                        "/actuator/health_check/readiness/**"
                );
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
        resolvers.add(tempMemberArgumentResolver);
    }
}
