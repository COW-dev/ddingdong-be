package ddingdong.ddingdongBE.email;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.email.infrastructure.SesEmailRetryPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.RetryableException;
import software.amazon.awssdk.core.exception.SdkClientException;

class SesEmailRetryPolicyTest {

    private SesEmailRetryPolicy retryPolicy;

    @BeforeEach
    void setUp() {
        retryPolicy = new SesEmailRetryPolicy();
    }

    @DisplayName("RetryableException은 재시도 가능하다")
    @Test
    void retryableException_is_retryable() {
        // given
        Exception exception = RetryableException.create("Retryable error", new RuntimeException());

        // when
        boolean result = retryPolicy.isRetryable(exception);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("AWS 서비스 예외에서 Throttling 에러는 재시도 가능하다")
    @Test
    void aws_throttling_exception_is_retryable() {
        // given
        AwsServiceException exception = AwsServiceException.builder()
                .statusCode(429)
                .awsErrorDetails(
                        AwsErrorDetails.builder()
                                .errorCode("Throttling")
                                .errorMessage("Rate exceeded")
                                .serviceName("SES")
                                .build()
                )
                .build();

        // when
        boolean result = retryPolicy.isRetryable(exception);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("AWS 서비스 예외에서 5xx 상태코드는 재시도 가능하다")
    @Test
    void aws_5xx_exception_is_retryable() {
        // given
        AwsServiceException exception = AwsServiceException.builder()
                .statusCode(500)
                .awsErrorDetails(
                        AwsErrorDetails.builder()
                                .errorCode("InternalServerError")
                                .errorMessage("Internal server error mock")
                                .serviceName("SES")
                                .build()
                )
                .build();

        // when
        boolean result = retryPolicy.isRetryable(exception);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("AWS 서비스 예외에서 4xx 상태코드는 재시도 불가능하다")
    @Test
    void aws_4xx_exception_is_not_retryable() {
        // given
        AwsServiceException exception = AwsServiceException.builder()
                .statusCode(400)
                .awsErrorDetails(
                        AwsErrorDetails.builder()
                                .errorCode("InvalidParameterValue")
                                .errorMessage("Invalid parameter value mock")
                                .serviceName("SES")
                                .build()
                )
                .build();

        // when
        boolean result = retryPolicy.isRetryable(exception);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("SDK 클라이언트 예외에서 timeout 메시지가 포함된 경우 재시도 가능하다")
    @Test
    void sdk_timeout_exception_is_retryable() {
        // given
        SdkClientException exception = SdkClientException.create("Request timed out");

        // when
        boolean result = retryPolicy.isRetryable(exception);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("기타 예외는 재시도 불가능하다")
    @Test
    void other_exceptions_are_not_retryable() {
        // given
        Exception exception = new RuntimeException("Some runtime error");

        // when
        boolean result = retryPolicy.isRetryable(exception);

        // then
        assertThat(result).isFalse();
    }
}
