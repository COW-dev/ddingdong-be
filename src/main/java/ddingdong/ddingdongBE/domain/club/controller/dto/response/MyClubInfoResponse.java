package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
        name = "MyClubInfoResponse",
        description = "중앙동아리 - 내 동아리 정보 조회 응답"
)
public record MyClubInfoResponse(
        @Schema(description = "동아리명", example = "동아리명")
        String name,
        @Schema(description = "카테고리", example = "사회연구")
        String category,
        @Schema(description = "분과", example = "IT")
        String tag,
        @Schema(description = "회장 이름", example = "홍길동")
        String leader,
        @Schema(description = "연락처", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "동아리방 위치", example = "S1111")
        String location,
        @Schema(description = "모집시작날짜", example = "2024-01-01 00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startRecruitPeriod,
        @Schema(description = "모집마감날짜", example = "2024-01-01 00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endRecruitPeriod,
        @Schema(description = "정기활동", example = "정기활동")
        String regularMeeting,
        @Schema(description = "동아리 소개", example = "소개")
        String introduction,
        @Schema(description = "동아리 활동", example = "활동")
        String activity,
        @Schema(description = "인재상", example = "인재상")
        String ideal,
        @Schema(description = "모집Url", example = "url")
        String formUrl,
        @Schema(description = "동아리 프로필 이미지 Url", example = "url")
        MyClubInfoImageUrlResponse profileImageUrl,
        @Schema(description = "동아리 소개 이미지 Url ", example = "url")
        MyClubInfoImageUrlResponse introduceImageUrl,
        @Schema(description = "동아리원 목록")
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
                MyClubInfoImageUrlResponse.from(query.profileImageUrlQuery()),
                MyClubInfoImageUrlResponse.from(query.introductionImageUrlQuery()),
                query.clubMembers().stream()
                        .map(ClubMemberResponse::from)
                        .toList()
        );
    }

    @Schema(
            name = "MyClubInfoImageUrlResponse",
            description = "동아리 - 내 동아리 정보 이미지 URL 조회 응답"
    )
    record MyClubInfoImageUrlResponse(
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static MyClubInfoImageUrlResponse from(UploadedFileUrlQuery query) {
            return new MyClubInfoImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }

    @Schema(
            name = "ClubMemberResponse",
            description = "중앙동아리 - 동아리원 조회 응답"
    )
    public record ClubMemberResponse(
            @Schema(description = "식별자", example = "1")
            Long id,
            @Schema(description = "이름", example = "홍길동")
            String name,
            @Schema(description = "학번", example = "60001111")
            String studentNumber,
            @Schema(description = "전화번호", example = "010-1234-5678")
            String phoneNumber,
            @Schema(description = "동아리원 역할",
                    example = "LEADER",
                    allowableValues = {"LEADER", "EXECUTION", "MEMBER"}
            )
            String position,
            @Schema(description = "학과", example = "학과")
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
