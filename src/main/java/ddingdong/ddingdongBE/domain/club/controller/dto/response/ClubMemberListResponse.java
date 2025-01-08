package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.ClubMemberListQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ClubMemberResponse",
        description = "중앙동아리 - 동아리원 조회 응답"
)
public record ClubMemberListResponse(
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

    public static ClubMemberListResponse from(ClubMemberListQuery query) {
        return new ClubMemberListResponse(
                query.id(),
                query.name(),
                query.studentNumber(),
                query.phoneNumber(),
                query.position(),
                query.department()
        );
    }

}
