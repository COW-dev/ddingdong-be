package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class ParsingExcelFileException extends CustomException {

    public ParsingExcelFileException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class NonExcelFile extends ParsingExcelFileException {

        public NonExcelFile(String message) {
            super(message, BAD_REQUEST.value());
        }
    }

    public static final class ExcelIO extends ParsingExcelFileException {

        public ExcelIO(String message) {
            super(message, BAD_REQUEST.value());
        }
    }

    public static final class NonValidatedStringCellValue extends ParsingExcelFileException {

        public NonValidatedStringCellValue(String message) {
            super(message, BAD_REQUEST.value());
        }
    }
}
