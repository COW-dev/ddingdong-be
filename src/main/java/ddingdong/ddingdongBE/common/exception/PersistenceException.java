package ddingdong.ddingdongBE.common.exception;

sealed public class PersistenceException extends CustomException {

    public PersistenceException(String message) {
        super(message);
    }

    public static final class ResourceNotFound extends PersistenceException {

        public ResourceNotFound(String message) {
            super(message);
        }

    }
}
