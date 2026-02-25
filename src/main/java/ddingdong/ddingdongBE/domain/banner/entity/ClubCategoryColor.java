package ddingdong.ddingdongBE.domain.banner.entity;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ClubCategoryColor {

    봉사("#FFE8F4"),
    사회연구("#FFECDE"),
    연행예술("#FFF9E2"),
    전시창작("#E2FEF5"),
    종교("#E0F2FE"),
    체육("#E1E2FF"),
    학술("#E1C4FF"),
    준동아리("#E8E8E8");

    private final String hexColor;

    public static ClubCategoryColor fromCategory(String category) {
        return Arrays.stream(values())
                .filter(color -> color.name().equals(category))
                .findFirst()
                .orElse(준동아리);
    }
}
