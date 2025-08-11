package ddingdong.ddingdongBE.domain.clubmember.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import lombok.Builder;

@Builder
public record CreateClubMemberCommand(
        Long userId,
        String name,
        String studentNumber,
        String phoneNumber,
        Position position,
        String department
) {

    public ClubMember toEntity(Club club) {
        return ClubMember.builder()
                .name(name)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .position(position)
                .department(department)
                .club(club)
                .build();
    }
}
