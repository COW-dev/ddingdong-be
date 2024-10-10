package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubMemberResponse {

    private Long id;

    private String name;

    private String studentNumber;

    private String phoneNumber;

    private String position;

    private String department;

    @Builder
    public ClubMemberResponse(Long id, String name, String studentNumber, String phoneNumber, String position,
                              String department) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.department = department;
    }

    public static ClubMemberResponse from(ClubMember clubMember) {
        return ClubMemberResponse.builder()
                .id(clubMember.getId())
                .name(clubMember.getName())
                .studentNumber(clubMember.getStudentNumber())
                .phoneNumber(clubMember.getPhoneNumber())
                .position(clubMember.getPosition().getName())
                .department(clubMember.getDepartment()).build();
    }

}
