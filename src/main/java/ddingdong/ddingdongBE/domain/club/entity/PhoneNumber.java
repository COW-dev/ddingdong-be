package ddingdong.ddingdongBE.domain.club.entity;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
public class PhoneNumber {

    private static final String PHONE_NUMBER_REGEX = "010-\\d{3,4}-\\d{4}";

    @Column(name = "phone_number")
    private String number;

    private PhoneNumber(String number) {
        validate(number);
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(getNumber(), that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }

    public static PhoneNumber of(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

    private void validate(String number) {
        if (!number.matches(PHONE_NUMBER_REGEX)) {
            throw new IllegalArgumentException(ILLEGAL_CLUB_PHONE_NUMBER_PATTERN.getText());
        }
    }
}
