package ddingdong.ddingdongBE.sse.service;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;
import ddingdong.ddingdongBE.sse.repository.SseConnectionRepository;
import ddingdong.ddingdongBE.sse.service.dto.SseEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseConnectionService {

    private final SseConnectionRepository sseConnectionRepository;

    public SseEmitter subscribe(String id, Long timeout) {
        SseEmitter sseEmitter = new SseEmitter(timeout);
        checkExistingEmitter(id);
        log.info("SSE Connection established for user: {}", id);
        sseConnectionRepository.save(id, sseEmitter);

        // 연결 완료 콜백
        sseEmitter.onCompletion(() -> sseConnectionRepository.deleteById(id));

        // 연결 타임아웃 콜백
        sseEmitter.onTimeout(sseEmitter::complete);

        // 연결 에러 콜백
        sseEmitter.onError((ex) -> {
            log.warn("SSE Connection error: {}", ex.getMessage());
            sseConnectionRepository.deleteById(id);
        });

        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .id(id)
                            .name("connect")
                            .data("Connected successfully!")
            );
        } catch (IOException e) {
            log.error("Error sending initial SSE event to user: {}", id, e);
            sseConnectionRepository.deleteById(id);  // 초기 이벤트 전송 실패 시 제거
        }
        return sseEmitter;
    }

    public void sendVodProcessingNotification(VodProcessingJob vodProcessingJob, SseEvent<?> data) {
        String sseId = vodProcessingJob.getUserId();
        Optional<SseEmitter> optionalSseEmitter = sseConnectionRepository.findById(sseId);
        if(optionalSseEmitter.isPresent()) {
            SseEmitter sseEmitter = optionalSseEmitter.get();
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("sse")
                        .data(data));
                log.info("SSE Event 사용자에게 전송완료 userid:{}: {}", sseId, "sse");
            } catch (IOException e) {
                log.error("Error sending SSE event to user: {}", sseId, e);
                sseEmitter.complete();
            }
        }
        VodProcessingNotification vodProcessingNotification = vodProcessingJob.getVodProcessingNotification();
        vodProcessingNotification.updateVodNotificationStatusToSent(LocalDateTime.now());
    }

    private void checkExistingEmitter(String id) {
        Optional<SseEmitter> oldEmitter = sseConnectionRepository.findById(id);
        oldEmitter.ifPresent(ResponseBodyEmitter::complete);
    }

}
