package ddingdong.ddingdongBE.domain.calendar.service.dto.command;

import lombok.Builder;

@Builder
public record UpdateCategoryCommand(
        String name,
        String color
) {
}
