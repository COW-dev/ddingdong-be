package ddingdong.ddingdongBE.auth.controller.dto.request;

import lombok.Getter;

@Getter
public class SignInRequest {

    private String authId;

    private String password;

}
