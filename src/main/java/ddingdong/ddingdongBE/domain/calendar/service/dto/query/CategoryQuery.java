package ddingdong.ddingdongBE.domain.calendar.service.dto.query;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import lombok.Builder;

public record CategoryQuery(
        Long id,
        String name,
        String color
) {

    @Builder
    public CategoryQuery {
    }

    public static CategoryQuery from(Category category) {
        return new CategoryQuery(category.getId(), category.getName(), category.getColor());
    }
}
