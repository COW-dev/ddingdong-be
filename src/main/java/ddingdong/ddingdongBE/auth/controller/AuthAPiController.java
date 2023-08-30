package ddingdong.ddingdongBE.auth.controller;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.auth.controller.dto.response.SignInResponse;
import ddingdong.ddingdongBE.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/auth")
public class AuthAPiController {

    private final AuthService authService;

    @PostMapping(value = "sign-in")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        String authorizationHeader = authService.signIn(request);
        String role = authService.getUserRole();

        return SignInResponse.from(authorizationHeader, role);
    }

}
