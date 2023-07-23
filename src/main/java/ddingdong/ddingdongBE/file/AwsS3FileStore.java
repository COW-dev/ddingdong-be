package ddingdong.ddingdongBE.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import ddingdong.ddingdongBE.file.dto.UploadFileDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AwsS3FileStore implements FileStore{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${spring.s3.bucket}")
    private String bucketName;


    @Override
    public List<UploadFileDto> storeFile(List<MultipartFile> multipartFile, String filePath) {
        List<UploadFileDto> storedFiles = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            String uploadFileName = file.getOriginalFilename();
            String storeFileName = createStoreFileName(uploadFileName);
            String keyName = filePath + storeFileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, file.getInputStream(), objectMetadata));
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패");
            }

            storedFiles.add(UploadFileDto.builder()
                    .storedFileName(storeFileName)
                    .uploadFileName(uploadFileName).build());
        }
        return storedFiles;
    }

    @Override
    public void deleteFile(String filePath, String uploadFileName) {
        String keyName = filePath + uploadFileName;
        amazonS3Client.deleteObject(bucketName, keyName);
    }

    @Override
    public String getImageUrlPrefix() {
        return "https://" + "." + bucketName + ".s3." + region + ".amazonaws.com/";
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}