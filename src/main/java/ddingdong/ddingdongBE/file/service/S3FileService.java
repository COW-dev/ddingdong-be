package ddingdong.ddingdongBE.file.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsClient;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;
import java.net.URL;
import java.time.LocalDateTime;
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

    private static final String S3_URL_FORMAT = "https://%s.s3.%s.amazonaws.com/";
    private static final String FILE_CDN_URL = "https://ddn4vjj3ws13w.cloudfront.net/";
    private static final String VIDEO_CDN_URL = "https://d2syrtcctrfiup.cloudfront.net/";
    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 1000 * 60 * 5; // 5 minutes

    @Value("${spring.s3.input-bucket}")
    private String inputBucket;
    @Value("${spring.s3.output-bucket}")
    private String outputBucket;
    @Value("${spring.config.activate.on-profile}")
    private String serverProfile;

    private final AmazonS3Client amazonS3Client;
    private final FileMetaDataService fileMetaDataService;

    public GeneratePreSignedUrlRequestQuery generatePresignedUrlRequest(GeneratePreSignedUrlRequestCommand command) {
        UUID id = UuidCreator.getTimeOrderedEpoch();
        ContentType contentType = ContentType.fromExtension(extractFileExtension(command.fileName()));
        String key = generateKey(contentType, command, id);
        Date expiration = getExpirationTime();

        fileMetaDataService.create(FileMetaData.createPending(id, key, command.fileName()));

        GeneratePresignedUrlRequest request = createPresignedUrlRequest(key, contentType, expiration);
        return new GeneratePreSignedUrlRequestQuery(request, id, contentType.getMimeType());
    }

    public URL getPresignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {
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
        if (key == null) {
            return null;
        }
        String region = amazonS3Client.getRegionName();
        String[] splitKey = key.split("/");
        String originUrl = String.format(S3_URL_FORMAT, inputBucket, region) + key;
        String cdnUrl = FILE_CDN_URL +
                splitKey[splitKey.length - 3] + "/" +
                splitKey[splitKey.length - 2] + "/" +
                splitKey[splitKey.length - 1];
        return new UploadedFileUrlQuery(splitKey[splitKey.length - 1], originUrl, cdnUrl);
    }

    public UploadedFileUrlAndNameQuery getUploadedFileUrlAndName(String key, String fileName) {
        UploadedFileUrlQuery fileUrlQuery = getUploadedFileUrl(key);
        return new UploadedFileUrlAndNameQuery(
            fileUrlQuery.id(),
            fileName,
            fileUrlQuery.originUrl(),
            fileUrlQuery.cdnUrl()
        );
    }

    //TODO: video 피드 조회 시 수정 필요
    public UploadedVideoUrlQuery getUploadedVideoUrl(String key) {
        String fileId = extractFileId(key);
        String region = amazonS3Client.getRegionName();

        String thumbnailOriginUrl = generateS3Url(outputBucket, region, "thumbnails/", fileId, ".0000000.jpg");
        String thumbnailCdnUrl = generateCdnUrl("thumbnails/", fileId, ".0000000.jpg");
        String videoOriginUrl = generateS3Url(outputBucket, region, "hls/", fileId, "_720.m3u8");
        String videoCdnUrl = generateCdnUrl("hls/", fileId, "_720.m3u8");

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
                contentType.getKeyMediaType(),
                formatDate(command.generatedAt()),
                command.userId(),
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
