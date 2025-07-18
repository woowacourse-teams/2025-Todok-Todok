package todoktodok.backend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import todoktodok.backend.global.auth.Auth;
import todoktodok.backend.global.jwt.JwtTokenProvider;
import todoktodok.backend.global.jwt.TokenInfo;
import todoktodok.backend.global.auth.Role;

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
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }

        Method method = handlerMethod.getMethod();

        if (!method.isAnnotationPresent(Auth.class)) {
            return false;
        }

        Auth auth = method.getAnnotation(Auth.class);
        Role requiredRole = auth.value();

        if (requiredRole == Role.GUEST) {
            return true;
        }

        String token = request.getHeader("Authorization");
        final TokenInfo tokenInfo = jwtTokenProvider.getInfo(token);

        if (tokenInfo.role() == requiredRole) {
            return true;
        }

        log.warn("Authorization failed");
        return false;
    }
}
