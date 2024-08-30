package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubFeedResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostListResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostResponse;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.UpdateClubPostCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacadeClubPostService {

  private final ClubPostService clubPostService;
  private final ClubService clubService;

  public void create(CreateClubPostCommand command) {
    Club club = clubService.getByUserId(command.userId());
    ClubPost clubPost = command.toEntity(club);
    clubPostService.save(clubPost);
  }

  public void update(UpdateClubPostCommand command) {
    clubPostService.update(command.clubPostId(), command.toEntity());
  }

  public void delete(Long clubPostId) {
    clubPostService.deleteById(clubPostId);
  }

  public ClubPostResponse getByClubPostId(Long clubPostId) {
    return clubPostService.getResponseById(clubPostId);
  }

  public ClubPostListResponse getRecentAllByClubId(Long clubId) {
    List<ClubPost> clubPosts = clubPostService.getRecentAllByClubId(clubId);
    List<String> mediaUrls = clubPostService.getAllMediaUrl(clubPosts);
    return ClubPostListResponse.from(mediaUrls);
  }

  public ClubFeedResponse findAllRecentPostByClub() {
    List<ClubPost> clubPosts = clubPostService.findAllRecentPostByClub();
    List<String> mediaUrls = clubPostService.getAllMediaUrl(clubPosts);
    return ClubFeedResponse.from(mediaUrls);
  }
}
