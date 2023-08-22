package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateClubMemberRequest {

    List<ClubMemberDto> clubMemberList;

}
