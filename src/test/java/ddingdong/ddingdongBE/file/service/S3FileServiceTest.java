package ddingdong.ddingdongBE.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class S3FileServiceTest {

    @Mock
    private AmazonS3Client amazonS3Client;

    @InjectMocks
    private S3FileService s3FileService;

    @DisplayName("presignedUrl을 생성한다.")
    @Test
    void generatePreSignedUrl() throws MalformedURLException {
        //given
        String fileName = "image.jpg";

        URL expectedUrl = new URL("https://test-bucket.s3.amazonaws.com/test/jpg/image.jpg");
        given(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).willReturn(expectedUrl);

        //when
        UploadUrlResponse uploadUrlResponse = s3FileService.generatePreSignedUrl(fileName);

        //then
        Pattern UUID7_PATTERN = Pattern.compile(
                "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$"
        );
        assertThat(uploadUrlResponse.uploadUrl()).isEqualTo(expectedUrl.toString());
        assertThat(Pattern.matches(UUID7_PATTERN.pattern(), uploadUrlResponse.fileId().toString())).isTrue();
    }

    @DisplayName("s3 uploadedFileUrl을 조회한다.")
    @Test
    void getUploadedFileUrl() {
        //given
        String fileName = "image.jpg";
        UUID fileId = UuidCreator.getTimeOrderedEpoch();

        when(amazonS3Client.getRegionName()).thenReturn("ap-northeast-2");

        ReflectionTestUtils.setField(s3FileService, "bucketName", "test");
        ReflectionTestUtils.setField(s3FileService, "serverProfile", "test");

        //when
        String uploadedFileUrl = s3FileService.getUploadedFileUrl(fileName, fileId);

        //then
        String uploadFileName = fileId.toString();
        Assertions.assertThat(uploadedFileUrl)
                .isEqualTo("https://test.s3.ap-northeast-2.amazonaws.com/test/jpg/" + uploadFileName);
    }

}
