package ddingdong.ddingdongBE.domain.form.service.dto.command;

public record SendApplicationResultEmailCommand(
        Long formId,
        String title,
        String target,
        String message
) {

}
