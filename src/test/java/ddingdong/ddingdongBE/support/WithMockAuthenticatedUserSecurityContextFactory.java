package ddingdong.ddingdongBE.support;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAuthenticatedUserSecurityContextFactory implements
        WithSecurityContextFactory<WithMockAuthenticatedUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockAuthenticatedUser withMockPrincipalDetails) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .id(withMockPrincipalDetails.id())
                .userId(withMockPrincipalDetails.userId())
                .password(withMockPrincipalDetails.password())
                .role(Role.valueOf(withMockPrincipalDetails.role())).build();

        PrincipalDetails principalDetails = new PrincipalDetails(user);
        context.setAuthentication(new TestingAuthenticationToken(principalDetails, principalDetails.getPassword(),
                String.valueOf(principalDetails.getAuthorities())));
        return context;
    }
}
