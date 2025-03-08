package ddingdong.ddingdongBE.domain.formapplication.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileApplicationInfo;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormApplicationRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    private FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("폼 지원서id와 FileMetaData의 entityId와 조인하여 정보를 조회한다")
    @Test
    void findAllFileApplicationInfo() {
        // given
        FormApplication formApplication = fixtureMonkey.giveMeBuilder(FormApplication.class)
                .set("name", "이름1")
                .set("form", null)
                .sample();
        FormApplication savedFormApplication = formApplicationRepository.save(formApplication);
        FormApplication formApplication2 = fixtureMonkey.giveMeBuilder(FormApplication.class)
                .set("name", "이름2")
                .set("form", null)
                .sample();
        FormApplication savedFormApplication2 = formApplicationRepository.save(formApplication2);
        FileMetaData fileMetaData = FileMetaData.builder()
                .id(UUID.randomUUID())
                .entityId(savedFormApplication.getId())
                .fileName("파일 이름1")
                .domainType(DomainType.FORM_FILE)
                .fileStatus(FileStatus.COUPLED)
                .fileKey("1")
                .build();
        FileMetaData fileMetaData2 = FileMetaData.builder()
                .id(UUID.randomUUID())
                .entityId(savedFormApplication.getId())
                .fileName("파일 이름2")
                .domainType(DomainType.FORM_FILE)
                .fileStatus(FileStatus.COUPLED)
                .fileKey("1")
                .build();
        FileMetaData fileMetaData3 = FileMetaData.builder()
                .id(UUID.randomUUID())
                .entityId(savedFormApplication2.getId())
                .fileName("파일 이름3")
                .domainType(DomainType.FORM_FILE)
                .fileStatus(FileStatus.COUPLED)
                .fileKey("1")
                .build();
        fileMetaDataRepository.saveAll(List.of(fileMetaData, fileMetaData2, fileMetaData3));
        List<Long> ids = List.of(savedFormApplication.getId(), savedFormApplication2.getId());

        // when
        List<FileApplicationInfo> fileApplicationInfos = formApplicationRepository.findAllFileApplicationInfo(
                DomainType.FORM_FILE.name(), ids, FileStatus.COUPLED.name());

        // then
        assertSoftly(softly -> {
            softly.assertThat(fileApplicationInfos).hasSize(3);
            softly.assertThat(fileApplicationInfos.get(0).getFileName()).isEqualTo(fileMetaData.getFileName());
            softly.assertThat(fileApplicationInfos.get(0).getName()).isEqualTo("이름1");
            softly.assertThat(fileApplicationInfos.get(0).getId()).isEqualTo(savedFormApplication.getId());
            softly.assertThat(fileApplicationInfos.get(1).getFileName()).isEqualTo(fileMetaData2.getFileName());
            softly.assertThat(fileApplicationInfos.get(2).getFileName()).isEqualTo(fileMetaData3.getFileName());
        });
    }
}
