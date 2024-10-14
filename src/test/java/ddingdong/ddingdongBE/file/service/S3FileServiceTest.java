package ddingdong.ddingdongBE.file.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class S3FileServiceTest extends TestContainerSupport {

    @Autowired
    private S3FileService s3FileService;

    private static final Pattern UUID7_PATTERN = Pattern.compile(
            "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$"
    );

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

        String[] split = query.key().split("/");
        String uploadName = split[split.length - 1];
        assertThat(query.generatePresignedUrlRequest())
                .satisfies(request -> {
                    assertThat(request.getContentType())
                            .as("Content type should be image/jpeg")
                            .isEqualTo("image/jpeg");

                    assertThat(request.getKey())
                            .as("Key should contain correct date, authId, and fileId")
                            .contains(String.format("%s/%d-%d-%d/%s/",
                                    "file", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), authId))
                            .contains(uploadName);
                });
        assertThat(Pattern.matches(UUID7_PATTERN.pattern(), uploadName)).isTrue();
        assertThat(query.contentType()).isEqualTo("image/jpeg");
    }

    @DisplayName("GeneratePreSignedUrlRequest(VIDEO)를 생성한다.")
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
        String[] split = query.key().split("/");
        String uploadName = split[split.length - 1];
        assertThat(query.generatePresignedUrlRequest())
                .satisfies(request -> assertThat(request.getKey())
                        .as("Key should contain correct date, authId, and fileId")
                        .contains(String.format("%s/%d-%d-%d/%s/",
                                "video", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), authId))
                        .contains(uploadName));

        assertThat(Pattern.matches(UUID7_PATTERN.pattern(), uploadName)).isTrue();
    }

}
