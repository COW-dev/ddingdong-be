package ddingdong.ddingdongBE.common.fixture;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;

public class VodProcessingJobFixture {

    public static FileMetaData createFileMetaData(Long feedId) {
        return FileMetaData.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .fileKey("test-key-" + feedId)
                .fileName("test-" + feedId + ".mp4")
                .domainType(DomainType.FEED_VIDEO)
                .entityId(feedId)
                .fileStatus(FileStatus.COUPLED)
                .build();
    }

    public static VodProcessingJob createCompleteVodProcessingJob(FileMetaData fileMetaData) {
        return VodProcessingJob.builder()
                .fileMetaData(fileMetaData)
                .convertJobId(UuidCreator.getTimeOrderedEpoch().toString())
                .userId("testUser")
                .convertJobStatus(ConvertJobStatus.COMPLETE)
                .build();
    }

    public static VodProcessingJob createPendingVodProcessingJob(FileMetaData fileMetaData) {
        return VodProcessingJob.builder()
                .fileMetaData(fileMetaData)
                .convertJobId(UuidCreator.getTimeOrderedEpoch().toString())
                .userId("testUser")
                .convertJobStatus(ConvertJobStatus.PENDING)
                .build();
    }
}