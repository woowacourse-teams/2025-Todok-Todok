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
import todoktodok.backend.global.jwt.TokenInfo;

@Slf4j
@Component
@AllArgsConstructor
public class TempMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class)
                && parameter.hasParameterAnnotation(TempMember.class);
    }

    @Override
    public String resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        final String token = getTokenFromAuthorizationHeader(httpServletRequest);
        final TokenInfo tokenInfo = jwtTokenProvider.getInfoByAccessToken(token);

        if (tokenInfo.tempUserEmail() == null) {
            log.warn("JWT 토큰에서 이메일 정보를 확인할 수 없습니다");
            throw new JwtException("잘못된 로그인 시도입니다. 다시 시도해주세요.");
        }
        return tokenInfo.tempUserEmail();
    }

    private String getTokenFromAuthorizationHeader(final HttpServletRequest httpServletRequest) {
        try {
            return httpServletRequest.getHeader("Authorization");
        } catch (NullPointerException e) {
            log.warn("JWT 토큰이 존재하지 않습니다");
            throw new JwtException("잘못된 로그인 시도입니다. 다시 시도해 주세요.");
        }
    }
}
