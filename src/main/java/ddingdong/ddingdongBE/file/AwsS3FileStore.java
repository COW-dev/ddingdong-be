package ddingdong.ddingdongBE.file;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import ddingdong.ddingdongBE.file.service.dto.UploadFileDto;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

//TODO: 리팩토링 후 제거
@Component
@RequiredArgsConstructor
public class AwsS3FileStore implements FileStore {

	private final S3Client s3Client;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${spring.s3.input-bucket}")
	private String bucketName;

	@Override
	public List<UploadFileDto> storeFile(List<MultipartFile> files, String fileType, String filePath) {
		List<UploadFileDto> storedFiles = new ArrayList<>();
		for (MultipartFile file : files) {
			String uploadFileName = file.getOriginalFilename();
			String storeFileName = createStoreFileName(uploadFileName);
			String keyName = fileType + filePath + storeFileName;

			try {
				PutObjectRequest putObjectRequest = PutObjectRequest.builder()
						.bucket(bucketName)
						.key(keyName)
						.contentType(file.getContentType())
						.contentLength(file.getSize())
						.build();
				
				s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
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
	public List<UploadFileDto> storeDownloadableFile(List<MultipartFile> files, String fileType,
		String filePath) {
		List<UploadFileDto> storedFiles = new ArrayList<>();
		for (MultipartFile file : files) {
			String uploadFileName = file.getOriginalFilename();
			String storeFileName = createStoreFileName(uploadFileName);
			String keyName = fileType + filePath + storeFileName;

			try {
				assert uploadFileName != null;
				String contentDisposition = "attachment; filename=" + "\"" + URLEncoder.encode(uploadFileName, StandardCharsets.UTF_8) + "\"";
				
				PutObjectRequest putObjectRequest = PutObjectRequest.builder()
						.bucket(bucketName)
						.key(keyName)
						.contentType(file.getContentType())
						.contentLength(file.getSize())
						.contentDisposition(contentDisposition)
						.build();
				
				s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
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
	public void deleteFile(String fileType, String filePath, String uploadFileName) {
		String keyName = fileType + filePath + uploadFileName;
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(keyName)
				.build();
		s3Client.deleteObject(deleteObjectRequest);
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
