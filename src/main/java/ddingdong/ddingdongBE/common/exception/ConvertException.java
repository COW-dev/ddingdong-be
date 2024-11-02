package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class ConvertException extends CustomException {

    private static final String STRING_TO_LIST_CONVERT_ERROR_MESSAGE = "문자열을 리스트로 변환하는 것에 실패하였습니다.";
    private static final String LIST_TO_STRING_CONVERT_ERROR_MESSAGE = "리스트을 문자열로 변환하는 것에 실패하였습니다.";
    private static final String OBJECT_TO_JSON_ERROR_MESSAGE = "객체리스트를 JSON으로 변환하는 것에 실패하였습니다.";
    private static final String JSON_TO_OBJECT_CONVERT_ERROR_MESSAGE = "JSON을 객체리스트로 변환하는 것에 실패하였습니다.";


    public ConvertException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class StringToListConvertException extends ConvertException {
        public StringToListConvertException() {
            super(STRING_TO_LIST_CONVERT_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class ListToStringConvertException extends ConvertException {
        public ListToStringConvertException() {
            super(LIST_TO_STRING_CONVERT_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class ObjectToJsonConvertException extends ConvertException {
        public ObjectToJsonConvertException() {
            super(OBJECT_TO_JSON_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class JsonToObjectConvertException extends ConvertException {
        public JsonToObjectConvertException() {
            super(JSON_TO_OBJECT_CONVERT_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }
}
