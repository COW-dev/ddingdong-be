package ddingdong.ddingdongBE.auth.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {

    private String userId;

    private String password;

}
