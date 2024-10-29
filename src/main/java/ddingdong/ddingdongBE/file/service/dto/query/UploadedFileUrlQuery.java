package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedFileUrlQuery(
        String id,
        String originUrl,
        String cdnUrl
) {

}
