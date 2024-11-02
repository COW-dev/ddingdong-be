package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedFileUrlAndNameQuery(
    String id,
    String fileName,
    String originUrl,
    String cdnUrl
) {

}
