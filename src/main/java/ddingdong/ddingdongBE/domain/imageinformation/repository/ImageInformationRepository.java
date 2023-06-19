package ddingdong.ddingdongBE.domain.imageinformation.repository;

import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageInformation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageInformationRepository extends JpaRepository<ImageInformation, Long> {

    List<ImageInformation> findByFindParam(String findParam);

}
