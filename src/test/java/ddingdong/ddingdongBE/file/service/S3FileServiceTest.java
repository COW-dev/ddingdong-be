package ddingdong.ddingdongBE.file.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.file.service.dto.command.GeneratePreSignedUrlRequestCommand;
import ddingdong.ddingdongBE.file.service.dto.query.GeneratePreSignedUrlRequestQuery;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
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
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    private static final Pattern UUID7_PATTERN = Pattern.compile(
        "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$"
    );

    @DisplayName("GeneratePreSignedUrlRequest(FILE)를 생성한다.")
    @Test
    void generatePreSignedUrlRequest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Long userId = 1L;
        String fileName = "image.jpg";
        GeneratePreSignedUrlRequestCommand command =
            new GeneratePreSignedUrlRequestCommand(now, userId, fileName);

        //when
        GeneratePreSignedUrlRequestQuery query = s3FileService.generatePresignedUrlRequest(command);

        //then
        UUID fileId = query.id();
        Optional<FileMetaData> createdFileMetaData =
            fileMetaDataRepository.findById(fileId);

        assertThat(query.putObjectRequest())
            .satisfies(request -> {
                assertThat(request.contentType())
                    .as("Content type should be image/jpeg")
                    .isEqualTo("image/jpeg");

                assertThat(request.key())
                    .as("Key should contain correct date, userId, and fileId")
                    .contains(String.format("%s/%d-%d-%d/%s/",
                        "IMAGE", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), userId))
                    .contains(fileId.toString());
            });
        assertThat(Pattern.matches(UUID7_PATTERN.pattern(), fileId.toString())).isTrue();
        assertThat(query.contentType()).isEqualTo("image/jpeg");
        assertThat(createdFileMetaData).isPresent();
        assertThat(createdFileMetaData.get())
            .extracting("fileKey", "fileName", "fileStatus")
            .containsExactly(query.putObjectRequest().key(), fileName, PENDING);
    }

    @DisplayName("GeneratePreSignedUrlRequest(VIDEO)를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"video.mp4", "video.webm", "video.mov"})
    void generateVIDEOPreSignedUrlRequest(String fileName) {
        //given
        LocalDateTime now = LocalDateTime.now();
        Long userId = 1L;
        GeneratePreSignedUrlRequestCommand command =
            new GeneratePreSignedUrlRequestCommand(now, userId, fileName);

        //when
        GeneratePreSignedUrlRequestQuery query = s3FileService.generatePresignedUrlRequest(command);

        //then
        UUID id = query.id();

        assertThat(query.putObjectRequest())
            .satisfies(request -> {
                assertThat(request.contentType())
                    .as("Content type should match the video's MIME type")
                    .isEqualTo(expectedContentType(fileName));
                assertThat(request.key())
                    .as("Key should contain correct date, userId, and fileId")
                    .contains(String.format("%s/%d-%d-%d/%s/",
                        "VIDEO", now.getYear(), now.getMonthValue(), now.getDayOfMonth(), userId))
                    .contains(id.toString());

                assertThat(Pattern.matches(UUID7_PATTERN.pattern(), id.toString())).isTrue();
            });

    }

    private String expectedContentType(String fileName) {
        if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".webm")) {
            return "video/webm";
        } else if (fileName.endsWith(".mov")) {
            return "video/quicktime";
        } else {
            throw new IllegalArgumentException("Unsupported video format");
        }
    }
}
