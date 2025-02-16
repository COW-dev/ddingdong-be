package ddingdong.ddingdongBE.email.dto;

public record EmailContent(
        String subject,
        String htmlContent,
        String textContent
) {

    public static EmailContent of(String subject, String content) {
        String htmlContent = "<div>" + content.replace("\n", "<br>") + "</div>";

        return new EmailContent(subject, htmlContent, content);
    }
}
