package ddingdong.ddingdongBE.domain.feed.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeFeedService {

  private final FeedService feedService;
  private final FileInformationService fileInformationService;

  public List<FeedListQuery> getAllByClubId(Long clubId) {
    List<Feed> feeds = feedService.getAllByClubId(clubId);
    return feeds.stream()
        .map(FeedListQuery::from)
        .toList();
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
