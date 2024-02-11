package ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CollectedStampsResponse {

    private String stamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectedAt;

    public static CollectedStampsResponse of(String stampName, LocalDateTime collectedAt) {
        return CollectedStampsResponse.builder()
                .stamp(stampName)
                .collectedAt(collectedAt).build();
    }

}
