package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedFileUrlQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeFeedService {

  private final FeedService feedService;
  private final FileMetaDataService fileMetaDataService;
  private final S3FileService s3FileService;

  public ClubFeedPageQuery getFeedPageByClub(Long clubId, int size, Long currentCursorId) {
    Slice<Feed> feedPage = feedService.getFeedPageByClubId(clubId, size, currentCursorId);

    List<Feed> feeds = feedPage.getContent();
    List<FeedListQuery> feedListQueries = feeds.stream()
        .map(feed -> {
          FileMetaData fileMetaData = getFileMetaData(feed.getFeedType().getDomainType(), feed.getId());
          UploadedVideoUrlQuery urlQuery = s3FileService.getUploadedVideoUrl(fileMetaData.getFileKey());
          return FeedListQuery.of(feed, urlQuery);
        }).toList();

    PagingQuery pagingQuery = PagingQuery.of(currentCursorId, feeds.get(feeds.size() -1).getId(), feedPage.hasNext());
    return ClubFeedPageQuery.of(feedListQueries, pagingQuery);
  }

  public List<FeedListQuery> getNewestAll() {
    List<Feed> feeds = feedService.getNewestAll();
    return null;
  }

  public FeedQuery getById(Long feedId) {
    Feed feed = feedService.getById(feedId);
    ClubProfileQuery clubProfileQuery = extractClubInfo(feed.getClub());
    FeedFileUrlQuery feedFileUrlQuery = extractFeedFileInfo(feed);
    return FeedQuery.of(feed, clubProfileQuery, feedFileUrlQuery);
  }

  private FeedFileUrlQuery extractFeedFileInfo(Feed feed) {
    FileMetaData fileMetaData = getFileMetaData(feed.getFeedType().getDomainType(), feed.getId());
    if (feed.isImage()) {
      UploadedFileUrlQuery urlQuery = s3FileService.getUploadedFileUrl(fileMetaData.getFileKey());
      return new FeedFileUrlQuery(urlQuery.id(), urlQuery.originUrl(), urlQuery.cdnUrl());
    }

    if (feed.isVideo()) {
      UploadedVideoUrlQuery urlQuery = s3FileService.getUploadedVideoUrl(fileMetaData.getFileKey());
      return new FeedFileUrlQuery(fileMetaData.getId().toString(), urlQuery.videoOriginUrl(), urlQuery.videoCdnUrl());
    }

    throw new IllegalArgumentException("FeedType은 Image 혹은 Video여야 합니다.");
  }

  private ClubProfileQuery extractClubInfo(Club club) {
    String clubName = club.getName();
    FileMetaData fileMetaData = getFileMetaData(DomainType.CLUB_PROFILE, club.getId());
    UploadedFileUrlQuery urlQuery = s3FileService.getUploadedFileUrl(fileMetaData.getFileKey());
    return new ClubProfileQuery(club.getId(), clubName, urlQuery.originUrl(), urlQuery.cdnUrl());
  }

  private FileMetaData getFileMetaData(DomainType domainType, Long id) {
    return fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(domainType, id)
        .stream()
        .findFirst()
        .orElseThrow(() -> new ResourceNotFound("해당 FileMetaData(feedId: " + id + ")를 찾을 수 없습니다.)"));
  }
}
