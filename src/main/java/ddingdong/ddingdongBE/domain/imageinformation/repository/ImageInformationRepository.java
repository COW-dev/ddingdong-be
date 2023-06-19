package ddingdong.ddingdongBE.domain.imageinformation.repository;

import ddingdong.ddingdongBE.domain.imageinformation.entity.ImageInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageInformationRepository extends JpaRepository<ImageInformation, Long> {

}
