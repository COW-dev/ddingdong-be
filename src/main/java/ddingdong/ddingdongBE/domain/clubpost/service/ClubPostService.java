package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.domain.clubpost.repository.ClubPostRepository;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.UpdateClubPostCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubPostService {

  private final ClubPostRepository clubPostRepository;

  @Transactional
  public void create(Club club, CreateClubPostCommand command) {
    ClubPost clubPost = command.toEntity();
    clubPost.updateClub(club);
    clubPostRepository.save(clubPost);
  }

  @Transactional
  public void update(UpdateClubPostCommand command) {
    ClubPost clubPost = getById(command.clubPostId());
    clubPost.update(command.toEntity());
  }

  public ClubPost getById(Long clubPostId) {
    return clubPostRepository.findById(clubPostId)
        .orElseThrow(() -> new ResourceNotFound("존재하지 않은 동아리 게시물입니다."));
  }

  @Transactional
  public void deleteById(Long clubPostId) {
    clubPostRepository.deleteById(clubPostId);
  }

  public List<ClubPost> getRecentAllByClubId(Long clubId) {
    return clubPostRepository.findAllByClubIdOrderByCreatedAt(clubId);
  }

  public List<String> getAllMediaUrl(List<ClubPost> clubPosts) {
    return clubPosts.stream()
        .map(ClubPost::getMediaUrl)
        .toList();
  }

  public List<ClubPost> findAllRecentPostByClub() {
    return clubPostRepository.findLatestGroupByClub();
  }
}
