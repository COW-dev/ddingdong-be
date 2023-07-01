package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Getter;

@Getter
public class RegisterNoticeRequest {

    private String title;

    private String content;

    public Notice toEntity(User user) {
        return Notice.builder()
                .user(user)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
