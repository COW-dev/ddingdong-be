package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.AllClubMemberInfoQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AllClubMemberInfoResponse(
    @Schema(name = "동아리명", example = "COW")
    String clubName,
    @ArraySchema(schema = @Schema(implementation = ClubMemberListResponse.class))
    List<ClubMemberListResponse> clubMembers
) {

    public static AllClubMemberInfoResponse from(AllClubMemberInfoQuery query) {
        List<ClubMemberListResponse> responses = query.clubMemberListQueries().stream()
            .map(ClubMemberListResponse::from)
            .toList();
        return new AllClubMemberInfoResponse(query.clubName(), responses);
    }
}
