package ddingdong.ddingdongBE.domain.filemetadata.repository;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileMetaDataRepository extends JpaRepository<FileMetaData, UUID> {

    @Query("select fmd from FileMetaData fmd where fmd.domainType = :domainType and fmd.entityId = :entityId and fmd.fileStatus = :fileStatus")
    List<FileMetaData> findAllByDomainTypeAndEntityIdWithFileStatus(
            @Param("domainType") DomainType domainType,
            @Param("entityId") Long entityId,
            @Param("fileStatus") FileStatus fileStatus
    );

    List<FileMetaData> findAllByDomainTypeAndEntityId(DomainType domainType, Long entityId);

    List<FileMetaData> findByIdIn(List<UUID> ids);
}
