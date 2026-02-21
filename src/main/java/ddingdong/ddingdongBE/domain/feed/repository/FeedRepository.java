package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedRankingWinnerDto;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query(value = """
            select *
              from feed f
              where (:currentCursorId = -1 or id < :currentCursorId)
              and f.club_id = :clubId
              and deleted_at is null
              and (
                    f.feed_type != 'VIDEO'
                    or exists (
                         select 1
                            from (
                                select id
                                    from file_meta_data
                                    where entity_id = f.id
                                    and domain_type = 'FEED_VIDEO'
                            ) filtered_fm
                            join vod_processing_job vpj
                            on filtered_fm.id = vpj.file_meta_data_id
                            where vpj.convert_job_status = 'COMPLETE'
                            )
                    )
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
                   where f.deleted_at is null
                   and (
                       f.feed_type != 'VIDEO'
                       or exists (
                           select 1
                           from file_meta_data fm
                           join vod_processing_job vpj on fm.id = vpj.file_meta_data_id
                           where fm.entity_id = f.id
                           and fm.domain_type = 'FEED_VIDEO'
                           and vpj.convert_job_status = 'COMPLETE'
                       )
                   )
                   and (:currentCursorId = -1 or f.id < :currentCursorId)
                   ORDER BY f.id DESC
                   limit :size
        """, nativeQuery = true)
    Slice<Feed> getAllFeedPage(
            @Param("size") int size,
            @Param("currentCursorId") Long currentCursorId
    );

    @Query(value = """
            SELECT
                c.name                                 AS clubName,
                COUNT(DISTINCT f.id)                   AS feedCount,
                (SELECT COALESCE(SUM(f2.view_count), 0)
                 FROM feed f2
                 WHERE f2.club_id = c.id AND f2.deleted_at IS NULL
                   AND YEAR(f2.created_at) = YEAR(f.created_at)
                   AND MONTH(f2.created_at) = MONTH(f.created_at)) AS viewCount,
                COUNT(DISTINCT fl.id)                  AS likeCount,
                COUNT(DISTINCT fc.id)                  AS commentCount,
                (COUNT(DISTINCT f.id) * 10
                    + (SELECT COALESCE(SUM(f2.view_count), 0)
                       FROM feed f2
                       WHERE f2.club_id = c.id AND f2.deleted_at IS NULL
                         AND YEAR(f2.created_at) = YEAR(f.created_at)
                         AND MONTH(f2.created_at) = MONTH(f.created_at)) * 1
                    + COUNT(DISTINCT fl.id) * 3 + COUNT(DISTINCT fc.id) * 5) AS score,
                YEAR(f.created_at)                     AS targetYear,
                MONTH(f.created_at)                    AS targetMonth
            FROM feed f
            JOIN club c ON f.club_id = c.id
            LEFT JOIN feed_like fl ON fl.feed_id = f.id
            LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
            WHERE f.deleted_at IS NULL
              AND YEAR(f.created_at) = :year
              AND NOT (YEAR(f.created_at) = YEAR(CURRENT_DATE()) AND MONTH(f.created_at) = MONTH(CURRENT_DATE()))
            GROUP BY c.id, c.name, YEAR(f.created_at), MONTH(f.created_at)
            ORDER BY score DESC, c.id ASC
            LIMIT 1
            """, nativeQuery = true)
    Optional<FeedRankingWinnerDto> findYearlyWinner(@Param("year") int year);

    @Modifying
    @Query("UPDATE Feed f SET f.viewCount = f.viewCount + 1 WHERE f.id = :feedId")
    void incrementViewCount(@Param("feedId") Long feedId);

}
