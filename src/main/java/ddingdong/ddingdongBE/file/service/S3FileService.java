package ddingdong.ddingdongBE.file.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsClient;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsService;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileService {

    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Value("${spring.config.activate.on-profile}")
    private String serverProfile;

    private final AmazonS3Client amazonS3Client;

    public UploadUrlResponse generatePreSignedUrl(String fileName) {
        UUID fileId = UuidCreator.getTimeOrderedEpoch();
        String s3FilePath = createFilePath(fileName, fileId);

        Date expiration = setExpirationTime();
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
                    s3FilePath)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(expiration);

            URL uploadUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return UploadUrlResponse.of(uploadUrl.toString(), fileId.toString());
        } catch (AmazonServiceException e) {
            log.warn("AWS Service Error : {}", e.getMessage());
            throw new AwsService();
        } catch (AmazonClientException e) {
            log.warn("AWS Client Error : {}", e.getMessage());
            throw new AwsClient();
        }

    }

    public String getUploadedFileUrl(String fileName, String uploadFileName) {
        String region = amazonS3Client.getRegionName();
        String fileExtension = extractFileExtension(fileName);

        return String.format("https://%s.s3.%s.amazonaws.com/%s/%s/%s",
                bucketName,
                region,
                serverProfile,
                fileExtension,
                uploadFileName);
    }

    private Date setExpirationTime() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 5;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String createFilePath(String fileName, UUID uploadFileName) {
        String fileExtension = extractFileExtension(fileName);
        return String.format("%s/%s/%s", serverProfile, fileExtension, uploadFileName.toString());
    }

    private String extractFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

}
