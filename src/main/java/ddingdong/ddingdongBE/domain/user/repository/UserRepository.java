package ddingdong.ddingdongBE.domain.user.repository;

import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAuthId(String authId);

    Optional<User> findByAuthId(String authId);

}
