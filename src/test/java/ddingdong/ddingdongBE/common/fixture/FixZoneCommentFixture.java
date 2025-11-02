package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.user.entity.User;

public class FixZoneCommentFixture {

    public static FixZoneComment createFixZoneComment(User user, FixZone fixZone) {
        return FixZoneComment.builder()
                .user(user)
                .fixZone(fixZone)
                .content("픽스존 댓글 내용")
                .build();
    }

    public static FixZoneComment createFixZoneComment(User user, FixZone fixZone, String content) {
        return FixZoneComment.builder()
                .user(user)
                .fixZone(fixZone)
                .content(content)
                .build();
    }
}