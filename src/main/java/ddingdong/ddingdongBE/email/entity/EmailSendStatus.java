package ddingdong.ddingdongBE.email.entity;

public enum EmailSendStatus {

    PENDING,
    SENDING,
    TEMPORARY_FAILURE,
    PERMANENT_FAILURE,
    DELIVERY_SUCCESS,
    BOUNCE_REJECT,
    COMPLAINT_REJECT,
    ;
}
