package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import java.time.LocalDateTime;
import java.util.List;

public record MyClubInfoResponse(
        String name,
        String category,
        String tag,
        String leader,
        String phoneNumber,
        String location,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startRecruitPeriod,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endRecruitPeriod,
        String regularMeeting,
        String introduction,
        String activity,
        String ideal,
        String formUrl,
        List<String> profileImageUrls,
        List<String> introduceImageUrls,
        List<ClubMemberResponse> clubMembers
) {

    public static MyClubInfoResponse from(MyClubInfoQuery query) {
        return new MyClubInfoResponse(
                query.name(),
                query.category(),
                query.tag(),
                query.leader(),
                query.phoneNumber(),
                query.location(),
                query.startRecruitPeriod(),
                query.endRecruitPeriod(),
                query.regularMeeting(),
                query.introduction(),
                query.activity(),
                query.ideal(),
                query.formUrl(),
                query.profileImageUrls(),
                query.introduceImageUrls(),
                query.clubMembers().stream()
                        .map(ClubMemberResponse::from)
                        .toList()
        );
    }

    public record ClubMemberResponse(
            Long id,
            String name,
            String studentNumber,
            String phoneNumber,
            String position,
            String department
    ) {

        public static ClubMemberResponse from(ClubMember clubMember) {
            return new ClubMemberResponse(
                    clubMember.getId(),
                    clubMember.getName(),
                    clubMember.getStudentNumber(),
                    clubMember.getPhoneNumber(),
                    clubMember.getPosition().getName(),
                    clubMember.getDepartment()
            );
        }
    }

}
