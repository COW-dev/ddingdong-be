package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;

public class UserFixture {

    public static User createClubUser() {
        return User.builder()
                .name("랜덤 이름")
                .role(Role.CLUB)
                .build();
    }
}
