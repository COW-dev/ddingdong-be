package ddingdong.ddingdongBE.domain.filemetadata.service.dto;

public record FileMetaDataIdOrderDto(
        String fileMetaDatId,
        int fileMetaDataOrder

) {

    public static FileMetaDataIdOrderDto of(String fileMetaDatId, int fileMetaDataOrder) {
        return new FileMetaDataIdOrderDto(fileMetaDatId, fileMetaDataOrder);
    }

}
