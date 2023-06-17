package ddingdong.ddingdongBE.auth.service;

import ddingdong.ddingdongBE.auth.controller.dto.request.SignInRequest;
import ddingdong.ddingdongBE.domain.user.entity.User;

public interface AuthService {

    User registerClubUser(String userId, String password, String name);

    String signIn(SignInRequest request);

    String getUserRole();

}
