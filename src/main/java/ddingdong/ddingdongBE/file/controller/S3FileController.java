package ddingdong.ddingdongBE.file.controller;

import ddingdong.ddingdongBE.file.api.S3FileAPi;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import ddingdong.ddingdongBE.file.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3FileController implements S3FileAPi {

    private final S3FileService s3FileService;

    @Override
    public UploadUrlResponse getUploadUrl(String fileName) {
        String uploadUrl = s3FileService.generatePreSignedUrl(fileName).toString();
        return UploadUrlResponse.from(uploadUrl);
    }
}
