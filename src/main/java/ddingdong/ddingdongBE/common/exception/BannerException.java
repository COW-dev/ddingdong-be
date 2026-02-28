package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class BannerException extends CustomException {

    public BannerException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class BannerImageGenerationException extends BannerException {

        private static final String MESSAGE = "배너 이미지 변환 중 오류가 발생했습니다.";

        public BannerImageGenerationException() {
            super(MESSAGE, INTERNAL_SERVER_ERROR.value());
        }
    }
}
