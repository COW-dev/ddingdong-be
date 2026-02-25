package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FileException extends CustomException {

    private static final String UPLOADED_FILE_NOT_FOUND_MESSAGE = "업로드 할 파일이 없습니다.";
    private static final String FILE_READING_ERROR_MESSAGE = "파일을 읽을 수 없습니다.";

    public FileException(String message, int errorCode) { super(message, errorCode); }

    public static final class UploadedFileNotFoundException extends FileException {

        public UploadedFileNotFoundException() {
            super(UPLOADED_FILE_NOT_FOUND_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class FileReadingException extends FileException {

      public FileReadingException() {
        super(FILE_READING_ERROR_MESSAGE, BAD_REQUEST.value());
      }
    }
}
