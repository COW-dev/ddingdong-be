package ddingdong.ddingdongBE.domain.form.repository.dto;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;

public interface FieldListInfo {

    Long getId();

    String getQuestion();

    Integer getCount();

    FieldType getType();

    String getSection();
}
