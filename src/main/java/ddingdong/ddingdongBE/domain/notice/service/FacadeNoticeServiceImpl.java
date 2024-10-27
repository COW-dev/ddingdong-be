package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.common.converter.FileInfoConverter;
import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.GetNoticeListCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery.NoticeInfo;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FacadeNoticeServiceImpl implements FacadeNoticeService {

    private final NoticeServiceImpl noticeService;
    private final S3FileService s3FileService;
    private final FileInfoConverter fileInfoConverter;

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

        List<UploadedFileUrlQuery> imageUrls = notice.getImageKeys().stream()
            .map(s3FileService::getUploadedFileUrl)
            .toList();

        List<FileInfo> fileInfos = fileInfoConverter.convertToJson(notice.getFileInfos());

        List<String> fileNames = fileInfos
            .stream()
            .map(FileInfo::fileName)
            .toList();

        List<UploadedFileUrlQuery> fileUrls = fileInfos.stream()
            .map(fileInfo -> s3FileService.getUploadedFileUrl(fileInfo.fileKey()))
            .toList();

        return NoticeQuery.of(notice, imageUrls, fileNames, fileUrls);
    }

}
