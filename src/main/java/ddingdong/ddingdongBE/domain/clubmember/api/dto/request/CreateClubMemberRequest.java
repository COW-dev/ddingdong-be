package ddingdong.ddingdongBE.domain.clubmember.api.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Position;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.CreateClubMemberCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(
        name = "CreateClubMemberRequest",
        description = "동아리원 정보 생성 요청"
)
@Builder
public record CreateClubMemberRequest(

        @Schema(description = "이름", example = "홍길동")
        @NotNull(message = "이름은 필수로 입력해야 합니다.")
        String name,

        @Schema(description = "학번", example = "60001234")
        @NotNull(message = "학번은 필수로 입력해야 합니다.")
        String studentNumber,

        @Schema(description = "전화번호", example = "010-1234-5678")
        @NotNull(message = "전화번호는 필수로 입력해야 합니다.")
        String phoneNumber,

        @Schema(description = "동아리원 역할",
                example = "LEADER",
                allowableValues = {"LEADER", "EXECUTION", "MEMBER"}
        )
        @NotNull(message = "역할은 필수로 입력해야 합니다.")
        String position,

        @Schema(description = "학과(부)", example = "융합소프트웨어학부")
        @NotNull(message = "학과(부)는 필수로 입력해야 합니다.")
        String department
) {

    public CreateClubMemberCommand toCommand(Long userId) {
        return CreateClubMemberCommand.builder()
                .userId(userId)
                .name(name)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .position(Position.from(position))
                .department(department)
                .build();
    }
}
