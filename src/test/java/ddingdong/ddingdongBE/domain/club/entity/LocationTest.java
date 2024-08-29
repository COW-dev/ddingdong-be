package ddingdong.ddingdongBE.domain.club.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LocationTest {

    @DisplayName("동아리방 위치는 S + 4~5자리 숫자 양식의 이름으로 생성할 수 있다.")
    @ParameterizedTest()
    @ValueSource(strings = {"S1350", "S10111"})
    void createLocation(String givenValue) {
        //given
        //when
        Location location = Location.from(givenValue);

        //then
        assertThat(location.getValue()).isEqualTo(givenValue);
    }

    @DisplayName("동아리방 위치는 S + 4~5자리 숫자 양식이 아닌 이름으로 생성시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1350", "A1350", "S1", "S100001"})
    void createLocationWithIllegalRegex(String givenValue) {
        //given
        //when //then
        assertThatThrownBy(() -> Location.from(givenValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 동아리 위치 형식입니다.");
    }

}
