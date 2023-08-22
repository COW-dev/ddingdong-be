package ddingdong.ddingdongBE.domain.club.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {

    LEADER("회장"),
    EXECUTIVE("임원"),
    MEMBER("동아리원");

    private final String name;
}
