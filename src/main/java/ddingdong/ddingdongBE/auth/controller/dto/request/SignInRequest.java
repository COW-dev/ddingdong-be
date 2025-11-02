package ddingdong.ddingdongBE.auth.controller.dto.request;


public record SignInRequest(
        String authId,
        String password
) {

}
