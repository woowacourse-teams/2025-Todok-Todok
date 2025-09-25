package todoktodok.backend.global.resolver;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import todoktodok.backend.global.jwt.JwtTokenProvider;

@Slf4j
@Component
@AllArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Long resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        final String token = getTokenFromAuthorizationHeader(httpServletRequest);
        return jwtTokenProvider.getInfoByAccessToken(token).id();
    }

    private String getTokenFromAuthorizationHeader(HttpServletRequest httpServletRequest) {
        try {
            return httpServletRequest.getHeader("Authorization");
        } catch (NullPointerException e) {
            log.warn("JWT 토큰이 존재하지 않습니다");
            throw new JwtException("잘못된 로그인 시도입니다. 다시 시도해 주세요.");
        }
    }
}
