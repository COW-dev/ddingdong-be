package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.MyFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.service.VodProcessingJobService;
import ddingdong.ddingdongBE.sse.service.SseConnectionService;
import ddingdong.ddingdongBE.sse.service.dto.SseEvent;
import ddingdong.ddingdongBE.sse.service.dto.SseVodProcessingNotificationDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeClubFeedServiceImpl implements FacadeClubFeedService {

    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final FeedService feedService;
    private final VodProcessingJobService vodProcessingJobService;
    private final SseConnectionService sseConnectionService;
    private final FeedFileService feedFileService;

    @Override
    @Transactional
    public void create(CreateFeedCommand command) {
        Club club = clubService.getByUserId(command.user().getId());
        Feed feed = command.toEntity(club);
        Long createdId = feedService.create(feed);

        if (feed.isImage()) {
            fileMetaDataService.updateStatusToCoupled(command.mediaId(), DomainType.FEED_IMAGE, createdId);
            return;
        }

        if (feed.isVideo()) {
            fileMetaDataService.updateStatusToCoupled(command.mediaId(), DomainType.FEED_VIDEO, createdId);
            checkVodProcessingJobAndNotify(feed);
        }
    }

    @Override
    @Transactional
    public void update(UpdateFeedCommand command) {
        Feed originFeed = feedService.getById(command.feedId());
        Feed updateFeed = command.toEntity();
        originFeed.update(updateFeed);
    }

    @Override
    @Transactional
    public void delete(Long feedId) {
        Feed feed = feedService.getById(feedId);
        feedService.delete(feed);
        fileMetaDataService.updateStatusToDelete(feed.getFeedType().getDomainType(), feed.getId());
    }

    @Override
    public MyFeedPageQuery getMyFeedPage(User user, int size, Long currentCursorId) {
        Club club = clubService.getByUserId(user.getId());
        Slice<Feed> feedPage = feedService.getFeedPageByClubId(club.getId(), size, currentCursorId);
        if (feedPage == null) {
            return MyFeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();
        List<FeedListQuery> feedListQueries = completeFeeds.stream()
                .map(feedFileService::extractFeedThumbnailInfo)
                .toList();
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return MyFeedPageQuery.of(feedListQueries, pagingQuery);
    }

    private void checkVodProcessingJobAndNotify(Feed feed) {
        VodProcessingJob vodProcessingJob = vodProcessingJobService.findByVideoFeedId(feed.getId());
        if (vodProcessingJob != null && vodProcessingJob.isPossibleNotify()) {
            SseEvent<SseVodProcessingNotificationDto> sseEvent = SseEvent.of(
                    "vod-processing",
                    new SseVodProcessingNotificationDto(
                            vodProcessingJob.getVodProcessingNotification().getId(),
                            vodProcessingJob.getConvertJobStatus()),
                    LocalDateTime.now()
            );
            sseConnectionService.sendVodProcessingNotification(vodProcessingJob, sseEvent);
        }
    }
}
