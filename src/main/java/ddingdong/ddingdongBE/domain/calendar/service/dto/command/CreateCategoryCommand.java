package ddingdong.ddingdongBE.domain.calendar.service.dto.command;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;

public record CreateCategoryCommand(
        String name,
        String color
) {

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .color(color)
                .build();
    }
}
