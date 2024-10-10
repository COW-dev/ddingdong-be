package ddingdong.ddingdongBE.domain.clubmember.service.dto.command;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UpdateClubMemberListCommand(
        Long userId,
        MultipartFile clubMemberListFile
) {

}
