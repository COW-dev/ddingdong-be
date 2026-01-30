package ddingdong.ddingdongBE.file.service;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsClient;
import ddingdong.ddingdongBE.common.exception.AwsException.AwsService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final FileMetaDataService fileMetaDataService;

    public GeneratePreSignedUrlRequestQuery generatePresignedUrlRequest(GeneratePreSignedUrlRequestCommand command) {
        ContentType contentType = ContentType.fromExtension(extractFileExtension(command.fileName()));
        return buildPresignedUrlRequest(command, contentType);
    }

    public GeneratePreSignedUrlRequestQuery generateDownloadPresignedUrlRequest(GeneratePreSignedUrlRequestCommand command) {
        ContentType contentType = ContentType.OCTET_STREAM;
        return buildPresignedUrlRequest(command, contentType);
    }

    public URL getPresignedUrl(PutObjectRequest putObjectRequest) {
        try {
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(java.time.Duration.ofMillis(PRE_SIGNED_URL_EXPIRATION_TIME))
                    .putObjectRequest(putObjectRequest)
                    .build();
            
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            return presignedRequest.url();
        } catch (SdkServiceException e) {
            log.warn("AWS Service Error : {}", e.getMessage());
            throw new AwsService();
        } catch (SdkClientException e) {
            log.warn("AWS Client Error : {}", e.getMessage());
            throw new AwsClient();
        }
    }

    public UploadedFileUrlQuery getUploadedFileUrl(String key) {
        if (key == null) {
            return null;
        }
        String region = s3Client.serviceClientConfiguration().region().id();
        String[] splitKey = key.split("/");
        String originUrl = String.format(S3_URL_FORMAT, inputBucket, region) + key;
        String cdnUrl = FILE_CDN_URL + key;
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
        String region = s3Client.serviceClientConfiguration().region().id();

        String thumbnailOriginUrl = generateS3Url(outputBucket, region, "thumbnails/", fileId, ".0000000.jpg");
        String thumbnailCdnUrl = generateCdnUrl("thumbnails/", fileId, ".0000000.jpg");
        String videoOriginUrl = generateS3Url(outputBucket, region, "hls/", fileId, "_720.m3u8");
        String videoCdnUrl = generateCdnUrl("hls/", fileId, "_720.m3u8");

        return new UploadedVideoUrlQuery(thumbnailOriginUrl, thumbnailCdnUrl, videoOriginUrl, videoCdnUrl);
    }

    public String uploadMultipartFile(MultipartFile file, LocalDateTime dateTime, String directory) {
        UUID fileName = UuidCreator.getTimeOrderedEpoch();
        String extension = extractFileExtension(file.getOriginalFilename());
        ContentType contentType = ContentType.fromExtension(extension);

        String key = generateKey(contentType, dateTime, directory, fileName);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(inputBucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        } catch (SdkException e) {
            log.error("AWS Service Error : {}", e.getMessage());
            throw new AwsService();
        }
    }

    private GeneratePreSignedUrlRequestQuery buildPresignedUrlRequest(GeneratePreSignedUrlRequestCommand command, ContentType contentType) {
        UUID id = UuidCreator.getTimeOrderedEpoch();
        String key = generateKey(contentType, command, id);

        fileMetaDataService.create(FileMetaData.createPending(id, key, command.fileName()));

        PutObjectRequest request = createPutObjectRequest(key, contentType);
        return new GeneratePreSignedUrlRequestQuery(request, id, contentType.getMimeType());
    }

    private PutObjectRequest createPutObjectRequest(String key, ContentType contentType) {
        return PutObjectRequest.builder()
                .bucket(inputBucket)
                .key(key)
                .contentType(contentType.getMimeType())
                .build();
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

    private String generateKey(ContentType contentType, LocalDateTime dateTime, String directory, UUID uploadFileName) {
        return String.format("%s/%s/%s/%s/%s",
                serverProfile,
                contentType.getKeyMediaType(),
                formatDate(dateTime),
                directory,
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
        return S3FileService.VIDEO_CDN_URL + prefix + filename + suffix;
    }
}
