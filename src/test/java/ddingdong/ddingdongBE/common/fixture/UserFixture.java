package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.user.entity.Role;
import ddingdong.ddingdongBE.domain.user.entity.User;

public class UserFixture {

    public static User createAdminUser(String encode) {
        return User.builder()
                .authId("admin123")
                .password(encode)
                .name("관리자")
                .role(Role.ADMIN)
                .build();
    }

    public static User createAdminUser() {
        return User.builder()
                .authId("admin123")
                .password("1234")
                .name("관리자")
                .role(Role.ADMIN)
                .build();
    }

    public static User createClubUser(String encode) {
        return User.builder()
                .authId("club123")
                .password(encode)
                .name("동아리 사용자")
                .role(Role.CLUB)
                .build();
    }

    public static User createClubUser() {
        return User.builder()
                .authId("club123")
                .password("1234")
                .name("동아리 사용자")
                .role(Role.CLUB)
                .build();
    }

    public static User createGeneralUser(String encode) {
        return User.builder()
                .authId("club123")
                .password(encode)
                .name("동아리 사용자")
                .role(Role.USER)
                .build();
    }

    public static User createGeneralUser() {
        return User.builder()
                .authId("club123")
                .password("1234")
                .name("동아리 사용자")
                .role(Role.USER)
                .build();
    }
}
