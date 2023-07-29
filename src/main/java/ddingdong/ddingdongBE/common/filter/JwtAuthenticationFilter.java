package ddingdong.ddingdongBE.common.filter;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import com.auth0.jwt.exceptions.JWTVerificationException;
import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.auth.service.JwtAuthService;
import ddingdong.ddingdongBE.common.config.JwtConfig;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthService authService;
    private final JwtConfig config;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            verifyJwt(request);
        } catch (JWTVerificationException e) {
            request.setAttribute("exception", NON_VALIDATED_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    private void verifyJwt(HttpServletRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        String header = request.getHeader(config.getHeader());

        if (header == null || header.isBlank() || !header.startsWith(config.getPrefix())) {
            return;
        }

        String token = header
                .replace(config.getPrefix(), "")
                .trim();

        User user = authService.verify(token);

        if (user == null) {
            return;
        }

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                principalDetails.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }
}
