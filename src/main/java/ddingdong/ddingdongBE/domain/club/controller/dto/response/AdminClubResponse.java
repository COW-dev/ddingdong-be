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

    private float score;

    private List<String> profileImageUrls;

    @Builder
    private AdminClubResponse(Long id, String name, String category, float score, List<String> profileImageUrls) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.score = score;
        this.profileImageUrls = profileImageUrls;
    }

    @Builder
    public static AdminClubResponse of(Club club, List<String> profileImageUrls) {
        return AdminClubResponse.builder()
            .id(club.getId())
            .name(club.getName())
            .category(club.getCategory())
            .score(club.getScore().getValue())
            .profileImageUrls(profileImageUrls).build();
    }

}
