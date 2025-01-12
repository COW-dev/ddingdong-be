package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralVodProcessingNotificationService implements VodProcessingNotificationService {

    private final VodProcessingNotificationRepository notificationRepository;

    @Override
    @Transactional
    public VodProcessingNotification save(VodProcessingNotification vodProcessingNotification) {
        return notificationRepository.save(vodProcessingNotification);
    }

    @Override
    public VodProcessingNotification getById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("VodNotification(vodNotificationId=" + id + ")를 찾을 수 없습니다."));
    }

}
