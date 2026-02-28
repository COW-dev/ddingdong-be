package ddingdong.ddingdongBE.domain.banner.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ClubCategoryColorTest {

    @DisplayName("모든 분과 카테고리가 올바른 배경색으로 매핑된다")
    @ParameterizedTest
    @CsvSource({
            "봉사, #FFE8F4",
            "사회연구, #FFECDE",
            "연행예술, #FFF9E2",
            "전시창작, #E2FEF5",
            "종교, #E0F2FE",
            "체육, #E1E2FF",
            "학술, #E1C4FF",
            "준동아리, #E8E8E8"
    })
    void fromCategory_allCategories(String category, String expectedHex) {
        // when
        ClubCategoryColor result = ClubCategoryColor.fromCategory(category);

        // then
        assertThat(result.getHexColor()).isEqualTo(expectedHex);
    }

    @DisplayName("알 수 없는 카테고리는 준동아리 색상으로 매핑된다")
    @Test
    void fromCategory_unknownCategory() {
        // when
        ClubCategoryColor result = ClubCategoryColor.fromCategory("존재하지않는분과");

        // then
        assertThat(result).isEqualTo(ClubCategoryColor.PROVISIONAL);
        assertThat(result.getHexColor()).isEqualTo("#E8E8E8");
    }

    @DisplayName("null 카테고리는 준동아리 색상으로 매핑된다")
    @Test
    void fromCategory_nullCategory() {
        // when
        ClubCategoryColor result = ClubCategoryColor.fromCategory(null);

        // then
        assertThat(result).isEqualTo(ClubCategoryColor.PROVISIONAL);
    }
}
