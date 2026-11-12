package ddingdong.ddingdongBE.domain.form.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRegisterMode {
    APPEND("기존 동아리원 유지 후 합격자 추가"),
    REPLACE("기존 동아리원 삭제 후 합격자로 교체");

    private final String description;

    public boolean isReplace() {
        return this == REPLACE;
    }
}
