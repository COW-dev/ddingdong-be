package ddingdong.ddingdongBE.domain.formapplication.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileAnswerInfo;
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

    @Autowired
    private FormAnswerRepository formAnswerRepository;

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
        FormAnswer formAnswer = FormAnswer.builder()
                .formApplication(savedFormApplication)
                .value(List.of())
                .build();
        FormAnswer savedFormAnswer = formAnswerRepository.save(formAnswer);
        FormApplication formApplication2 = fixtureMonkey.giveMeBuilder(FormApplication.class)
                .set("name", "이름2")
                .set("form", null)
                .sample();
        FormApplication savedFormApplication2 = formApplicationRepository.save(formApplication2);
        FormAnswer formAnswer2 = FormAnswer.builder()
                .formApplication(savedFormApplication2)
                .value(List.of())
                .build();
        FormAnswer savedFormAnswer2 = formAnswerRepository.save(formAnswer2);
        FileMetaData fileMetaData = FileMetaData.builder()
                .id(UUID.randomUUID())
                .entityId(savedFormAnswer.getId())
                .fileName("파일 이름1")
                .domainType(DomainType.FORM_FILE)
                .fileStatus(FileStatus.COUPLED)
                .fileKey("1")
                .build();
        FileMetaData fileMetaData2 = FileMetaData.builder()
                .id(UUID.randomUUID())
                .entityId(savedFormAnswer2.getId())
                .fileName("파일 이름2")
                .domainType(DomainType.FORM_FILE)
                .fileStatus(FileStatus.COUPLED)
                .fileKey("1")
                .build();
        fileMetaDataRepository.saveAll(List.of(fileMetaData, fileMetaData2));
        List<Long> ids = List.of(savedFormAnswer.getId(), savedFormAnswer2.getId());

        // when
        List<FileAnswerInfo> fileAnswerInfos = formAnswerRepository.findAllFileAnswerInfo(
                DomainType.FORM_FILE.name(), ids, FileStatus.COUPLED.name());

        // then
        assertSoftly(softly -> {
            softly.assertThat(fileAnswerInfos).hasSize(2);
            softly.assertThat(fileAnswerInfos.get(0).getFileName()).isEqualTo(fileMetaData.getFileName());
            softly.assertThat(fileAnswerInfos.get(0).getName()).isEqualTo(savedFormApplication.getName());
            softly.assertThat(fileAnswerInfos.get(0).getId()).isEqualTo(savedFormAnswer.getId());
            softly.assertThat(fileAnswerInfos.get(1).getFileName()).isEqualTo(fileMetaData2.getFileName());
        });
    }
}
