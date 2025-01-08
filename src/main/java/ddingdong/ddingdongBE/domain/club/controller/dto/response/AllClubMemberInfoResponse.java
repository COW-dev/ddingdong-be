package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
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
        List<ClubMemberListResponse> responses = query.clubMembers().stream()
            .map(ClubMemberListResponse::from)
            .toList();
        return new AllClubMemberInfoResponse(query.clubName(), responses);
    }

    @Schema(
        name = "ClubMemberResponse",
        description = "중앙동아리 - 동아리원 조회 응답"
    )
    record ClubMemberListResponse(
        @Schema(description = "식별자", example = "1")
        Long id,
        @Schema(description = "이름", example = "홍길동")
        String name,
        @Schema(description = "학번", example = "60001111")
        String studentNumber,
        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "동아리원 역할",
            example = "LEADER",
            allowableValues = {"LEADER", "EXECUTION", "MEMBER"}
        )
        String position,
        @Schema(description = "학과", example = "학과")
        String department
    ) {

        public static ClubMemberListResponse from(ClubMember clubMember) {
            return new ClubMemberListResponse(
                clubMember.getId(),
                clubMember.getName(),
                clubMember.getStudentNumber(),
                clubMember.getPhoneNumber(),
                clubMember.getPosition().getName(),
                clubMember.getDepartment()
            );
        }

    }
}
