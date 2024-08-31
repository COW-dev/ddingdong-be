package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.common.utils.FileTypeClassifier;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubFeedResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostListResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostResponse;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.UpdateClubPostCommand;
import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
import ddingdong.ddingdongBE.file.entity.FileCategory;
import ddingdong.ddingdongBE.file.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeClubPostService {

  private final ClubPostService clubPostService;
  private final ClubService clubService;
  private final S3FileService s3FileService;
  private final FileMetaDataService fileMetaDataService;
  private final FileTypeClassifier fileTypeClassifier;

  @Transactional
  public void create(CreateClubPostCommand command) {
    FileMetaDataCommand fileMetaDataCommand = command.fileMetaDataCommand();
    FileCategory fileCategory = fileTypeClassifier.classifyFileType(fileMetaDataCommand.fileName());
    fileMetaDataService.create(fileMetaDataCommand.fileId(), fileMetaDataCommand.fileName(), fileCategory);

    Club club = clubService.getByUserId(command.userId());
    String fileUrl = s3FileService.getUploadedFileUrl(fileMetaDataCommand.fileName(), fileMetaDataCommand.fileId());
    ClubPost clubPost = command.toEntity(club, fileUrl);
    clubPostService.save(clubPost);
  }

  @Transactional
  public void update(UpdateClubPostCommand command) {
    Long clubPostId = command.clubPostId();
    UUID updateFileId = command.fileMetaDataCommand().fileId();
    String updateFileName = command.fileMetaDataCommand().fileName();

    String updateFileUrl = s3FileService.getUploadedFileUrl(updateFileName, updateFileId);
    if(isChangeFile(clubPostId, updateFileUrl)) {
      FileCategory fileCategory = fileTypeClassifier.classifyFileType(updateFileName);
      fileMetaDataService.create(updateFileId, updateFileName, fileCategory);
      return;
    }
    clubPostService.update(clubPostId, command.toEntity(updateFileUrl));
  }

  @Transactional
  public void delete(Long clubPostId) {
    clubPostService.deleteById(clubPostId);
  }

  public ClubPostResponse getByClubPostId(Long clubPostId) {
    ClubPost clubPost = clubPostService.getById(clubPostId);
    Club club = clubPost.getClub();
    FileUrlResponse postFileResponse = fileMetaDataService.getFileUrlResponseByUrl(clubPost.getFileUrl());
    FileUrlResponse clubProfileResponse = fileMetaDataService.getFileUrlResponseByUrl(club.getProfileImageUrl());
    return ClubPostResponse.of(clubPost, postFileResponse, clubProfileResponse);
  }

  public ClubPostListResponse getRecentAllByClubId(Long clubId) {
    List<ClubPost> clubPosts = clubPostService.getRecentAllByClubId(clubId);
    List<String> fileUrls = clubPostService.getAllMediaUrl(clubPosts);
    return ClubPostListResponse.from(fileUrls);
  }

  public ClubFeedResponse findAllRecentPostByClub() {
    List<ClubPost> clubPosts = clubPostService.findAllRecentPostByClub();
    List<String> fileUrls = clubPostService.getAllMediaUrl(clubPosts);
    return ClubFeedResponse.from(fileUrls);
  }

  private boolean isChangeFile(Long clubPostId, String updateFileUrl) {
    ClubPost clubPost = clubPostService.getById(clubPostId);
    return !updateFileUrl.equals(clubPost.getFileUrl());
  }
}
