package ddingdong.ddingdongBE.sse.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseConnectionMapRepository implements SseConnectionRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public void save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
    }

    @Override
    public Optional<SseEmitter> findById(String id) {
        return Optional.ofNullable(emitters.get(id));
    }

    @Override
    public void deleteById(String id) {
        SseEmitter removed = this.emitters.remove(id);
        if (removed != null) {
            removed.complete();
        }
    }

    @Override
    public void deleteAll() {
        emitters.values().forEach(SseEmitter::complete);
        emitters.clear();
    }
}
