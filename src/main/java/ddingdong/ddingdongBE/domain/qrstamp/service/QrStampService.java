package ddingdong.ddingdongBE.domain.qrstamp.service;


import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request.StudentInfoParam;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.CollectedStampsResponse;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.CollectionResultResponse;
import ddingdong.ddingdongBE.domain.qrstamp.entity.ClubStamp;
import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import ddingdong.ddingdongBE.domain.qrstamp.repository.StampHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QrStampService {

    private final StampHistoryRepository stampHistoryRepository;

    @Transactional
    public void collectStamp(StudentInfoParam param, String clubCode, LocalDateTime collectedAt) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        param.getStudentName(),
                        param.getStudentNumber())
                .orElse(param.toStampHistoryEntity());

        ClubStamp clubStamp = ClubStamp.getByClubCode(clubCode);
        stampHistory.collectStamp(clubStamp, collectedAt);

        stampHistoryRepository.save(stampHistory);
    }

    public CollectionResultResponse getCollectionResult(StudentInfoParam param) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        param.getStudentName(),
                        param.getStudentNumber())
                .orElse(param.toStampHistoryEntity());

        List<CollectedStampsResponse> collectedStampsResponse = stampHistory.getCollectedStamps().keySet().stream()
                .map(stamp -> CollectedStampsResponse.of(stamp.getName(),
                        stampHistory.getCollectedStamps().get(stamp)))
                .toList();
        boolean isCompleted = stampHistory.getCollectedStamps().size() == ClubStamp.values().length;
        return CollectionResultResponse.of(isCompleted, collectedStampsResponse);
    }

}
