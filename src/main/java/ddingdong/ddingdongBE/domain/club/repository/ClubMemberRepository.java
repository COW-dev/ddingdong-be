package ddingdong.ddingdongBE.domain.club.repository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.ClubMember;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    @Modifying
    @Query("delete from ClubMember c where c.club = :club")
    void deleteAllByClub(@Param("club") Club club);
}
