package ddingdong.ddingdongBE.domain.formapplication.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FileMetaDataFixture;
import ddingdong.ddingdongBE.common.fixture.FormAnswerFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.fixture.FormFieldFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileAnswerInfo;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.TextAnswerInfo;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormAnswerRepositoryTest extends DataJpaTestSupport {

    @Autowired
    ClubRepository clubRepository;
    @Autowired
    FormAnswerRepository formAnswerRepository;
    @Autowired
    FormFieldRepository formFieldRepository;
    @Autowired
    FormRepository formRepository;
    @Autowired
    FormApplicationRepository formApplicationRepository;
    @Autowired
    FileMetaDataRepository fileMetaDataRepository;

    @BeforeEach
    void setUp() {
        formAnswerRepository.deleteAllInBatch();
        formApplicationRepository.deleteAllInBatch();
        formRepository.deleteAllInBatch();
        clubRepository.deleteAllInBatch();
        fileMetaDataRepository.deleteAllInBatch();
    }

    @DisplayName("주어진 FormField와 연관된 FormAnswer의 value를 모두 반환한다.")
    @Test
    void findAllValueByFormFieldId() {
        // given
        FormField savedField = formFieldRepository.save(FormFieldFixture.create(null));
        FormAnswer formAnswer1 = FormAnswerFixture.create(null, savedField, List.of("서버", "웹"));
        FormAnswer formAnswer2 = FormAnswerFixture.create(null, savedField,
                List.of("서버입니다", "웹입니다"));
        formAnswerRepository.saveAll(List.of(formAnswer1, formAnswer2));

        // when
        List<String> allValueByFormField = formAnswerRepository.findAllValueByFormFieldId(
                savedField.getId());

        // then
        assertThat(allValueByFormField).hasSize(2);
        assertThat(allValueByFormField.get(0)).isEqualTo("[\"서버\",\"웹\"]");
    }

    @Test
    @DisplayName("특정 질문에 대한 지원자 이름과 답변 내용을 조회한다")
    void getTextAnswerInfosByFormFieldId() {
        // given
        Club savedClub = clubRepository.save(ClubFixture.createClub());
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));
        FormField savedFormField = formFieldRepository.save(FormFieldFixture.create(savedForm));

        FormApplication savedApplication1 = formApplicationRepository.save(
                FormApplicationFixture.createWithName(savedForm, "지원자A"));
        FormApplication savedApplication2 = formApplicationRepository.save(
                FormApplicationFixture.createWithName(savedForm, "지원자B"));

        FormAnswer answer1 = FormAnswerFixture.create(savedApplication1, savedFormField,
                List.of("답변A"));
        FormAnswer answer2 = FormAnswerFixture.create(savedApplication2, savedFormField,
                List.of("답변B"));
        formAnswerRepository.saveAll(List.of(answer1, answer2));

        // when
        List<TextAnswerInfo> results = formAnswerRepository.getTextAnswerInfosByFormFieldId(
                savedFormField.getId());

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("지원자A");
        assertThat(results.get(0).getValue()).isEqualTo("[\"답변A\"]");

        assertThat(results.get(1).getName()).isEqualTo("지원자B");
        assertThat(results.get(1).getValue()).isEqualTo("[\"답변B\"]");
    }

    @Test
    @DisplayName("업로드된 파일 답변에 대한 메타데이터 정보를 조회한다")
    void findAllFileAnswerInfo() {
        // given
        Form form = formRepository.save(FormFixture.createForm(null));
        FormField fileField = formFieldRepository.save(FormFieldFixture.create(form));

        FormApplication formApplication = formApplicationRepository.save(
                FormApplicationFixture.createWithName(form, "이름")
        );
        FormAnswer savedAnswer = formAnswerRepository.save(
                FormAnswerFixture.create(formApplication, fileField, List.of("filename.pdf"))
        );

        FileMetaData fileMetaData = FileMetaDataFixture.create(
                UUID.randomUUID(),
                savedAnswer.getId(),
                "filename.pdf"
        );
        fileMetaDataRepository.save(fileMetaData);

        // when
        List<Long> answerIds = List.of(savedAnswer.getId());
        List<FileAnswerInfo> results = formAnswerRepository.findAllFileAnswerInfo(
                DomainType.FORM_FILE.name(),
                answerIds,
                FileStatus.COUPLED.name()
        );

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("이름");
        assertThat(results.get(0).getFileName()).isEqualTo("filename.pdf");
        assertThat(results.get(0).getId()).isEqualTo(savedAnswer.getId());
    }

    @DisplayName("FormID에 해당하는 모든 FormAnswer ID를 EntityId로 보유한 FileMetaData를 모두 조회한다")
    @Test
    void getAllFileByForm() {
        // given
        Form form = FormFixture.formWithClubNull();
        Form savedForm = formRepository.save(form);
        FormField formField = FormFixture.formFieldWithFormNull();

        formField.setFormForConvenience(savedForm);
        FormField savedField = formFieldRepository.save(formField);

        FormAnswer formAnswer1 = FormFixture.formAnswerByFormField(savedField);
        FormAnswer savedFormAnswer1 = formAnswerRepository.save(formAnswer1);

        FormAnswer formAnswer2 = FormFixture.formAnswerByFormField(savedField);
        FormAnswer savedFormAnswer2 = formAnswerRepository.save(formAnswer2);

        FormAnswer formAnswer3 = FormFixture.formAnswerByFormField(savedField);
        FormAnswer savedFormAnswer3 = formAnswerRepository.save(formAnswer3);

        UUID fileId1 = UUID.randomUUID();
        UUID fileId2 = UUID.randomUUID();
        UUID fileId3 = UUID.randomUUID();

        FileMetaData fileMetaData1 = FileMetaDataFixture.create(fileId1, savedFormAnswer1.getId());
        FileMetaData fileMetaData2 = FileMetaDataFixture.create(fileId2, savedFormAnswer2.getId());
        FileMetaData fileMetaData3 = FileMetaDataFixture.create(fileId3, savedFormAnswer3.getId());
        fileMetaDataRepository.saveAll(List.of(fileMetaData1, fileMetaData2, fileMetaData3));

        // when
        List<FileMetaData> allFileByForm = formAnswerRepository.getAllFileByForm(savedForm.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(allFileByForm.get(0).getEntityId())
                    .isEqualTo(savedFormAnswer1.getId());
            softly.assertThat(allFileByForm.get(1).getEntityId())
                    .isEqualTo(savedFormAnswer2.getId());
            softly.assertThat(allFileByForm.get(2).getEntityId())
                    .isEqualTo(savedFormAnswer3.getId());
        });
    }

    @DisplayName("폼 지원서id와 FileMetaData의 entityId와 조인하여 정보를 조회한다")
    @Test
    void findAllFileApplicationInfo() {
        // given
        FormApplication formApplication = FormApplicationFixture.createWithName(null, "이름1");
        FormApplication savedFormApplication = formApplicationRepository.save(formApplication);
        FormAnswer savedFormAnswer1 = formAnswerRepository.save(FormAnswerFixture.create(savedFormApplication, null));

        FormApplication formApplication2 = FormApplicationFixture.createWithName(null, "이름2");
        FormApplication savedFormApplication2 = formApplicationRepository.save(formApplication2);
        FormAnswer savedFormAnswer2 = formAnswerRepository.save(FormAnswerFixture.create(savedFormApplication2, null));

        FileMetaData fileMetaData1 = FileMetaDataFixture.create(UUID.randomUUID(), savedFormAnswer1.getId(), "파일 이름1");
        FileMetaData fileMetaData2 = FileMetaDataFixture.create(UUID.randomUUID(), savedFormAnswer1.getId(), "파일 이름2");
        fileMetaDataRepository.saveAll(List.of(fileMetaData1, fileMetaData2));
        List<Long> ids = List.of(savedFormAnswer1.getId(), savedFormAnswer2.getId());

        // when
        List<FileAnswerInfo> fileAnswerInfos = formAnswerRepository.findAllFileAnswerInfo(
                DomainType.FORM_FILE.name(), ids, FileStatus.COUPLED.name());

        // then
        assertSoftly(softly -> {
            softly.assertThat(fileAnswerInfos).hasSize(2);
            softly.assertThat(fileAnswerInfos.get(0).getFileName()).isEqualTo(fileMetaData1.getFileName());
            softly.assertThat(fileAnswerInfos.get(0).getName()).isEqualTo(savedFormApplication.getName());
            softly.assertThat(fileAnswerInfos.get(0).getId()).isEqualTo(savedFormAnswer1.getId());
            softly.assertThat(fileAnswerInfos.get(1).getFileName()).isEqualTo(fileMetaData2.getFileName());
        });
    }
}
