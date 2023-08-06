package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import java.util.List;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminClubResponse {

    private Long id;

    private String name;

    private String category;

    private int score;

    private List<String> imageUrls;

    @Builder
    private AdminClubResponse(Long id, String name, String category, int score, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.score = score;
        this.imageUrls = imageUrls;
    }

    @Builder
    public static AdminClubResponse of(Club club, List<String> imageUrls) {
        return AdminClubResponse.builder()
            .id(club.getId())
            .name(club.getName())
            .category(club.getCategory())
            .score(club.getScore().getValue())
            .imageUrls(imageUrls).build();
    }

}
