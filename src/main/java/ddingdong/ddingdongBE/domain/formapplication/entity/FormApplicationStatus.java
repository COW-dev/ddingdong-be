package ddingdong.ddingdongBE.domain.formapplication.entity;

import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidatedEnumValue;
import java.util.Arrays;

public enum FormApplicationStatus {
  SUBMITTED,
  FIRST_PASS,
  FINAL_PASS,
  FIRST_FAIL,
  FINAL_FAIL;

  public static FormApplicationStatus findStatus(String status) {
    return Arrays.stream(values())
        .filter(findStatus -> findStatus.name().equals(status))
        .findFirst()
        .orElseThrow(() -> new InvalidatedEnumValue(
            "FormApplicationStatus (status=" + status + ")를 찾을 수 없습니다."));
  }
}
