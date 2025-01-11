package ddingdong.ddingdongBE.sse.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.sse.repository.SseConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootTest
@Slf4j
class SseConnectionServiceTest extends TestContainerSupport {

    @Autowired
    private SseConnectionService sseConnectionService;

    @Autowired
    private SseConnectionRepository sseConnectionRepository;

    private final String TEST_ID = "testId";
    private final long TEST_TIMEOUT = 60000L;

    @DisplayName("sse 구독 요청 시 sse 연결이 성공적으로 생성되어야 한다.")
    @Test
    void subscribe_Success() {
        // when
        SseEmitter emitter = sseConnectionService.subscribe(TEST_ID, TEST_TIMEOUT);

        // then
        assertThat(emitter).isNotNull();
        assertThat(sseConnectionRepository.findById(TEST_ID)).isNotNull();
    }

    @DisplayName("동일한 ID로 재구독 시 기존 연결은 종료되고 새로운 연결이 생성되어야 한다")
    @Test
    void subscribe_ExistingEmitter() {
        // given
        SseEmitter firstEmitter = sseConnectionService.subscribe(TEST_ID, TEST_TIMEOUT);

        // when
        SseEmitter secondEmitter = sseConnectionService.subscribe(TEST_ID, TEST_TIMEOUT);

        // then
        assertThat(firstEmitter).isNotEqualTo(secondEmitter);
        assertThat(sseConnectionRepository.findById(TEST_ID)).isPresent();
        assertThat(sseConnectionRepository.findById(TEST_ID).get()).isEqualTo(secondEmitter);
    }

    @DisplayName("서로 다른 ID로 구독 시 각각 독립적인 SSE 연결이 생성되어야 한다")
    @Test
    void multipleSubscribers_WorkIndependently() {
        // given
        String TEST_ID_2 = "testId2";

        // when
        sseConnectionService.subscribe(TEST_ID, TEST_TIMEOUT);
        sseConnectionService.subscribe(TEST_ID_2, TEST_TIMEOUT);

        // then
        assertThat(sseConnectionRepository.findById(TEST_ID)).isNotNull();
        assertThat(sseConnectionRepository.findById(TEST_ID_2)).isNotNull();
        assertThat(sseConnectionRepository.findById(TEST_ID))
                .isNotEqualTo(sseConnectionRepository.findById(TEST_ID_2));
    }
}
