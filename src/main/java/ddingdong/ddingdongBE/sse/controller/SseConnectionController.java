package ddingdong.ddingdongBE.sse.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.sse.api.SseConnectionApi;
import ddingdong.ddingdongBE.sse.service.SseConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseConnectionController implements SseConnectionApi {

    private final SseConnectionService sseConnectionService;

    //TODO :  API의 헤더에 X-Accel-Buffering: no 설정해, nginx 버퍼링 제거하기
    @Override
    public SseEmitter subscribe(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        Long timeout = 60L * 1000; // 1분 (60초 * 1000밀리초)
        return sseConnectionService.subscribe(user.getId().toString(), timeout);
    }
}
