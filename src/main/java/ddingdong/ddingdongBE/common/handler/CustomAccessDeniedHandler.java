package ddingdong.ddingdongBE.common.handler;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.common.exception.ExceptionResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        ExceptionResponse exceptionResponse = ExceptionResponse.of(HttpStatus.FORBIDDEN, ACCESS_DENIED.getText());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
    }
}
