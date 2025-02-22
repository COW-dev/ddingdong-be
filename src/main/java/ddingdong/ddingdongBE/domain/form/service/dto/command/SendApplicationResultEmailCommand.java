package ddingdong.ddingdongBE.domain.form.service.dto.command;

public record SendApplicationResultEmailCommand(
        Long userId,
        Long formId,
        String title,
        String target,
        String message
) {

}
