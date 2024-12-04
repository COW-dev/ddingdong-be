package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.GetNoticeListCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery.NoticeInfo;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeNoticeServiceImpl implements FacadeNoticeService {

    private final NoticeService noticeService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public NoticeListPagingQuery getNoticeList(GetNoticeListCommand command) {
        List<Notice> notices = noticeService.getNoticeListByPage(command.page(), command.limit());

        List<NoticeInfo> noticeInfos = notices.stream()
            .map(NoticeInfo::from)
            .toList();

        int totalPage = noticeService.getNoticePageCount();

        return NoticeListPagingQuery.of(noticeInfos, totalPage);
    }

    public NoticeQuery getNotice(Long noticeId) {
        Notice notice = noticeService.getById(noticeId);

        List<UploadedFileUrlQuery> imageUrlQueries = fileMetaDataService
            .getCoupledAllByDomainTypeAndEntityIdOrderedAsc(DomainType.NOTICE_IMAGE, noticeId)
            .stream()
            .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
            .toList();

        List<UploadedFileUrlAndNameQuery> fileUrlAndNameQueries = fileMetaDataService
            .getCoupledAllByDomainTypeAndEntityId(DomainType.NOTICE_FILE, noticeId)
            .stream()
            .map(fileMetaData -> s3FileService.getUploadedFileUrlAndName(fileMetaData.getFileKey(),
                fileMetaData.getFileName()))
            .toList();

        return NoticeQuery.of(notice, imageUrlQueries, fileUrlAndNameQueries);
    }

}
