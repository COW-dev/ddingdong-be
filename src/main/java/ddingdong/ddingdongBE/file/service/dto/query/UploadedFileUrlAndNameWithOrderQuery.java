package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedFileUrlAndNameWithOrderQuery(
        String id,
        int order,
        String fileName,
        String originUrl,
        String cdnUrl
) {

    public static UploadedFileUrlAndNameWithOrderQuery of(UploadedFileUrlAndNameQuery query, int order) {
        return new UploadedFileUrlAndNameWithOrderQuery(
                query.id(),
                order,
                query.fileName(),
                query.originUrl(),
                query.cdnUrl());
    }

}
