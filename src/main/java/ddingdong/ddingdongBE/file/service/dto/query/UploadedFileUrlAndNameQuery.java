package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedFileUrlAndNameQuery(
        String originUrl,
        String cdnUrl,
        String fileName
) {

}
