package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubPostFacadeService {

  private final ClubPostService clubPostService;
  private final ClubService clubService;

  public void create(CreateClubPostCommand command) {
    Club club = clubService.getByUserId(command.userId());
    clubPostService.create(club, command);
  }

  public void update(UpdateClubPostCommand command) {
    clubPostService.update(command);
  }

  public void delete(Long clubPostId) {
    clubPostService.deleteById(clubPostId);
  }
}
