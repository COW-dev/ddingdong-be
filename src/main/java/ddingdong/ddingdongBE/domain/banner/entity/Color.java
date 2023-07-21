package ddingdong.ddingdongBE.domain.banner.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {

    RED("#fee2e2"),
    ORANGE("#ffedd5"),
    YELLOW("#fef9c3"),
    GREEN("#dcfce7"),
    SKYBLUE("#e0f2fe"),
    INDIGO("#e0e7ff");

    private final String code;

}
