package ddingdong.ddingdongBE.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseConnectionRepository {

    void save(String id, SseEmitter sseEmitter);

    SseEmitter getById(String id);

    void deleteById(String id);

    void deleteAll();

}
