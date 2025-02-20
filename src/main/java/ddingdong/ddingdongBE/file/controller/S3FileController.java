package ddingdong.ddingdongBE.file.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.api.S3FileAPi;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3FileController implements S3FileAPi {

    private final S3FileService s3FileService;

    @Override
    public UploadUrlResponse getPreSignedUrl(PrincipalDetails principalDetails, String fileName) {
        User user = principalDetails.getUser();
        LocalDateTime now = LocalDateTime.now();
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        GeneratePreSignedUrlRequestQuery query =
                s3FileService.generatePresignedUrlRequest(
                        new GeneratePreSignedUrlRequestCommand(now, user.getId(), decodedFileName));
        URL presingedUrl = s3FileService.getPresignedUrl(query.generatePresignedUrlRequest());
        return UploadUrlResponse.of(query, presingedUrl);
    }

    @Override
    public UploadUrlResponse getFormApplicationPreSignedUrl(String fileName) {
        LocalDateTime now = LocalDateTime.now();
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        GeneratePreSignedUrlRequestQuery query =
                s3FileService.generateDownloadPresignedUrlRequest(
                        new GeneratePreSignedUrlRequestCommand(now, 9999L, decodedFileName));
        URL presingedUrl = s3FileService.getPresignedUrl(query.generatePresignedUrlRequest());
        return UploadUrlResponse.of(query, presingedUrl);
    }
}
