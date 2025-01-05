package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand.ImageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateFixZoneRequest(
        @NotNull
        @Schema(description = "제목")
        String title,
        @NotNull
        @Schema(description = "내용")
        String content,
        @Schema(description = "픽스존 이미지 정보 목록")
        List<ImageInfoRequest> images
) {

    public CreateFixZoneCommand toCommand(Long userId) {
        return new CreateFixZoneCommand(
                userId,
                title,
                content,
                images.stream()
                        .map(image -> new ImageInfo(image.id, image.order()))
                        .toList()
        );
    }

    public record ImageInfoRequest(
            @Schema(description = "이미지 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
            String id,
            @Schema(description = "이미지 순서", example = "1")
            int order
    ) {

    }

}
