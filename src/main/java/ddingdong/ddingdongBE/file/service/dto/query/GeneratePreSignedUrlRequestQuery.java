package ddingdong.ddingdongBE.file.service.dto.query;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.UUID;

public record GeneratePreSignedUrlRequestQuery(
        GeneratePresignedUrlRequest generatePresignedUrlRequest,
        UUID fileId
) {

}
