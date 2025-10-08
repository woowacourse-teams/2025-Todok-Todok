package todoktodok.backend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import todoktodok.backend.global.jwt.JwtTokenProvider;

@Slf4j
@Component
@AllArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        return true;
//
//        if (!(handler instanceof HandlerMethod handlerMethod)) {
//            return true;
//        }
//
//        final Method method = handlerMethod.getMethod();
//
//        if (!method.isAnnotationPresent(Auth.class)) {
//            log.error(String.format("Auth 어노테이션이 없는 메서드에 대한 요청입니다: requestURI = %s", request.getRequestURI()));
//            throw new IllegalStateException("서버 내부 오류가 발생했습니다");
//        }
//
//        final Auth auth = method.getAnnotation(Auth.class);
//        final Role requiredRole = auth.value();
//
//        if (requiredRole == Role.GUEST || requiredRole == Role.EXPIRED_USER) {
//            return true;
//        }
//
//        final String token = request.getHeader("Authorization");
//        final TokenInfo tokenInfo = jwtTokenProvider.getInfoByAccessToken(token);
//
//        if (tokenInfo.role() == requiredRole) {
//            return true;
//        }
//
//        log.warn(String.format("접근 권한이 없습니다: requiredRole = %s, tokenRole = %s", requiredRole.name(), tokenInfo.role().name()));
//        throw new JwtException("접근 권한이 없습니다");
    }
}
