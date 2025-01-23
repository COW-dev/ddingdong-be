package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
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
        where f.id in
        (select max(id)
        from feed
        where deleted_at is null
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
