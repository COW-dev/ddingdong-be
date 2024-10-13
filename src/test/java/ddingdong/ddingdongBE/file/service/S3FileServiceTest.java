package ddingdong.ddingdongBE.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3Client;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @DisplayName("GeneratePreSignedUrlRequest(FILE)를 생성한다.")
    @Test
    void generateFILEPreSignedUrlRequest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        String authId = "test";
        String fileName = "image.jpg";
        GeneratePreSignedUrlRequestCommand command =
                new GeneratePreSignedUrlRequestCommand(now, authId, fileName);

        //when
        GeneratePreSignedUrlRequestQuery query = s3FileService.generatePreSignedUrlRequest(command);

        //then
        Pattern UUID7_PATTERN = Pattern.compile(
                "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$"
        );
        assertThat(query.generatePresignedUrlRequest())
                .satisfies(request -> {
                    assertThat(request.getContentType())
                            .as("Content type should be image/jpeg")
                            .isEqualTo("image/jpeg");

                    assertThat(request.getKey())
                            .as("Key should contain correct date, authId, and fileId")
                            .contains(String.format("%s/%d-%d-%d/%s/",
                                    "file", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), authId))
                            .contains(query.fileId().toString());
                });

        assertThat(Pattern.matches(UUID7_PATTERN.pattern(), query.fileId().toString())).isTrue();
    }

    @DisplayName("GeneratePreSignedUrlRequest(VIDEO"
            + ")를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"video.mp4", "video.webm", "video.mov"})
    void generateVIDEOPreSignedUrlRequest(String fileName) {
        //given
        LocalDateTime now = LocalDateTime.now();
        String authId = "test";
        GeneratePreSignedUrlRequestCommand command =
                new GeneratePreSignedUrlRequestCommand(now, authId, fileName);

        //when
        GeneratePreSignedUrlRequestQuery query = s3FileService.generatePreSignedUrlRequest(command);

        //then
        Pattern UUID7_PATTERN = Pattern.compile(
                "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$"
        );
        assertThat(query.generatePresignedUrlRequest())
                .satisfies(request -> assertThat(request.getKey())
                        .as("Key should contain correct date, authId, and fileId")
                        .contains(String.format("%s/%d-%d-%d/%s/",
                                "video", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), authId))
                        .contains(query.fileId().toString()));

        assertThat(Pattern.matches(UUID7_PATTERN.pattern(), query.fileId().toString())).isTrue();
    }

    @DisplayName("s3 uploadedFileUrl을 조회한다.")
    @Test
    void getUploadedFileUrl() {
        //given
        String fileName = "image.jpg";
        String uploadFileName = "test";

        when(amazonS3Client.getRegionName()).thenReturn("ap-northeast-2");

        ReflectionTestUtils.setField(s3FileService, "bucketName", "test");
        ReflectionTestUtils.setField(s3FileService, "serverProfile", "test");

        //when
        String uploadedFileUrl = s3FileService.getUploadedFileUrl(fileName, uploadFileName);

        //then
        Assertions.assertThat(uploadedFileUrl).isEqualTo("https://test.s3.ap-northeast-2.amazonaws.com/test/jpg/test");
    }

}
