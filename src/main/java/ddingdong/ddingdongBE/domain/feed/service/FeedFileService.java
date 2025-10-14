package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedFileInfoQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedFileService {

    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    public FeedListQuery extractFeedThumbnailInfo(Feed feed) {
        FileMetaData fileMetaData = getFileMetaData(feed.getFeedType().getDomainType(), feed.getId());
        if (feed.isImage()) {
            UploadedFileUrlQuery urlQuery = s3FileService.getUploadedFileUrl(fileMetaData.getFileKey());
            return new FeedListQuery(feed.getId(), urlQuery.cdnUrl(), urlQuery.originUrl(), feed.getFeedType().name(), fileMetaData.getFileName());
        }

        if (feed.isVideo()) {
            UploadedVideoUrlQuery urlQuery = s3FileService.getUploadedVideoUrl(fileMetaData.getFileKey());
            return new FeedListQuery(feed.getId(), urlQuery.thumbnailCdnUrl(), urlQuery.thumbnailOriginUrl(), feed.getFeedType().name(), fileMetaData.getFileName());
        }

        throw new IllegalArgumentException("FeedType은 Image 혹은 Video여야 합니다.");
    }

    public FeedFileInfoQuery extractFeedFileInfo(Feed feed) {
        FileMetaData fileMetaData = getFileMetaData(feed.getFeedType().getDomainType(), feed.getId());
        if (feed.isImage()) {
            UploadedFileUrlQuery urlQuery = s3FileService.getUploadedFileUrl(fileMetaData.getFileKey());
            return new FeedFileInfoQuery(urlQuery.id(), urlQuery.originUrl(), urlQuery.cdnUrl(), fileMetaData.getFileName());
        }

        if (feed.isVideo()) {
            UploadedVideoUrlQuery urlQuery = s3FileService.getUploadedVideoUrl(fileMetaData.getFileKey());
            return new FeedFileInfoQuery(fileMetaData.getId().toString(), urlQuery.videoOriginUrl(), urlQuery.videoCdnUrl(), fileMetaData.getFileName());
        }

        throw new IllegalArgumentException("FeedType은 Image 혹은 Video여야 합니다.");
    }

    public ClubProfileQuery extractClubInfo(Club club) {
        String clubName = club.getName();
        UploadedFileUrlAndNameQuery urlQuery = getFileUrlAndName(DomainType.CLUB_PROFILE, club.getId());
        if (urlQuery == null) {
            return new ClubProfileQuery(club.getId(), clubName, null, null, null);
        }
        return new ClubProfileQuery(club.getId(), clubName, urlQuery.originUrl(), urlQuery.cdnUrl(), urlQuery.fileName());
    }

    private FileMetaData getFileMetaData(DomainType domainType, Long id) {
        return fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(domainType, id)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFound("해당 FileMetaData(feedId: " + id + ")를 찾을 수 없습니다.)"));
    }

    private UploadedFileUrlAndNameQuery getFileUrlAndName(DomainType domainType, Long clubId) {
        return fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(domainType, clubId)
            .stream()
            .map(fileMetaData -> s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(), fileMetaData.getFileName()))
            .findFirst()
            .orElse(null);
    }
}
