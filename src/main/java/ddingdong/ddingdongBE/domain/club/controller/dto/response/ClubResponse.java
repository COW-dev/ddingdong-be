package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubResponse {

    private Long id;

    private String name;

    private String category;

    private String tag;

    private boolean isRecruit;

    @Builder
    public ClubResponse(Long id, String name, String category, String tag, boolean isRecruit) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tag = tag;
        this.isRecruit = isRecruit;
    }

    public static ClubResponse of(Club club, boolean isRecruit) {
        return ClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .category(club.getCategory())
                .tag(club.getTag())
                .isRecruit(isRecruit).build();
    }

}
