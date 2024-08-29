package ddingdong.ddingdongBE.file.repository;

import ddingdong.ddingdongBE.file.entity.FileMetaData;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, UUID> {

}
