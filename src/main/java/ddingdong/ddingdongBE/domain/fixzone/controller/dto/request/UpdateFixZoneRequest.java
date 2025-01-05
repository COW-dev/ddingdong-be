package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand.ImageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateFixZoneRequest(
        @NotNull
        @Schema(description = "제목")
        String title,
        @NotNull
        @Schema(description = "내용")
        String content,
        @Schema(description = "픽스존 이미지 정보 목록")
        List<ImageInfoRequest> images
) {

    public UpdateFixZoneCommand toCommand(Long fixZoneId) {
        return new UpdateFixZoneCommand(
                fixZoneId,
                title,
                content,
                (images != null) ?
                        images.stream()
                                .map(image -> new ImageInfo(image.id, image.order()))
                                .toList() :
                        List.of()
        );
    }

    public record ImageInfoRequest(
            @Schema(description = "이미지 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
            String id,
            @Schema(description = "이미지 순서", example = "1")
            @Min(value = 1, message = "이미지 순서는 1 이상이어야 합니다")
            int order
    ) {

    }

}
