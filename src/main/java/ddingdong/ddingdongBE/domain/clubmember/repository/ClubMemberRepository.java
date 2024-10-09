package ddingdong.ddingdongBE.domain.clubmember.repository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findClubMembersByClubId(Long clubId);

    @Modifying
    @Query("delete from ClubMember c where c.club = :club")
    void deleteAllByClub(@Param("club") Club club);
}
