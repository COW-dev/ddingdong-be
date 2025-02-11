package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidatedEnumValue;
import java.util.Arrays;

public enum FieldType {
    CHECK_BOX,
    RADIO,
    TEXT,
    LONG_TEXT,
    FILE;

    public static FieldType findType(String type) {
        return Arrays.stream(values())
                .filter(fieldType -> fieldType.name().equals(type))
                .findFirst()
                .orElseThrow(
                        () -> new InvalidatedEnumValue("FieldType(type=" + type + ")를 찾을 수 없습니다."));
    }
}
