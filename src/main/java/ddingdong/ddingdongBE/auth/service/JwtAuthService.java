package ddingdong.ddingdongBE.auth.service;


import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.common.config.JwtConfig;
import ddingdong.ddingdongBE.common.exception.AuthenticationException;
import ddingdong.ddingdongBE.domain.user.entity.Password;
import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.Collection;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtAuthService implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Override
    public User registerClubUser(String userId, String password, String name) {
        checkExistUserId(userId);

        User clubUser = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(Password.of(password).getValue()))
                .name(name)
                .role(Role.CLUB)
                .build();
        return userRepository.save(clubUser);
    }

    @Override
    public String signIn(SignInRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new AuthenticationException(UNREGISTER_ID));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException(INVALID_PASSWORD);
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
                .orElseThrow(() -> new IllegalArgumentException("USER_ROLE이 존재하지 않습니다."))
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
        if (userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException(ALREADY_EXIST_CLUB_ID.getText());
        }
    }
}
