package ddingdong.ddingdongBE.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_URI = List.of(
            "/actuator/**",
            "/swagger-ui/**",
            "/api-docs",
            "/favicon.ico"
    );

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        if (isExcluded(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String requestId = createRequestId();
        final long startTime = System.currentTimeMillis();

        logRequest(request, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            final long duration = System.currentTimeMillis() - startTime;
            logResponse(request, response, requestId, duration);
            MDC.remove("request_id");
        }
    }

    private String createRequestId() {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("request_id", requestId);
        return requestId;
    }

    private void logRequest(HttpServletRequest request, String requestId) {
        log.info("[{}] → {} {} | IP: {}",
                requestId,
                request.getMethod(),
                getFullRequestUrl(request),
                getClientIpAddress(request)
        );
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response,
            String requestId, long duration) {
        log.info("[{}] ← {} | Status: {} | {}ms",
                requestId,
                request.getMethod(),
                response.getStatus(),
                duration
        );
    }

    private String getFullRequestUrl(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return queryString != null ? request.getRequestURI() + "?" + queryString : request.getRequestURI();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        // AWS ALB에서 설정하는 X-Forwarded-For 헤더 확인
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // 첫 번째 IP가 실제 클라이언트 IP
            return xForwardedFor.split(",")[0].trim();
        }

        // fallback: Remote Address
        return request.getRemoteAddr();
    }

    private boolean isExcluded(String requestURI) {
        return EXCLUDE_URI.stream()
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestURI));
    }
}
