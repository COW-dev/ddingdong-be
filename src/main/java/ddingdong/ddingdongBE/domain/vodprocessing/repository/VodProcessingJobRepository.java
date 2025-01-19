package ddingdong.ddingdongBE.domain.vodprocessing.repository;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VodProcessingJobRepository extends JpaRepository<VodProcessingJob, Long> {

    Optional<VodProcessingJob> findByConvertJobId(String convertJobId);

    @Query("""
            SELECT vj FROM VodProcessingJob vj
            JOIN FETCH vj.fileMetaData fm
            WHERE fm.entityId = :entityId
            AND fm.domainType = :domainType
            """)
    Optional<VodProcessingJob> findFirstByFileMetaDataEntityIdAndDomainType(
            @Param("entityId") Long entityId,
            @Param("domainType") DomainType domainType
    );

}
