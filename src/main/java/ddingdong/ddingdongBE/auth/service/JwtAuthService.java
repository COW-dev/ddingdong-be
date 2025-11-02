package ddingdong.ddingdongBE.auth.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.common.config.JwtConfig;
import ddingdong.ddingdongBE.common.exception.AuthenticationException.InvalidPassword;
import ddingdong.ddingdongBE.common.exception.AuthenticationException.NonExistUserRole;
import ddingdong.ddingdongBE.common.exception.AuthenticationException.UnRegisteredId;
import ddingdong.ddingdongBE.common.exception.RegisterClubException.AlreadyExistClubId;
import ddingdong.ddingdongBE.domain.user.entity.Password;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.Collection;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JwtAuthService implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Override
    public User registerClubUser(String authId, String password, String name) {
        checkExistUserId(authId);

        User clubUser = User.builder()
                .authId(authId)
                .password(passwordEncoder.encode(Password.of(password).getValue()))
                .name(name)
                .role(Role.CLUB)
                .build();
        return userRepository.save(clubUser);
    }

    @Override
    public String signIn(SignInRequest request) {
        log.info("사용자 아이디 요청 : {}", request.authId());
        User user = userRepository.findByAuthId(request.authId())
                .orElseThrow(UnRegisteredId::new);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPassword();
        }
        PrincipalDetails principalDetails = new PrincipalDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                principalDetails.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        return createAuthorizationHeader(user);
    }

    @Override
    public String getUserRole() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();

        return authorities.stream()
                .findFirst()
                .orElseThrow(NonExistUserRole::new)
                .getAuthority();
    }

    public User verify(String token) {
        JWTVerifier verifier = JWT
                .require(jwtConfig.getAlgorithm())
                .build();

        Long id = verifier
                .verify(token)
                .getClaim("id")
                .asLong();

        return userRepository
                .findById(id)
                .orElse(null);
    }

    private String createAuthorizationHeader(User user) {
        String jwt = createJwt(user);
        return String.format("%s %s", jwtConfig.getPrefix(), jwt);
    }

    private String createJwt(User user) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + jwtConfig.getExpiration() * 1000L);

        return JWT
                .create()
                .withIssuer(jwtConfig.getIssuer())
                .withIssuedAt(now)
                .withExpiresAt(expireAt)
                .withClaim("id", user.getId())
                .sign(Algorithm.HMAC512(jwtConfig.getSecret()));
    }


    private void checkExistUserId(String userId) {
        if (userRepository.existsByAuthId(userId)) {
            throw new AlreadyExistClubId();
        }
    }
}
