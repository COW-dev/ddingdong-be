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

    @Query("""
        select fmd from FileMetaData fmd
        where fmd.domainType = :domainType
        and fmd.entityId = :entityId
        and fmd.fileStatus = :fileStatus
        and fmd.fileStatus != 'DELETED'
        """)
    List<FileMetaData> findAllByDomainTypeAndEntityIdWithFileStatus(
            @Param("domainType") DomainType domainType,
            @Param("entityId") Long entityId,
            @Param("fileStatus") FileStatus fileStatus
    );

    @Query("select fmd from FileMetaData fmd where fmd.entityId = :entityId and fmd.fileStatus = :fileStatus")
    List<FileMetaData> findAllByEntityIdWithFileStatus(
            @Param("entityId") Long entityId,
            @Param("fileStatus") FileStatus fileStatus
    );

    @Query(value = """
        select * from file_meta_data
        where domain_type = :#{#domainType.name()}
        and entity_id = :entityId
        and file_status != 'DELETED'
        """, nativeQuery = true)
    List<FileMetaData> findAllByDomainTypeAndEntityId(
            @Param("domainType") DomainType domainType,
            @Param("entityId") Long entityId
    );

    @Query(value = """
        select *
        from file_meta_data
        where id in (:ids)
        and file_status != 'DELETED'
        """, nativeQuery = true)
    List<FileMetaData> findByIdIn(@Param("ids") List<UUID> ids);

    @Query("""
        select fmd from FileMetaData fmd
        where fmd.domainType = :domainType
        and fmd.entityId = :entityId
        and fmd.fileStatus = :fileStatus
        and fmd.fileStatus != 'DELETED'
        order by fmd.id asc
        """)
    List<FileMetaData> findAllByDomainTypeAndEntityIdWithFileStatusOrderedAsc(
            @Param("domainType") DomainType domainType,
            @Param("entityId") Long entityId,
            @Param("fileStatus") FileStatus fileStatus
    );
}
