package ddingdong.ddingdongBE.domain.user.repository;

import static ddingdong.ddingdongBE.domain.user.entity.Role.*;
import static org.assertj.core.api.Assertions.*;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 아이디가 존재할 경우 true를 반환한다.")
    @Test
    void existsByAuthId() {
        //given
        String authId = "test1";

        User user1 = createUser("test1");
        User user2 = createUser("test2");
        userRepository.saveAll(List.of(user1, user2));

        //when
        boolean result = userRepository.existsByAuthId(authId);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("회원 아이디가 존재하지 않을 경우 false를 반환한다.")
    @Test
    void existsByUserIdWithNoneExistAuthId() {
        //given
        String authId = "test3";

        User user1 = createUser("test1");
        User user2 = createUser("test2");
        userRepository.saveAll(List.of(user1, user2));

        //when
        boolean result = userRepository.existsByAuthId(authId);

        //then
        assertThat(result).isFalse();
    }

    private User createUser(String authId) {
        return User.builder()
                .authId(authId)
                .role(CLUB)
                .name("동아리")
                .password("1234").build();
    }

}
