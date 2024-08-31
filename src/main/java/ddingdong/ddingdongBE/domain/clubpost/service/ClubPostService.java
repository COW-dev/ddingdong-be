package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.domain.clubpost.repository.ClubPostRepository;
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
  public void save(ClubPost createClubPost) {
    clubPostRepository.save(createClubPost);
  }

  @Transactional
  public void update(Long clubPostId, ClubPost updateClubPostInfo) {
    ClubPost clubPost = getById(clubPostId);
    clubPost.update(updateClubPostInfo);
  }

  public ClubPost getById(Long clubPostId) {
    return clubPostRepository.findById(clubPostId)
        .orElseThrow(() -> new ResourceNotFound("존재하지 않는 동아리 게시물입니다."));
  }

  @Transactional
  public void deleteById(Long clubPostId) {
    clubPostRepository.deleteById(clubPostId);
  }

  public List<ClubPost> getRecentAllByClubId(Long clubId) {
    return clubPostRepository.findAllByClubIdOrderById(clubId);
  }

  public List<String> getAllMediaUrl(List<ClubPost> clubPosts) {
    return clubPosts.stream()
        .map(ClubPost::getFileUrl)
        .toList();
  }

  public List<ClubPost> findAllRecentPostByClub() {
    return clubPostRepository.findLatestGroupByClub();
  }
}
