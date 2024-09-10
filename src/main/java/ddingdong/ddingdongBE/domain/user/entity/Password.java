package ddingdong.ddingdongBE.domain.user.entity;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import java.util.Objects;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";

    @Column(name = "password")
    private String value;

    private Password(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password = (Password) o;
        return Objects.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }


    public static Password of(String value) {
        validatePassword(value);
        return new Password(value);
    }

    private static void validatePassword(String value) {
        if (!value.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException(ILLEGAL_PASSWORD_PATTERN.getText());
        }
    }
}
