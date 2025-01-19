package ddingdong.ddingdongBE.common.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockAuthenticatedUserSecurityContextFactory.class)
public @interface WithMockAuthenticatedUser {

    long id() default 1L;

    String authId() default "user";

    String role() default "USER";

    String password() default "password";

}
