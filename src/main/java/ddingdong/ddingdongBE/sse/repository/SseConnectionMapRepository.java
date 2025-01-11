package ddingdong.ddingdongBE.sse.repository;

import ddingdong.ddingdongBE.common.exception.PersistenceException;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.common.exception.SseException;
import ddingdong.ddingdongBE.common.exception.SseException.SseEmitterNotFound;
import java.util.Map;
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
    public SseEmitter getById(String id) {
        SseEmitter sseEmitter = emitters.get(id);
        if(sseEmitter == null) {
            throw new SseEmitterNotFound(id);
        }
        return sseEmitter;
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
