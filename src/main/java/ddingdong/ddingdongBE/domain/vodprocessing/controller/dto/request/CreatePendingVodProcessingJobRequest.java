package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import jakarta.validation.constraints.NotNull;
import java.util.regex.Pattern;

public record CreatePendingVodProcessingJobRequest(
        @NotNull(message = "변환 작업 ID는 필수입니다")
        String convertJobId,
        @NotNull(message = "userId는 필수입니다.")
        String userId,
        @NotNull(message = "파일Id는 필수입니다.")
        String fileId
) {
    private static final Pattern UUID7_PATTERN = Pattern.compile(
            "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$"
    );

    public CreatePendingVodProcessingJobCommand toCommand() {
        if (!UUID7_PATTERN.matcher(fileId).matches()) {
            throw new IllegalArgumentException("[ERROR] fileId must be a valid UUID v7 format: " + fileId);
        }
        return new CreatePendingVodProcessingJobCommand(convertJobId, userId, fileId);
    }

}
