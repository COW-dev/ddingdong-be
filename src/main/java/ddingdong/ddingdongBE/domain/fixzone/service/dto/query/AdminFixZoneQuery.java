package ddingdong.ddingdongBE.domain.fixzone.service.dto.query;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameWithOrderQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlWithOrderQuery;
import java.time.LocalDateTime;
import java.util.List;

public record AdminFixZoneQuery(
        Long id,
        String clubLocation,
        String clubName,
        String title,
        String content,
        boolean isCompleted,
        LocalDateTime requestedAt,
        List<UploadedFileUrlAndNameWithOrderQuery> imageUrlQueries,
        List<FixZoneCommentQuery> fixZoneCommentQueries
) {

    public static AdminFixZoneQuery of(
            FixZone fixZone,
            List<UploadedFileUrlAndNameWithOrderQuery> fixZoneImageUrlQueries,
            UploadedFileUrlQuery commenterProfileImageUrlQuery) {
        return new AdminFixZoneQuery(
                fixZone.getId(),
                fixZone.getClub().getLocation().getValue(),
                fixZone.getClub().getName(),
                fixZone.getTitle(),
                fixZone.getContent(),
                fixZone.isCompleted(),
                fixZone.getCreatedAt(),
                fixZoneImageUrlQueries,
                fixZone.getFixZoneComments().stream()
                        .map(fixZoneComment -> FixZoneCommentQuery.of(fixZoneComment, commenterProfileImageUrlQuery))
                        .toList()
        );
    }

    public record FixZoneCommentQuery(
            Long id,
            String commenter,
            String content,
            UploadedFileUrlQuery profileImageQuery,
            LocalDateTime createdAt
    ) {

        public static FixZoneCommentQuery of(
                FixZoneComment fixZoneComment,
                UploadedFileUrlQuery commenterProfileImageUrlQuery) {
            return new FixZoneCommentQuery(
                    fixZoneComment.getId(),
                    fixZoneComment.getClub().getName(),
                    fixZoneComment.getContent(),
                    commenterProfileImageUrlQuery,
                    fixZoneComment.getCreatedAt()
            );
        }
    }

}
