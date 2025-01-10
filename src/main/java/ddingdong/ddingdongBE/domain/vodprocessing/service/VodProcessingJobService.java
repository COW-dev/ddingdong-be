package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;

public interface VodProcessingJobService {

    Long save(VodProcessingJob vodProcessingJob);

    VodProcessingJob getById(Long vodProcessingJobId);

    VodProcessingJob getByConvertJobId(String convertJobId);

    VodProcessingJob getByVideoFeedId(Long videoFeedId);

}
