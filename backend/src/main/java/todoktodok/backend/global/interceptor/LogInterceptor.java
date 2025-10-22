package todoktodok.backend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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
    private static final String SERVER_IP = getEc2PrivateIp();

    private static String getEc2PrivateIp() {
        final String localEnv = System.getenv("SPRING_PROFILES_ACTIVE");
        if ("local".equals(localEnv) || localEnv == null) {
            return "localhost";
        }

        try {
            final HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();

            final HttpRequest tokenRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://169.254.169.254/latest/meta-data/local-ipv4"))
                    .header("X-aws-ec2-metadata-token-ttl-seconds", "21600")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(1))
                    .build();

            final HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
            final String token = tokenResponse.body();

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://169.254.169.254/latest/api/token"))
                    .header("X-aws-ec2-metadata-token", token)
                    .GET()
                    .timeout(Duration.ofSeconds(1))
                    .build();

            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (final IOException | InterruptedException e) {
            log.error("EC2 private IP 확인 실패", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // InterruptedException 안전하게 처리
            }
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
