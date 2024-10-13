package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "CreateClubRequest",
        description = "어드민 - 동아리 생성 요청"
)
public record CreateClubRequest(
        @Schema(description = "동아리명", example = "cow")
        @NotNull(message = "동아리명은 필수로 입력해야 합니다.")
        String clubName,
        @Schema(description = "카테고리", example = "사회연구")
        @NotNull(message = "카테고리는 필수로 입력해야 합니다.")
        String category,
        @Schema(description = "분과", example = "IT")
        @NotNull(message = "분과는 필수로 입력해야 합니다.")
        String tag,
        @Schema(description = "회장이름", example = "홍길동")
        @NotNull(message = "회장이름은 필수로 입력해야 합니다.")
        String leaderName,
        @Schema(description = "동아리계정", example = "abcd1234")
        @NotNull(message = "동아리계정은 필수로 입력해야 합니다.")
        String authId,
        @Schema(description = "비밀번호", example = "abcd1234")
        @NotNull(message = "비밀번호는 필수로 입력해야 합니다.")
        String password

) {

    public CreateClubCommand toCommand() {
        return CreateClubCommand.builder()
                .clubName(clubName)
                .category(category)
                .tag(tag)
                .leaderName(leaderName)
                .authId(authId)
                .password(password)
                .build();
    }

}
