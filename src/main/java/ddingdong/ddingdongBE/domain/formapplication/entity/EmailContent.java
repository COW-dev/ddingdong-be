package ddingdong.ddingdongBE.domain.formapplication.entity;

import ddingdong.ddingdongBE.domain.club.entity.Club;

public record EmailContent(
        String subject,
        String htmlContent,
        String textContent
) {

    public static EmailContent of(String subject, String content, Club club) {
        String senderMessage = "\n\n※ 이 메일은 발신자 전용 메시지입니다. " + club.getName() + "(" + club.getClubUrl() + ")";
        String htmlSenderMessage = String.format(
                "<br><br><small style='color: #666;'>※ 이 메일은 발신자 전용 메시지입니다. <a href='%s' style='color: #666;'>%s</a></small>",
                club.getClubUrl(), club.getName()
        );

        String htmlContent = "<div>" + content.replace("\n", "<br>") + htmlSenderMessage + "</div>";
        String plainContent = content + senderMessage;

        return new EmailContent(subject, htmlContent, plainContent);
    }
}

