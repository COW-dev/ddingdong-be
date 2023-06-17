package ddingdong.ddingdongBE.domain.user.entity;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @DisplayName("영어와 숫자를 포함한 8자 이상인 비밀번호를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a1234567", "A1234567", "abcdegf1", "abcd1234567"})
    void createPassword(String givenPassword) {
        //given
        //when
        Password password = Password.of(givenPassword);

        //then
        assertThat(password.getValue()).isEqualTo(givenPassword);
    }


    @DisplayName("영어와 숫자를 포함한 8자 이상이 아닌 양식으로 비밀번호 생성 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a1", "abcdefgh", "12345678", ""})
    void createPasswordWithIllegalRegex(String givenPassword) {
        //given
        //when //then
        assertThatThrownBy(() -> Password.of(givenPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 비밀번호 양식이 아닙니다.");
    }

}
