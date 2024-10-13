package ddingdong.ddingdongBE.domain.club.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CreateClubCommand(
        String clubName,
        String category,
        String leaderName,
        String tag,
        String authId,
        String password

) {

    public Club toEntity(User user) {
        return Club.builder()
                .user(user)
                .name(clubName)
                .category(category)
                .tag(tag)
                .leader(leaderName)
                .location(Location.from("S0000"))
                .phoneNumber(PhoneNumber.from("010-0000-0000"))
                .score(Score.from(BigDecimal.ZERO)).build();
    }

}
