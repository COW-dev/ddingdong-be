package ddingdong.ddingdongBE.common.handler;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.INVALID_PASSWORD;
import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NON_VALIDATED_TOKEN;
import static ddingdong.ddingdongBE.common.exception.ErrorMessage.valueOf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.common.exception.ErrorMessage;
import ddingdong.ddingdongBE.common.exception.ExceptionResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorMessage errorMessage = valueOf(request.getAttribute("exception").toString());

        if (errorMessage.equals(NON_VALIDATED_TOKEN)) {
            responseAuthenticationException(response, errorMessage);
            return;
        }
        responseAuthenticationException(response, INVALID_PASSWORD);
    }

    private void responseAuthenticationException(HttpServletResponse response, ErrorMessage errorMessage)
            throws IOException {
        ExceptionResponse exceptionResponse = ExceptionResponse.of(HttpStatus.UNAUTHORIZED,
                errorMessage.getText());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
    }
}
