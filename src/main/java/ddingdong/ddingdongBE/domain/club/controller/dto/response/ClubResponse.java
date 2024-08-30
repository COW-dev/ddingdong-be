package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Schema(
        name = "ClubResponse",
        description = "동아리 정보 조회 응답"
)
@Builder
public record ClubResponse(
        @Schema(description = "동아리명", example = "cow")
        String name,
        @Schema(description = "분과", example = "학술")
        String category,
        @Schema(description = "태그", example = "IT")
        String tag,
        @Schema(description = "동아리 회장", example = "홍길동")
        String leader,
        @Schema(description = "회장 전화번호", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "동아리방 위치", example = "S0000")
        String location,
        @Schema(description = "모집 시작 기간", example = "2024-08-19 00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startRecruitPeriod,
        @Schema(description = "모집 마감 기간", example = "2024-08-19 00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endRecruitPeriod,
        @Schema(description = "정기모임", example = "매주 월요일 18:00시")
        String regularMeeting,
        @Schema(description = "동아리 소개", example = "소개")
        String introduction,
        @Schema(description = "활동 내용", example = "활동 내용")
        String activity,
        @Schema(description = "인재상", example = "인재상")
        String ideal,
        @Schema(description = "모집 폼지", example = "https://form.com")
        String formUrl,
        FileUrlResponse profileImageFile,
        FileUrlResponse introductionImageFile,
        List<ClubMemberResponse> clubMembers
) {

    public static ClubResponse of(Club club, FileUrlResponse profileImageFile, FileUrlResponse introductionImageFile,
                                  List<ClubMemberResponse> clubMembers) {
        return ClubResponse.builder()
                .name(club.getName())
                .category(club.getCategory())
                .tag(club.getTag())
                .leader(club.getLeader())
                .phoneNumber(club.getPhoneNumber().getNumber())
                .location(club.getLocation().getValue())
                .startRecruitPeriod(club.getStartRecruitPeriod())
                .endRecruitPeriod(club.getEndRecruitPeriod())
                .regularMeeting(club.getRegularMeeting())
                .introduction(club.getIntroduction())
                .activity(club.getActivity())
                .ideal(club.getIdeal())
                .formUrl(club.getFormUrl())
                .profileImageFile(profileImageFile)
                .introductionImageFile(introductionImageFile)
                .clubMembers(clubMembers)
                .build();
    }

}
