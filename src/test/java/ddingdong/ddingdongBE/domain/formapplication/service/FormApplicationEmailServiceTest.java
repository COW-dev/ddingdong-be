package ddingdong.ddingdongBE.domain.formapplication.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.email.entity.EmailContent;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationEmailSender;
import ddingdong.ddingdongBE.domain.formapplication.repository.EmailSendHistoryRepository;
import java.util.List;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FormApplicationEmailServiceTest {

    @Mock
    private FormApplicationEmailSender formApplicationEmailSender;

    @Mock
    private Executor emailAsyncExecutor;

    @Mock
    private EmailSendHistoryRepository emailSendHistoryRepository;

    private FormApplicationEmailService formApplicationEmailService;

    @BeforeEach
    void setUp() {
        formApplicationEmailService = new FormApplicationEmailService(
                formApplicationEmailSender,
                emailAsyncExecutor,
                emailSendHistoryRepository
        );
    }

    @DisplayName("여러 지원자에게 대량 이메일을 비동기로 전송한다")
    @Test
    void sendBulkResult() {
        // given
        FormApplication formApplication1 = FormApplicationFixture.firstPassFormApplication();
        FormApplication formApplication2 = FormApplicationFixture.firstPassFormApplication();

        List<FormApplication> formApplications = List.of(formApplication1, formApplication2);
        
        EmailContent emailContent = EmailContent.of("제목", "내용", ClubFixture.createClub());
        
        EmailSendHistory emailSendHistory1 = EmailSendHistory.createPending(formApplication1);
        EmailSendHistory emailSendHistory2 = EmailSendHistory.createPending(formApplication2);
        
        given(emailSendHistoryRepository.save(any(EmailSendHistory.class)))
                .willReturn(emailSendHistory1, emailSendHistory2);

        // when
        formApplicationEmailService.sendBulkResult(formApplications, emailContent);

        // then
        verify(emailSendHistoryRepository, times(2)).save(any(EmailSendHistory.class));
        verify(emailAsyncExecutor, times(2)).execute(any(Runnable.class));
    }

    @DisplayName("EmailSendHistory가 생성되고 비동기 실행자에 작업이 등록된다")
    @Test
    void sendBulkResult_creates_history_and_schedules_async_work() {
        // given
        FormApplication formApplication = FormApplicationFixture.pendingFormApplication();
        List<FormApplication> formApplications = List.of(formApplication);
        EmailContent emailContent = EmailContent.of("제목", "내용", ClubFixture.createClub());
        
        EmailSendHistory emailSendHistory = EmailSendHistory.createPending(formApplication);
        given(emailSendHistoryRepository.save(any(EmailSendHistory.class)))
                .willReturn(emailSendHistory);

        // when
        formApplicationEmailService.sendBulkResult(formApplications, emailContent);

        // then
        verify(emailSendHistoryRepository).save(argThat(history -> 
                history.getFormApplication().equals(formApplication) &&
                history.getStatus().name().equals("PENDING")
        ));
        
        verify(emailAsyncExecutor).execute(any(Runnable.class));
    }

    @DisplayName("빈 지원자 리스트일 때는 아무 작업도 수행하지 않는다")
    @Test
    void sendBulkResult_with_empty_list() {
        // given
        List<FormApplication> emptyFormApplications = List.of();
        EmailContent emailContent = EmailContent.of("제목", "내용", ClubFixture.createClub());

        // when
        formApplicationEmailService.sendBulkResult(emptyFormApplications, emailContent);

        // then
        verify(emailSendHistoryRepository, never()).save(any());
        verify(emailAsyncExecutor, never()).execute(any());
        verify(formApplicationEmailSender, never()).sendResult(any(), any(), any());
    }
}
