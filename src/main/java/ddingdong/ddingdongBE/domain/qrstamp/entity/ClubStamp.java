package ddingdong.ddingdongBE.domain.qrstamp.entity;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubStamp {

    COW("COW"),
    TEST("TEST");

    private final String code;

    public static ClubStamp getByClubCode(String code) {
        return Arrays.stream(ClubStamp.values())
                .filter(clubStamp -> clubStamp.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("동아리 코드를 확인해주세요."));
    }
}
