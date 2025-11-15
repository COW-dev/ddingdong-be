package ddingdong.ddingdongBE.common.support;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakeSesClient implements SesClient {
    
    private final List<SendEmailRequest> sentEmails = new ArrayList<>();
    private boolean shouldThrowRetryableException = false;
    private boolean shouldThrowNonRetryableException = false;
    private String customMessageId = null;

    @Override
    public SendEmailResponse sendEmail(SendEmailRequest sendEmailRequest) {
        sentEmails.add(sendEmailRequest);
        
        if (shouldThrowRetryableException) {
            throw AwsServiceException.builder()
                    .statusCode(500)
                    .message("Internal Server Error")
                    .build();
        }
        
        if (shouldThrowNonRetryableException) {
            throw SdkClientException.create("Invalid email address");
        }
        
        String messageId = customMessageId != null ? customMessageId : UUID.randomUUID().toString();
        
        return SendEmailResponse.builder()
                .messageId(messageId)
                .build();
    }

    public void reset() {
        sentEmails.clear();
        shouldThrowRetryableException = false;
        shouldThrowNonRetryableException = false;
        customMessageId = null;
    }

    public void setThrowRetryableException(boolean shouldThrow) {
        this.shouldThrowRetryableException = shouldThrow;
    }

    public void setThrowNonRetryableException(boolean shouldThrow) {
        this.shouldThrowNonRetryableException = shouldThrow;
    }

    public void setCustomMessageId(String messageId) {
        this.customMessageId = messageId;
    }

    public List<SendEmailRequest> getSentEmails() {
        return new ArrayList<>(sentEmails);
    }

    public int getSentEmailCount() {
        return sentEmails.size();
    }

    @Override
    public String serviceName() {
        return "ses";
    }

    @Override
    public void close() {
    }
}