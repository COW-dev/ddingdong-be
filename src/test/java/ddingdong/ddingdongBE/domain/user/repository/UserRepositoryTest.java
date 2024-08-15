package ddingdong.ddingdongBE.domain.user.repository;

import static ddingdong.ddingdongBE.domain.user.entity.Role.*;
import static org.assertj.core.api.Assertions.*;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

class UserRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 아이디가 존재할 경우 true를 반환한다.")
    @Test
    void existsByUserId() {
        //given
        String userId = "test1";

        User user1 = createUser("test1");
        User user2 = createUser("test2");
        userRepository.saveAll(List.of(user1, user2));

        //when
        boolean result = userRepository.existsByUserId(userId);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("회원 아이디가 존재하지 않을 경우 false를 반환한다.")
    @Test
    void existsByUserIdWithNoneExistUserId() {
        //given
        String userId = "test3";

        User user1 = createUser("test1");
        User user2 = createUser("test2");
        userRepository.saveAll(List.of(user1, user2));

        //when
        boolean result = userRepository.existsByUserId(userId);

        //then
        assertThat(result).isFalse();
    }

    private User createUser(String userId) {
        return User.builder()
                .userId(userId)
                .role(CLUB)
                .name("동아리")
                .password("1234").build();
    }

}
