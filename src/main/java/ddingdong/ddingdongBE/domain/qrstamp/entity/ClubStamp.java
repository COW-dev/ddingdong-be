package ddingdong.ddingdongBE.domain.qrstamp.entity;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubStamp {

    너나들이("너나들이", "EVENTNNDE"),
    키비탄("키비탄", "EVENTKBT"),
    RCY("RCY", "EVENTRCY"),
    PTPI("PTPI", "EVENTPTPI"),
    인액터스("인액터스 ", "EVENTIACT"),
    COW("COW", "EVENTCOW"),
    비주얼("비주얼", "EVENTVISUAL"),
    SK_루키("SK루키", "EVENTSKROOKIE"),
    명지챌린저스("명지챌린저스", "EVENTMC"),
    농어민후생연구회_흙("농어민후생연구회 흙", "EVENTSOIL"),
    흑풍("흑풍", "EVENTBLACK"),
    통해("통해", "EVENTTH"),
    MGH("MGH", "EVENTMGH"),
    화이트홀스("화이트홀스", "EVENTWH"),
    주리랑("주리랑", "EVENTJURI"),
    극예술연구회_알("극예술연구회 알", "EVENTAL"),
    그림패시만화("그림패시만화", "EVENTPIC"),
    포토랩("포토랩", "EVENTPHO"),
    디비전("디비전", "EVENTDIV"),
    코아("코아 ", "EVENTCOA "),
    씨네메이션("씨네메이션", "EVENTCINE"),
    실로암("실로암", "EVENTSIL"),
    CCC("ccc", "EVENTCCC"),
    CFM("Cfm", "EVENTCFM"),
    UBF("UBF", "EVENTUBF"),
    BB("BB", "EVENTBB"),
    씨네필("씨네필", "EVENTFIL"),
    에뚜알("에뚜알", "EVENTALL"),
    DEPTH("DEPth", "EVENTDEPTH"),
    IVF("IVF", "EVENTIVF"),
    명월("명월 ", "EVENTMON"),
    나라오르다("나라오르다", "EVENTFLY"),
    굴렁쇠("굴렁쇠", "EVENTBIKE"),
    나이너스("나이너스", "EVENTBASE"),
    FC_명지("FC명지", "EVENTFC"),
    삼박자("삼박자", "EVENTSAM"),
    무릉도원("무릉도원", "EVENTKAL"),
    오버행("오버행", "EVENTHANG"),
    MJTA("MJTA", "EVENTMJTA"),
    콕콕콕("콕콕콕", "EVENTCOK"),
    바다이야기("바다이야기", "EVENTSEA"),
    파인("파인", "EVENTFINE"),
    TIME("TIME", "EVENTTIME"),
    명지서법("명지서법", "EVENTWORD"),
    MIRS("MIRS", "EVENTMIRS");

    private final String name;
    private final String code;

    public static ClubStamp getByClubCode(String code) {
        return Arrays.stream(ClubStamp.values())
                .filter(clubStamp -> clubStamp.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("동아리 코드를 확인해주세요."));
    }
}
