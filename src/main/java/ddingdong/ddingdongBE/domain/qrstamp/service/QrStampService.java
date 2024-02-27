package ddingdong.ddingdongBE.domain.qrstamp.service;


import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request.CollectStampRequest;
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
    public String collectStamp(CollectStampRequest request, LocalDateTime collectedAt) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        request.getStudentName(),
                        request.getStudentNumber())
                .orElse(request.toStampHistoryEntity());
        if (stampHistory.isCompleted()) {
            return "10개의 벚꽃을 모두 채우셨습니다. 이벤트 응모를 완료해주세요!";
        }
        ClubStamp clubStamp = ClubStamp.getByClubCode(request.getClubCode());
        stampHistory.collectStamp(clubStamp, collectedAt);

        stampHistoryRepository.save(stampHistory);
        return "ok";
    }

    public CollectionResultResponse getCollectionResult(String studentNumber, String studentName) {
        StampHistory stampHistory = stampHistoryRepository.findStampHistoryByStudentNameAndStudentNumber(
                        studentName, studentNumber)
                .orElse(StampHistory.builder()
                        .studentNumber(studentNumber)
                        .studentName(studentName).build());

        List<CollectedStampsResponse> collectedStampsResponse = stampHistory.getCollectedStamps().keySet().stream()
                .map(stamp -> CollectedStampsResponse.of(stamp.getName(),
                        stampHistory.getCollectedStamps().get(stamp)))
                .toList();
        return CollectionResultResponse.of(stampHistory.isCompleted(), collectedStampsResponse);
    }
}
