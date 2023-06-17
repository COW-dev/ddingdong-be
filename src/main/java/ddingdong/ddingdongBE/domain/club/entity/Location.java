package ddingdong.ddingdongBE.domain.club.entity;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    private static final String LOCATION_REGEX = "^S[0-9]{4,5}";

    private String location;

    private Location(String location) {
        validateLocation(location);
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location1 = (Location) o;
        return Objects.equals(getLocation(), location1.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocation());
    }

    public static Location of(String location) {

        return new Location(location);
    }

    private void validateLocation(String location) {
        if (!location.matches(LOCATION_REGEX)) {
            throw new IllegalArgumentException("올바르지 않은 동아리방 위치 양식입니다.");
        }
    }
}
