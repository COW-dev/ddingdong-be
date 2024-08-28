package ddingdong.ddingdongBE.domain.club.entity;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Location {

    private static final String LOCATION_REGEX = "^S[0-9]{4,5}";

    @Column(name = "location")
    private String value;

    private Location(String value) {
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
        Location location = (Location) o;
        return Objects.equals(getValue(), location.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    public static Location from(String value) {
        validateLocation(value);
        return Location.builder().value(value).build();
    }

    private static void validateLocation(String value) {
        if (!value.matches(LOCATION_REGEX)) {
            throw new IllegalArgumentException(ILLEGAL_CLUB_LOCATION_PATTERN.getText());
        }
    }
}
