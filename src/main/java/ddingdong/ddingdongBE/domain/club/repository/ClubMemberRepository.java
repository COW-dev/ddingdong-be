package ddingdong.ddingdongBE.domain.club.repository;

import ddingdong.ddingdongBE.domain.club.entity.ClubMember;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    List<ClubMember> findClubMembersByClubId(Long clubId);

    @Modifying
    @Transactional
    @Query("delete from ClubMember c where c.id in :memberIds")
    void deleteAllById(List<Long> memberIds);
}