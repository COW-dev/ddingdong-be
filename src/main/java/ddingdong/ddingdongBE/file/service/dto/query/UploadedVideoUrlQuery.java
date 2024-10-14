package ddingdong.ddingdongBE.file.service.dto.query;

public record UploadedVideoUrlQuery(
        String thumbnailOriginUrl,
        String thumbnailCdnUrl,
        String videoOriginUrl,
        String videoCdnUrl
) {

}
