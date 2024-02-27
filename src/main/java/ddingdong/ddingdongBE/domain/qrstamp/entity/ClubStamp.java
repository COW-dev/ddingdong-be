package ddingdong.ddingdongBE.domain.qrstamp.entity;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubStamp {

    너나들이("너나들이", "EVENT너나들이"),
    키비탄("키비탄", "EVENT키비탄"),
    RCY("RCY", "EVENTRCY"),
    PTPI("PTPI", "EVENTPTPI"),
    인액터스("인액터스 ", "EVENT인액터스"),
    COW("COW", "EVENTCOW"),
    비주얼("비주얼", "EVENT비주얼"),
    SK_루키("SK루키", "EVENTSK루키"),
    명지챌린저스("명지챌린저스", "EVENT명지챌린저스"),
    농어민후생연구회_흙("농어민후생연구회 흙", "EVENT농어민후생연구회흙"),
    흑풍("흑풍", "EVENT흑풍"),
    통해("통해", "EVENT통해"),
    MGH("MGH", "EVENTMGH"),
    화이트홀스("화이트홀스", "EVENT화이트홀스"),
    주리랑("주리랑", "EVENT주리랑"),
    극예술연구회_알("극예술연구회 알", "EVENT극예술연구회알"),
    그림패시만화("그림패시만화", "EVENT그림패시만화"),
    포토랩("포토랩", "EVENT포토랩"),
    디비전("디비전", "EVENT디비전"),
    코아("코아 ", "EVENT코아 "),
    씨네메이션("씨네메이션", "EVENT씨네메이션"),
    실로암("실로암", "EVENT실로암"),
    CCC("ccc", "EVENTCCC"),
    CFM("Cfm", "EVENTCfm"),
    UBF("UBF", "EVENTUBF"),
    BB("BB", "EVENTBB"),
    씨네필("씨네필", "EVENT씨네필"),
    에뚜알("에뚜알", "EVENT에뚜알"),
    DEPTH("DEPth", "EVENTDEPth"),
    IVF("IVF", "EVENTIVF"),
    명월("명월 ", "EVENT명월"),
    나라오르다("나라오르다", "EVENT나라오르다"),
    굴렁쇠("굴렁쇠", "EVENT굴렁쇠"),
    나이너스("나이너스", "EVENT나이너스"),
    FC_명지("FC명지", "EVENTFC명지"),
    삼박자("삼박자", "EVENT삼박자"),
    무릉도원("무릉도원", "EVENT무릉도원"),
    오버행("오버행", "EVENT오버행"),
    MJTA("MJTA", "EVENTMJTA"),
    콕콕콕("콕콕콕", "EVENT콕콕콕"),
    바다이야기("바다이야기", "EVENT바다이야기"),
    파인("파인", "EVENT파인"),
    TIME("TIME", "EVENTTIME"),
    명지서법("명지서법", "EVENT명지서법"),
    MIRS("MIRS", "EVENTMIRS");

    private final String name;
    private final String code;

    public static ClubStamp getByClubCode(String code) {
        return Arrays.stream(ClubStamp.values())
                .filter(clubStamp -> clubStamp.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("동아리 코드를 확인해주세요."));
    }
}
