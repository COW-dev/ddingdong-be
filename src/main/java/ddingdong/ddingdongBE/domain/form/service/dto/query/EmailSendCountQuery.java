package ddingdong.ddingdongBE.domain.form.service.dto.query;

public record EmailSendCountQuery(
        int totalCount,
        int successCount,
        int failCount
) {


}
