package ddingdong.ddingdongBE.domain.clubmember.service.dto;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import lombok.Builder;

@Builder
public record UpdateClubMemberCommand(
        String name,
        String studentNumber,
        String phoneNumber,
        Position position,
        String department
) {

    public ClubMember toEntity() {
        return ClubMember.builder()
                .name(name)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .position(position)
                .department(department).build();
    }


}
