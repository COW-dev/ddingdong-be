package ddingdong.ddingdongBE.domain.activityreport.domain;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    private String name;
    private String studentId;
    private String department;
}
