package ddingdong.ddingdongBE.domain.feed.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
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
          FileMetaData fileMetaData = getFileMetaData(feed);
          UploadedVideoUrlQuery urlQuery = s3FileService.getUploadedVideoUrl(fileMetaData.getFileKey());
          return FeedListQuery.of(feed, urlQuery);
        }).toList();

    PagingQuery pagingQuery = PagingQuery.from(feedPage, feeds.get(feeds.size() -1).getId());
    return ClubFeedPageQuery.of(feedListQueries, pagingQuery);
  }

  private FileMetaData getFileMetaData(Feed feed) {
    return fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(feed.getFeedType().getDomainType(), feed.getId())
        .stream()
        .findFirst()
        .orElseThrow(() -> new ResourceNotFound("해당 FileMetaData(feedId: " + feed.getId() + ")를 찾을 수 없습니다.)"));
  }

  public List<FeedListQuery> getNewestAll() {
    List<Feed> feeds = feedService.getNewestAll();
    return feeds.stream()
        .map(FeedListQuery::from)
        .toList();
  }

  public FeedQuery getById(Long feedId) {
    Feed feed = feedService.getById(feedId);
    ClubProfileQuery clubProfileQuery = extractClubInfo(feed.getClub());
    return FeedQuery.of(feed, clubProfileQuery);
  }

  private ClubProfileQuery extractClubInfo(Club club) {
    String clubName = club.getName();
    List<String> profileImageUrls = fileInformationService.getImageUrls(
        IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId()
    );
    String profileImageUrl = profileImageUrls.stream()
        .findFirst()
        .orElse(null);

    return ClubProfileQuery.builder()
        .id(club.getId())
        .name(clubName)
        .profileImageUrl(profileImageUrl)
        .build();
  }
}
