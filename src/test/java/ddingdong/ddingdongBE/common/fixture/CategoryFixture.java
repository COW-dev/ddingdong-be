package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;

public class CategoryFixture {

    public static Category createCategory() {
        return createCategory("testCategory", "#FFFFFF");
    }

    public static Category createCategory(String name, String color) {
        return Category.builder()
                .name(name)
                .color(color)
                .build();
    }
}
