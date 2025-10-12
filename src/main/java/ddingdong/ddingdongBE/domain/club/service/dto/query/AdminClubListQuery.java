package ddingdong.ddingdongBE.domain.club.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminClubListQuery(
        Long id,
        String name,
        String category,
        BigDecimal score,
        UploadedFileUrlAndNameQuery profileImageUrlQuery
) {

    public static AdminClubListQuery of(Club club, UploadedFileUrlAndNameQuery profileImageUrlQuery) {
        return AdminClubListQuery.builder()
                .id(club.getId())
                .name(club.getName())
                .category(club.getCategory())
                .score(club.getScore().getValue())
                .profileImageUrlQuery(profileImageUrlQuery)
                .build();
    }

}
