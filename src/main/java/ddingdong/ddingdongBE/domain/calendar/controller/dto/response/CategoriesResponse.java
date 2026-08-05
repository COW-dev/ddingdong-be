package ddingdong.ddingdongBE.domain.calendar.controller.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record CategoriesResponse(
        @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))
        List<CategoryResponse> categories
) {
}
