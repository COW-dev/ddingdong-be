package ddingdong.ddingdongBE.domain.club.controller.dto.response;

import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

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
        LocalDate startDate,
        @Schema(description = "모집마감날짜", example = "2024-01-01 00:00")
        LocalDate endDate,
        @Schema(description = "정기활동", example = "정기활동")
        String regularMeeting,
        @Schema(description = "동아리 소개", example = "소개")
        String introduction,
        @Schema(description = "동아리 활동", example = "활동")
        String activity,
        @Schema(description = "인재상", example = "인재상")
        String ideal,
        @Schema(description = "동아리 프로필 이미지 Url", example = "url")
        MyClubInfoImageUrlResponse profileImage,
        MyClubInfoImageUrlResponse introductionImage
) {

    public static MyClubInfoResponse from(MyClubInfoQuery query) {
        return new MyClubInfoResponse(
                query.name(),
                query.category(),
                query.tag(),
                query.leader(),
                query.phoneNumber(),
                query.location(),
                query.startDate(),
                query.endDate(),
                query.regularMeeting(),
                query.introduction(),
                query.activity(),
                query.ideal(),
                MyClubInfoImageUrlResponse.from(query.profileImageUrlQuery()),
                MyClubInfoImageUrlResponse.from(query.introductionImageUrlQuery())
        );
    }

    @Schema(
            name = "MyClubInfoImageUrlResponse",
            description = "동아리 - 내 동아리 정보 이미지 URL 조회 응답"
    )
    record MyClubInfoImageUrlResponse(
            @Schema(description = "파일 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
            String id,
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static MyClubInfoImageUrlResponse from(UploadedFileUrlQuery query) {
            if (query == null) {
                return null;
            }
            return new MyClubInfoImageUrlResponse(query.id(), query.originUrl(), query.cdnUrl());
        }

    }

}
