package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
                .name(this.clubName)
                .category(this.category)
                .tag(this.tag)
                .leader(this.leaderName)
                .score(Score.of(0)).build();
    }

}
