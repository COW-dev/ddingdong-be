package ddingdong.ddingdongBE.domain.clubpost.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubFeedResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostListResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.PresignedUrlResponse;
import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.CreateClubPostCommand;
import ddingdong.ddingdongBE.domain.clubpost.service.dto.UpdateClubPostCommand;
import ddingdong.ddingdongBE.file.controller.dto.response.UploadUrlResponse;
import ddingdong.ddingdongBE.file.entity.FileCategory;
import ddingdong.ddingdongBE.file.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
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

  @Transactional
  public PresignedUrlResponse create(CreateClubPostCommand command) {
    UploadUrlResponse uploadUrlResponse = s3FileService.generatePreSignedUrl(command.fileName());

    Club club = clubService.getByUserId(command.userId());
    String fileUrl = s3FileService.getUploadedFileUrl(command.fileName(), uploadUrlResponse.fileId());
    ClubPost clubPost = command.toEntity(club, fileUrl);

    fileMetaDataService.create(uploadUrlResponse.fileId(), command.fileName(), FileCategory.CLUB_POST_FILE);
    clubPostService.save(clubPost);
    return PresignedUrlResponse.of(uploadUrlResponse.uploadUrl());
  }

  @Transactional
  public PresignedUrlResponse update(UpdateClubPostCommand command) {
    Long clubPostId = command.clubPostId();
    UUID originFileId = command.fileMetaDataCommand().fileId();
    String updateFileName = command.fileMetaDataCommand().fileName();

    if(isNotChangeFile(clubPostId, originFileId, updateFileName)) {
      String originFileUrl = s3FileService.getUploadedFileUrl(updateFileName, originFileId);
      clubPostService.update(clubPostId, command.toEntity(originFileUrl));
      return null;
    }

    UploadUrlResponse uploadUrlResponse = s3FileService.generatePreSignedUrl(updateFileName);
    String updateFileUrl = s3FileService.getUploadedFileUrl(updateFileName, originFileId);
    fileMetaDataService.delete(originFileId);
    fileMetaDataService.create(uploadUrlResponse.fileId(), updateFileName, FileCategory.CLUB_POST_FILE);
    clubPostService.update(clubPostId, command.toEntity(updateFileName));
    return PresignedUrlResponse.of(uploadUrlResponse.uploadUrl());
  }

  @Transactional
  public void delete(Long clubPostId) {
    clubPostService.deleteById(clubPostId);
  }

  public ClubPostResponse getByClubPostId(Long clubPostId) {
    return clubPostService.getResponseById(clubPostId);
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

  private boolean isNotChangeFile(Long clubPostId, UUID updateFileId, String updateFileName) {
    ClubPost clubPost = clubPostService.getById(clubPostId);
    String updateFileUrl = s3FileService.getUploadedFileUrl(updateFileName, updateFileId);
    return updateFileUrl.equals(clubPost.getFileUrl());
  }
}
