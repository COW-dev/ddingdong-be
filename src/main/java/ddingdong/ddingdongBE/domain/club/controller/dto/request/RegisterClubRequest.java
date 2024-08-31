package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class RegisterClubRequest {

    private String clubName;

    private String category;

    private String leaderName;

    private String tag;

    private String userId;

    private String password;

    public Club toEntity(User user) {
        return Club.builder()
                .user(user)
                .name(clubName)
                .category(category)
                .tag(tag)
                .leader(leaderName)
                .location(Location.from("S0000"))
                .phoneNumber(PhoneNumber.from("010-0000-0000"))
                .score(Score.from(new BigDecimal(0))).build();
    }

}
