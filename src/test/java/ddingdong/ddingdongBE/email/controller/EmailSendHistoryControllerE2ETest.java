package ddingdong.ddingdongBE.email.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.support.NonTxTestContainerSupport;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.email.controller.dto.UpdateEmailSendStatusRequest;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmailSendHistoryControllerE2ETest extends NonTxTestContainerSupport {

    @LocalServerPort
    private int port;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    private FormApplication formApplication;
    private EmailSendHistory emailSendHistory;
    private String messageTrackingId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        
        formApplication = FormApplicationFixture.pendingFormApplication();
        formApplication = formApplicationRepository.save(formApplication);
        
        messageTrackingId = "msg-12345678";
        emailSendHistory = EmailSendHistoryFixture.createWithMessageId(formApplication, messageTrackingId);
        emailSendHistory = emailSendHistoryRepository.save(emailSendHistory);
    }

    @DisplayName("이메일 전송 상태를 Delivery로 성공적으로 업데이트한다")
    @Test
    void updateEmailSendStatus_delivery_success() {
        // given
        UpdateEmailSendStatusRequest request = new UpdateEmailSendStatusRequest(
                "Delivery",
                messageTrackingId
        );

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/server/internal/email-histories/status")
        .then()
                .statusCode(HttpStatus.OK.value());

        // verify
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getStatus()).isEqualTo(EmailSendStatus.DELIVERY_SUCCESS);
    }

    @DisplayName("이메일 전송 상태를 Bounce로 성공적으로 업데이트한다")
    @Test
    void updateEmailSendStatus_bounce_success() {
        // given
        UpdateEmailSendStatusRequest request = new UpdateEmailSendStatusRequest(
                "Bounce",
                messageTrackingId
        );

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/server/internal/email-histories/status")
        .then()
                .statusCode(HttpStatus.OK.value());

        // verify
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getStatus()).isEqualTo(EmailSendStatus.BOUNCE_REJECT);
    }
}
