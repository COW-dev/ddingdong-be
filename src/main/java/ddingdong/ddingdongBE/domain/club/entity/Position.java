package ddingdong.ddingdongBE.domain.club.entity;

import ddingdong.ddingdongBE.common.exception.InvalidatedMappingException.InvalidatedEnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {

    LEADER("회장"),
    EXECUTIVE("임원"),
    MEMBER("동아리원");

    private final String name;

    public static Position from(String position) {
        try {
            return Position.valueOf(position);
        } catch (IllegalArgumentException e) {
            throw new InvalidatedEnumValue("동아리원의 역할은 LEADER, EXECUTIVE, MEMBER 중 하나입니다.");
        }
    }
}
