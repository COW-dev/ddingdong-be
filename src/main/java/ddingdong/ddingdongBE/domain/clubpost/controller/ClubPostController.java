package ddingdong.ddingdongBE.domain.clubpost.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.clubpost.api.ClubPostApi;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.CreateClubPostRequest;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.UpdateClubPostRequest;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubFeedResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostListResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.PresignedUrlResponse;
import ddingdong.ddingdongBE.domain.clubpost.service.FacadeClubPostService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubPostController implements ClubPostApi {

  private final FacadeClubPostService facadeClubPostService;

  @Override
  public PresignedUrlResponse createClubPost(
      PrincipalDetails principalDetails,
      CreateClubPostRequest request
  ) {
    User user = principalDetails.getUser();
    return facadeClubPostService.create(request.toCommand(user.getId()));
  }

  @Override
  public PresignedUrlResponse updateClubPost(
      Long clubPostId,
      UpdateClubPostRequest request
  ) {
    return facadeClubPostService.update(request.toCommand(clubPostId));
  }

  @Override
  public void deleteClubPost(Long clubPostId) {
    facadeClubPostService.delete(clubPostId);
  }

  @Override
  public ClubPostResponse getClubPost(Long clubPostId) {
    return facadeClubPostService.getByClubPostId(clubPostId);
  }

  @Override
  public ClubPostListResponse getClubPosts(Long clubId) {
    return facadeClubPostService.getRecentAllByClubId(clubId);
  }

  @Override
  public ClubFeedResponse getClubFeeds() {
    return facadeClubPostService.findAllRecentPostByClub();
  }
}
