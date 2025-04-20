package ddingdong.ddingdongBE.domain.form.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FormTest {

    private Form form;
    private FormField field1, field2, field3, updatedField2, newField4;

    @BeforeEach
    void setUp() {
        form = new Form();
        field1 = FormField.builder()
                .id(1L)
                .question("질문 1")
                .fieldType(FieldType.TEXT)
                .required(true)
                .fieldOrder(1)
                .section("섹션 1")
                .options(null)
                .build();

        field2 = FormField.builder()
                .id(2L)
                .question("질문 2")
                .fieldType(FieldType.RADIO)
                .required(false)
                .fieldOrder(2)
                .section("섹션 1")
                .options(List.of("옵션1", "옵션2"))
                .build();

        field3 = FormField.builder()
                .id(3L)
                .question("질문 3")
                .fieldType(FieldType.CHECK_BOX)
                .required(true)
                .fieldOrder(3)
                .section("섹션 2")
                .options(List.of("옵션A", "옵션B", "옵션C"))
                .build();

        form.addFormFields(field1);
        form.addFormFields(field2);
        form.addFormFields(field3);

        updatedField2 = FormField.builder()
                .id(2L) // field2와 같은 ID
                .question("수정된 질문 2")
                .fieldType(FieldType.RADIO)
                .required(true) // 변경됨
                .fieldOrder(5) // 변경됨
                .section("섹션 1")
                .options(List.of("옵션1", "옵션2", "새 옵션")) // 옵션 추가됨
                .build();

        newField4 = FormField.builder()
                .id(4L)
                .question("새 질문 4")
                .fieldType(FieldType.LONG_TEXT)
                .required(false)
                .fieldOrder(4)
                .section("섹션 3")
                .options(null)
                .build();
    }

    @DisplayName("폼지 수정: 삭제대상 폼지 질문을 삭제한다.")
    @Test
    void shouldRemoveDeletedFields() {
        // given
        List<FormField> updatedFields = List.of(field1, field3);

        // when
        form.updateFormFields(updatedFields);

        // then
        assertThat(form.getFormFields()).hasSize(2);
        assertThat(form.getFormFields())
                .extracting(FormField::getId)
                .containsExactlyInAnyOrder(1L, 3L)
                .doesNotContain(2L);
    }
    
    @DisplayName("폼지 수정: 추가 대상 폼지 질문을 추가한다.")
    @Test
    void shouldAddNewFields() {
        //given
        List<FormField> updatedFields = List.of(field1, field2, field3, newField4);

        //when
        form.updateFormFields(updatedFields);
    
        //then
        Assertions.assertThat(form.getFormFields()).hasSize(4);

        FormField addedField = form.getFormFields().stream()
                .filter(field -> field.getId() != null && field.getId().equals(4L))
                .findFirst()
                .orElse(null);
        assertThat(addedField).isNotNull();
        assertThat(addedField.getQuestion()).isEqualTo("새 질문 4");
        assertThat(addedField.getFieldType()).isEqualTo(FieldType.LONG_TEXT);
        assertThat(addedField.getSection()).isEqualTo("섹션 3");
        assertThat(addedField.getForm()).isEqualTo(form);
    }

    @DisplayName("폼지 수정: 기존 폼지 질문을 수정한다.")
    @Test
    void shouldUpdateExistingFields() {
        //given
        List<FormField> updatedFields = List.of(field1, updatedField2, field3);

        //when
        form.updateFormFields(updatedFields);

        //then
        assertThat(form.getFormFields()).hasSize(3);
        FormField updatedField = form.getFormFields().stream()
                .filter(field -> field.getId().equals(2L))
                .findFirst()
                .orElse(null);
        assertThat(updatedField).isNotNull();
        assertThat(updatedField.getQuestion()).isEqualTo("수정된 질문 2");
        assertThat(updatedField.isRequired()).isTrue();
        assertThat(updatedField.getFieldOrder()).isEqualTo(5);
        assertThat(updatedField.getOptions())
                .hasSize(3)
                .contains("옵션1", "옵션2", "새 옵션");
    }

    @DisplayName("현재 날짜가 기간 내 포함된다면 알맞는 FormStatus를 반환한다.")
    @Test
    void isDateInRange() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(1);
        LocalDate endDate = now.plusDays(1);

        Form form = Form.builder()
                .title("테스트 폼")
                .description("설명")
                .startDate(startDate)
                .endDate(endDate)
                .hasInterview(false)
                .sections(List.of())
                .club(null)
                .formFields(List.of())
                .build();

        // when
        FormStatus formStatus = form.getFormStatus(now);

        // then
        Assertions.assertThat(formStatus).isEqualTo(FormStatus.ONGOING);
    }
}
