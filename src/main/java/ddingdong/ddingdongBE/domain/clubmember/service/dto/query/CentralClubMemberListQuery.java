package ddingdong.ddingdongBE.domain.clubmember.service.dto.query;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;

public record CentralClubMemberListQuery(
        Long id,
        String name,
        String studentNumber,
        String phoneNumber,
        String position,
        String department
) {

    public static CentralClubMemberListQuery from(ClubMember clubMember){
        return new CentralClubMemberListQuery(
                clubMember.getId(),
                clubMember.getName(),
                clubMember.getStudentNumber(),
                clubMember.getPhoneNumber(),
                clubMember.getPosition().getName(),
                clubMember.getDepartment()
        );
    }

}
