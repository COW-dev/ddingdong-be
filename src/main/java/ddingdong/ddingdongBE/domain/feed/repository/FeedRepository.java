package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query(value = """
        SELECT * FROM feed f
        WHERE f.id in
        (SELECT max(id)
        FROM feed
        WHERE deleted_at IS NULL
        GROUP BY club_id)
        ORDER BY id DESC
        """
        , nativeQuery = true)
    List<Feed> findNewestAll();


    @Query(value = """
        select *
          from feed f
          where (:currentCursorId = -1 or id < :currentCursorId)
          and f.club_id = :clubId
          and deleted_at is null
          order by f.id DESC
          limit :size
        """, nativeQuery = true)
    Slice<Feed> findPageByClubIdOrderById(
        @Param("clubId") Long clubId,
        @Param("size") int size,
        @Param("currentCursorId") Long currentCursorId
    );

    @Query(value = """
        select * from feed f
        where f.id in
        (select max(id)
        from feed
        where deleted_at is null
        GROUP BY club_id)
        and (:currentCursorId = -1 or id < :currentCursorId)
        ORDER BY id DESC
        limit :size
        """,
    nativeQuery = true)
    Slice<Feed> findNewestPerClubPage(
        @Param("size") int size,
        @Param("currentCursorId") Long currentCursorId
    );

}
