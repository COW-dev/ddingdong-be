package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedFileUrlWithOrderQuery(
        String id,
        int order,
        String originUrl,
        String cdnUrl
) {

    public static UploadedFileUrlWithOrderQuery of(UploadedFileUrlQuery query, int order) {
        return new UploadedFileUrlWithOrderQuery(
                query.id(),
                order,
                query.originUrl(),
                query.cdnUrl());
    }

}
