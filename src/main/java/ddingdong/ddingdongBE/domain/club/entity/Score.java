package ddingdong.ddingdongBE.domain.club.entity;

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
    private int value;

    private Score(int value) {
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

    public static Score of(int value) {
        validateScore(value);
        return new Score(value);
    }

    private static void validateScore(int value) {
        if (value < 0 || value > 1000) {
            throw new IllegalArgumentException("동아리 점수는 0 ~ 999점 입니다.");
        }
    }

}
