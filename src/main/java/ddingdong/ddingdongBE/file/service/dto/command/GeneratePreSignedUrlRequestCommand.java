package ddingdong.ddingdongBE.file.service.dto.command;

import java.time.LocalDateTime;

public record GeneratePreSignedUrlRequestCommand(
        LocalDateTime generatedAt,
        String authId,
        String fileName
) {

}
