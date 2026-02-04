package ddingdong.ddingdongBE.domain.pairgame.controller.dto.request;

import ddingdong.ddingdongBE.domain.pairgame.service.dto.command.CreatePairGameApplierCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreatePairGameApplierRequest (

    @NotNull(message = "응모자 이름은 필수 입력 사항입니다.")
    @Schema(description = "응모자 이름", example = "김띵동")
    String name,

    @NotNull(message = "응모자 학과는 필수 입력 사항입니다.")
    @Schema(description = "응모자 학과", example = "융합소프트웨어학부")
    String department,

    @NotNull(message = "응모자 학번은 필수 입력 사항입니다.")
    @Schema(description = "응모자 학번", example = "60000000")
    String studentNumber,

    @NotNull(message = "응모자 전화번호는 필수 입력 사항입니다.")
    @Schema(description = "응모자 전화번호", example = "010-0000-0000")
    String phoneNumber
    ) {
    public CreatePairGameApplierCommand toCommand(MultipartFile studentFeeImageFile) {
        return CreatePairGameApplierCommand.builder()
                .name(name)
                .department(department)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .studentFeeImageFile(studentFeeImageFile)
                .build();
    }
}
