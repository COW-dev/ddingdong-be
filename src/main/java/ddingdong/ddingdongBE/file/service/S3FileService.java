package ddingdong.ddingdongBE.file.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsClient;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsService;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileService {

    private static final String S3_URL_FORMAT = "https://%s.s3.%s.amazonaws.com/";
    private static final String FILE_CDN_URL = "https://ddn4vjj3ws13w.cloudfront.net";
    private static final String VIDEO_CDN_URL = "https://d2syrtcctrfiup.cloudfront.net";
    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 1000 * 60 * 5; // 5 minutes

    @Value("${spring.s3.input-bucket}")
    private String inputBucket;
    @Value("${spring.s3.output-bucket}")
    private String outputBucket;
    @Value("${spring.config.activate.on-profile}")
    private String serverProfile;

    private final AmazonS3Client amazonS3Client;

    public GeneratePreSignedUrlRequestQuery generatePreSignedUrlRequest(GeneratePreSignedUrlRequestCommand command) {
        UUID fileId = UuidCreator.getTimeOrderedEpoch();
        ContentType contentType = ContentType.fromExtension(extractFileExtension(command.fileName()));
        String key = generateKey(contentType, command, fileId);
        Date expiration = getExpirationTime();

        GeneratePresignedUrlRequest request = createPresignedUrlRequest(key, contentType, expiration);
        return new GeneratePreSignedUrlRequestQuery(request, key, contentType.getMimeType());
    }

    public URL getPresingedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {
        try {
            return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        } catch (AmazonServiceException e) {
            log.warn("AWS Service Error : {}", e.getMessage());
            throw new AwsService();
        } catch (AmazonClientException e) {
            log.warn("AWS Client Error : {}", e.getMessage());
            throw new AwsClient();
        }
    }

    public UploadedFileUrlQuery getUploadedFileUrl(String key) {
        String region = amazonS3Client.getRegionName();
        String originUrl = String.format(S3_URL_FORMAT, inputBucket, region) + key;
        String cdnUrl = FILE_CDN_URL + key;
        return new UploadedFileUrlQuery(originUrl, cdnUrl);
    }

    public UploadedVideoUrlQuery getUploadedVideoUrl(String key) {
        String fileId = extractFileId(key);
        String region = amazonS3Client.getRegionName();

        String thumbnailOriginUrl = generateS3Url(outputBucket, region, "thumbnail", fileId, ".jpg");
        String thumbnailCdnUrl = generateCdnUrl("thumbnail", fileId, ".jpg");
        String videoOriginUrl = generateS3Url(outputBucket, region, "hls", fileId, "_720.m3u8");
        String videoCdnUrl = generateCdnUrl("hls", fileId, "_720.m3u8");

        return new UploadedVideoUrlQuery(thumbnailOriginUrl, thumbnailCdnUrl, videoOriginUrl, videoCdnUrl);
    }

    private GeneratePresignedUrlRequest createPresignedUrlRequest(String key, ContentType contentType,
                                                                  Date expiration) {
        return new GeneratePresignedUrlRequest(inputBucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration)
                .withContentType(contentType.getMimeType());
    }

    private Date getExpirationTime() {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + PRE_SIGNED_URL_EXPIRATION_TIME);
        return expiration;
    }

    private String generateKey(ContentType contentType, GeneratePreSignedUrlRequestCommand command,
                               UUID uploadFileName) {
        return String.format("%s/%s/%s/%s/%s",
                serverProfile,
                contentType.isVideo() ? "video" : "file",
                formatDate(command.generatedAt()),
                command.authId(),
                uploadFileName.toString());
    }

    private String formatDate(LocalDateTime dateTime) {
        return String.format("%d-%d-%d", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
    }

    private String extractFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private String extractFileId(String key) {
        String[] splitKey = key.split("/");
        return splitKey[splitKey.length - 1];
    }

    private String generateS3Url(String bucket, String region, String prefix, String filename, String suffix) {
        return String.format(S3_URL_FORMAT + "%s", bucket, region, prefix) + filename + suffix;
    }

    private String generateCdnUrl(String prefix, String filename, String suffix) {
        return S3FileService.VIDEO_CDN_URL + "/" + prefix + filename + suffix;
    }
}
