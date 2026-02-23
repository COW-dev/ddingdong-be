package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MyFeedStatDto;
import java.util.List;
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

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE feed SET view_count = view_count + 1 WHERE id = :feedId", nativeQuery = true)
    void incrementViewCount(@Param("feedId") Long feedId);

    @Query(value = """
            SELECT c.id AS clubId,
                   c.name AS clubName,
                   COUNT(f.id) AS feedCount,
                   COALESCE(SUM(f.view_count), 0) AS viewCount,
                   COALESCE(SUM(sub_like.like_cnt), 0) AS likeCount,
                   COALESCE(SUM(sub_comment.comment_cnt), 0) AS commentCount
              FROM club c
              LEFT JOIN feed f ON f.club_id = c.id
                              AND f.deleted_at IS NULL
                              AND YEAR(f.created_at) = :year
                              AND MONTH(f.created_at) = :month
              LEFT JOIN (
                  SELECT fl.feed_id, COUNT(*) AS like_cnt
                    FROM feed_like fl
                   GROUP BY fl.feed_id
              ) sub_like ON sub_like.feed_id = f.id
              LEFT JOIN (
                  SELECT fc.feed_id, COUNT(*) AS comment_cnt
                    FROM feed_comment fc
                   WHERE fc.deleted_at IS NULL
                   GROUP BY fc.feed_id
              ) sub_comment ON sub_comment.feed_id = f.id
             WHERE c.deleted_at IS NULL
             GROUP BY c.id, c.name
            """, nativeQuery = true)
    List<MonthlyFeedRankingDto> findMonthlyRankingByClub(
            @Param("year") int year,
            @Param("month") int month
    );

    @Query(value = """
            SELECT COUNT(f.id) AS feedCount,
                   COALESCE(SUM(f.view_count), 0) AS totalViewCount,
                   COALESCE(SUM(CASE WHEN f.feed_type = 'IMAGE' THEN 1 ELSE 0 END), 0) AS imageCount,
                   COALESCE(SUM(CASE WHEN f.feed_type = 'VIDEO' THEN 1 ELSE 0 END), 0) AS videoCount
            FROM feed f
            WHERE f.deleted_at IS NULL
              AND f.club_id = :clubId
            """, nativeQuery = true)
    MyFeedStatDto findMyFeedStat(@Param("clubId") Long clubId);

}
