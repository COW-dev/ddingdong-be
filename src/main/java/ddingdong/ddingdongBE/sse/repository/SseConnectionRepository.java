package ddingdong.ddingdongBE.sse.repository;

import java.util.Optional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseConnectionRepository {

    void save(String id, SseEmitter sseEmitter);

    Optional<SseEmitter> findById(String id);

    void deleteById(String id);

    void deleteAll();

}
