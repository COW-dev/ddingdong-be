package ddingdong.ddingdongBE.sse.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE", description = "SSE subscribe API")
@RequestMapping("/server/sse")
public interface SseConnectionApi {

    @Operation(
            summary = "SSE 구독",
            description = "SSE 연결을 설정합니다. 연결 타임아웃은 60초입니다."
    )
    @ApiResponse(responseCode = "200", description = "SSE 구독 연결 성공")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails);

}
