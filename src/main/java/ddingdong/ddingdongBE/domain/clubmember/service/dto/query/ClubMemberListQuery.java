package ddingdong.ddingdongBE.domain.clubmember.service.dto.query;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;

public record ClubMemberListQuery(
        Long id,
        String name,
        String studentNumber,
        String phoneNumber,
        String position,
        String department
) {

    public static ClubMemberListQuery from(ClubMember clubMember){
        return new ClubMemberListQuery(
                clubMember.getId(),
                clubMember.getName(),
                clubMember.getStudentNumber(),
                clubMember.getPhoneNumber(),
                clubMember.getPosition().getName(),
                clubMember.getDepartment()
        );
    }

}
