package ddingdong.ddingdongBE.file.service.dto.query;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.UUID;

public record GeneratePreSignedUrlRequestQuery(
        PutObjectRequest putObjectRequest,
        UUID id,
        String contentType
) {

}
