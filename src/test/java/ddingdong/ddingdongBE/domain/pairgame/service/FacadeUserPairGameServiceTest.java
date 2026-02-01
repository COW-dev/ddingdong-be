package ddingdong.ddingdongBE.domain.pairgame.service;

import ddingdong.ddingdongBE.common.exception.FileException.UploadedFileNotFoundException;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.pairgame.entity.PairGameApplier;
import ddingdong.ddingdongBE.domain.pairgame.repository.PairGameRepository;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.command.CreatePairGameApplierCommand;
import ddingdong.ddingdongBE.domain.pairgame.service.dto.query.PairGameMetaDataQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class FacadeUserPairGameServiceTest extends TestContainerSupport {

    @Autowired
    private FacadeUserPairGameService facadeUserPairGameService;

    @Autowired
    private PairGameRepository pairGameRepository;

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    private ClubRepository clubRepository;

    @MockitoBean
    private S3FileService s3FileService;

    @Test
    @DisplayName("유저: 학번 중복 검사 후 응모자를 생성할 수 있다.")
    void createPairGameApplier_Success() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
        CreatePairGameApplierCommand command = new CreatePairGameApplierCommand(
                "김띵동", "융합소프트웨어학부", "60112233", "010-1234-5678", file
        );
        given(s3FileService.uploadMultipartFile(eq(file), any(LocalDateTime.class), eq("pair-game")))
                .willReturn("s3-uploaded-file-key");

        // when
        facadeUserPairGameService.createPairGameApplier(command);

        // then
        List<PairGameApplier> appliers = pairGameRepository.findAll();
        assertThat(appliers).hasSize(1);

        PairGameApplier savedApplier = appliers.get(0);
        assertThat(savedApplier.getName()).isEqualTo("김띵동");
        assertThat(savedApplier.getStudentNumber()).isEqualTo("60112233");
    }

    @Test
    @DisplayName("유저: 응모자 생성 요청에 파일이 없으면 예외가 발생한다.")
    void createPairGameApplier_Fail_NoFile() {
        // given
        CreatePairGameApplierCommand command = new CreatePairGameApplierCommand(
                "김띵동", "융합소프트웨어학부", "60112233", "010-1234-5678", null
        );

        // when & then
        assertThatThrownBy(() -> facadeUserPairGameService.createPairGameApplier(command))
                .isInstanceOf(UploadedFileNotFoundException.class);
    }

    @Test
    @DisplayName("유저: 동아리 로고 이미지 URL과 이름을 랜덤으로 18개 조회할 수 있다.")
    void getPairGameMetaData_Integration() {
        // given
        List<Club> clubs = IntStream.range(0, 20)
                .mapToObj(i -> Club.builder()
                        .name("동아리" + i)
                        .leader("회장" + i)
                        .build())
                .toList();
        clubRepository.saveAll(clubs);

        List<FileMetaData> metaDataList = IntStream.range(0, 20)
                .mapToObj(i -> FileMetaData.builder()
                        .id(UUID.randomUUID())
                        .domainType(DomainType.CLUB_PROFILE)
                        .entityId(clubs.get(i).getId())
                        .fileKey("key" + i)
                        .fileName("test.jpg")
                        .fileStatus(FileStatus.COUPLED)
                        .build())
                .toList();
        fileMetaDataRepository.saveAll(metaDataList);

        given(s3FileService.getUploadedFileUrl(any()))
                .willReturn(new UploadedFileUrlQuery("id", "originUrl", "cdnUrl"));

        // when
        PairGameMetaDataQuery result = facadeUserPairGameService.getPairGameMetaData();

        // then
        assertThat(result.metaData()).hasSize(18);
        String firstClubName = result.metaData().get(0).clubName();
        String firstImageUrl = result.metaData().get(0).imageUrl();

        assertThat(firstClubName).startsWith("동아리");
        assertThat(firstImageUrl).isEqualTo("cdnUrl");
    }
}