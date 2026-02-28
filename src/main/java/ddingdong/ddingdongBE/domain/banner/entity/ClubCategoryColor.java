package ddingdong.ddingdongBE.domain.banner.entity;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ClubCategoryColor {

    VOLUNTEER("봉사", "#FFE8F4"),
    SOCIAL_RESEARCH("사회연구", "#FFECDE"),
    PERFORMING_ARTS("연행예술", "#FFF9E2"),
    EXHIBITION_CREATIVE("전시창작", "#E2FEF5"),
    RELIGION("종교", "#E0F2FE"),
    SPORTS("체육", "#E1E2FF"),
    ACADEMIC("학술", "#E1C4FF"),
    PROVISIONAL("준동아리", "#E8E8E8");

    private final String label;
    private final String hexColor;

    public static ClubCategoryColor fromCategory(String category) {
        return Arrays.stream(values())
                .filter(color -> color.label.equals(category))
                .findFirst()
                .orElse(PROVISIONAL);
    }
}
