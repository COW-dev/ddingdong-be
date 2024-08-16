package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;
import ddingdong.ddingdongBE.domain.club.entity.Position;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateClubMemberRequest {

    List<UpdatedClubMember> clubMemberList;

    @Getter
    @NoArgsConstructor
    public static class UpdatedClubMember {

        private Long id;

        private String name;

        private String studentNumber;

        private String phoneNumber;

        private String position;

        private String department;

        @Builder
        public UpdatedClubMember(Long id, String name, String studentNumber, String phoneNumber, String position,
                                 String department) {
            this.id = id;
            this.name = name;
            this.studentNumber = studentNumber;
            this.phoneNumber = phoneNumber;
            this.position = position;
            this.department = department;
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
}
