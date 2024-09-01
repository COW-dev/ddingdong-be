package ddingdong.ddingdongBE.domain.scorehistory.entity;

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

    public static Score from(float value) {
        validateScore(value);
        return new Score(value);
    }

    private static void validateScore(float value) {
        if (value < 0.0F || value > 1000.0F) {
            throw new IllegalArgumentException(INVALID_CLUB_SCORE_VALUE.getText());
        }
    }

}
