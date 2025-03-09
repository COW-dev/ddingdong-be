package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.FileAnswerInfo;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.TextAnswerInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {

    int countByFormField(FormField formField);

    List<FormAnswer> findAllByFormApplication(FormApplication formApplication);

    @Query(value = """
            SELECT fa.value
            FROM form_answer fa
            WHERE fa.field_id = :fieldId
            """, nativeQuery = true)
    List<String> findAllValueByFormFieldId(@Param("fieldId") Long fieldId);

    @Query(value = """
            SELECT fap.id as id, fap.name as name, field_answer.value as value
            FROM (
                SELECT *
                FROM form_answer fa
                WHERE fa.field_id = :fieldId
                ) field_answer
            JOIN form_application fap
            ON fap.id = field_answer.application_id
            ORDER BY fap.id
            """, nativeQuery = true)
    List<TextAnswerInfo> getTextAnswerInfosByFormFieldId(Long fieldId);

    @Query(value = """
            SELECT DISTINCT fa.id
            FROM form_answer fa
            WHERE fa.field_id = :fieldId
            """, nativeQuery = true)
    List<Long> findAllAnswerByFormFieldId(@Param("fieldId") Long fieldId);

    @Query(value = """
            SELECT fa.id as id, fap.name as name, fmd.file_name as fileName
            FROM form_answer fa
            JOIN file_meta_data fmd
            ON fmd.entity_id = fa.id
            JOIN form_application fap
            ON fa.application_id = fap.id
            WHERE fmd.domain_type = :domainType
            AND fmd.entity_id IN (:answerIds)
            AND fmd.file_status = :fileStatus
            ORDER BY fmd.file_name
            """, nativeQuery = true)
    List<FileAnswerInfo> findAllFileAnswerInfo(
            @Param("domainType") String domainType,
            @Param("answerIds") List<Long> answerIds,
            @Param("fileStatus") String fileStatus    );
}
