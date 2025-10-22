package todoktodok.backend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final int HTTP_STATUS_SUCCESS_MIN = 200;
    private static final int HTTP_STATUS_CLIENT_ERROR_MIN = 400;
    private static final int HTTP_STATUS_SERVER_ERROR_MIN = 500;

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private static final String SERVER_IP = resolveServerIp();

    private static String resolveServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (final UnknownHostException e) {
            return "unknown";
        }
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String requestURI = getRequestURI(request);
        final String clientIp = getClientIp(request);

        MDC.put("clientIp", clientIp);
        MDC.put("serverIp", SERVER_IP);

        log.info("[API REQUEST] {} {}", request.getMethod(), requestURI);
        return true;
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex
    ) {
        try {
            final String requestURI = getRequestURI(request);
            final String method = request.getMethod();
            final int status = response.getStatus();

            if (status >= HTTP_STATUS_SUCCESS_MIN && status < HTTP_STATUS_CLIENT_ERROR_MIN) {
                log.info("[API RESPONSE] {} -> {}: {}", method, requestURI, status);
            } else if (status >= HTTP_STATUS_CLIENT_ERROR_MIN && status < HTTP_STATUS_SERVER_ERROR_MIN) {
                log.warn("[API RESPONSE] {} -> {}: {}", method, requestURI, status);
            } else {
                log.error("[API RESPONSE] {} -> {}: {}", method, requestURI, status);
            }
        } finally {
            MDC.remove("clientIp");
            MDC.remove("serverIp");
        }
    }

    private String getRequestURI(final HttpServletRequest request) {
        final String requestQueryParam = request.getQueryString();
        if (requestQueryParam == null || requestQueryParam.isBlank()) {
            return request.getRequestURI();
        }
        return String.format("%s?%s", request.getRequestURI(), requestQueryParam);
    }

    private String getClientIp(final HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
