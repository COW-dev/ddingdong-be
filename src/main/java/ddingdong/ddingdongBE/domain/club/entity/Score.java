package ddingdong.ddingdongBE.domain.club.entity;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

    @Column(name = "score")
    private float value;

    private Score(float value) {
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
        Score score = (Score) o;
        return getValue() == score.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    public static Score of(float value) {
        value = roundToThirdPoint(value);
        return new Score(value);
    }

    private static float roundToThirdPoint(float value) {
        return Math.round(value * 1000.0) / 1000.0F;
    }

}
