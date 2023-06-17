package ddingdong.ddingdongBE.auth.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {

    private final String token;
    private final String role;

    @Builder
    private SignInResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public static SignInResponse from(String token, String role) {
        return SignInResponse.builder()
                .token(token)
                .role(role).build();
    }
}
