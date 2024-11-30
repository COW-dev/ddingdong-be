package ddingdong.ddingdongBE.domain.banner.repository;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.dto.BannerWithFileMetaData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByOrderByIdDesc();

    @Query("SELECT new ddingdong.ddingdongBE.domain.banner.repository.dto.BannerWithFileMetaData(" +
            "b, f) " +
            "FROM Banner b " +
            "LEFT JOIN FileMetaData f ON b.id = f.entityId " +
            "ORDER BY b.id DESC")
    List<BannerWithFileMetaData> findAllBannersWithFileMetaData();

}
