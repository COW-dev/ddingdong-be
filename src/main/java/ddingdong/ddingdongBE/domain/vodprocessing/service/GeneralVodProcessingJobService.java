package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralVodProcessingJobService implements VodProcessingJobService {

    private final VodProcessingJobRepository vodProcessingJobRepository;

    @Override
    @Transactional
    public Long save(VodProcessingJob vodProcessingJob) {
        VodProcessingJob saveVodProcessingJob = vodProcessingJobRepository.save(vodProcessingJob);
        return saveVodProcessingJob.getId();
    }

    @Override
    public VodProcessingJob getById(Long vodProcessingJobId) {
        return vodProcessingJobRepository.findById(vodProcessingJobId)
                .orElseThrow(() -> new ResourceNotFound(
                        "VodProcessingJob(vodProcessingJobId=" + vodProcessingJobId + ")를 찾을 수 없습니다."));
    }

    @Override
    public VodProcessingJob getByConvertJobId(String convertJobId) {
        return vodProcessingJobRepository.findByConvertJobId(convertJobId)
                .orElseThrow(() -> new ResourceNotFound(
                        "VodProcessingJob(convertJobId=" + convertJobId + ")를 찾을 수 없습니다."));
    }

    @Override
    public VodProcessingJob getByVideoFeedId(Long videoFeedId) {
        return vodProcessingJobRepository.findFirstByFileMetaDataEntityIdAndDomainType(
                        videoFeedId,
                        DomainType.FEED_VIDEO)
                .orElseThrow(() -> new ResourceNotFound(
                        "VodProcessingJob(videoFeedId=" + videoFeedId + ")를 찾을 수 없습니다."));
    }
}
