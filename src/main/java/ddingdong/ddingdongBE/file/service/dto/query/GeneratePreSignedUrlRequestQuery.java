package ddingdong.ddingdongBE.file.service.dto.query;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public record GeneratePreSignedUrlRequestQuery(
        GeneratePresignedUrlRequest generatePresignedUrlRequest,
        String key,
        String contentType
) {

}
