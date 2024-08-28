package ddingdong.ddingdongBE.file.repository;

import ddingdong.ddingdongBE.file.entity.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {

}
