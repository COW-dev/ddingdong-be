package ddingdong.ddingdongBE.domain.clubpost.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.clubpost.api.ClubPostAPI;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.CreateClubPostRequest;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.UpdateClubPostRequest;
import ddingdong.ddingdongBE.domain.clubpost.service.ClubPostFacadeService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubPostController implements ClubPostAPI {

  private final ClubPostFacadeService clubPostFacadeService;

  @Override
  public void createClubPost(
      PrincipalDetails principalDetails,
      CreateClubPostRequest request
  ) {
    User user = principalDetails.getUser();
    Long userId = user.getId();
    clubPostFacadeService.create(request.toCommand(userId));
  }

  @Override
  public void updateClubPost(
      Long clubPostId,
      UpdateClubPostRequest request
  ) {
    clubPostFacadeService.update(request.toCommand(clubPostId));
  }

  @Override
  public void deleteClubPost(Long clubPostId) {
    clubPostFacadeService.delete(clubPostId);
  }
}
