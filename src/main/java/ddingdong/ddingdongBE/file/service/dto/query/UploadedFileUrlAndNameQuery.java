package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedFileUrlAndNameQuery(
        String id,
        String name,
        String originUrl,
        String cdnUrl
) {

}
