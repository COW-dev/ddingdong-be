package ddingdong.ddingdongBE.domain.calendar.repository;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByNameAsc();

    boolean existsByName(String name);
}
