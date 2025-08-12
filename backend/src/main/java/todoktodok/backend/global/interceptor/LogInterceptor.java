package todoktodok.backend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final int HTTP_STATUS_SUCCESS_MIN = 200;
    private static final int HTTP_STATUS_ERROR_MIN = 400;

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String requestURI = request.getRequestURI();
        final String clientIp = getClientIp(request);
        request.setAttribute("clientIp", clientIp);
        log.info("[API REQUEST] {}", requestURI);

        return true;
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex
    ) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final int status = response.getStatus();

        String clientIp = (String) request.getAttribute("clientIp");

        log.error("[API RESPONSE] [{}] {} from {}: {}", method, requestURI, clientIp, status);

        if (status >= HTTP_STATUS_SUCCESS_MIN && status < HTTP_STATUS_ERROR_MIN) {
            log.info("[API RESPONSE] [{}] {}: {}", method, requestURI, status);
        } else {
            log.error("[API RESPONSE] [{}] {}: {}", method, requestURI, status);
        }
    }

    private String getClientIp(final HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
