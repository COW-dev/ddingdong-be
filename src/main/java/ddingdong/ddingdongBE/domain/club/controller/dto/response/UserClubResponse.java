package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
        name = "UserClubResponse",
        description = "유저 - 동아리 정보 조회 응답"
)
public record UserClubResponse(
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
        @Schema(description = "모집 폼 ID", example = "6")
        Long formId,
        UserClubImageUrlResponse profileImage,
        UserClubImageUrlResponse introductionImage
) {

    public static UserClubResponse from(UserClubQuery query) {
        return new UserClubResponse(
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
                query.formId(),
                UserClubImageUrlResponse.from(query.profileImageUrlQuery()),
                UserClubImageUrlResponse.from(query.introductionImageUrlQuery())
        );
    }

    @Schema(
            name = "UserClubImageUrlResponse",
            description = "유저 - 동아리 정보 이미지 URL 조회 응답"
    )
    record UserClubImageUrlResponse(
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static UserClubImageUrlResponse from(UploadedFileUrlQuery query) {
            if (query == null) {
                return null;
            }
            return new UserClubImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }
}
