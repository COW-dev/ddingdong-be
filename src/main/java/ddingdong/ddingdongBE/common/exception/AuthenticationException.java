package ddingdong.ddingdongBE.common.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException{

    private final ErrorMessage errorMessage;
    private final String message;

    public AuthenticationException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        this.message = errorMessage.getText();
    }
}
