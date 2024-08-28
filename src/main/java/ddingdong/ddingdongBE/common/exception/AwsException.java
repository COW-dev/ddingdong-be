package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

sealed public class AwsException extends CustomException {

    public static final String AWS_SERVICE_ERROR_MESSAGE = "AWS 서비스 오류로 인해 Presigned URL 생성에 실패했습니다.";
    public static final String AWS_CLIENT_ERROR_MESSAGE = "AWS 클라이언트 오류로 인해 Presigned URL 생성에 실패했습니다.";

    public AwsException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class AwsService extends AwsException {

        public AwsService() {
            super(AWS_SERVICE_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.value());
        }
    }

    public static final class AwsClient extends AwsException {

        public AwsClient() {
            super(AWS_CLIENT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.value());
        }
    }
}
