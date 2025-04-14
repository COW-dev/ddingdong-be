package ddingdong.ddingdongBE.domain.formapplication.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.FileMetaDataFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.FormFieldRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormAnswerRepositoryTest extends DataJpaTestSupport {

    @Autowired
    FormAnswerRepository formAnswerRepository;
    @Autowired
    FormFieldRepository formFieldRepository;
    @Autowired
    FormRepository formRepository;
    @Autowired
    FileMetaDataRepository fileMetaDataRepository;

    @DisplayName("주어진 FormField와 연관된 FormAnswer의 value를 모두 반환한다.")
    @Test
    void findAllValueByFormFieldId() {
        // given
        FormField formField = FormField.builder()
                .question("질문입니다")
                .required(true)
                .fieldOrder(1)
                .section("서버")
                .options(List.of("지문1", "지문2"))
                .fieldType(FieldType.CHECK_BOX)
                .form(null)
                .build();
        FormField savedField = formFieldRepository.save(formField);
        FormAnswer formAnswer = FormAnswer.builder()
                .value(List.of("서버", "웹"))
                .formField(savedField)
                .build();
        FormAnswer formAnswer2 = FormAnswer.builder()
                .value(List.of("서버입니다", "웹입니다."))
                .formField(savedField)
                .build();
        formAnswerRepository.saveAll(List.of(formAnswer, formAnswer2));
        // when
        List<String> allValueByFormField = formAnswerRepository.findAllValueByFormFieldId(savedField.getId());
        // then
        Assertions.assertThat(allValueByFormField).hasSize(2);
        Assertions.assertThat(allValueByFormField.get(0)).isEqualTo("[\"서버\",\"웹\"]");
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

        FileMetaData fileMetaData1 = FileMetaDataFixture.formFileMetaData(fileId1, savedFormAnswer1.getId());
        FileMetaData fileMetaData2 = FileMetaDataFixture.formFileMetaData(fileId2, savedFormAnswer2.getId());
        FileMetaData fileMetaData3 = FileMetaDataFixture.formFileMetaData(fileId3, savedFormAnswer3.getId());
        fileMetaDataRepository.saveAll(List.of(fileMetaData1, fileMetaData2, fileMetaData3));

        // when
        List<FileMetaData> allFileByForm = formAnswerRepository.getAllFileByForm(savedForm.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(allFileByForm.get(0).getEntityId()).isEqualTo(savedFormAnswer1.getId());
            softly.assertThat(allFileByForm.get(1).getEntityId()).isEqualTo(savedFormAnswer2.getId());
            softly.assertThat(allFileByForm.get(2).getEntityId()).isEqualTo(savedFormAnswer3.getId());
        });
    }
}
