package ddingdong.ddingdongBE.domain.fileinformation.repository;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInformationRepository extends JpaRepository<FileInformation, Long> {

    List<FileInformation> findByFindParam(String findParam);

}
