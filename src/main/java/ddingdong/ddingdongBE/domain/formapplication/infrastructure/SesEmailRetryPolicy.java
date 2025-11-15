package ddingdong.ddingdongBE.domain.formapplication.infrastructure;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.RetryableException;
import software.amazon.awssdk.core.exception.SdkClientException;

@Component
public class SesEmailRetryPolicy {

    public boolean isRetryable(Exception e) {
        if (e instanceof RetryableException) {
            return true;
        }

        if (e instanceof AwsServiceException awsEx) {
            String code = awsEx.awsErrorDetails().errorCode();
            return "Throttling".equals(code) || awsEx.statusCode() >= 500;
        }

        if (e instanceof SdkClientException sdkEx) {
            String msg = sdkEx.getMessage();
            return msg != null && msg.contains("timed out");
        }

        return false;
    }
}
