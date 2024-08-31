package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class ParsingFileException extends CustomException {

    public static final String NON_EXCEL_FILE_ERROR_MESSAGE = "엑셀 파일(.xls, .xlxs) 이 아닙니다.";
    public static final String EXCEL_IO_ERROR_MESSAGE = "올바른 엑셀 파일을 사용해주세요.";
    public static final String UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE = "해당 파일은 지원하지 않는 타입입니다.";

    public ParsingFileException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class NonExcelFile extends ParsingFileException {

        public NonExcelFile() {
            super(NON_EXCEL_FILE_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class ExcelIO extends ParsingFileException {

        public ExcelIO() {
            super(EXCEL_IO_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class UnsupportedFileTypeException extends ParsingFileException {

        public UnsupportedFileTypeException() {
            super(UNSUPPORTED_FILE_TYPE_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }
}
