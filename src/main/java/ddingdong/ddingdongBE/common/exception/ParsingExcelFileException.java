package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class ParsingExcelFileException extends CustomException {

    public static final String NON_EXCEL_FILE_ERROR_MESSAGE = "엑셀 파일(.xls, .xlxs) 이 아닙니다.";
    public static final String EXCEL_IO_ERROR_MESSAGE = "올바른 엑셀 파일을 사용해주세요.";

    public ParsingExcelFileException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class NonExcelFile extends ParsingExcelFileException {

        public NonExcelFile() {
            super(NON_EXCEL_FILE_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class ExcelIO extends ParsingExcelFileException {

        public ExcelIO() {
            super(EXCEL_IO_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }
}
