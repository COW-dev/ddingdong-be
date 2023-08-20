package ddingdong.ddingdongBE.domain.notice.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.NOTICE;

import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.repository.FileInformationRepository;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.RegisterNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.request.UpdateNoticeRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.DetailNoticeResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.notice.repository.NoticeRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.FileStore;
import ddingdong.ddingdongBE.file.dto.FileResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileInformationService fileInformationService;
    private final FileInformationRepository fileInformationRepository;
    private final FileStore fileStore;

    public Long register(User user, RegisterNoticeRequest request) {
        Notice notice = request.toEntity(user);
        Notice savedNotice = noticeRepository.save(notice);

        return savedNotice.getId();
    }

    @Transactional(readOnly = true)
    public List<NoticeResponse> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetailNoticeResponse getNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        List<String> imageUrls = fileInformationService.getImageUrls(IMAGE.getFileType() + NOTICE.getFileDomain() + noticeId);
        List<FileResponse> fileUrls = fileInformationService.getFileUrls(FILE.getFileType() + NOTICE.getFileDomain() + noticeId);

        return DetailNoticeResponse.of(notice, imageUrls, fileUrls);
    }

    public void update(Long noticeId, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        List<FileInformation> imageInformation = fileInformationService.getFileInformation(
            IMAGE.getFileType() + NOTICE.getFileDomain() + notice.getId());
        if (!request.getImgUrls().isEmpty()) {
            List<FileInformation> deleteInformation = imageInformation.stream()
                .filter(information -> !request.getImgUrls()
                    .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                        .getFileType() + information.getFileDomainCategory().getFileDomain() + information.getStoredName()))
                .toList();

            fileInformationRepository.deleteAll(deleteInformation);
        } else {
            fileInformationRepository.deleteAll(imageInformation);
        }

        List<FileInformation> fileInformation = fileInformationService.getFileInformation(
            FILE.getFileType() + NOTICE.getFileDomain() + notice.getId());
        if (!request.getFileUrls().isEmpty()) {
            List<FileInformation> deleteInformation = fileInformation.stream()
                .filter(information -> !request.getImgUrls()
                    .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                        .getFileType() + information.getFileDomainCategory().getFileDomain() + information.getStoredName()))
                .toList();

            fileInformationRepository.deleteAll(deleteInformation);
        } else {
            fileInformationRepository.deleteAll(fileInformation);
        }

        notice.update(request);
    }

    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_NOTICE.getText()));

        noticeRepository.delete(notice);
    }

}
