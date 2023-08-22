package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubMemberDto {

    private Long id;

    private String name;

    private String studentNumber;

    private String phoneNumber;

    private String position;

    private String department;

    @Builder
    public ClubMemberDto(Long id, String name, String studentNumber, String phoneNumber, String position,
                         String department) {
        this.id = id;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.department = department;
    }

    public static ClubMemberDto from(ClubMember clubMember) {
        return ClubMemberDto.builder()
                .id(clubMember.getId())
                .name(clubMember.getName())
                .studentNumber(clubMember.getStudentNumber())
                .phoneNumber(clubMember.getPhoneNumber())
                .position(clubMember.getPosition().getName())
                .department(clubMember.getDepartment()).build();
    }

    public ClubMember toEntity(Club club) {
        return ClubMember.builder()
                .club(club)
                .name(name)
                .studentNumber(studentNumber)
                .phoneNumber(phoneNumber)
                .position(Position.valueOf(position))
                .department(department).build();
    }


}
