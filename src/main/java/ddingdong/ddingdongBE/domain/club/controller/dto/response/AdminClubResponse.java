package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import lombok.Getter;

@Getter
public class AdminClubResponse {

    private Long id;

    private String name;

    private String category;

    private int score;

    private AdminClubResponse(Long id, String name, String category, int score) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.score = score;
    }

    public static AdminClubResponse from(Club club) {
        return new AdminClubResponse(club.getId(), club.getName(), club.getCategory(), club.getScore().getValue());
    }

}
