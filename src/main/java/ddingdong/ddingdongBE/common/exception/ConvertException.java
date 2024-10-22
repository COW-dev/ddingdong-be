package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

sealed public class ConvertException extends CustomException {

    private static final String STRING_TO_LIST_CONVERT_ERROR_MESSAGE = "문자열을 리스트로 변환하는 것에 실패하였습니다.";
    private static final String LIST_TO_STRING_CONVERT_ERROR_MESSAGE = "리스트을 문자열로 변환하는 것에 실패하였습니다.";

    public ConvertException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class StringToListConvertException extends ConvertException {

        public StringToListConvertException() {
            super(STRING_TO_LIST_CONVERT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.value());
        }
    }

    public static final class ListToStringConvertException extends ConvertException {

        public ListToStringConvertException() {
            super(LIST_TO_STRING_CONVERT_ERROR_MESSAGE, INTERNAL_SERVER_ERROR.value());
        }
    }
}
